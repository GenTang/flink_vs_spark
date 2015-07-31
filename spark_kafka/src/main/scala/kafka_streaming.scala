package test

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.Milliseconds
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.rdd.RDD
import kafka.serializer.StringDecoder
import scala.util.Random

object streamingConsumer {
  
  def main(args: Array[String]) {

    if (args.length < 3) {
      System.err.println("Usage: streamingConsumer <topics> <Milliseconds> <outputPath>")
      System.exit(1)
    }

    val topic = args(0)
    val millis = args(1).toInt
    val outputPath = args(2)

    val sc = new SparkConf()
      .setAppName("StreamingKafkaConsumer")
     
    val ssc:StreamingContext = new StreamingContext(sc, Milliseconds(millis))
    
    // http://kafka.apache.org/08/configuration.html -> See section 3.2 Consumer Configs
    val kafkaParams = Map(
      "zookeeper.connect" -> "localhost:2181", //We run local kafka test
      "zookeeper.connection.timeout.ms" -> "6000",
      "group.id" -> "tanggen"
    )

    // Map of (topic_name -> numPartitions) to consume. Each partition is consumed in its own thread
    val topics = Map(
      topic -> 1
    )

    // Assuming very small data volumes for example app - tune as necessary.
    val storageLevel = StorageLevel.MEMORY_ONLY
  
    val messages = KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics, storageLevel)
    val data = messages.map(_._2).map(x => {
        val receiveTime = System.nanoTime()
        (x, receiveTime, receiveTime - x.toLong)
    })
    data.saveAsTextFiles(outputPath)
    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate (manually or due to any error)
  }
}
