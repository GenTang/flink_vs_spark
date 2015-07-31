lazy val root = (project in file(".")).
  settings(
    name := "spark_kafka",
    version := "1.0",
    scalaVersion := "2.10.5"
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-streaming-kafka" % "1.4.0",
  "org.apache.spark" %% "spark-core" % "1.4.0",
  "org.apache.spark" %% "spark-streaming" % "1.4.0",
  "org.apache.kafka" %% "kafka" % "0.8.2.0"
)

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case "META-INF/jersey-module-version"        => MergeStrategy.first
  case _                                       => MergeStrategy.first
}
