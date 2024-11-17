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
            when{
                anyOf {
                    branch 'dev';
                    branch 'release/*';
                }
            }
            steps {
                script{
                    docker.withServer('tcp://172.18.50.38:2375', 'server_access') {
                        customImage = docker.build("uniteltmais/curse")
                    }
                }
            }
        }

        stage ('implantacao') {
            when{
                anyOf {
                    branch 'dev';
                    branch 'release/*';
                }
            }
            steps{
                script{
                    docker.withServer('tcp://172.18.50.38:2375', 'server_access') {
                        try{
                            sh 'docker stop curse1'
                            sh 'docker rm curse1'
                        }catch(e){

                        }

                        sh 'docker run --name curse1 -p 8889:8082 -d uniteltmais/curse:latest'
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