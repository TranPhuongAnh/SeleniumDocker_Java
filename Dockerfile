FROM maven:3.9.8-eclipse-temurin-22-alpine

# Tạo thư mục làm việc
WORKDIR /app

# Copy mã nguồn và file pom.xml vào container
COPY . /app

RUN mvn clean install

## Copy source code vào container
#COPY src/main/java /app/src
#
## Biến ENV để lưu tên file main cần chạy
#ENV MAIN_CLASS=org.example.Main
#
## Compile Java source
#RUN javac src/org/example/Main.java

# Chạy ứng dụng
CMD ["java", "-cp", "src", "org.example.Main"]
