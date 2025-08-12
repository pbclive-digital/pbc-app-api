echo "------> Generating google-credentials.json from Heroku config var"
echo ${GOOGLE_CREDENTIALS} > pbc-live-service-account-key-staging.json
cat pbc-live-service-account-key-staging.json
mv pbc-live-service-account-key-staging.json module-remote-datastore/src/main/resources/firebase/