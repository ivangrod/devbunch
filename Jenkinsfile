pipeline {
     agent any    //Docker agent. In the future, we will work with docker
     tools { //Jenkins installed tools
        maven 'maven3' //Maven tool defined in jenkins configuration
        jdk 'JDK8' //Java tool defined in jenkin configuration
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
            }
       }
       stage('checkout') {
            steps {
                git(url: 'https://github.com/david-romero/devbunch', branch: 'feature/addPipeline', credentialsId: 'github-credentials', poll: true, changelog: true)
            }
       }
       stage ('Build') { //Compile stage
            steps {
                 bat "mvn -T 4 -B --batch-mode -V -U clean package"
            }
       }
       stage ('Test') {
            //Tests stage. We use parrallel mode.
            steps {
                 parallel 'Integration & Unit Tests': {
                     bat "mvn -T 4 -B --batch-mode -V -U -e test"
                 }, 'Performance Test': {
                     bat "mvn jmeter:jmeter"
                 }
           }
       }
       stage ('QA') {
       //QA stage. Parallel mode with Sonar, Coverage and OWASP
           steps {
                parallel 'Sonarqube Analysis': {
                    bat "mvn -B --batch-mode -V -U clean verify"
                    bat "mvn -B --batch-mode -V -U sonar:sonar"
               }, 'Check code coverage' : {
                    //Check coverage
                    //If coverage is under 80% the pipeline fails.
                    bat "mvn -B --batch-mode -V -U -e verify"
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
      stage ('Deploy to Pre-production environment') {
           steps {
                bat "docker run"
           }
      }
      stage ('Confirmation') {
      //In this stage, pipeline wait until user confirm next stage.
      //It sends slack messages
           steps {
                slackSend channel: '@boss',color: '#00FF00', message: '\u00BFDo you want to deploy to production environment?. \n Link: ${BLUE_OCEAN_URL}' , teamDomain: 'my-company', token: 'XXXXXXXXXXX'
                timeout(time: 72, unit: 'HOURS') {
                    input 'Should the project ' + APP_NAME + ' be deployed to production environment\u003F'
                }
           }
      }
      stage ('Tagging the release candidate') {
           steps {
               //Tagging from trunk to tag
               echo "Tagging the release Candidate";
               bat "mvn scm:tag"
          }
      }
      stage ('Deploy to Production environment') {
           //We deploy in parrallel mode during 3 times. 
           steps {
                parallel 'Server 1': {
                    retry(3) {
                        bat "docker run"
                    }
                }, 'Server 2' : {
                    retry(3) {
                        bat "docker run"
                    }
                }
           }
      }
      stage ('CleanUp') {
      //The pipeline remove all temporal files.
           steps {
                deleteDir()
           }
      }
    } //End of stages
    //Post-workflow actions.
    //The pipeline sends messages with the result of the execution
    post {
      success {
           // Sending message through slack 
      }
      failure {
           // Sending message through slack
      }
      unstable {
           // Sending message through slack'
      }
    }
   }
