#!/bin/zsh

STAGING_FILE=module-firebase/src/main/resources/firebase/pbc-live-service-account-key-staging.json
PROD_FILE=module-firebase/src/main/resources/firebase/pbc-live-service-account-key-prod.json

STAGING_SECRET=pbc-app-secrets/firebase-service-account-secrets/pbc-live-service-account-key-staging.json
PROD_SECRET=pbc-app-secrets/firebase-service-account-secrets/pbc-live-service-account-key-prod.json

SECRETS_PATH=module-firebase/src/main/resources/firebase/

function clone_secrets() {
    git clone git@github.com:pbclive-digital/pbc-app-secrets.git
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
    if [[ $env == "staging" ]]; then
      if check_staging_file_existence; then
        echo "service account key already available"
      else
        cp $STAGING_SECRET $SECRETS_PATH
      fi
    else
      if [[ $env == "prod" ]]; then
        if check_prod_file_existence; then
          echo "service account key already available"
        else
          cp $STAGING_SECRET $SECRETS_PATH
        fi
      fi
    fi
}

## Referring static arguments
args=()
env=$1

## Execute
clone_secrets
move_secrets $env
delete_clone_secrets