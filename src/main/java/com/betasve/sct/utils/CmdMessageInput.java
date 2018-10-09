package com.betasve.sct.utils;

import org.apache.commons.cli.*;

import java.security.InvalidParameterException;
import java.util.HashMap;

public class CmdMessageInput {
    private String[] cmdOptions;
    private Options options = new Options();
    private HashMap<String, String> errors = new HashMap<>();
    private CommandLine parsedOptions;
    private final HashMap<String, String> optionSet = new HashMap<>();
    private static final String TYPE = "type";
    private static final String TYPE_MSG = "Message type (emotion|text";
    private static final String PAYLOAD = "payload";
    private static final String PAYLOAD_MSG = "Message body";

    {
        optionSet.put(TYPE, TYPE_MSG);
        optionSet.put(PAYLOAD, PAYLOAD_MSG);
    }

    public CmdMessageInput(String[] cmdOptions) {
        this.cmdOptions = cmdOptions;
        defineOptionsSet();
        parseCmdLineOptions();
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }

    public void setCmdOptions(String[] cmdOptions) {
        this.cmdOptions = cmdOptions;
    }

    public boolean valid() {
        if (parsedOptions == null) {
            return false;
        }

        boolean isValid = optionSet.keySet()
                .stream()
                .allMatch( e -> parsedOptions.hasOption(e) || addToErrors(e, "Missing key"));

        if (isValid) {
            return isValid;
        } else {
            throw new InvalidParameterException("Invalid params, bro");
        }
    }

    public String getOption(String key) {
        if (parsedOptions == null) {
            return new String();
        }

        return parsedOptions.getOptionValue(key);
    }

    public String getType() {
        return getOption(TYPE);
    }

    public String getPayload() {
        return getOption(PAYLOAD);
    }

    private void defineOptionsSet() {
        optionSet.keySet().forEach(i -> options.addOption(i, true, optionSet.get(i)));
    }

    private boolean addToErrors(String key, String reason) {
        errors.put(key, reason);
        return false;
    }

    private void parseCmdLineOptions() {
        CommandLineParser parser = new DefaultParser();
        try {
            parsedOptions = parser.parse(options, cmdOptions);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
