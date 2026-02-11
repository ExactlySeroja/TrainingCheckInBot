plugins {
	java
	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.seroja"
version = "0.0.1-SNAPSHOT"
description = "Practice project"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
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
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.telegram:telegrambots:6.9.7.1")
	implementation("org.telegram:telegrambots-spring-boot-starter:6.9.7.1")
	implementation("org.telegram:telegrambots-springboot-longpolling-starter:9.2.1")
	implementation("org.telegram:telegrambots-client:9.2.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
