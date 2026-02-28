# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/student-management-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "student-management-0.0.1-SNAPSHOT.jar"]
