/*
  This jenkins pipeline should be configured as Multibranch Pipeline job in Jenkins

  Required Jenkins Plugins:
    * All basic Jenkins Pipeline Plugins
    * Git plugin
    * Docker Pipeline Plugin
    * Amazon ECR plugin (if push to ECR)
    * Lockable Resources Plugin
*/

pipeline {
  agent {
    label "${params.AGENT_LABEL ?: 'build'}"
  }
  
  parameters {
    string(name: 'AGENT_LABEL', defaultValue: 'build')
    booleanParam(name: 'BINTRAY_PUBLISH', defaultValue: false)
    string(name: 'BINTRAY_CERDENTIAL_ID', defaultValue: 'visenze-jfrog-bintray',
        description: 'The jenkins credential ID to push to bintray')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        script {
          sh("sudo update-alternatives --set java /usr/lib/jvm/java-8-oracle/jre/bin/java")
          sh("java -version")
        }
      }
    }
  }
}