# DFL Manager Scheduler CLI Scripts

This directory contains shell scripts for running DFL Manager scheduler handlers via command line. All scripts have been modernized to use Spring Boot's CommandLineRunner pattern.

## Overview

The scripts provide a convenient way to execute various scheduler handlers without manually typing long Java commands. Each script routes to a specific handler through the `SchedulerCommandLineRunner` infrastructure.

## Prerequisites

- Java 8 or later
- Built scheduler JAR file: `scheduler/target/dfl-manager-scheduler.jar`
- Set `$APP_HOME` environment variable pointing to the project root

Example:
```bash
export APP_HOME=/path/to/dfl-manager
```

## Available Handlers

### Core Data Loading

#### `run_afl_fixture_download.sh`
Loads AFL fixtures from external source.

**Handler:** `afl-fixture-loader`

**Arguments:**
- `-all` - Load all rounds (0-24)
- `-s <start>` - Start round for scraping
- `-e <end>` - End round for scraping (used with `-s`)

**Examples:**
```bash
# Load all fixtures
./run_afl_fixture_download.sh -all

# Load fixtures from round 10 to 15
./run_afl_fixture_download.sh -s 10 -e 15

# Load fixtures from round 5 onwards
./run_afl_fixture_download.sh -s 5
```

#### `run_afl_player_loader.sh`
Loads AFL player data.

**Handler:** `afl-player-loader`

**Examples:**
```bash
./run_afl_player_loader.sh
```

#### `run_complete_afl_fixtures.sh`
Checks AFL game completion status.

**Handler:** `afl-game-completion`

**Examples:**
```bash
./run_complete_afl_fixtures.sh
```

### DFL Processing

#### `run_dfl_fixture_generator.sh`
Generates DFL fixtures.

**Handler:** `dfl-fixture-generator`

**Examples:**
```bash
./run_dfl_fixture_generator.sh
```

#### `run_best_22.sh`
Calculates best 22 team for a round.

**Handler:** `best22`

**Arguments:**
- `-r <round>` - Round number (required)

**Examples:**
```bash
./run_best_22.sh -r 10
```

### Round Processing

#### `run_start_round.sh`
Starts round processing, sends lockout emails.

**Handler:** `start-round`

**Arguments:**
- `<round>` - Round number (required)
- `<email>` - Optional email override

**Examples:**
```bash
# Start round 5
./run_start_round.sh 5

# Start round 5 with email override
./run_start_round.sh 5 test@example.com
```

#### `run_end_round.sh`
Ends round processing, calculates results and medals.

**Handler:** `end-round`

**Arguments:**
- `<round>` - Round number (required)
- `<email>` - Optional email override

**Examples:**
```bash
# End round 5
./run_end_round.sh 5

# End round 5 with email override
./run_end_round.sh 5 test@example.com
```

### Statistics Processing

#### `run_get_stats_for_round.sh`
Downloads raw player statistics for a DFL round.

**Handler:** `get-stats`

**Arguments:**
- `-r <round>` - Round number (required)
- `-f` - Final run (marks games as stats downloaded)

**Examples:**
```bash
# Get stats for round 5 (in progress)
./run_get_stats_for_round.sh -r 5

# Get final stats for round 5
./run_get_stats_for_round.sh -r 5 -f
```

#### `run_get_stats_for_stats_round.sh`
Processes stats for a specific stats round.

**Handler:** `stats-round`

**Arguments:**
- `-r <round>` - Round number (required)

**Examples:**
```bash
./run_get_stats_for_stats_round.sh -r 0
```

#### `run_raw_stats_downloader.sh`
Direct stats downloader (typically called via get-stats).

**Handler:** `raw-stats-downloader`

**Arguments:**
- `-r <round>` - Round number (required)
- `-all` - Download all stats

**Examples:**
```bash
./run_raw_stats_downloader.sh -r 5
```

#### `run_raw_stats_downloader_all.sh`
Alias for downloading all stats for a round.

**Handler:** `get-stats`

**Examples:**
```bash
./run_raw_stats_downloader_all.sh -r 5 -f
```

#### `run_preseasonstats_download.sh`
Downloads preseason statistics.

**Handler:** `preseason-stats`

**Arguments:**
- `-r <round>` - Preseason round number (required)

**Examples:**
```bash
./run_preseasonstats_download.sh -r 1
```

### Team Selections

#### `run_selections.sh`
Creates team selections for a round.

**Handler:** `selections`

**Arguments:**
- `-r <round>` - Round number (required)

**Examples:**
```bash
./run_selections.sh -r 5
```

#### `run_email_selections.sh`
Processes email-based team selections.

**Handler:** `email-selections`

**Examples:**
```bash
./run_email_selections.sh
```

### Results and Reporting

#### `run_results.sh`
Calculates match results for a round.

**Handler:** `results`

**Arguments:**
- `-r <round>` - Round number (required)

**Examples:**
```bash
./run_results.sh -r 5
```

#### `run_medals.sh`
Calculates all medals (Adam Goodes, Callum Chambers, Matthew Allen).

**Handler:** `medals`

**Examples:**
```bash
./run_medals.sh
```

### Job Generators

These scripts generate Quartz scheduler jobs for automated execution.

#### `run_start_round_job_generator.sh`
Generates start round jobs.

**Handler:** `start-round-job-generator`

**Examples:**
```bash
./run_start_round_job_generator.sh
```

#### `run_end_round_job_generator.sh`
Generates end round jobs.

**Handler:** `end-round-job-generator`

**Examples:**
```bash
./run_end_round_job_generator.sh
```

#### `run_results_job_generator.sh`
Generates results calculation jobs.

**Handler:** `results-job-generator`

**Examples:**
```bash
./run_results_job_generator.sh
```

#### `run_email_selections_job_generator.sh`
Generates email selections processing jobs.

**Handler:** `email-selections-job-generator`

**Examples:**
```bash
./run_email_selections_job_generator.sh
```

#### `run_stats_round_job_generator.sh`
Generates stats round processing jobs.

**Handler:** `stats-round-job-generator`

**Examples:**
```bash
./run_stats_round_job_generator.sh
```

### Composite Scripts

#### `run_refresh.sh`
Comprehensive refresh script that runs multiple handlers in sequence.

**Arguments:**
- `now` - Run immediately (optional, otherwise runs on Monday only)

**Executes:**
1. AFL Fixture Loader
2. DFL Round Info Calculator
3. Start Round Job Generator
4. End Round Job Generator
5. Results Job Generator

**Examples:**
```bash
# Run immediately
./run_refresh.sh now

# Run only on Monday (checks current day)
./run_refresh.sh
```

## Direct Java Execution

You can also run handlers directly via Java without using shell scripts:

```bash
# Run AFL fixture loader
java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar \
  --handler=afl-fixture-loader \
  -all

# Run start round for round 5
java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar \
  --handler=start-round \
  5

# Run get stats for round 10
java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar \
  --handler=get-stats \
  -r 10 -f
```

## Running as Scheduler

To run the application as a scheduler (letting Quartz handle scheduled jobs):

```bash
java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar
```

When run without arguments, the application starts in scheduler mode and waits for scheduled jobs to execute.

## Handler List

Quick reference of all available handlers:

| Handler Name | Purpose |
|-------------|---------|
| `afl-fixture-loader` | Load AFL fixtures |
| `afl-player-loader` | Load AFL players |
| `best22` | Calculate best 22 |
| `afl-game-completion` | Check game completion |
| `dfl-fixture-generator` | Generate DFL fixtures |
| `dfl-round-info-calculator` | Calculate round info |
| `email-selections` | Process email selections |
| `end-round` | End round processing |
| `get-stats` | Get raw player stats |
| `preseason-stats` | Load preseason stats |
| `raw-stats-downloader` | Download raw stats |
| `medals` | Calculate medals |
| `start-round` | Start round processing |
| `selections` | Create selections |
| `results` | Calculate results |
| `ladder` | Calculate ladder |
| `prediction` | Generate predictions |
| `stats-round` | Process stats round |
| `scores-calculator` | Calculate scores |
| `start-round-job-generator` | Generate start round jobs |
| `end-round-job-generator` | Generate end round jobs |
| `results-job-generator` | Generate results jobs |
| `email-selections-job-generator` | Generate email selections jobs |
| `stats-round-job-generator` | Generate stats round jobs |

## Troubleshooting

### Script Permissions

If scripts aren't executable:
```bash
chmod +x scheduler/bin/*.sh
```

### APP_HOME Not Set

If you get "APP_HOME not set" errors:
```bash
export APP_HOME=/path/to/dfl-manager
```

### JAR File Not Found

Ensure the scheduler module is built:
```bash
cd $APP_HOME
mvn clean package -DskipTests
```

### Handler Not Found

Check the handler name is correct by running:
```bash
java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=invalid
```

This will show available handlers.

## Migration Notes

These scripts have been modernized from the old classpath-based approach to use Spring Boot's executable JAR with CommandLineRunner pattern. The old approach used:

```bash
# OLD (deprecated)
export CLASSPATH=$APP_HOME/target/dflmngr.jar:$APP_HOME/target/dependency/*
java -classpath $CLASSPATH net.dflmngr.handlers.HandlerClass
```

The new approach uses:

```bash
# NEW (current)
java -jar $APP_HOME/scheduler/target/dfl-manager-scheduler.jar --handler=handler-name
```

Old handler main methods remain for backward compatibility but are deprecated.

## See Also

- `SchedulerCommandLineRunner.java` - Main CLI entry point
- Handler classes in `net.dflmngr.handlers` package
- Job generator classes in `net.dflmngr.scheduler.generators` package
