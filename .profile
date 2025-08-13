#echo "------> Generating google-credentials.json from Heroku config var"
#echo ${GOOGLE_CREDENTIALS} > pbc-live-service-account-key-staging.json
#cat pbc-live-service-account-key-staging.json
#mkdir -p module-remote-datastore/src/main/resources/firebase/
#mv pbc-live-service-account-key-staging.json module-remote-datastore/src/main/resources/firebase/