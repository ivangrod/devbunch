pipeline {
    agent any    //Docker agent. In the future, we will work with docker
    tools { //Jenkins installed tools
        maven 'maven3' //Maven tool defined in jenkins configuration
        jdk 'jdk8' //Java tool defined in jenkin configuration
    }
    options {
        //If after 3 days the pipeline does not finish, please abort
        timeout(time: 76, unit: 'HOURS') 
    }
    environment {
        //Project name
        APP_NAME = 'devbunch'
    }
    stages { //Stages definition
       stage ('Initialize') { //Send message to slack at the beginning
             steps {
                  // Sending message through slack
                  sh 'mvn --version'
            }
       }
       stage('checkout') {
            steps {
                git(url: 'https://github.com/david-romero/devbunch', branch: 'feature/addPipeline', credentialsId: 'github-credentials', poll: true, changelog: true)
            }
       }
       stage ('Build') { //Compile stage
            steps {
                 sh "mvn -T 4 -B --batch-mode -V -U clean compile"
            }
       }
       stage ('Test') {
            //Tests stage. We use parrallel mode.
            steps {
                 parallel 'Integration & Unit Tests': {
                     sh "mvn -T 4 -B --batch-mode -V -U test"
                 }, 'Performance Test': {
                     sh "mvn jmeter:jmeter"
                 }
           }
       }
       stage ('QA') {
           //QA stage. Parallel mode with Sonar, Coverage and OWASP
           steps {
                parallel 'Sonarqube Analysis': {
                    bat "mvn -B --batch-mode -U clean verify"
                    bat "mvn -B --batch-mode -U sonar:sonar"
               }, 'Check code coverage' : {
                    //Check coverage
                    //If coverage is under 80% the pipeline fails.
                    bat "mvn -B --batch-mode -U verify"
               }, 'OWASP Analysis' : {
                    bat "mvn -B -X --batch-mode -V -U -e dependency-check:check"
               }
          }
          //We store tests reports.
          post {
               success {
                    junit 'target/surefire-reports/**/*.xml' 
               }
          }
      }
    } //End of stages
    //Post-workflow actions.
    //The pipeline sends messages with the result of the execution
    post {
      success {
           // Sending message through slack 
           sh 'mvn --version'
      }
      failure {
           // Sending message through slack
           sh 'mvn --version'
      }
      unstable {
           // Sending message through slack'
           sh 'mvn --version'
      }
    }
}