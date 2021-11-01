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
    }
  }
}
