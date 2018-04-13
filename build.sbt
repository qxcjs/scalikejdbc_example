name := "scalikejdbc_example"

version := "0.1"

scalaVersion := "2.12.4"

enablePlugins(ScalikejdbcPlugin)

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "3.2.2",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.2.2",
  "org.scalikejdbc" %% "scalikejdbc-joda-time" % "3.2.2",
  "com.h2database" % "h2" % "1.4.197",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "joda-time" % "joda-time" % "2.9.9"
)
        