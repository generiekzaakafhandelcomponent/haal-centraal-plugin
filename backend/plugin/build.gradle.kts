/*
 * Copyright 2015-2025 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

val haalCentraalAuthVersion: String by project
val kotlinLoggingVersion: String by project
val mockitoKotlinVersion: String by project
val reactorNettyVersion: String by project
val springWebfluxVersion: String by project

dockerCompose {
    setProjectName("HaalCentraal")
    isRequiredBy(project.tasks.integrationTesting)

    tasks.integrationTesting {
        useComposeFiles.addAll("$rootDir/docker-resources/docker-compose-base-test.yml")
    }
}

dependencies {
    compileOnly("com.ritense.valtimo:core")
    compileOnly("com.ritense.valtimo:object-management")
    compileOnly("com.ritense.valtimo:plugin")
    compileOnly("com.ritense.valtimo:plugin-valtimo")

    compileOnly("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")

    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")
    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    compileOnly("com.ritense.valtimoplugins:haal-centraal-authentication:$haalCentraalAuthVersion")

    // Netty and WebClient
    implementation("io.projectreactor.netty:reactor-netty-core:$reactorNettyVersion")
    implementation("io.projectreactor.netty:reactor-netty-http:$reactorNettyVersion")
    compileOnly("org.springframework:spring-webflux:$springWebfluxVersion")

    // Testing
    testImplementation("com.ritense.valtimo:building-block")
    testImplementation("com.ritense.valtimo:contract")
    testImplementation("com.ritense.valtimo:core")
    testImplementation("com.ritense.valtimo:plugin")
    testImplementation("com.ritense.valtimo:temporary-resource-storage")
    testImplementation("com.ritense.valtimo:test-utils-common")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.postgresql:postgresql")
    testImplementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")

    testImplementation("com.ritense.valtimo:plugin-valtimo")
    testImplementation("com.ritense.valtimoplugins:haal-centraal-authentication:$haalCentraalAuthVersion") {
        isTransitive = false
    }
}

apply(from = "gradle/publishing.gradle")
