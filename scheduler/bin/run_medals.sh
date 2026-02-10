#!/bin/bash
# Modernized to use Spring Boot CommandLineRunner
# This handler runs all three medal handlers (AdamGoodes, CallumChambers, MatthewAllen)
java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar \
  --handler=medals \
  "$@"
