pipeline {
    agent any

    // ─── Poll Git every 5 minutes for changes ───────────────────────────────
    triggers {
        pollSCM('H/5 * * * *')
    }

    // ─── Environment variables ───────────────────────────────────────────────
    environment {
        APP_NAME        = 'demo'
        GIT_REPO_URL    = 'https://github.com/SokhomPanha/DevOps-Final-Exam'
        GIT_BRANCH      = 'master'
        CC_EMAIL        = 'sokhompanha70@gmail.com'
        ANSIBLE_PLAYBOOK = 'ansible/playbook.yml'
        ANSIBLE_INVENTORY = 'ansible/inventory.ini'
    }

    stages {

        // ─── Stage 1: Checkout ───────────────────────────────────────────────
        stage('Checkout') {
            steps {
                echo "Checking out branch: ${env.GIT_BRANCH} from ${env.GIT_REPO_URL}"
                git branch: "${env.GIT_BRANCH}",
                    url: "${env.GIT_REPO_URL}"
            }
        }

        // ─── Stage 2: Build ──────────────────────────────────────────────────
        stage('Build') {
            steps {
                echo 'Building application with Maven...'
                sh './mvnw clean package -DskipTests -B'
            }
        }

        // ─── Stage 3: Test ───────────────────────────────────────────────────
        stage('Test') {
            steps {
                echo 'Running tests with SQLite test profile...'
                sh './mvnw -Dspring.profiles.active=test -B test'
            }
            post {
                always {
                    // Publish JUnit test results
                    junit testResults: '**/target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        // ─── Stage 4: Deploy via Ansible ─────────────────────────────────────
        stage('Deploy') {
            steps {
                echo 'Deploying to Web Server via Ansible...'
                sh """
                    ansible-playbook -i ${env.ANSIBLE_INVENTORY} ${env.ANSIBLE_PLAYBOOK} \
                        --extra-vars "app_name=${env.APP_NAME}"
                """
            }
        }

    }

    // ─── Post-pipeline notifications ─────────────────────────────────────────
    post {

        success {
            echo 'Pipeline completed successfully!'
            emailext(
                subject: "[Jenkins] ✅ BUILD SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
<p>Good news! The build and deployment completed successfully.</p>
<ul>
  <li><b>Project:</b> ${env.JOB_NAME}</li>
  <li><b>Build Number:</b> ${env.BUILD_NUMBER}</li>
  <li><b>Branch:</b> ${env.GIT_BRANCH}</li>
  <li><b>Commit:</b> ${env.GIT_COMMIT}</li>
  <li><b>Duration:</b> ${currentBuild.durationString}</li>
</ul>
<p><a href="${env.BUILD_URL}">View Build Details</a></p>
                """,
                mimeType: 'text/html',
                to: "${env.CC_EMAIL}",
                recipientProviders: [[$class: 'DevelopersRecipientProvider']]
            )
        }
        failure {
            echo 'Pipeline FAILED — sending error email...'
            emailext(
                subject: "[Jenkins] ❌ BUILD FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
<p>⚠️ The build has <b>FAILED</b>. Please investigate immediately.</p>
<ul>
  <li><b>Project:</b> ${env.JOB_NAME}</li>
  <li><b>Build Number:</b> ${env.BUILD_NUMBER}</li>
  <li><b>Branch:</b> ${env.GIT_BRANCH}</li>
  <li><b>Commit:</b> ${env.GIT_COMMIT}</li>
  <li><b>Triggered by:</b> ${env.BUILD_CAUSE}</li>
  <li><b>Duration:</b> ${currentBuild.durationString}</li>
</ul>
<p><a href="${env.BUILD_URL}console">View Console Output</a></p>
<p>This email was sent to the committer and CC'd to the team lead.</p>
                """,
                mimeType: 'text/html',
                to: "${env.CC_EMAIL}",
                // Automatically emails the developer who committed the broken code
                recipientProviders: [
                    [$class: 'DevelopersRecipientProvider'],
                    [$class: 'RequesterRecipientProvider']
                ]
            )
        }

        unstable {
            echo 'Pipeline is UNSTABLE (test failures).'
            emailext(
                subject: "[Jenkins] ⚠️ BUILD UNSTABLE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
<p>The build is <b>UNSTABLE</b> — some tests failed.</p>
<ul>
  <li><b>Project:</b> ${env.JOB_NAME}</li>
  <li><b>Build Number:</b> ${env.BUILD_NUMBER}</li>
  <li><b>Branch:</b> ${env.GIT_BRANCH}</li>
  <li><b>Commit:</b> ${env.GIT_COMMIT}</li>
</ul>
<p><a href="${env.BUILD_URL}testReport">View Test Report</a></p>
                """,
                mimeType: 'text/html',
                to: "${env.CC_EMAIL}",
                recipientProviders: [[$class: 'DevelopersRecipientProvider']]
            )
        }

    }
}