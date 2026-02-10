package net.dflmngr.cli;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import net.dflmngr.handlers.AflFixtureLoaderHandler;
import net.dflmngr.handlers.AflGameCompletionCheckerHandler;
import net.dflmngr.handlers.AflPlayerLoaderHandler;
import net.dflmngr.handlers.Best22Handler;
import net.dflmngr.handlers.DflFixtureGeneratorHandler;
import net.dflmngr.handlers.DflRoundInfoCalculatorHandler;
import net.dflmngr.handlers.EmailSelectionsHandler;
import net.dflmngr.handlers.EndRoundHandler;
import net.dflmngr.handlers.LadderCalculatorHandler;
import net.dflmngr.handlers.PredictionHandler;
import net.dflmngr.handlers.PreSeasonStatsHandler;
import net.dflmngr.handlers.RawPlayerStatsHandler;
import net.dflmngr.handlers.ResultsHandler;
import net.dflmngr.handlers.SelectionsHandler;
import net.dflmngr.handlers.StartRoundHandler;
import net.dflmngr.handlers.StatsDownloaderHandler;
import net.dflmngr.handlers.AdamGoodesHandler;
import net.dflmngr.handlers.CallumChambersHandler;
import net.dflmngr.handlers.MatthewAllenHandler;
import net.dflmngr.handlers.StatsRoundPlayerStatsHandler;
import net.dflmngr.handlers.ScoresCalculatorHandler;
import net.dflmngr.services.AflFixtureService;
import net.dflmngr.services.GlobalsService;
import net.dflmngr.scheduler.generators.StartRoundJobGenerator;
import net.dflmngr.scheduler.generators.EndRoundJobGenerator;
import net.dflmngr.scheduler.generators.ResultsJobGenerator;
import net.dflmngr.scheduler.generators.EmailSelectionsJobGenerator;
import net.dflmngr.scheduler.generators.StatsRoundJobGenerator;
import net.dflmngr.logging.LoggingUtils;

/**
 * Spring Boot CommandLineRunner for executing scheduler handlers via CLI.
 *
 * This provides a modern CLI entry point that routes to different handlers based on
 * the --handler= argument. When no arguments are provided, the application runs as
 * a scheduler (Quartz handles scheduled jobs).
 *
 * Usage:
 *   java -jar scheduler.jar                           # Run as scheduler
 *   java -jar scheduler.jar --handler=<name> [args]  # Run specific handler
 */
@Component
public class SchedulerCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    private final LoggingUtils loggerUtils = new LoggingUtils("CLI");

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            // No arguments - run as scheduler (Quartz will handle scheduling)
            loggerUtils.log("info", "Scheduler started - waiting for scheduled jobs");
            return;
        }

        // Parse --handler= argument
        String handlerName = null;
        List<String> handlerArgs = new ArrayList<>();

        for (String arg : args) {
            if (arg.startsWith("--handler=")) {
                handlerName = arg.substring(10);
            } else {
                handlerArgs.add(arg);
            }
        }

        if (handlerName == null) {
            loggerUtils.log("error", "No handler specified. Use --handler=<name>");
            printUsage();
            System.exit(1);
        }

        try {
            // Route to appropriate handler
            switch (handlerName) {
                case "afl-fixture-loader":
                    runAflFixtureLoader(handlerArgs);
                    break;
                case "afl-player-loader":
                    runAflPlayerLoader(handlerArgs);
                    break;
                case "best22":
                    runBest22(handlerArgs);
                    break;
                case "afl-game-completion":
                    runAflGameCompletion(handlerArgs);
                    break;
                case "dfl-fixture-generator":
                    runDflFixtureGenerator(handlerArgs);
                    break;
                case "dfl-round-info-calculator":
                    runDflRoundInfoCalculator(handlerArgs);
                    break;
                case "email-selections":
                    runEmailSelections(handlerArgs);
                    break;
                case "end-round":
                    runEndRound(handlerArgs);
                    break;
                case "get-stats":
                    runRawPlayerStats(handlerArgs);
                    break;
                case "preseason-stats":
                    runPreSeasonStats(handlerArgs);
                    break;
                case "raw-stats-downloader":
                    runStatsDownloader(handlerArgs);
                    break;
                case "medals":
                    runMedals(handlerArgs);
                    break;
                case "start-round":
                    runStartRound(handlerArgs);
                    break;
                case "selections":
                    runSelections(handlerArgs);
                    break;
                case "results":
                    runResults(handlerArgs);
                    break;
                case "ladder":
                    runLadder(handlerArgs);
                    break;
                case "prediction":
                    runPrediction(handlerArgs);
                    break;
                case "stats-round":
                    runStatsRound(handlerArgs);
                    break;
                case "scores-calculator":
                    runScoresCalculator(handlerArgs);
                    break;
                case "start-round-job-generator":
                    runStartRoundJobGenerator(handlerArgs);
                    break;
                case "end-round-job-generator":
                    runEndRoundJobGenerator(handlerArgs);
                    break;
                case "results-job-generator":
                    runResultsJobGenerator(handlerArgs);
                    break;
                case "email-selections-job-generator":
                    runEmailSelectionsJobGenerator(handlerArgs);
                    break;
                case "stats-round-job-generator":
                    runStatsRoundJobGenerator(handlerArgs);
                    break;
                default:
                    loggerUtils.log("error", "Unknown handler: {}", handlerName);
                    printUsage();
                    System.exit(1);
            }

            // Exit after handler completes (don't keep Spring running)
            loggerUtils.log("info", "Handler completed successfully");
            System.exit(0);

        } catch (Exception ex) {
            loggerUtils.logException("Handler execution failed", ex);
            System.exit(1);
        }
    }

    private void runAflFixtureLoader(List<String> args) throws Exception {
        Options options = new Options();
        Option all = new Option("all", "All rounds");
        Option startRound = Option.builder("s")
                            .argName("start")
                            .hasArg()
                            .desc("Round to start fixture scraping")
                            .type(Number.class)
                            .build();
        Option endRound = Option.builder("e")
                            .argName("end")
                            .hasArg()
                            .desc("Round to end fixture scraping")
                            .type(Number.class)
                            .build();
        options.addOption(all);
        options.addOption(startRound);
        options.addOption(endRound);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        AflFixtureLoaderHandler handler = context.getBean(AflFixtureLoaderHandler.class);
        List<Integer> rounds = new ArrayList<>();

        if (cli.hasOption("all")) {
            for (int i = 0; i <= 24; i++) {
                rounds.add(i);
            }
        } else if (cli.hasOption("s")) {
            int startAtRound = ((Number) cli.getParsedOptionValue("s")).intValue();
            int endAtRound = 24;
            if (cli.hasOption("e")) {
                endAtRound = ((Number) cli.getParsedOptionValue("e")).intValue();
            }
            for (int i = startAtRound; i <= endAtRound; i++) {
                rounds.add(i);
            }
        } else {
            // Default: get refresh start from service
            AflFixtureService aflFixtureService = context.getBean(AflFixtureService.class);
            int startAtRound = aflFixtureService.getRefreshFixtureStart();
            for (int i = startAtRound; i <= 24; i++) {
                rounds.add(i);
            }
        }

        handler.execute(rounds);
    }

    private void runAflPlayerLoader(List<String> args) throws Exception {
        AflPlayerLoaderHandler handler = context.getBean(AflPlayerLoaderHandler.class);
        handler.execute();
    }

    private void runBest22(List<String> args) throws Exception {
        Options options = new Options();
        Option roundOpt = Option.builder("r")
                            .argName("round")
                            .hasArg()
                            .desc("round to run on")
                            .type(Number.class)
                            .required()
                            .build();
        options.addOption(roundOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        int round = ((Number) cli.getParsedOptionValue("r")).intValue();

        Best22Handler handler = context.getBean(Best22Handler.class);
        handler.configureLogging("batch.name", "batch-logger", ("Best22_R" + round));
        handler.execute(round);
    }

    private void runAflGameCompletion(List<String> args) throws Exception {
        AflGameCompletionCheckerHandler handler = context.getBean(AflGameCompletionCheckerHandler.class);
        handler.execute();
    }

    private void runDflFixtureGenerator(List<String> args) throws Exception {
        DflFixtureGeneratorHandler handler = context.getBean(DflFixtureGeneratorHandler.class);
        handler.execute();
    }

    private void runDflRoundInfoCalculator(List<String> args) throws Exception {
        DflRoundInfoCalculatorHandler handler = context.getBean(DflRoundInfoCalculatorHandler.class);
        handler.execute();
    }

    private void runEmailSelections(List<String> args) throws Exception {
        EmailSelectionsHandler handler = context.getBean(EmailSelectionsHandler.class);
        handler.execute();
    }

    private void runEndRound(List<String> args) throws Exception {
        String email = null;
        int round = 0;

        if (args.size() > 2 || args.size() < 1) {
            loggerUtils.log("info", "usage: EndRoundHandler <round> optional [<email>]");
            throw new IllegalArgumentException("Invalid arguments");
        } else {
            round = Integer.parseInt(args.get(0));
            if (args.size() == 2) {
                email = args.get(1);
            }

            EndRoundHandler handler = context.getBean(EndRoundHandler.class);
            handler.configureLogging("batch.name", "batch-logger", "EndRound");
            handler.execute(round, email);
        }
    }

    private void runRawPlayerStats(List<String> args) throws Exception {
        Options options = new Options();
        Option roundOpt = Option.builder("r")
                            .argName("round")
                            .hasArg()
                            .desc("round to run on")
                            .type(Number.class)
                            .required()
                            .build();
        Option isFinalOpt = new Option("f", "final run");

        options.addOption(roundOpt);
        options.addOption(isFinalOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        int round = ((Number) cli.getParsedOptionValue("r")).intValue();
        boolean isFinal = cli.hasOption("f");

        RawPlayerStatsHandler handler = context.getBean(RawPlayerStatsHandler.class);
        handler.configureLogging("RawPlayerStatsHandlerTesting");
        handler.execute(round, isFinal);
    }

    private void runPreSeasonStats(List<String> args) throws Exception {
        Options options = new Options();
        Option roundOpt = Option.builder("r")
                            .argName("round")
                            .hasArg()
                            .desc("round to run on")
                            .type(Number.class)
                            .required()
                            .build();
        options.addOption(roundOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        int round = ((Number) cli.getParsedOptionValue("r")).intValue();

        PreSeasonStatsHandler handler = context.getBean(PreSeasonStatsHandler.class);
        handler.execute(round);
    }

    private void runStatsDownloader(List<String> args) throws Exception {
        Options options = new Options();
        Option roundOpt = Option.builder("r")
                            .argName("round")
                            .hasArg()
                            .desc("round to run on")
                            .type(Number.class)
                            .required()
                            .build();
        Option allOpt = new Option("all", "all stats");

        options.addOption(roundOpt);
        options.addOption(allOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        int round = ((Number) cli.getParsedOptionValue("r")).intValue();
        boolean downloadAll = cli.hasOption("all");

        StatsDownloaderHandler handler = context.getBean(StatsDownloaderHandler.class);
        handler.setRound(round);
        // Note: StatsDownloaderHandler execution requires more context from RawPlayerStatsHandler
        // This is a simplified entry point
        loggerUtils.log("warn", "StatsDownloader should typically be called via get-stats handler");
    }

    private void runMedals(List<String> args) throws Exception {
        // Run all three medal handlers - requires round parameter
        Options options = new Options();
        Option roundOpt = Option.builder("r")
                            .argName("round")
                            .hasArg()
                            .desc("round to run on")
                            .type(Number.class)
                            .required()
                            .build();
        options.addOption(roundOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        int round = ((Number) cli.getParsedOptionValue("r")).intValue();

        // Run all three medal handlers as per the shell script
        AdamGoodesHandler adamGoodesHandler = context.getBean(AdamGoodesHandler.class);
        CallumChambersHandler callumChambersHandler = context.getBean(CallumChambersHandler.class);
        MatthewAllenHandler matthewAllenHandler = context.getBean(MatthewAllenHandler.class);

        adamGoodesHandler.execute(round);
        callumChambersHandler.execute(round);
        matthewAllenHandler.execute(round);
    }

    private void runStartRound(List<String> args) throws Exception {
        String email = null;
        int round = 0;

        if (args.size() > 2 || args.size() < 1) {
            loggerUtils.log("info", "usage: StartRoundHandler <round> optional [<email>]");
            throw new IllegalArgumentException("Invalid arguments");
        } else {
            round = Integer.parseInt(args.get(0));
            if (args.size() == 2) {
                email = args.get(1);
            }

            StartRoundHandler handler = context.getBean(StartRoundHandler.class);
            handler.configureLogging("batch.name", "batch-logger", "StartRound");
            handler.execute(round, email, false);
        }
    }

    private void runSelections(List<String> args) throws Exception {
        Options options = new Options();
        Option roundOpt = Option.builder("r")
                            .argName("round")
                            .hasArg()
                            .desc("round to run on")
                            .type(Number.class)
                            .required()
                            .build();
        options.addOption(roundOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        int round = ((Number) cli.getParsedOptionValue("r")).intValue();

        SelectionsHandler handler = context.getBean(SelectionsHandler.class);
        handler.configureLogging("batch.name", "batch-logger", ("SelectionsHandler" + round));
        handler.execute(round);
    }

    private void runResults(List<String> args) throws Exception {
        Options options = new Options();
        Option roundOpt = Option.builder("r")
                            .argName("round")
                            .hasArg()
                            .desc("round to run on")
                            .type(Number.class)
                            .required()
                            .build();
        options.addOption(roundOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        int round = ((Number) cli.getParsedOptionValue("r")).intValue();

        // ResultsHandler.execute(int inputRound, boolean isFinal, String emailOverride, boolean skipStats, boolean sendReport)
        ResultsHandler handler = context.getBean(ResultsHandler.class);
        handler.execute(round, false, null, false, true);
    }

    private void runLadder(List<String> args) throws Exception {
        Options options = new Options();
        Option roundOpt = Option.builder("r")
                            .argName("round")
                            .hasArg()
                            .desc("round to run on")
                            .type(Number.class)
                            .required()
                            .build();
        options.addOption(roundOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        int round = ((Number) cli.getParsedOptionValue("r")).intValue();

        // LadderCalculatorHandler.execute(int round, boolean liveLadderOveride)
        LadderCalculatorHandler handler = context.getBean(LadderCalculatorHandler.class);
        handler.execute(round, false);
    }

    private void runPrediction(List<String> args) throws Exception {
        Options options = new Options();
        Option roundOpt = Option.builder("r")
                            .argName("round")
                            .hasArg()
                            .desc("round to run on")
                            .type(Number.class)
                            .required()
                            .build();
        options.addOption(roundOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        int round = ((Number) cli.getParsedOptionValue("r")).intValue();

        // PredictionHandler.execute(int round, String teamCode, boolean doPlayers)
        // Run prediction for all teams (teamCode = null means all teams)
        PredictionHandler handler = context.getBean(PredictionHandler.class);
        handler.execute(round, null, true);
    }

    private void runStatsRound(List<String> args) throws Exception {
        Options options = new Options();
        Option roundOpt = Option.builder("r")
                            .argName("round")
                            .hasArg()
                            .desc("round to run on")
                            .type(Number.class)
                            .required()
                            .build();
        options.addOption(roundOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        int round = ((Number) cli.getParsedOptionValue("r")).intValue();

        StatsRoundPlayerStatsHandler handler = context.getBean(StatsRoundPlayerStatsHandler.class);
        handler.execute(round);
    }

    private void runScoresCalculator(List<String> args) throws Exception {
        Options options = new Options();
        Option roundOpt = Option.builder("r")
                            .argName("round")
                            .hasArg()
                            .desc("round to run on")
                            .type(Number.class)
                            .required()
                            .build();
        options.addOption(roundOpt);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(options, args.toArray(new String[0]));

        int round = ((Number) cli.getParsedOptionValue("r")).intValue();

        ScoresCalculatorHandler handler = context.getBean(ScoresCalculatorHandler.class);
        handler.execute(round);
    }

    private void runStartRoundJobGenerator(List<String> args) throws Exception {
        StartRoundJobGenerator generator = context.getBean(StartRoundJobGenerator.class);
        generator.execute();
    }

    private void runEndRoundJobGenerator(List<String> args) throws Exception {
        EndRoundJobGenerator generator = context.getBean(EndRoundJobGenerator.class);
        generator.execute();
    }

    private void runResultsJobGenerator(List<String> args) throws Exception {
        ResultsJobGenerator generator = context.getBean(ResultsJobGenerator.class);
        generator.execute();
    }

    private void runEmailSelectionsJobGenerator(List<String> args) throws Exception {
        EmailSelectionsJobGenerator generator = context.getBean(EmailSelectionsJobGenerator.class);
        generator.execute();
    }

    private void runStatsRoundJobGenerator(List<String> args) throws Exception {
        StatsRoundJobGenerator generator = context.getBean(StatsRoundJobGenerator.class);
        generator.execute();
    }

    private void printUsage() {
        System.out.println("\nUsage: java -jar scheduler.jar --handler=<name> [options]");
        System.out.println("\nAvailable handlers:");
        System.out.println("  afl-fixture-loader           - Load AFL fixtures");
        System.out.println("  afl-player-loader            - Load AFL players");
        System.out.println("  best22                       - Calculate best 22 team");
        System.out.println("  afl-game-completion          - Check AFL game completion");
        System.out.println("  dfl-fixture-generator        - Generate DFL fixtures");
        System.out.println("  dfl-round-info-calculator    - Calculate DFL round info");
        System.out.println("  email-selections             - Process email selections");
        System.out.println("  end-round                    - End round processing");
        System.out.println("  get-stats                    - Get raw player stats");
        System.out.println("  preseason-stats              - Load preseason stats");
        System.out.println("  raw-stats-downloader         - Download raw stats");
        System.out.println("  medals                       - Calculate medals");
        System.out.println("  start-round                  - Start round processing");
        System.out.println("  selections                   - Process selections");
        System.out.println("  results                      - Calculate results");
        System.out.println("  ladder                       - Calculate ladder");
        System.out.println("  prediction                   - Generate predictions");
        System.out.println("  stats-round                  - Process stats round");
        System.out.println("  scores-calculator            - Calculate scores");
        System.out.println("  start-round-job-generator    - Generate start round jobs");
        System.out.println("  end-round-job-generator      - Generate end round jobs");
        System.out.println("  results-job-generator        - Generate results jobs");
        System.out.println("  email-selections-job-generator - Generate email selections jobs");
        System.out.println("  stats-round-job-generator    - Generate stats round jobs");
        System.out.println("\nRun without arguments to start as scheduler.");
    }
}
