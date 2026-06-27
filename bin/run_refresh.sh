#!/bin/bash
export CLASSPATH=$APP_HOME/target/dflmngr.jar:$APP_HOME/target/dependency/*

TZ=Austrlia/Melbourne
run=$1
skip=$2

if [[ $run == "now" ]]; then
    echo "Running Now"
    if [[ $skip != "safl" ]]; then
        java -classpath $CLASSPATH net.dflmngr.handlers.AflFixtureLoaderHandler && \
        java -classpath $CLASSPATH net.dflmngr.handlers.DflRoundInfoCalculatorHandler && \
        java -classpath $CLASSPATH net.dflmngr.scheduler.generators.StartRoundJobGenerator &&\
        java -classpath $CLASSPATH net.dflmngr.scheduler.generators.EndRoundJobGenerator && \
        java -classpath $CLASSPATH net.dflmngr.scheduler.generators.ResultsJobGenerator
    else
        java -classpath $CLASSPATH net.dflmngr.handlers.DflRoundInfoCalculatorHandler && \
        java -classpath $CLASSPATH net.dflmngr.scheduler.generators.StartRoundJobGenerator &&\
        java -classpath $CLASSPATH net.dflmngr.scheduler.generators.EndRoundJobGenerator && \
        java -classpath $CLASSPATH net.dflmngr.scheduler.generators.ResultsJobGenerator
    fi
else
    if [[ $(date +%u) -eq 1 ]]; then
        echo "Monday Running"
        if [[ $skip != "safl" ]]; then
            java -classpath $CLASSPATH net.dflmngr.handlers.AflFixtureLoaderHandler && \
            java -classpath $CLASSPATH net.dflmngr.handlers.DflRoundInfoCalculatorHandler && \
            java -classpath $CLASSPATH net.dflmngr.scheduler.generators.StartRoundJobGenerator && \
            java -classpath $CLASSPATH net.dflmngr.scheduler.generators.EndRoundJobGenerator && \
            java -classpath $CLASSPATH net.dflmngr.scheduler.generators.ResultsJobGenerator
        else
            java -classpath $CLASSPATH net.dflmngr.handlers.DflRoundInfoCalculatorHandler && \
            java -classpath $CLASSPATH net.dflmngr.scheduler.generators.StartRoundJobGenerator && \
            java -classpath $CLASSPATH net.dflmngr.scheduler.generators.EndRoundJobGenerator && \
            java -classpath $CLASSPATH net.dflmngr.scheduler.generators.ResultsJobGenerator
        fi
    else
        echo "Not Monday Skipping"
    fi
fi

