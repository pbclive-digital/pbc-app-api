# PBC-app-api
This is a REST-API build on top of sprint-boot framework. This catering set of end-points for Pittsburgh Buddhist Center.  This is using by mobile app 
and web app to deliver few of functionalities to devotes.

## Prerequisites
 * Java 24
 * Heroku CLI

## Technologies & Tools
 * Kotlin -- v2.2.0
 * Gradle -- v8.14.3
 * Sprint-Boot -- 4.0.0-M1
 * Firebase - FireStore

## Build
Build the code by running following command.
<br />
````
$ ./gradlew clean --no-build-cache build
````

## New Development
In this application development, we are trying to follow trunk-base approach for branching. Therefore, if there is any new development or bug-fx or hot-fix
available, our development branch will be `main` branch.

## Deploy
Primary deployment is in Heroku dyno. For this application has two environments. One is for staging and other one is for
production. Both environments are in Heroku.

### Set Heroku ENVIRONMENTAL VARIABLES
For the application functionality with firebase integration, Before we deploy, make sure in the heroku application, set the following environmental variables
* `GOOGLE_CREDENTIALS` -> Google Firebase `google-service-account.json` file content
* `PBC_ENV` -> According to the deployment environment, set the value as `staging` o `prod`.

> [!NOTE]
> This values already set in the existing heroku application. So no need to worry about that. But in case of new deployment, need to make sure these set-up correctly.

### Deploy any custom branch to Staging env to testing.
If we need to deploy a change to staging server (QA) without merging to develop branch as a release, use following command
to deploy feature branch to staging server.
````
   git push heroku-staging <feature-development>:master
````

### Release Naming
Application is following pattern to name the releases. It built with release year, major or feature drop version, minor or bug-fix.
````
Pattern : <release-year>.<major/feature-drop>.<minor/bug-fx> 
Example : 2025.1.0
````

### Use `depploy.sh` script for different deployment scenarios.
This `deploy.sh` script is created for make the deployment process easy to all developers. Currently, developer can execute the `deploy.sh` script as follows.
````
    ./deploy.sh <env-name-to-deploy>
````

This `env-name-to-deploy` is an argument while execute the script. From that script will identify which environment that developer want to deploy the new app.
Following are the available options developer can pass as environment.

|option (env)  |Description    |
|--------------|---------------|
|local         |This is to run the application in local system. |
|staging       |This is to deploy the application into staging heroku application|
|prod          |This is to deploy the application into production heroku application|

#### Prerequisites for `deploy.sh`
- Bash/Unix environment (Mac/Linux)
- Access to `pbc-app-secrets` repository.
- Heroku CLI installed in the system
- Need to add the `heroku-staging` & `heroku-prod` remotes to the local git repository.

## Notes
 * All tool version is configured in `pbc-app-api/gradle/version-catalog/libs.versions.toml`.
 * To Change Java version, make sure to change it in `libs.versions.toml` & `settings.properties`. By changing in `libs.versions.toml` will apply changes to 
   gradle and the build configurations and by changing in `settings.properties` will define version to Heroku.
   - In libs.versions.toml
     ````
       [versions]
        ...
        jvmTarget = "24"
        ...
     ````
   - In settings.properties
     ````
       # Java Runtime version definition for Heroku
       java.runtime.version=24
     ````

### Debug using IntelliJ
 * Before attached debugger in IntelliJ, open the `AppProperties.kt` file and hardcode the values to `appEnv="staging"`.
   While debugging, it failed to read the properties. 

