import hudson.model.User
import hudson.security.HudsonPrivateSecurityRealm
import jenkins.model.Jenkins

def instance = Jenkins.get()
def adminId = System.getenv('JENKINS_ADMIN_ID') ?: 'admin'
def adminPassword = System.getenv('JENKINS_ADMIN_PASSWORD') ?: 'admin123'

def realm = instance.getSecurityRealm()
if (!(realm instanceof HudsonPrivateSecurityRealm)) {
  return
}

def user = User.getById(adminId, false)
if (user == null) {
  realm.createAccount(adminId, adminPassword)
  instance.save()
  return
}

user.addProperty(HudsonPrivateSecurityRealm.Details.fromPlainPassword(adminPassword))
user.save()
instance.save()
