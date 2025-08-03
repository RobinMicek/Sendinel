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

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
	implementation("org.postgresql:postgresql:42.7.7")
	implementation("org.flywaydb:flyway-core:11.10.2")
	implementation("org.flywaydb:flyway-database-postgresql:11.10.2")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("com.warrenstrange:googleauth:1.5.0")
	implementation("org.modelmapper:modelmapper:2.4.2")
	implementation("com.google.zxing:core:3.5.2")
	implementation("com.google.zxing:javase:3.5.2")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.bouncycastle:bcprov-jdk15on:1.70")
	implementation("org.apache.commons:commons-compress:1.26.0")
	implementation("com.github.erosb:everit-json-schema:1.14.6")
	implementation("org.json:json:20240303")
	implementation("com.github.jknack:handlebars:4.3.1")
	implementation("com.openhtmltopdf:openhtmltopdf-core:1.0.10")
	implementation("com.openhtmltopdf:openhtmltopdf-pdfbox:1.0.10")

	compileOnly("org.projectlombok:lombok")

	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-gson:0.11.5")

	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
