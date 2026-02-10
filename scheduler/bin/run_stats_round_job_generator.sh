#!/bin/bash
# Modernized to use Spring Boot CommandLineRunner
java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar \
  --handler=stats-round-job-generator \
  "$@"