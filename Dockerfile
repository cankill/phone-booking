FROM gradle:7.1-jdk11 AS build
COPY ./src /home/app/src
COPY ./build.gradle /home/app/build.gradle
COPY ./settings.gradle /home/app/settings.gradle
WORKDIR /home/app
RUN gradle clean build

FROM ghcr.io/graalvm/graalvm-ce:java11-21.1.0
RUN microdnf install nc
RUN mkdir /app
RUN groupadd -r phonebooking && useradd -r -s /bin/false -g phonebooking phonebooking
RUN chown -R phonebooking:phonebooking /app
WORKDIR /app
COPY dependencies/config/wait-for.sh wait-for.sh
RUN chmod +x wait-for.sh
COPY --from=build /home/app/build/libs/PhoneBooking-*.jar phone-booking.jar
USER phonebooking
ENTRYPOINT ["/bin/bash", "-c", "/app/wait-for.sh ${POSTGRES_HOST:-postgres}:${POSTGRES_PORT:-5432} -t 15 -- java -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI -XX:+UseJVMCICompiler -jar /app/phone-booking.jar"]