# IMPORTANT:
# To build the image, run the docker command from the PROJECT ROOT like this:
# docker build -t image-name -f worker/Dockerfile .
# The '-f' flag points to this file, and the final '.' sets the build context to the root.

# ---- Stage 1: Build the application ----
FROM gradle:8.7.0-jdk21 AS builder

WORKDIR /app

# Copy root-level Gradle files.
COPY ../settings.gradle.kts ./

# Copy the build files for each module
COPY ../worker/build.gradle.kts ./worker/
COPY ../shared/build.gradle.kts ./shared/

# Download dependencies for all modules to leverage caching
RUN gradle dependencies --no-daemon

# Copy the rest of the source code from the project root
COPY ../. .

# Build only the 'worker' module's bootJar
RUN gradle :worker:bootJar --no-daemon


# ---- Stage 2: Create the runtime image ----
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/worker/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
