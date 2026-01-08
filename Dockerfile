# Stage 1: Build the WAR with Maven
FROM maven:3.9.6-amazoncorretto-17 AS builder

WORKDIR /app

# Copy necessary files
COPY pom.xml .
COPY local.properties .
COPY src ./src

# Now build without overrides (properties plugin will read the modified file)
RUN mvn clean package

# Stage 2: Deploy to Tomcat 10.1.8
FROM tomcat:10.1.8-jdk17

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the built WAR from the builder stage
COPY --from=builder /app/target/taxi-brouse.war /usr/local/tomcat/webapps/ROOT.war

# Expose Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]