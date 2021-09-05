pipeline { 

    agent { 
        label "linux" 
    }

    tools { 
        maven 'maven-3'
        jdk 'java-11'
    }
    
    environment {
        GIT_BRANCH_REF = "${GIT_BRANCH}"
        GIT_LOCAL_BRANCH = "${GIT_BRANCH}"
    }

    stages { 
        
        stage ('clone') {
            
            steps {
        
                script {
                    GIT_LOCAL_BRANCH = GIT_BRANCH_REF.replace("refs/heads/", "")
                    GIT_LOCAL_BRANCH = GIT_LOCAL_BRANCH.replace("origin/", "")
                }
        
                echo 'Building Branch: ' + GIT_LOCAL_BRANCH
        
                git poll: false,
                    branch: "${GIT_LOCAL_BRANCH}",
                    credentialsId: 'GIT_SSH',
                    url: 'ssh://git@pi4.chux.net:3322/gerrit/pzl-longest-connected-path.git'
                 
            }
                
        }
        
        stage ('verify') {
            steps {
                withMaven (
                    maven: 'maven-3',
                    options: [
                        artifactsPublisher(disabled: true), 
                        findbugsPublisher(disabled: true), 
                        openTasksPublisher(disabled: true),
                        jacocoPublisher(disabled: false),
                        dependenciesFingerprintPublisher(disabled: true),
                        junitPublisher(disabled: true),
                        spotbugsPublisher(disabled: true)
                    ]
                ){
                    sh "mvn -e -ntp -B -U clean verify"
                }
            }
        }

        stage('sonar') {
        
            steps {
                withSonarQubeEnv(installationName: 'sonar') {
                    withMaven (
                        maven: 'maven-3',
                        options: [
                            artifactsPublisher(disabled: true), 
                            findbugsPublisher(disabled: true), 
                            openTasksPublisher(disabled: true),
                            jacocoPublisher(disabled: true),
                            dependenciesFingerprintPublisher(disabled: true),
                            junitPublisher(disabled: true),
                            spotbugsPublisher(disabled: true)
                        ]
                    ){
                        sh "mvn -e -ntp -B -U sonar:sonar"
                    }
                }
            }

        }

        stage("quality") {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('deploy') {
            steps {
                withMaven (
                    maven: 'maven-3',
                    options: [
                        artifactsPublisher(disabled: true), 
                        findbugsPublisher(disabled: true), 
                        openTasksPublisher(disabled: true),
                        jacocoPublisher(disabled: true),
                        dependenciesFingerprintPublisher(disabled: true),
                        junitPublisher(disabled: true),
                        spotbugsPublisher(disabled: true)
                    ]
                ){
                    sh "mvn -e -ntp -DskipTests=true -Dmaven.skip.test=true -B -U clean deploy"
                }
            }
        }

    }

    post {
    
        success {
            build   job: '/CHUX/update-grok', 
                    parameters: [
                        [
                            $class: 'StringParameterValue', 
                            name: 'GROK_PROJECT', 
                            value: 'cs-puzzles'
                        ],
                        [
                            $class: 'StringParameterValue', 
                            name: 'GIT_REPOSITORY', 
                            value: 'pzl-longest-connected-path'
                        ],
                        [
                            $class: 'StringParameterValue', 
                            name: 'GIT_BRANCH', 
                            value: "${GIT_LOCAL_BRANCH}"
                        ]
                    ]
             build	job: '/CHUX/update-code', 
                    parameters: [
                        [
                            $class: 'StringParameterValue', 
                            name: 'CODE_PROJECT', 
                            value: 'compsci/puzzles'
                        ],
                        [
                            $class: 'StringParameterValue', 
                            name: 'GIT_REPOSITORY', 
                            value: 'pzl-longest-connected-path'
                        ],
                        [
                            $class: 'StringParameterValue', 
                            name: 'GIT_BRANCH', 
                            value: "${GIT_LOCAL_BRANCH}"
                        ]
                    ]
        }

    }

}
