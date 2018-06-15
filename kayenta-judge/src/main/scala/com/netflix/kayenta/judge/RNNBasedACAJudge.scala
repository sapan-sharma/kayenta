/*
Created by: Sapan Sharma
*/

import java.util

import com.netflix.kayenta.canary.results._
import com.netflix.kayenta.canary.{CanaryClassifierThresholdsConfig, CanaryConfig, CanaryJudge}
import com.netflix.kayenta.judge.classifiers.metric._
import com.netflix.kayenta.judge.classifiers.score.{ScoreClassification, ThresholdScoreClassifier}
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

case class Metric(name: String, values: Array[Double], label: String)

@Component
class RandomDummyJudge extends CanaryJudge {
  private final val _judgeName = "Opsmx ACA Judge"

  override def getName: String = _judgeName

  val random = new scala.util.Random()

  override def judge(canaryConfig: CanaryConfig,
                     scoreThresholds: CanaryClassifierThresholdsConfig,
                     metricSetPairList: util.List[MetricSetPair]): CanaryJudgeResult = {

    //make inputToR.json
      // val metricResults = metricSetPairList.asScala.toList.map { metricPair =>
      // val metricNames = metricPair.getValues.get("name").asScala.map().toArray
      // }
      //save metric objects in csv's on filesystem

    // val metricResults = metricSetPairList.asScala.toList.map { metricPair =>
    //   val metricConfig = canaryConfig.getMetrics.asScala.find(m => m.getName == metricPair.getName) match {
    //     case Some(config) => config
    //     case None => throw new IllegalArgumentException(s"Could not find metric config for ${metricPair.getName}")
    //   }

    //   val experimentValues = Array(0.0,0.0,0.0)
    //   val controlValues = Array(0.0,0.0,0.0)

    //   val experimentMetric = Metric(metricPair.getName, experimentValues, label="Canary")
    //   val controlMetric = Metric(metricPair.getName, controlValues, label="Baseline")

    //   val experimentStats = DescriptiveStatistics.summary(experimentMetric)
    //   val controlStats = DescriptiveStatistics.summary(controlMetric)

    //   val randomValue = "%.2f".format(random.nextDouble * 100).toDouble

    //   // The classification of each individual metric will be done randomly, ignoring the actual data points.
    //   val classification = {
    //     if (randomValue >= 66) {
    //       High.toString
    //     } else if (randomValue >= 33) {
    //       Pass.toString
    //     } else {
    //       Low.toString
    //     }
    //   }

    //   CanaryAnalysisResult.builder()
    //     .name(metricPair.getName)
    //     .id(metricPair.getId)
    //     .tags(metricPair.getTags)
    //     .classification(classification)
    //     .groups(metricConfig.getGroups)
    //     .experimentMetadata(Map("stats" -> DescriptiveStatistics.toMap(experimentStats).asJava.asInstanceOf[Object]).asJava)
    //     .controlMetadata(Map("stats" -> DescriptiveStatistics.toMap(controlStats).asJava.asInstanceOf[Object]).asJava)
    //     .build()
    // }

    // val groupWeights = Option(canaryConfig.getClassifier.getGroupWeights) match {
    //   case Some(groups) => groups.asScala.mapValues(_.toDouble).toMap
    //   case None => Map[String, Double]()
    // }

    // val weightedSumScorer = new WeightedSumScorer(groupWeights)
    // val scores = weightedSumScorer.score(metricResults)

    // val scoreClassifier = new ThresholdScoreClassifier(scoreThresholds.getPass, scoreThresholds.getMarginal)
    // val scoreClassification = scoreClassifier.classify(scores)

    // val groupScores = scores.groupScores match {
    //   case None => List(CanaryJudgeGroupScore.builder().build())
    //   case Some(groups) => groups.map{ group =>
    //     CanaryJudgeGroupScore.builder()
    //       .name(group.name)
    //       .score(group.score)
    //       .classification("")
    //       .classificationReason("")
    //       .build()
    //   }
    // }

    // val summaryScore = CanaryJudgeScore.builder()
    //   .score(scoreClassification.score)
    //   .classification(scoreClassification.classification.toString)
    //   .classificationReason(scoreClassification.reason.getOrElse(""))
    //   .build()

    // val results = metricResults.asJava
    CanaryJudgeResult.builder().build()
  }

}
