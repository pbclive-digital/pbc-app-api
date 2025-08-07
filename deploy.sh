#!/bin/zsh

STAGING_FILE=module-remote-datastore/src/main/resources/firebase/pbc-live-service-account-key-staging.json
PROD_FILE=module-remote-datastore/src/main/resources/firebase/pbc-live-service-account-key-prod.json

SECRETS_REPO=pbc-app-secrets/
STAGING_SECRET=pbc-app-secrets/firebase-service-account-secrets/pbc-live-service-account-key-staging.json
PROD_SECRET=pbc-app-secrets/firebase-service-account-secrets/pbc-live-service-account-key-prod.json

SECRETS_PATH=module-remote-datastore/src/main/resources/firebase/

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
          cp $STAGING_SECRET $SECRETS_PATH
        fi
        ;;
      "prod")
        if check_prod_file_existence; then
          echo "service account key already available"
        else
          cp $PROD_SECRET $SECRETS_PATH
        fi
        ;;
      *)
        echo "Not valid environment provided."
        ;;
    esac
}

function deploy_execution() {
    case $env in
      "local")
        ./gradlew clean --no-build-cache bootRun
        ;;
      "staging")
        #git add -f module-remote-datastore/src/main/resources/firebase/pbc-live-service-account-key-staging.json
        #git commit -m "Add the secret key file for heroku deployment"
        #git push heroku-staging main
        #git reset HEAD~
        ;;
      *)
        echo "Coming Soon"
        ;;
    esac
}

## Referring static arguments
args=()
env=$1

## Execute
if clone_secrets; then
  move_secrets
  delete_clone_secrets
  deploy_execution
else
  echo "Can not continue this operation without application secrets."
fi