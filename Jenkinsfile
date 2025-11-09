pipeline {
    agent any

    parameters {
        choice(name: 'ENV', choices: ['local', 'qa', 'staging'], description: 'Select target environment')
        choice(name: 'GROUPS', choices: ['all', 'smoke', 'regression'], description: 'TestNG group to execute')
        string(name: 'EMAIL_TO', defaultValue: 'qa-team@company.com', description: 'Comma-separated list of notification emails')

    }

    environment {
        ALLURE_RESULTS = 'build/allure-results'
        ALLURE_HISTORY = 'build/allure-history'
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
                    sh "./gradlew clean test ${groupArg} -Denv=${params.ENV}"
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo "🧾 Generating Allure report..."
                // Ensure allure results directory exists
                sh 'mkdir -p build/allure-results'
                // Generate raw report data
                sh './gradlew allureReport'
            }
        }

        stage('Publish Allure Report') {
            steps {
                echo "📊 Publishing Allure report..."
                allure([
                    includeProperties: false,
                    jdk: '',
                    commandline: 'allure', // 👈 matches the name in "Manage Jenkins → Tools → Allure Commandline"
                    results: [[path: "${ALLURE_RESULTS}"]],
                    reportBuildPolicy: 'ALWAYS'
                ])
            }
        }
    }

    post {
        always {
            echo "📦 Archiving test reports..."
            junit 'build/test-results/test/*.xml'
            archiveArtifacts artifacts: 'build/reports/tests/test/**', fingerprint: true

            // 🧭 Archive Allure results and history to preserve trend graphs
            script {
                if (fileExists("${ALLURE_RESULTS}")) {
                    echo "📚 Archiving Allure results for trend tracking..."
                    archiveArtifacts artifacts: "${ALLURE_RESULTS}/**", fingerprint: true
                }
            }
        }

        success {
            echo "✅ Tests completed successfully!"
        }

        failure {
            echo "❌ Tests failed! Sending email notification..."
            emailext(
                subject: "🚨 API Test Failure - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <html>
                        <body>
                            <p>Hi QA Team,</p>
                            <p><b>${env.JOB_NAME}</b> (Build #${env.BUILD_NUMBER}) has <b>failed</b>.</p>
                            <p>Environment: <b>${params.ENV}</b></p>
                            <p>Test Group: <b>${params.GROUPS}</b></p>
                            <p>See the full Jenkins report and Allure results here:</p>
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
}
