pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                dir('lab4/zoo-fantastico') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                dir('lab4/zoo-fantastico') {
                    sh 'docker build -t zoo-fantastic-app:latest .'
                }
            }
        }
        
        stage('Docker Run') {
            steps {
                sh '''
                    docker rm -f zoo-app || true
                    docker run -d --name zoo-app \
                      --network zoo-network \
                      -p 8081:8080 \
                      -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-zoo:3306/zoodb \
                      -e SPRING_DATASOURCE_USERNAME=zoouser \
                      -e SPRING_DATASOURCE_PASSWORD=zoopass \
                      zoo-fantastic-app:latest
                '''
            }
        }
    }
    post {
        always {
            echo 'Pipeline terminado.'
        }
        success {
            echo '¡Despliegue exitoso!'
        }
        failure {
            echo 'Error en el pipeline. Revisar los logs.'
        }
    }
}
