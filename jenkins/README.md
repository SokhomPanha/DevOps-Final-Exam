Jenkins setup (Option A) — ready-to-import job

Files added:
- `jenkins/jenkins-job.xml` — importable job config (pipeline from SCM)
- `Jenkinsfile` (project root) — pipeline script (polls SCM, builds, tests, deploys)

Quick checklist (what Jenkins needs)
- Jenkins master (or agent) with: Java, Git, Docker (optional), Maven (or use `./mvnw`), Ansible installed on agent that runs the playbook.
- Plugins to install (suggested): `pipeline`, `workflow-aggregator`, `git`, `email-ext`, `credentials`, `ssh-credentials`, `ansicolor` (optional), `pipeline-stage-view`.

Create credentials
1. Create Git credential in Jenkins (Credentials → System → Global) with ID `git-creds` (SSH private key or username/password). Replace in `jenkins-job.xml` if you choose a different id.
2. Ensure the agent has SSH access to the `springboot-web` container or target servers for Ansible. Use SSH credentials in Jenkins if needed.

Import job (UI)
1. Jenkins → New Item → enter a name (e.g. `demo-pipeline`) → Pipeline → OK
2. In the job config select "Pipeline script from SCM"
   - SCM: Git
   - Repository URL: your repo URL (or leave blank if you imported XML)
   - Credentials: select the `git-creds` you created
   - Branches to build: `*/master` (or `*/main`)
   - Script Path: `Jenkinsfile`
3. Save. Job will poll SCM per `Jenkinsfile` or per job trigger.

Import job (CLI)
1. Download Jenkins CLI jar from your Jenkins: `wget http://<JENKINS_URL>/jnlpJars/jenkins-cli.jar`
2. Create the job:
```bash
java -jar jenkins-cli.jar -s http://<JENKINS_URL>/ create-job demo-pipeline < jenkins/jenkins-job.xml
```

Email notifications
- Configure SMTP in Jenkins (Manage Jenkins → Configure System → E-mail Notification / Extended E-mail). The pipeline uses `emailext` in the `Jenkinsfile` — ensure `email-ext` plugin is installed and SMTP is configured.

Agent requirements
- `ansible` installed and reachable inventory paths (or run playbook from master if it has Ansible).
- Tools: `maven` (or `./mvnw` in repo), `git`, `docker` (if you run container tasks), `ssh` client.

Notes & tips
- The `Jenkinsfile` in the repo already polls SCM every 5 minutes (via `pollSCM('H/5 * * * *')`). The job-level trigger is included in `jenkins-job.xml` as well.
- Update `jenkins-job.xml` replacing `GIT_REPO_URL` with your Git URL if you want to import the XML directly.
- For secure operation, prefer SSH keys for Git access and create secrets in Jenkins credentials store.
