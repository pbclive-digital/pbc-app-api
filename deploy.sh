#!/bin/zsh

SECRETS_PATH=module-remote-datastore/src/main/resources/firebase

STAGING_FILE=$SECRETS_PATH/pbc-live-service-account-key-staging.json
PROD_FILE=$SECRETS_PATH/pbc-live-service-account-key-prod.json

SECRETS_REPO=pbc-app-secrets/
STAGING_SECRET=pbc-app-secrets/firebase-service-account-secrets/pbc-live-service-account-key-staging.json
PROD_SECRET=pbc-app-secrets/firebase-service-account-secrets/pbc-live-service-account-key-prod.json

PROPERTY_FILE=gradle.properties
PROPERTY_KEY=versionName

function fetchApplicationVersion() {
    APP_VERSION=`cat $PROPERTY_FILE | grep "$PROPERTY_KEY" | cut -d"=" -f2 | tr -d " "`
}

function clone_secrets() {
    git clone git@github.com:pbclive-digital/pbc-app-secrets.git

    if [ -d $SECRETS_REPO ]; then
      echo "Clone the secrets successfully."
      return 0
    else
      echo "This environment don't have permission to clone secrets."
      return 1
    fi
}

function check_staging_file_existence() {
  if [ -f $STAGING_FILE ]; then
    return 0
  else
    return 1
  fi
}

function check_prod_file_existence() {
  if [ -f $PROD_FILE ]; then
    return 0
  else
    return 1
  fi
}

function delete_clone_secrets() {
    rm -rf pbc-app-secrets
}

function move_secrets() {
    case $env in
      "staging"|"local")
        if check_staging_file_existence; then
          echo "service account key already available"
        else
          mkdir -p $SECRETS_PATH
          cp $STAGING_SECRET $SECRETS_PATH
        fi
        ;;
      "prod")
        if check_prod_file_existence; then
          echo "service account key already available"
        else
          mkdir -p $SECRETS_PATH
          cp $PROD_SECRET $SECRETS_PATH
        fi
        ;;
      *)
        echo "Not valid environment provided."
        ;;
    esac
}

function check_heroku_session() {
    HEROKU_SESSION=`heroku whoami`
    local emailRegex="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$"
    if [[ $HEROKU_SESSION =~ $emailRegex ]]; then
      return 0
    else
      return 1
    fi
}

function create_procfile_for_staging() {
    echo "web: java -jar -Dspring.profiles.active=staging pbc-api/build/libs/pbc-api-$APP_VERSION.jar --server.port=\$PORT" > Procfile
}

function create_procfile_for_prod() {
    echo "web: java -jar -Dspring.profiles.active=prod pbc-api/build/libs/pbc-api-$APP_VERSION.jar --server.port=\$PORT" > Procfile
}

function heroku_staging_deploy() {
    git add -f Procfile
    git commit -m "Add and commit the Procfile for heroku staging deployment - v$APP_VERSION"
    if check_heroku_session; then
      echo "Heroku Session available for user: [$HEROKU_SESSION]"
    else
      heroku login
    fi
    git push heroku-staging main
}

function heroku_prod_deploy() {
    git add -f Procfile
    git commit -m "Add and commit the Procfile for heroku production deployment - v$APP_VERSION"
    if check_heroku_session; then
      echo "Heroku Session available for user: [$HEROKU_SESSION]"
    else
      heroku login
    fi
    #git push heroku-prod main
}

function deploy_execution() {
    case $env in
      "local")
        ./gradlew clean --no-build-cache bootRun
        ;;
      "staging")
        create_procfile_for_staging
        heroku_staging_deploy
        ;;
      *)
        create_procfile_for_prod
        heroku_prod_deploy
        ;;
    esac
}

## Referring static arguments
args=()
env=$1

## Fetching the application version defined in gradle.properties
fetchApplicationVersion

## Execute the operation according to provided environment
case $env in
  "local")
    if check_staging_file_existence; then
      deploy_execution
    else
      if clone_secrets; then
        move_secrets
        delete_clone_secrets
        deploy_execution
      else
        echo "Can not continue this operation without application secrets."
      fi
    fi
    ;;
  "staging"|"prod")
    deploy_execution
    ;;
  *)
    echo "Given environment [$env] is not valid. Operation ABORT!!!"
    ;;
esac