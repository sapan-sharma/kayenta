// /*
//  * Created by: Sapan Sharma | 14/06/2018
//  */

package com.netflix.kayenta.judge

import java.util

import com.netflix.kayenta.canary.results._
import com.netflix.kayenta.canary.{CanaryClassifierThresholdsConfig, CanaryConfig, CanaryJudge}
import com.netflix.kayenta.judge.classifiers.metric._
import com.netflix.kayenta.judge.classifiers.score.{ScoreClassification, OpsMxScoreClassifier}
import com.netflix.kayenta.judge.detectors.IQRDetector
import com.netflix.kayenta.judge.preprocessing.Transforms
import com.netflix.kayenta.judge.scorers.{ScoreResult, WeightedSumScorer}
import com.netflix.kayenta.judge.stats.DescriptiveStatistics
import com.netflix.kayenta.judge.utils.MapUtils
import com.netflix.kayenta.mannwhitney.MannWhitneyException
import com.netflix.kayenta.metrics.MetricSetPair
import com.typesafe.scalalogging.StrictLogging
import org.springframework.stereotype.Component
import scala.collection.JavaConverters._

import com.netflix.kayenta.judge.scorers.ScoreResult
import com.netflix.kayenta.canary.results.CanaryAnalysisResult
import com.netflix.kayenta.judge.classifiers.metric.{High, Low, Pass}

import scala.collection.JavaConverters._

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.List;
import scala.collection.mutable._

import java.io._
// import org.saddle._

@Component
class OpsMxACAJudge extends CanaryJudge with StrictLogging {
  private final val judgeName = "OpsMxACAJudge-v1.0"

  override def isVisible: Boolean = true
  override def getName: String = judgeName

  override def judge(canaryConfig: CanaryConfig,
                     scoreThresholds: CanaryClassifierThresholdsConfig,
                     metricSetPairList: util.List[MetricSetPair]): CanaryJudgeResult = {
  	//create config object
  	//create canary id(for config)
  	val canaryid = 1

    val metricsList = metricSetPairList.asScala.toList.map { metricPair =>
    	val metricName = metricPair.getName
    	val experimentValues = metricPair.getValues.get("experiment").asScala.map(_.toDouble).toArray
    	val controlValues = metricPair.getValues.get("control").asScala.map(_.toDouble).toArray
      var baseDir = "/home/opsmx/Documents/work/data_with_load/ScoringAndPCA/"
      val writer = new PrintWriter(new File(baseDir + "1/KayentaData/version1/" +  metricName + ".csv"))
      for (x <- controlValues) {
        writer.write(x + "\n")  // however you want to format it
      }
      writer.close()
      val writer = new PrintWriter(new File(baseDir + "1/KayentaData/version2/" +  metricName + ".csv"))
      for (x <- experimentValues) {
        writer.write(x + "\n")
      }
      writer.close()
  	}

  	CanaryJudgeResult.builder().build()

    //call OpsMx Analysis
    val results = "/usr/bin/Rscript /home/opsmx/Documents/work/Analytics/R-code/kayentaWrapper.R" !!
    logger.info(results)

    // consume ResultJson and build result object
    //create 1. metricResults, 2. scoreClassification
    //1.
    val metricResults = metricSetPairList.asScala.toList.map { metricPair =>
	    val metricClassification = MetricClassification(Pass, None, 1.0)
	    CanaryAnalysisResult.builder()
	    .name(metricPair.getName)
	    .id(metricPair.getId)
	    .tags(metricPair.getTags)
	      // .groups(metricConfig.getGroups)
	      // .experimentMetadata(Map("stats" -> DescriptiveStatistics.toMap(experimentStats).asJava.asInstanceOf[Object]).asJava)
	      // .controlMetadata(Map("stats" -> DescriptiveStatistics.toMap(controlStats).asJava.asInstanceOf[Object]).asJava)
	      // .critical(critical)
	    .classification(metricClassification.classification.toString)
		.classificationReason(metricClassification.reason.orNull)
	    .build()
    }
    
    //2.
    val scoreClassifier = new OpsMxScoreClassifier(scoreThresholds.getPass, scoreThresholds.getMarginal)
    val scoreClassification = scoreClassifier.classify(null)
    buildCanaryResult(null, scoreClassification, metricResults)
	}



	def buildCanaryResult(scores: ScoreResult, scoreClassification: ScoreClassification,
                        metricResults: List[CanaryAnalysisResult]): CanaryJudgeResult ={
    //Construct the summary score result object
    val summaryScore = CanaryJudgeScore.builder()
      .score(scoreClassification.score)
      .classification(scoreClassification.classification.toString)
      .classificationReason(scoreClassification.reason.getOrElse(""))
      .build()
    //Construct the judge result object
    val results = metricResults.asJava
    CanaryJudgeResult.builder()
      .judgeName(judgeName)
      .score(summaryScore)
      .results(results)
      // .groupScores(groupScores.asJava)
      .build()
  }
}
