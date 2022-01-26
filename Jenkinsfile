
def ENVIRONMENT_IP=''
def ENVIRONMENT_PEM_FILE=''
def USER='ec2-user'

pipeline {
	
    agent any
	
    environment{	    
        STAGE_IP='65.2.11.163'
	STAGE_PEM='/var/jenkins_home/workspace/ATMAX-ASG-EC2.pem'	    
        QA_IP='13.235.49.151'	
	QA_PEM='/var/jenkins_home/workspace/atmaxbackend.pem'
	OLD_STAGE_IP=''
	OLD_STAGE_PEM=''
	UAT_IP=''
	UAT_PEM=''
	PROD_IP=''	    
    }
    
    options {
       timeout(time: 7, unit: 'MINUTES')
    }
	
    parameters {
        choice(name: 'envToDeploy', choices: ['Stage','Qa', 'OldStage', 'Uat', 'Prod'], description: 'Deployment')
        booleanParam(name: 'executeSonar', defaultValue: false, description: 'Execute Sonar')
        booleanParam(name: 'executeBuild', defaultValue: false, description: 'Execute Build')
        booleanParam(name: 'executeBackup', defaultValue: false, description: 'Execute Backup')
    }
       
    stages {
	    
	 stage('Build info'){
	    steps{
	    echo "BUILD_NUMBER ${BUILD_NUMBER}"
	    echo "BUILD_URL ${BUILD_URL}"
	    echo "BUILD_ID ${BUILD_ID}"
	      }
	  }

      stage('ENV IDENTIFIED'){
	   steps{
	     script{
		     if("${envToDeploy}" == "Stage"){
		    	ENVIRONMENT_IP= "${STAGE_IP}"
		    	ENVIRONMENT_PEM_FILE = "${STAGE_PEM}"
		     }
		    
		    if("${envToDeploy}" == "Qa"){
		    	ENVIRONMENT_IP= "${QA_IP}"
		    	ENVIRONMENT_PEM_FILE = "${QA_PEM}"
		    }

		    if("${envToDeploy}" == "OldStage"){
		    	ENVIRONMENT_IP= "${OLD_STAGE_IP}"
		    	ENVIRONMENT_PEM_FILE = "${QA_PEM}"
		    }

		    if("${envToDeploy}" == "Uat"){
		    	ENVIRONMENT_IP= "${UAT_IP}"
		    	ENVIRONMENT_PEM_FILE = "${UAT_PEM}"
		    }

    		    if("${envToDeploy}" == "Prod"){
		    	ENVIRONMENT_IP= "${PROD_IP}"
		    	ENVIRONMENT_PEM_FILE = "${PROD_PEM}"
		    }

	         echo "Deployment Env IP ${ENVIRONMENT_IP}  ${ENVIRONMENT_PEM_FILE} Pem File  "
		}
	    }
	}
	    
   /*** Build Process Starts : Only executeBuild is true ***/
        stage ('Build') {
	 when {expression { params.executeBuild } }
            steps {
                echo "Starting Build Process"
                sh 'pwd'
                sh './gradlew :bootJar'           
            }
        }
			
	    
   /*** Running Sonar Starts : Only executeSonar is true ***/
        stage ('Running Sonar') {
	 when {expression { params.executeSonar } }
            steps {
                echo "Running Sonar Build"
                sh 'pwd'                
                sh './gradlew sonarqube \
                      -Dsonar.projectKey=api_atmax \
                      -Dsonar.host.url=http://13.235.48.9:9000 \
                      -Dsonar.login=74e8454ec427fea555c6839c2766434fd322b1b8 \
                      -Dsonar.verbose=true \
                      -Dsonar.ce.javaOpts=-Xmx2048m'
            }
        }//stage
	    
   /*** Running Backup : Only executeBackup is true ***/	    
	stage ('Backup') {
	    when {expression {  params.executeBackup } }
		
	     steps {
	       script{
	         echo "start copy to another folder :${envToDeploy}"
	            if( "${envToDeploy}" == "Stage"){		     
		        sh 'ssh -o StrictHostKeyChecking=no  -i $ENVIRONMENT_PEM_FILE -T $USER@$ENVIRONMENT_IP  /opt/copyPrviousJarfile.sh'
		   }  
		 }
	       }
	  }

   /*** Running Copying Jar  ***/	    	    
        stage ('Copying Jar') {
            steps {
	      script{
                echo "Starting Copy to : ${envToDeploy}"
		sh "scp -o StrictHostKeyChecking=no -i $ENVIRONMENT_PEM_FILE -T build/libs/*.jar $USER@$ENVIRONMENT_IP:/opt/"
	      }
            }
        } // stage
   
   /*** Stop Server  ***/	    	    
         stage ('Stoping Server') {
            steps {
	      script{	    
		      echo "Stopping server to : ${envToDeploy}"
		      sh "ssh -o StrictHostKeyChecking=no  -i $ENVIRONMENT_PEM_FILE  -T $USER@$ENVIRONMENT_IP /opt/stopScript.sh"
	      }
            }
        }

   /*** Start Server  ***/	    	            
        stage ('Start Server') {
            steps {
	     script{
		 echo "Starting server to : ${envToDeploy}"
		 sh "ssh -o StrictHostKeyChecking=no  -i $ENVIRONMENT_PEM_FILE -T $USER@$ENVIRONMENT_IP /opt/startScript.sh"
	      }
            }
        }
	    
   }// stages
}//pipeline
