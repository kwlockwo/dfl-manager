#!/bin/bash
# Modernized to use Spring Boot CommandLineRunner
# This script runs multiple handlers in sequence

TZ=Austrlia/Melbourne
run=$1

if [[ $run == "now" ]]; then
    echo "Running Now"
    java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=afl-fixture-loader && \
    java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=dfl-round-info-calculator && \
    java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=start-round-job-generator && \
    java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=end-round-job-generator && \
    java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=results-job-generator
else
    if [[ $(date +%u) -eq 1 ]]; then
        echo "Monday Running"
        java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=afl-fixture-loader && \
        java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=dfl-round-info-calculator && \
        java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=start-round-job-generator && \
        java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=end-round-job-generator && \
        java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=results-job-generator
    else
        echo "Not Monday Skipping"
    fi
fi

