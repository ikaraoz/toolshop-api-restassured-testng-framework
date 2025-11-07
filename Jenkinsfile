pipeline {
  agent any
  tools { jdk 'jdk21' }
  stages {
    stage('Checkout') {
      steps { checkout scm }
    }
    stage('Build & Test') {
      steps {
        sh './gradlew clean test -Denv=local -Pgroups=smoke,regression'
      }
      post {
        always {
          archiveArtifacts artifacts: 'build/reports/tests/test/**', fingerprint: true
          archiveArtifacts artifacts: 'build/allure-results/**', fingerprint: true
        }
      }
    }
    stage('Allure Report') {
      steps {
        allure includeProperties: false, jdk: '', reportBuildPolicy: 'ALWAYS', results: [[path: 'build/allure-results']]
      }
    }
  }
}
