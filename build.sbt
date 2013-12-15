name := """libserviceapi"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  // Select Play modules
  jdbc,      // The JDBC connection pool and the play.api.db API
  javaEbean,
  anorm,   
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "org.specs2" %% "specs2" % "2.3.3" % "test",
  "org.mockito" % "mockito-all" % "1.9.5"
)

play.Project.playScalaSettings
