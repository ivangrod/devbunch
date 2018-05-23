#!groovy
 
import jenkins.model.*
import hudson.security.*
import jenkins.security.s2m.AdminWhitelistRule
import hudson.EnvVars
 
def instance = Jenkins.getInstance()
 
/* read credentials from environment properties  */
def user = EnvVars.masterEnvVars.get("JENKINS_USER");
def pass = EnvVars.masterEnvVars.get("JENKINS_PASS");

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount(user, pass)
instance.setSecurityRealm(hudsonRealm)
 
def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)
instance.save()
 
Jenkins.instance.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)