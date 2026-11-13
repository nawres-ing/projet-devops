pipeline {
  agent any

  // Déclenchement automatique à chaque push (webhook GitHub)
  triggers { githubPush() }

  options {
    timestamps()
    timeout(time: 25, unit: 'MINUTES')
    buildDiscarder(logRotator(numToKeepStr: '10'))
    disableConcurrentBuilds()
  }

  environment {
    // Java 8 pour compiler et tester le projet
    JAVA_HOME = '/usr/lib/jvm/java-1.8.0-openjdk'
    // Java 21 réservé au scanner SonarQube
    JAVA21_HOME = '/usr/lib/jvm/java-21-openjdk'
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
      // JaCoCo génère aussi le rapport de couverture (target/site/jacoco)
      steps { sh './mvnw -B test' }
      post {
        always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
      }
    }
    stage('SonarQube Analysis') {
      steps {
        // Injecte l'URL et le token du serveur "SonarQube" configuré dans Jenkins
        withSonarQubeEnv('SonarQube') {
          sh '''
            export JAVA_HOME=$JAVA21_HOME
            export PATH=$JAVA_HOME/bin:$PATH
            ./mvnw -B org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356:sonar \
              -Dsonar.projectKey=achat \
              -Dsonar.projectName=achat
          '''
        }
      }
    }
    stage('Package') {
      steps { sh './mvnw -B package -DskipTests' }
      post {
        success { archiveArtifacts artifacts: 'target/*.jar', fingerprint: true }
      }
    }
  }

  post {
    success { echo 'Pipeline terminé avec succès.' }
    failure { echo 'Pipeline en échec : consulter les logs.' }
    always  { deleteDir() }
  }
}
