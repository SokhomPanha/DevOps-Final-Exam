# Demo Spring Boot Terrains Project

This is a sample Spring Boot application with entities for Terrain, TerrainImage, Booking, Payment, Review, and Favorite.

Configure MySQL in `src/main/resources/application.properties` and run:

```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.war
```

To push to GitHub and add a collaborator, create a remote repository and run:

```bash
git remote add origin https://github.com/<your-user>/<repo>.git
git push -u origin main
# Add collaborator via GitHub web UI or API/gh CLI
```
