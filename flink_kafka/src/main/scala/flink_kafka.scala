package test

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.api.KafkaSource
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

object streamingConsumer {

  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println("Usage: streamingConsumer <topics> <outputPath>")
      System.exit(1)
    }

    val topic = args(0)
    val outputPath = args(1)
    
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val text = env.addSource(new KafkaSource[String]("localhost:2181", topic, new SimpleStringSchema))

    val pair = text.map(x => {
        val receiveTime = System.nanoTime()
        (x, receiveTime, receiveTime - x.toLong)
    })

    pair.writeAsText(outputPath, 1)

    env.execute("word count")
  }
}
