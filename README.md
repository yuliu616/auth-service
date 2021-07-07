# Auth Service

> code name: `auth-service`.

A micro-service for authentication and authorization. Authentication is done by JWT token.

## Framework used

- Spring Boot 2.3
- MyBatis 3.5

## Environment (dependency)

- Java 8+ (runtime)
- MySQL 5.7+ (service = hellodb:3306)
  - database table name prefix: `auth_`.
  - db user: `auth_dbuser`.
  - schema files: `/db`.

## Development Setup

- maven

## Configuration

> this project use spring-boot based app config,
> the config file is defined in `/src/main/resources/application.yaml`

Some key configuration parameter:

- `auth-service.options.echo-login-info`: if true, print debug info for every login process.
- `auth-service.options.enable-debug-endpoint`: if true, expose debug-use(high risk) endpoint.
- `auth-service.options.accept-expired-auth-token`: if true, auth token expiry time will be ignored.
- `auth-service.jwt.private-key-PEM`: private key (in PEM) for signing JWT token.
- `auth-service.jwt.public-key-PEM`: public key (in PEM) for verifying JWT token.
- `auth-service.jwt.token-valid-time-sec`: how long will a token become expired (in seconds).
- `auth-service.jwt.token-issuer`: token issuer (just a field).

## Debugging

```
mvn spring-boot:run
```

## Testing

- mocha test
```
cd src/test/mocha-test
npm test
```

## Building

```
mvn package
```

## Running (production)

```
java -jar target/auth-service-1.0.0.jar 
```

## Entry points

> detail refer to `/doc/auth-service.raml`

## Health checking

```
curl 'http://127.0.0.1:8080/api/1.0/about'
```

# Docker support

## building docker image

- after building the app (maven), run this:

```sh
docker build -t auth-service:1.0 .
```
