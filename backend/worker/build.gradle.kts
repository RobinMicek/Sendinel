plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "cz.promptly"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(project(":shared"))

	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")

	annotationProcessor("org.projectlombok:lombok")

	compileOnly("org.projectlombok:lombok")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Senders
	implementation("org.springframework.boot:spring-boot-starter-mail")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
