
pipeline {
    agent any
    
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    whoami
                '''
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true install' 
          
            }
     
     
        }
        stage ('copy') {
            steps {
                echo "radhe radhey"
               sh 'scp -o StrictHostKeyChecking=no -i singapurkey.pem radhey ec2-user@3.1.201.220:/home/ec2-user/'
                    }
        }
    
        stage ('deploy'){
        steps {
            
           sh 'scp -o StrictHostKeyChecking=no -i singapurkey.pem target/iExpress-0.0.1-SNAPSHOT.war  ec2-user@3.1.201.220:/opt/apache-tomcat-8.5.72/webapps/'
            sh """
            ssh -i singapurkey.pem ec2-user@3.1.201.220
            
            """
        }
    
}
         stage('Build Image') {
             steps {
                 script {                    
                      sh 'ssh jenkins@3.1.201.220 ls'
                      sh 'ssh jenkins@3.1.201.220 rm -rf /home/jenkins/test'
                      sh 'ssh jenkins@3.1.201.220 ls'
                      sh "echo pwd"
                    }
                 }
             }
    
}
}

