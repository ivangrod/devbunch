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
        CURRENT_BRACH = '$BRANCH_NAME'
    }
    stages { //Stages definition
       stage ('Initialize') { //Send message to slack at the beginning
             steps {
                  // Sending message through slack
                  sh 'mvn --version'
            }
       }
       stage ('CleanUp') {
           //The pipeline remove all temporal files.
           steps {
                deleteDir()
           }
       }
       stage('Checkout') {
            steps {
                  checkout scm
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
                     sh "mvn --version"
                 }
           }
       }
       stage ('QA') {
           //QA stage. Parallel mode with Sonar, Coverage and OWASP
           steps {
               parallel 'Sonarqube Analysis': {
                    sh "mvn -B --batch-mode -U clean verify"
                    sh "mvn --version"
               }, 'OWASP Analysis' : {
                    sh "mvn --version"
               }
          }
          //We store tests reports.
          post {
               success {
                    junit 'collector/target/surefire-reports/**/*.xml' 
                    junit 'extractor/target/surefire-reports/**/*.xml' 
                    junit 'graph-model/target/surefire-reports/**/*.xml' 
               }
          }
      }
      stage ('Install') {
            when {
              expression { return env.BRANCH_NAME.equals('develop')  || env.BRANCH_NAME.equals('master')  }
            }
            steps {
                 sh "mvn clean install"
            }
      }
      stage ('Checking PR commits') {
            when {
                expression { return env.BRANCH_NAME.startsWith('PR-') }
            }
            steps {
                echo 'Review ' + env.BRANCH_NAME
            }
      }
      stage ('Confirmation') {
           when {
            branch 'master'
           }
           //In this stage, pipeline wait until user confirm next stage.
           //It sends slack messages
           steps {
                /*slackSend channel: '@boss',color: '#00FF00', message: '\u00BFDo you want to deploy to production environment?. \n Link: ${BLUE_OCEAN_URL}' , teamDomain: 'my-company', token: 'XXXXXXXXXXX'*/
                timeout(time: 72, unit: 'HOURS') {
                    input 'Should the project ' + APP_NAME + ' be deployed to production environment\u003F'
                }
           }
      }
      stage ('Tagging the release candidate') {
           when {
            branch 'master'
           }
           steps {
              //Tagging from master to tag
              echo "Tagging the release Candidate";
           }
      }
      stage ('Deploy to Production environment') {
           when {
            branch 'master'
           }
           //We deploy in parrallel mode during 6 times. 
           steps {
                parallel 'Server 1': {
                    retry(6) {
                        sh "mvn --version"
                    }
                }, 'Server 2' : {
                    retry(6) {
                        sh "mvn --version"
                    }
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