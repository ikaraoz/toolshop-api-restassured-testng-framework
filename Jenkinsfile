pipeline {
    agent any

    parameters {
        choice(name: 'ENV', choices: ['local', 'qa', 'staging'], description: 'Select target environment')
        choice(name: 'GROUPS', choices: ['all', 'smoke', 'regression'], description: 'TestNG group to execute')
        string(name: 'EMAIL_TO', defaultValue: 'qa-team@company.com', description: 'Comma-separated list of email recipients')
    }

    environment {
        ALLURE_RESULTS = 'build/allure-results'
        ALLURE_REPORT  = 'build/reports/allure-report'
    }

    triggers {
        // Nightly at ~2 AM
        cron('H 2 * * *')
        // Optional: run when main branch updates
        // pollSCM('H/5 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                echo "📦 Checking out source code..."
                checkout scm
            }
        }

        stage('Setup') {
            steps {
                echo "⚙️ Setting up environment for ${params.ENV}"
                sh 'chmod +x gradlew'
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    echo "🚀 Running TestNG tests for environment: ${params.ENV}"
                    def groupArg = params.GROUPS != 'all' ? "-Dgroups=${params.GROUPS}" : ""

                    // Capture exit code instead of failing immediately
                    def status = sh(script: "./gradlew clean test ${groupArg} -Denv=${params.ENV}", returnStatus: true)

                    // Always try generating Allure report
                    sh './gradlew allureReport'

                    if (status != 0) {
                        currentBuild.result = 'FAILURE'
                        echo "❌ Some tests failed. Check reports for details."
                    } else {
                        echo "✅ All tests passed successfully."
                    }
                }
            }
        }

        stage('Publish Allure Report') {
            steps {
                echo "📊 Publishing Allure report..."
                allure includeProperties: false, jdk: '', results: [[path: "${ALLURE_RESULTS}"]]
            }
        }
    }

    post {
        always {
            echo "📦 Archiving test reports..."
            junit 'build/test-results/test/*.xml'
            archiveArtifacts artifacts: 'build/reports/**', fingerprint: true

            // Send email regardless of build outcome
            script {
                def statusEmoji = currentBuild.result == 'FAILURE' ? "❌" : (currentBuild.result == 'SUCCESS' ? "✅" : "⚠️")
                emailext(
                    subject: "${statusEmoji} API Test Report - ${env.JOB_NAME} #${env.BUILD_NUMBER} (${currentBuild.result})",
                    body: """
                        <html>
                            <body>
                                <p>Hi QA Team,</p>
                                <p>Job <b>${env.JOB_NAME}</b> (Build #${env.BUILD_NUMBER}) finished with status:
                                   <b style="color:${currentBuild.result == 'FAILURE' ? 'red' : 'green'}">${currentBuild.result}</b></p>
                                <p><b>Environment:</b> ${params.ENV}</p>
                                <p><b>Test Group:</b> ${params.GROUPS}</p>
                                <p><b>Allure Report:</b> (if available)</p>
                                <p><a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                                <br/>
                                <p>-- Jenkins CI</p>
                            </body>
                        </html>
                    """,
                    mimeType: 'text/html',
                    to: "${params.EMAIL_TO}"
                )
            }
        }

        failure {
            echo "⚠️ Build marked as failed. Email notification already sent."
        }

        success {
            echo "✅ Build succeeded. Email notification sent."
        }
    }
}
