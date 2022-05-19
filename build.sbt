
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

organization := "damian"

lazy val root = (project in file("."))
  .settings(
    name := "kafka-streams-docker"
  )

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.36"
libraryDependencies += "org.slf4j" % "slf4j-reload4j" % "1.7.36"
val kafkaVersion = "3.1.0"
libraryDependencies += "org.apache.kafka" % "kafka-streams" % kafkaVersion
libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % kafkaVersion

enablePlugins(DockerPlugin)

docker / dockerfile := {
  val jarFile: File = (Compile / packageBin / sbt.Keys.`package`).value
  val classpath = (Compile / managedClasspath).value
  val mainclass = (Compile / packageBin / mainClass).value.getOrElse(sys.error("Expected exactly one main class"))
  val jarTarget = s"/app/${jarFile.getName}"
  // Make a colon separated classpath with the JAR file
  val classpathString = classpath.files.map("/app/" + _.getName)
    .mkString(":") + ":" + jarTarget
  new Dockerfile {
    // Base image
    from("openjdk:11-jre")
    // Add all files on the classpath
    add(classpath.files, "/app/")
    // Add the JAR file
    add(jarFile, jarTarget)
    // On launch run Java with the classpath and the main class
    entryPoint("java", "-cp", classpathString, mainclass)
  }
}

docker / imageNames := Seq(
  // Sets the latest tag
  ImageName(s"${organization.value}/${name.value}:latest")
)