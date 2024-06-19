FROM amazoncorretto:21-alpine AS builder
ARG PROFILE
ARG JASTPY_PASSWORD

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew	#gradlew 실행 권한 부여
RUN ./gradlew bootJar -x test -Pprofile=${PROFILE}	#gradlew를 통해 실행 가능한 jar파일 생성

FROM amazoncorretto:21-alpine
COPY --from=builder build/libs/*.jar booking-beacon.jar
ENV PROFILE dev
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-DJASYPT_PASSWORD=${JASTPY_PASSWORD}" ,"-jar", "/booking-beacon.jar"]
VOLUME /tmp

