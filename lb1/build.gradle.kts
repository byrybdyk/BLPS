plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("jvm")
}

group = "com.zarubovandlevchenko"
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
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	implementation("javax.transaction:javax.transaction-api:1.3")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.17.0")

	implementation("com.atomikos:transactions-jta:6.0.0:jakarta")
	implementation("com.atomikos:transactions-jdbc:6.0.0:jakarta")
	implementation("com.atomikos:transactions-jms:6.0.0:jakarta")

	implementation(kotlin("stdlib-jdk8"))
}


tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.withType<JavaCompile> {
	options.compilerArgs.add("-Xlint:-deprecation")
}
