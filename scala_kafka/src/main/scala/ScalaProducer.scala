package test

import kafka.producer.ProducerConfig
import java.util.Properties
import kafka.producer.Producer
import kafka.producer.Producer
import kafka.producer.Producer
import kafka.producer.Producer
import kafka.producer.KeyedMessage
import java.util.Date

object kafkaProducer {
  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println("Usage: kafkaProducer <topics> <brokers>")
      System.exit(1)
    }

    val topic = args(0)
    val brokers = args(1)
    val props = new Properties()
    props.put("metadata.broker.list", brokers)
    props.put("serializer.class", "kafka.serializer.StringEncoder")
    props.put("producer.type", "async")

    val config = new ProducerConfig(props)
    val producer = new Producer[String, String](config)
    
    while(true) {
       producer.send(new KeyedMessage[String, String](topic, System.nanoTime().toString))
   }

   producer.close()
  }
}
