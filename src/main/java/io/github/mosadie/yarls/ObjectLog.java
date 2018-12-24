package io.github.mosadie.yarls;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents the log files for a given object.
 * The object itself is used mainly as a 'key' to look up this ObjectLog at a later time,
 * and to find the log files on the file system.
 * 
 * To create your own ObjectLog designed for your object, extend this class
 * and override the methods makeLogFolder(), getObject(), getType(), logCurrentStatus(LOG_LEVEL logLevel)
 * as well as the constructor to match what you need.
 * See docs for more info.
 */
public class ObjectLog {
    /**
     * The object to log information about.
     */
    protected final Object objectToLog;
    /**
     * The folder to store the log files in.
     */
    protected File logFolder;
    
    /**
     * The log file for INFO level logging.
     */
    protected File infoLog;
    /**
     * The log file for WARN level logging.
     */
    protected File warnLog;
    /**
     * The log file for ERROR level logging.
     */
    protected File errorLog;
    
    /**
     * Construct a new ObjectLog for a given object.
     * @param objectToLog The Object to log information about.
     */
    public ObjectLog(Object objectToLog) {
        this.objectToLog = objectToLog;
        makeLogFolder();
        createLogFiles();
    }
    
    /**
     * Create the log folder for this object, if it doesn't exist already.
     */
    protected void makeLogFolder() {
        this.logFolder = new File(YARLS.getInstance().logDirPath + objectToLog.toString() + "/");
        logFolder.mkdir();
    }
    
    /** 
     * Get the object used to create the ObjectLog.
     * @return The object used to create the ObjectLog.
     */
    public Object getObject() {
        return objectToLog;
    }
    
    /**
     * Get the type of the object used to create this ObjectLog.
     * It is returned using the Class type.
     * @return The type of the object used to create this ObjectLog
     */
    public static Class<?> getType() {
        return Object.class;
    }
    
    /**
     * Creates the actual log files in the log folder.
     */
    protected void createLogFiles() {
        try {
            infoLog = new File(logFolder, "info.txt");
            infoLog.createNewFile();
        } catch (IOException e) {
            YARLS.getInstance().logException(e);
        }
        
        try {
            warnLog = new File(logFolder, "warn.txt");
            warnLog.createNewFile();
        } catch (IOException e) {
            YARLS.getInstance().logException(e);
        }
        
        try {
            errorLog = new File(logFolder, "error.txt");
            errorLog.createNewFile();
        } catch (IOException e) {
            YARLS.getInstance().logException(e);
        }
    }

    /**
     * Log the current status to the INFO log.
     */
    public void logCurrentStatus() {
        logCurrentStatus(LOG_LEVEL.INFO);
    }

    /**
     * Log the current status to the log level of your choosing.
     * @param logLevel The level of logging to use when logging the current status.
     */
    public void logCurrentStatus(LOG_LEVEL logLevel) {
        log(logLevel, getObject().toString());
    }
    
    /**
     * The various levels of logging:
     * INFO - Things users wouldn't care about. Usually debugging messages.
     * WARN - Things users should care about, but doesn't crash the program or cause major issues.
     * ERROR - The user needs to be able to get this information on it's own w/o looking through large files.
     */
    public enum LOG_LEVEL {INFO, WARN, ERROR};

    /**
     * Log a message to a log file.
     * @param logLevel The level of log file to write to.
     * @param message The message to write to the log file.
     */
    protected void log(LOG_LEVEL logLevel, String message) {
        File logFile;
        switch(logLevel) {
            case INFO:
            logFile = infoLog;
            break;

            case WARN:
            logFile = warnLog;
            break;

            case ERROR:
            logFile = errorLog;
            break;

            default:
            logFile = errorLog;
            break;
        }
        
        try(FileWriter fileWriter = new FileWriter(logFile)) {
            fileWriter.write(getPrefix() + message + "\n");
        } catch (IOException e) {
            YARLS.getInstance().logException(e);
        }
    }

    /**
     * Logs to the INFO log file.
     * @param message The message to log.
     */
    public void info(String message) {
        log(LOG_LEVEL.INFO, message);
    }

    /**
     * Logs to the WARN log file.
     * @param message The message to log.
     */
    public void warn(String message) {
        log(LOG_LEVEL.WARN, message);
    }

    /**
     * Logs to the ERROR log file.
     * @param message The message to log.
     */
    public void error(String message) {
        log(LOG_LEVEL.ERROR, message);
    }
    
    /**
     * Get the prefix for every entry in the log file.
     * It's in the form '[h:m:s:ms] ' where:
     * h = Hours since YARLS was constructed.
     * m = Minutes since YARLS was constructed, that weren't accounted for in hours.
     * s = Seconds since YARLS was constructed, that weren't accounted for in minutes.
     * ms = Millaseconds since YARLS was constructed, that weren't accounted for in seconds.
     * @return The prefix used in front of each entry in the log file.
     */
    protected String getPrefix() {
        long[] timeSinceStart = YARLS.getInstance().getTimeSinceStart();
        return "[" + timeSinceStart[0] + ":" + timeSinceStart[1] + ":" + timeSinceStart[2] + "] ";
    }
}