#!/bin/zsh

STAGING_REMOTE=https://git.heroku.com/pbc-api-staging.git
PROD_REMOTE=https://git.heroku.com/pbc-api-prod.git

function setup_git_remotes() {
  # Add staging remote
  git remote add heroku-staging $STAGING_REMOTE

  # Add prod remote
  git remote add heroku-prod $PROD_REMOTE

  printf "\n=====================================================================\n"
  printf "Successfully added remotes for staging and production deployments.\n
          For use them developer needs to be a contributor of relevant heroku accounts."
  printf "\n=====================================================================\n"
}

function is_required_properties_available() {
    if grep -q "PBC_MAIL_USERNAME" "local.properties"; then
        return 0
    else
        return 1
    fi
}

function setup_local_properties() {
    if [ -f "./local.properties" ]; then
      if ! is_required_properties_available; then
          echo "# Mail server username config" >> local.properties
          echo "PBC_MAIL_USERNAME=<add email address>" >> local.properties
          echo "# Mail server passcode config" >> local.properties
          echo "PBC_MAIL_PASSWORD=<add email passcode>" >> local.properties
      fi
    else
      touch local.properties
      echo "# Mail server username config" > local.properties
      echo "PBC_MAIL_USERNAME=<add email address>" >> local.properties
      echo "# Mail server passcode config" >> local.properties
      echo "PBC_MAIL_PASSWORD=<add email passcode>" >> local.properties
    fi

    printf "\n=====================================================================\n"
    printf "For local debugging and running purpose, local.properties file was created with required config parameters.\n
            Assign relevant values to them."
    printf "\n=====================================================================\n"
}

setup_git_remotes
setup_local_properties