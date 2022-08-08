import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("com.bmuschko.docker-remote-api") version "7.4.0"
	id("org.springframework.boot") version "2.7.2"
	id("io.spring.dependency-management") version "1.0.12.RELEASE"
	kotlin("jvm") version "1.7.10"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.serialization") version "1.7.10"
}

group = "com.ajvm"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	jcenter()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
	implementation("com.google.code.gson:gson:2.9.1")
	implementation("io.github.cdimascio:dotenv-kotlin:6.3.1")

	implementation("org.springframework.boot:spring-boot-starter-data-redis:2.7.3")
//	implementation("redis.clients:jedis:4.3.0-m1")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.+")

	implementation("org.jetbrains.exposed:exposed-core:0.38.2")
	implementation("org.jetbrains.exposed:exposed-dao:0.38.2")
	implementation("org.jetbrains.exposed:exposed-jdbc:0.38.2")
	runtimeOnly("org.postgresql:postgresql")
	implementation("com.expediagroup", "graphql-kotlin-spring-server", "6.0.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
