package org.anadoxin.mailconfig.boot;

import org.anadoxin.mailconfig.*;
import java.io.*;
import org.apache.commons.cli.*;

class Boot {
    private MailConfig mconfig;
    private String configFileName;

    public static void main(String[] args) {
        new Boot().run(args);
    }

    private boolean parseArguments(String[] args) {
        Options options = new Options();

        options.addOption(Option.builder("c").
            required().
            longOpt("config").
            argName("PATH").
            numberOfArgs(1).
            desc("Specify full path to HJSON config file").
            build());


        options.addOption(Option.builder("h").
            longOpt("help").
            desc("Show this helpful screen").
            build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        String helpHeader = "Easily regenerate fetchmail config files\n\n";
        String helpFooter = "\nReport bugs to http://github.com/antekone/mailconfig";

        try {
            cmd = parser.parse(options, args);
        } catch(org.apache.commons.cli.ParseException ex) {
            Log.put("Failure during parsing commandline options: %s", ex.getMessage());
            formatter.printHelp("mailconfig", helpHeader, options, helpFooter, true);
            return false;
        }

        if(cmd.hasOption("help")) {
            formatter.printHelp("mailconfig", helpHeader, options, helpFooter, true);
            return false;
        }

        this.configFileName = cmd.getOptionValue("config");
        return true;
    }

    public void run(String[] args) {
        try {
            if(!parseArguments(args)) {
                return;
            }

            InputStreamReader rdr = new InputStreamReader(new FileInputStream(new File(this.configFileName)));
            mconfig = new MailConfig();

            if(!mconfig.initFromReader(rdr)) {
                Log.put("Error: processing configuration file `%s` failed. Aborting.", this.configFileName);
                return;
            }

            RendererQueue rq = new RendererQueue();
            rq.setMailConfig(mconfig);
            rq.addRenderer(new PrintRenderer());
            rq.addRenderer(new FetchmailRenderer());
            rq.run();
        } catch(IOException e) {
            Log.put("IOException: %s", e.toString());
        }
    }
}
