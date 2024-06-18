FROM amazoncorretto:21-alpine AS builder
ARG PROFILE

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew	#gradlew 실행 권한 부여
RUN ./gradlew bootJar -x test -Pprofile=${PROFILE}	#gradlew를 통해 실행 가능한 jar파일 생성

