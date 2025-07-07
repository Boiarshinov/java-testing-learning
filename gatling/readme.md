# Gatling

How to test:

```shell
cd gatling
# Deploy test application
docker compose up -d
# run gatling load tests
../gradlew :gatling:gatlingRun
# after tests were passed open the report with the link in the logs
# Turn off test application
docker compose down
```
