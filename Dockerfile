FROM eclipse-temurin:23-jdk

RUN apt update && apt install -y \
    nginx \
    openssh-server \
    php-cli \
    sudo \
    git

# set root password and allow root login via SSH
RUN echo "root:123" | chpasswd
RUN sed -i 's/^#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config || true
RUN sed -i 's/^PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config || true
RUN sed -i 's/^#PasswordAuthentication yes/PasswordAuthentication yes/' /etc/ssh/sshd_config || true
RUN sed -i 's/^PasswordAuthentication no/PasswordAuthentication yes/' /etc/ssh/sshd_config || true

RUN mkdir /var/run/sshd

# create a deploy user with sudo privileges
RUN useradd -m -s /bin/bash deploy && echo "deploy:deploy" | chpasswd && usermod -aG sudo deploy

COPY nginx/default.conf /etc/nginx/sites-available/default

WORKDIR /app

COPY . .

EXPOSE 22
EXPOSE 8080
EXPOSE 80

CMD service ssh start && \
    nginx && \
    ./mvnw spring-boot:run