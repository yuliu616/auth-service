FROM openjdk:15.0.2-oraclelinux7

# suggested mounting point:
# - app jar: /usr/src/app/app.jar
# - app config file: /usr/src/app/application.yaml
# - logger config file: /usr/src/app/logback.xml
# - log folder (output): /usr/src/app/log

ENV PORT=8080

# Create app directory
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# Bundle app source
COPY target/auth-service-1.0.0.jar /usr/src/app/app.jar
COPY src/main/resources/application.yaml /usr/src/app/
COPY src/main/resources/logback.xml /usr/src/app/

EXPOSE ${PORT}
HEALTHCHECK --interval=10s --timeout=5s \
  CMD curl -f http://127.0.0.1:${PORT}/api/about || exit 1

CMD java -jar /usr/src/app/app.jar \
  --spring.config.location=classpath:/application.yaml,file:/usr/src/app/application.yaml \
  --logging.config=/usr/src/app/logback.xml

# build with this:
# > docker build -t auth-service:1.0 .