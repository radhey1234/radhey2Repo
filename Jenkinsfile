
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
               sh 'scp -o StrictHostKeyChecking=no -i singapurkey.pem radhey ec2-user@3.0.92.136:/home/ec2-user/'
                    }
        }
    
        stage ('deploy'){
        steps {
            
           sh 'scp -o StrictHostKeyChecking=no -i singapurkey.pem target/iExpress-0.0.1-SNAPSHOT.war  ec2-user@3.0.92.136:/opt/apache-tomcat-8.5.72/webapps/'
            sh """
            ssh -i singapurkey.pem ec2-user@3.0.92.136
            
            """
        }
    
}
         stage('Build Image') {
             steps {
                 script {
                     sshagent(['f3283119-b670-4115-a930-c441abb9909d']) {
    // some block
}
                     {
                        sh "echo pwd"
                        sh 'ssh -t -t jenkins@3.0.92.136 -o StrictHostKeyChecking=no'
                        sh "echo pwd"
                        sh 'sudo -i -u root'
                        sh 'cd /opt/'
                        sh 'echo pwd'
                    }
                 }
             }
    
}
}

