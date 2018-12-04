# Scheduling and listing API
[![Build Status](https://travis-ci.org/hmcts/snl-api.svg?branch=master)](https://travis-ci.org/hmcts/snl-api)

## Purpose

Scheduling and Listing project provides application for managing sessions, listing requests and hearings.
The purpose of this service is to provide endpoints for and work with the [frontend](https://github.com/hmcts/snl-frontend). 

## What's inside

It contains:
 * application
 * database schema change-sets using [Liquibase](https://www.liquibase.org/)
 * docker setup
 * swagger configuration for api documentation
 * MIT license and contribution information

The application exposes health endpoint (http://localhost:8090/health) and metrics endpoint
(http://localhost:8090/metrics).

## Building the application

The project uses [Gradle](https://gradle.org) as a build tool. It already contains
`./gradlew` wrapper script, so there's no need to install gradle.

To build the project execute the following command:

```bash
  ./gradlew build
```

## Running the application

Create the image of the application by executing the following command:

```bash
  ./gradlew bootRepackage
```

Create docker image:

```bash
  docker-compose build
```

### Running Locally (Recommended)

For this approach, the database must still be served via docker:
```bash
  docker-compose up snl-api-db
```

The application can be run locally using IntelliJ or by executing the following command (in another terminal window):
```bash
  ./gradlew bootRun
```

### Running in Docker

Run the distribution (created in `build/libs` directory)
by executing the following command:

```bash
  docker-compose up
```

This will start the API container exposing the application's port `8090` and PostgreSql database.


In order to test if the application is up, you can call its health endpoint:

```bash
  curl http://localhost:8090/health
```

You should get a response similar to this:

```
  {"status":"UP"}
```

### Alternative script to run application in Docker

To skip all the setting up and building, just execute the following command:

```bash
  ./bin/run-in-docker.sh
```

For more information:

```bash
  ./bin/run-in-docker.sh -h
```

Script includes bare minimum environment variables necessary to start api instance. Whenever any variable is changed or any other script regarding docker image/container build, the suggested way to ensure all is cleaned up properly is by this command:

```bash
  docker-compose rm
```

It clears stopped containers correctly. Might consider removing clutter of images too, especially the ones fiddled with:

```bash
  docker images

  docker image rm <image-id>
```

There is no need to remove postgres and java or similar core images.

## Testing and Preparing for Pull Requests

Before creating a PR, ensure that all of the code styling checks and tests have been done locally (they will be caught on Jenkins if there are any discrepancies)

### 1. Code Style

```bash
./gradlew checkStyleMain

./gradlew checkStyleIntegration

./gradlew checkStyleTest
```

### 2. Testing

```bash
./gradlew test
```

 
## Postman Collections

The ./tools/postman-collections contains a set of files to load into postman: collections, globals, environments.

### Envs

The hostname and port are parametrized and taken from postman's environment variables. At this point there are two envs: 'Local' and 'AAT-master'
(You need a properly configured proxy to execute call from Postman to Azure environments like AAT)

### Sign In

1. Set username and password in globals
2. Execute sign-in request
3. The access token is saved in globals and appended later on to every request

### Actions and entity modifications

Complex transaction mechanism requires keeping track of transactionIds, versions, commit and rollback etc.
To ease that, postman globals keep track of recent transactionId and recently modified entity (namely: its 'id' and 'version')
These variables are injected into bodies of other requests, ie:
1. Create a session -> transactionId and recentSessionId variables are saved as globals
2. Commit/Rollback transaction requests have the latest transactionId injected into their bodies automatically
3. Get session by id -> obtain created session and save its 'version' value to globals to avoid OptimisticLockConflicts in further requests
3. Amend a session -> 'recentSessionId' and 'version' are injected into its body, so you can easily modify previously created session
4. Commit/Rollback

## Other
### Check dependencies updates

Task to determine which dependencies have updates. Usage:

    ```bash
      ./gradlew dependencyUpdates -Drevision=release
    ```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
