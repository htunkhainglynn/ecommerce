FROM maven:3.8.5-openjdk-17

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Build the application
RUN mvn -B clean verify -DskipTests

# Expose port 8080
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "target/ecommerce-0.0.1-SNAPSHOT.jar"]
