plugins {
	java
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.volodymyr.test"
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
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-test")
	implementation("org.liquibase:liquibase-core")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.assertj:assertj-core:3.26.3")
	implementation("org.wiremock.integrations:wiremock-spring-boot:3.2.0")
	implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.9.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.4.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
