# Auth Service

> code name: `auth-service`.

A mirco-service for authentication and authorization. Authentication is done by JWT token.

## Framework used

- Spring Boot 2.3
- MyBatis 3.5

## Environment (dependency)

- Java 8+
- MySQL 5.7+
  - database table name prefix: `auth_`.
  - db user: `auth_dbuser`.
  - schema files: `/db`.

## Development Setup

- maven

## Configuration

> this project use spring-boot based app config,
> the config file is defined in `/src/main/resources/application.yaml`

Some key configuration parameter:

- `authService.options.echoLoginInfo`: if true, print debug info for every login process.
- `authService.options.enableDebugEndpoint`: if true, expose debug-use(high risk) endpoint.
- `authService.options.acceptExpiredAuthToken`: if true, auth token expiry time will be ignored.
- `authService.jwt.privateKey_PEM`: private key (in PEM) for signing JWT token.
- `authService.jwt.publicKey_PEM`: public key (in PEM) for verifying JWT token.
- `authService.jwt.tokenValidTimeSec`: how long will a token become expired (in seconds).
- `authService.jwt.tokenIssuer`: token issuer (just a field).

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
curl 'http://127.0.0.1:8080/api/about'
```
