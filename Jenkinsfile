pipeline {
  agent any

  // Déclenchement automatique à chaque push (webhook GitHub)
  triggers { githubPush() }

  // Options générales du pipeline
  options {
    timestamps()                                   // horodatage dans les logs
    timeout(time: 20, unit: 'MINUTES')             // évite les builds bloqués
    buildDiscarder(logRotator(numToKeepStr: '10')) // garde les 10 derniers builds
    disableConcurrentBuilds()                      // un seul build à la fois
  }

  environment {
    JAVA_HOME = '/usr/lib/jvm/java-1.8.0-openjdk'
    PATH = "${JAVA_HOME}/bin:${env.PATH}"
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }                       // récupère le code depuis GitHub
    }
    stage('Build') {
      steps { sh 'chmod +x mvnw && ./mvnw -B clean compile' }
    }
    stage('Tests') {
      steps { sh './mvnw -B test' }
      post {
        always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
      }
    }
    stage('Package') {
      steps { sh './mvnw -B package -DskipTests' }
      post {
        success { archiveArtifacts artifacts: 'target/*.jar', fingerprint: true }  // conserve le .jar
      }
    }
  }

  post {
    success { echo 'Pipeline terminé avec succès.' }
    failure { echo 'Pipeline en échec : consulter les logs.' }
    always  { deleteDir() }                        // nettoie le workspace
  }
}
