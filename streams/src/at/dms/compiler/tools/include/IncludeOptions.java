// Generated by optgen from IncludeOptions.opt
package at.dms.compiler.tools.include;

import at.dms.compiler.getopt.Getopt;
import at.dms.compiler.getopt.LongOpt;

public class IncludeOptions extends at.dms.compiler.tools.common.Options {

    public IncludeOptions(String name) {
        super(name);
    }

    public IncludeOptions() {
        this("Include");
    }
    public String output = null;
    public String pattern = "%%include";
    public String directory = ".";

    public boolean processOption(int code, Getopt g) {
        switch (code) {
        case 'o':
            output = getString(g, ""); return true;
        case 'p':
            pattern = getString(g, ""); return true;
        case 'd':
            directory = getString(g, ""); return true;
        default:
            return super.processOption(code, g);
        }
    }

    public String[] getOptions() {
        String[]    parent = super.getOptions();
        String[]    total = new String[parent.length + 3];
        System.arraycopy(parent, 0, total, 0, parent.length);
        total[parent.length + 0] = "  --output, -o<String>: The name of the output file";
        total[parent.length + 1] = "  --pattern, -p<String>: The pattern preceding the file name to include [%%include]";
        total[parent.length + 2] = "  --directory, -d<String>: The directory to search for include files [.]";
    
        return total;
    }


    public String getShortOptions() {
        return "o:p:d:" + super.getShortOptions();
    }


    public void version() {
        System.out.println("Version 1.5B released 9 August 2001");
    }


    public void usage() {
        System.err.println("usage: at.dms.compiler.tools.include.Main [option]* [--help] <input-file>");
    }


    public void help() {
        System.err.println("usage: at.dms.compiler.tools.include.Main [option]* [--help] <input-file>");
        printOptions();
        System.err.println();
        version();
        System.err.println();
        System.err.println("This program is part of the Kopi Suite.");
        System.err.println("For more info, please see: http://www.dms.at/kopi");
    }

    public LongOpt[] getLongOptions() {
        LongOpt[]   parent = super.getLongOptions();
        LongOpt[]   total = new LongOpt[parent.length + LONGOPTS.length];
    
        System.arraycopy(parent, 0, total, 0, parent.length);
        System.arraycopy(LONGOPTS, 0, total, parent.length, LONGOPTS.length);
    
        return total;
    }

    private static final LongOpt[] LONGOPTS = {
        new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
        new LongOpt("pattern", LongOpt.REQUIRED_ARGUMENT, null, 'p'),
        new LongOpt("directory", LongOpt.REQUIRED_ARGUMENT, null, 'd')
    };
}
