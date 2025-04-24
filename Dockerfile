FROM openjdk:22-slim

# Tạo thư mục làm việc
WORKDIR /app

# Copy mã nguồn và file pom.xml vào container
COPY . /app

# Cài đặt Maven nếu chưa có
RUN apt-get update && \
    apt-get install -y maven && \
    mvn clean install

# Chạy ứng dụng
CMD ["java", "-jar", "target/selenium-java-tests.jar"]
