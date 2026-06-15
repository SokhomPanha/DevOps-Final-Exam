Ansible playbook for deploy/build/test/backup

Usage

- Edit `inventory.ini` and replace `web.example.com` with your server address and set `ansible_user`.
- Adjust variables in `playbook.yml` (for example `repo_path`, `branch`, `deploy_user`, and MySQL credentials).
- Run the playbook locally:

```bash
ansible-playbook -i ansible/inventory.ini ansible/playbook.yml
```

What it does

- Verifies there are no local git changes, then pulls the latest branch.
- Runs `composer install` if `composer.json` exists.
- Runs `npm ci` and `npm run build` if `package.json` exists.
- Runs `mvn test` with the test DB set to a SQLite file (overrides `SPRING_DATASOURCE_URL`).
- Dumps the configured MySQL database to `backup_dir` with a date/time filename.

Notes & safety

- The playbook will fail if there are uncommitted local changes in the repo directory.
- `mysqldump` is executed on the target host; ensure the `mysql` client is installed and credentials are correct.
- Update `repo_path` and user settings to match your environment before running.
