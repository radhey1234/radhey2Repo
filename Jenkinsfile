
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
               sh 'scp -o StrictHostKeyChecking=no -i singapurkey.pem radhey ec2-user@54.169.153.161:/home/ec2-user/'
                    }
        }
    
        stage ('deploy'){
        steps {
            
           sh 'scp -o StrictHostKeyChecking=no -i singapurkey.pem target/iExpress-0.0.1-SNAPSHOT.war  ec2-user@54.169.153.161:/opt/apache-tomcat-8.5.72/webapps/'
                   sshagent(['f3283119-b670-4115-a930-c441abb9909d']) {
    // some block
               sh """
               ssh ec2-user@13.229.201.1 /opt/starttomcat

             """
}

}
        }
    
}
    
}

