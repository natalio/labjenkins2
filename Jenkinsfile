def customImage

pipeline {
    agent any
    environment {
        COMPOSITE_APPLICATION_PATH = "${env.WORKSPACE}"
    }
    stages {
        stage ("Configurando ambiente") {
            steps {
                script{
                    echo 'BRANCH_NAME...' + env.BRANCH_NAME
                }
            }
        }
    	stage ('Contruindo e Tentando') {
            parallel {
                stage ("Contruindo Jar") {
                    steps {

                        withMaven(
                            maven: 'maven-3',
                            mavenLocalRepo: '.repository',
                            traceability: true
                        ) {
                            sh "mvn clean install -DskipTests"
                        }
                    }
                }
                stage ("Tentando") {
                    steps {
                        withMaven(
                            maven: 'maven-3',
                            mavenLocalRepo: '.repository',
                            traceability: true
                        ) {
                            sh "mvn test"
                        }
                    }
                }
            }
        }

        stage('Preparando implantacao') {
            steps {
                script{
                    docker.withServer('tcp://172.18.50.38:2375', 'server_access') {
                        customImage = docker.build("uniteltmais/auth2")
                    }
                }
            }
        }

        stage ('Implantando') {
            steps{
                script{
                    docker.withServer('tcp://172.18.50.38:2375', 'server_access') {
                        sh 'docker stop auth2test'
                        sh 'docker rm auth2test'
                        sh 'docker run --name auth2test -p 8888:8080 -d uniteltmais/auth2:latest'
                    }
                }
            }
        }

    }

    post {
        cleanup {
            cleanWs()
        }
        success {
            echo 'Conclido com success'
        }
        failure{
            echo 'Conclido com falha'
        }
    }

}