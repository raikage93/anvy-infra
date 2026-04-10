import hudson.model.ParametersDefinitionProperty
import hudson.model.StringParameterDefinition
import jenkins.model.Jenkins
import org.jenkinsci.plugins.parameterizedscheduler.ParameterizedTimerTrigger
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob

def instance = Jenkins.get()
def jobName = 'follow-up-reminder-job'
def pipelineScript = '''
pipeline {
  agent any
  options {
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '30'))
  }
  parameters {
    string(name: 'DAYS_BEFORE', defaultValue: '3', description: 'So ngay truoc lich tai kham de gui thong bao')
  }
  stages {
    stage('Send Telegram follow-up reminders') {
      steps {
        sh """
          set -eu
          curl --fail --show-error --silent -X POST "${BACKEND_INTERNAL_URL}/api/internal/follow-up-reminders/run?days_before=${DAYS_BEFORE}" \
            -H "x-cron-secret: ${CRON_SECRET}" \
            -H "Content-Type: application/json"
        """
      }
    }
  }
}
'''.stripIndent()

def job = instance.getItem(jobName)
if (!(job instanceof WorkflowJob)) {
  job?.delete()
  job = instance.createProject(WorkflowJob, jobName)
}

job.description = 'Quet ket qua kham co ngay tai kham sap toi va gui Telegram cho admin.'
job.setDefinition(new CpsFlowDefinition(pipelineScript, true))
job.removeProperty(ParametersDefinitionProperty)
job.addProperty(
  new ParametersDefinitionProperty(
    new StringParameterDefinition('DAYS_BEFORE', '3', 'So ngay truoc lich tai kham de gui thong bao')
  )
)
job.getTriggers().values().each { trigger -> trigger.stop() }
job.getTriggers().clear()
def trigger = new ParameterizedTimerTrigger('H * * * * % DAYS_BEFORE=3')
job.addTrigger(trigger)
trigger.start(job, true)
job.save()
instance.save()
