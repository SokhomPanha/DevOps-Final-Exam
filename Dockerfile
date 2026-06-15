FROM eclipse-temurin:23-jdk

RUN apt update && apt install -y \
    nginx \
    openssh-server \
    php-cli

RUN mkdir /var/run/sshd

COPY nginx/default.conf /etc/nginx/sites-available/default

WORKDIR /app

COPY . .

EXPOSE 22
EXPOSE 8080
EXPOSE 80

CMD service ssh start && \
    nginx && \
    ./mvnw spring-boot:run