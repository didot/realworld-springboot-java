#!/usr/bin/env groovy

pipeline {
  agent any

  stages {
    stage('Capture env') {
      steps {
        sh '''
          env
        '''
      }
    }
    stage('Build') {
      steps {
        sh '''
          ./gradlew build
        '''
      }

      post {
        always {
          jacoco(execPattern: 'build/jacoco/test.exec')
          recordIssues(tools: [junitParser(pattern: 'build/test-results/test/*.xml')])
        }
      }
    }
  }
}
