#!/bin/zsh

STAGING_REMOTE=https://git.heroku.com/pbc-api-staging.git
PROD_REMOTE=https://git.heroku.com/pbc-api-prod.git

function setup_git_remotes() {
  # Add staging remote
  git remote add heroku-staging $STAGING_REMOTE

  # Add prod remote
  git remote add heroku-prod $PROD_REMOTE

  printf "\n=====================================================================\n"
  printf "Successfully added remotes for staging and production deployments.\nFor use them developer needs to be a contributor of relevant heroku accounts."
  printf "\n=====================================================================\n"
}

function is_required_properties_available() {
    if grep -q "PBC_MAIL_USERNAME" "local.properties"; then
        return 0
    else
        return 1
    fi
}

function write_content_to_local_properties() {
  echo "# Mail server - one config" >> local.properties
  echo "PBC_MAIL_BROADCASTER_ONE_USERNAME=<add email address>" >> local.properties
  echo "PBC_MAIL_BROADCASTER_ONE_PASSWORD=<add email passcode>" >> local.properties
  echo "# Mail server - two config" >> local.properties
  echo "PBC_MAIL_BROADCASTER_TWO_USERNAME=<add email address>" >> local.properties
  echo "PBC_MAIL_BROADCASTER_TWO_PASSWORD=<add email passcode>" >> local.properties
  echo "# Mail server - three config" >> local.properties
  echo "PBC_MAIL_BROADCASTER_THREE_USERNAME=<add email address>" >> local.properties
  echo "PBC_MAIL_BROADCASTER_THREE_PASSWORD=<add email passcode>" >> local.properties
  echo "# Mail server - four config" >> local.properties
  echo "PBC_MAIL_BROADCASTER_FOUR_USERNAME=<add email address>" >> local.properties
  echo "PBC_MAIL_BROADCASTER_FOUR_PASSWORD=<add email passcode>" >> local.properties
  echo "# Mail server - five config" >> local.properties
  echo "PBC_MAIL_BROADCASTER_FIVE_USERNAME=<add email address>" >> local.properties
  echo "PBC_MAIL_BROADCASTER_FIVE_PASSWORD=<add email passcode>" >> local.properties
}

function setup_local_properties() {
    if [ -f "./local.properties" ]; then
      if ! is_required_properties_available; then
        write_content_to_local_properties
      fi
    else
      touch local.properties
      write_content_to_local_properties
    fi

    printf "\n=====================================================================\n"
    printf "For local debugging and running purpose, local.properties file was created with required config parameters.\nAssign relevant values to them."
    printf "\n=====================================================================\n"
}

setup_git_remotes
setup_local_properties