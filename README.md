# PBC-app-api
This is a REST-API build on top of sprint-boot framework. This catering set of end-points for Pittsburgh Buddhist Center.  This is using by mobile app 
and web app to deliver few of functionalities to devotes.

### Prerequisites
 * Java 24
 * Heroku CLI

### Technologies & Tools
 * Kotlin -- v2.2.0
 * Gradle -- v8.14.3
 * Sprint-Boot -- 4.0.0-M1
 * Firebase - FireStore

### Build
Build the code by running following command.
<br />
````
$ ./gradlew clean --no-build-cache build
````

### New Development
In this application development, we are trying to follow trunk-base approach for branching. Therefore, if there is any new development or bug-fx or hot-fix
available, our development branch will be `main` branch.

#### Release Naming
Application is following pattern to name the releases. It built with release year, major or feature drop version, minor or bug-fix.
````
Pattern : <release-year>.<major/feature-drop>.<minor/bug-fx> 
Example : 2025.1.0
````

### Notes
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

