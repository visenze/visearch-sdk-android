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

  environment {
    ANDROID_HOME = tool(name: 'android-sdk-tools', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool')
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
          sh "./gradlew assembleRelease"
        }
      }
    }

    stage('Push to Bintray') {
      when {
        expression {
          params.BINTRAY_PUBLISH
        }
      }
      steps {
        script {
          withCredentials([usernamePassword(credentialsId: params.BINTRAY_CERDENTIAL_ID, usernameVariable: 'bintrayUser', passwordVariable: 'bintrayKey')]) {
            sh "./gradlew bintrayUpload -PbintrayUser=${bintrayUser} -PbintrayKey=${bintrayKey}"
          }
        }
      }
    }
  }
}