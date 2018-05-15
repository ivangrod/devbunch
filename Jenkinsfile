pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        git(url: 'https://github.com/david-romero/devbunch', branch: 'feature/addPipeline', credentialsId: 'github-credentials', poll: true, changelog: true)
      }
    }
    stage('compile') {
      steps {
        tool(name: 'maven3', type: 'Maven')
        sh 'mvn clean compile'
      }
    }
  }
}
