#!groovy

//Source of scripts: https://groups.google.com/forum/#!topic/jenkinsci-users/U9HLBVB5_CM
//===========================
//Sonar Script
//==========================
/*import jenkins.model.*
import hudson.plugins.sonar.*
import hudson.plugins.sonar.model.*
import org.jfrog.*
import org.jfrog.hudson.*
import org.jfrog.hudson.util.Credentials;

def desc = Jenkins.getInstance().getDescriptor("hudson.plugins.sonar.SonarPublisher")

def sinst = new SonarInstallation(
  "sonar4.5.1",
  false,
  "http://sonar:9000/",
  "jdbc:h2:tcp://sonar:9092/sonar",
  "org.h2.Driver",
  "sonar",
  "sonar",
  "",
  "-Dsonar.sourceEncoding=\"UTF-8\"",
  new TriggersConfig(),
  "admin",
  "admin"
)
desc.setInstallations(sinst)

desc.save()
*/

//==================================================
//Artifactory Script
//==================================================
/*

def desc2 = Jenkins.getInstance().getDescriptor("org.jfrog.hudson.ArtifactoryBuilder")

def deployerCredentials = new Credentials("admin", "password")
def resolverCredentials = new Credentials("", "")

def sinst2 = [new ArtifactoryServer(
  "server-id",
  "http://artifactory:8081/artifactory",
  deployerCredentials,
  resolverCredentials,
  300,
  false )
]

desc2.setArtifactoryServers(sinst2)

desc2.save()*/