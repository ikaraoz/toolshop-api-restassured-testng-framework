pipeline {
    agent any

    parameters {
        choice(name: 'ENV', choices: ['local', 'qa', 'staging'], description: 'Select target environment')
        choice(name: 'GROUPS', choices: ['all', 'smoke', 'regression'], description: 'TestNG group to execute')
        string(name: 'EMAIL_TO', defaultValue: 'qa-team@company.com', description: 'Comma-separated recipients')
    }

    environment {
        ALLURE_RESULTS = 'build/allure-results'
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
                    sh "./gradlew clean test ${groupArg} -Denv=${params.ENV} || true"
                    // 👆 '|| true' ensures pipeline doesn't abort on test failures
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo "🧾 Generating Allure report..."
                sh './gradlew allureReport || true'
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
            archiveArtifacts artifacts: 'build/reports/tests/test/**', fingerprint: true
            archiveArtifacts artifacts: 'build/allure-results/**', fingerprint: true
        }

        failure {
            echo "❌ Tests failed! Sending email notification..."
            emailext(
                subject: "🚨 API Test Failure - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <html>
                        <body>
                            <p><b>${env.JOB_NAME}</b> (Build #${env.BUILD_NUMBER}) has <b>failed</b>.</p>
                            <p>Environment: <b>${params.ENV}</b></p>
                            <p>Test Group: <b>${params.GROUPS}</b></p>
                            <p>See Jenkins for details:</p>
                            <p><a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        </body>
                    </html>
                """,
                mimeType: 'text/html',
                to: "${params.EMAIL_TO}"
            )
        }

        success {
            echo "✅ Tests completed successfully!"
        }
    }
}
