pipeline {
  agent any
  environment {
    JAVA_HOME = '/usr/lib/jvm/java-1.8.0-openjdk'
    PATH = "${JAVA_HOME}/bin:${env.PATH}"
  }
  stages {
    stage('Checkout') {
      steps { checkout scm }
    }
    stage('Build') {
      steps { sh 'chmod +x mvnw && ./mvnw -B clean compile' }
    }
    stage('Tests') {
      steps { sh './mvnw -B test' }
      post { always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' } }
    }
    stage('Package') {
      steps { sh './mvnw -B package -DskipTests' }
    }
  }
}
