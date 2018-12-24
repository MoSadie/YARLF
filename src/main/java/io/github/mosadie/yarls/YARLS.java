package io.github.mosadie.yarls;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.reflections.Reflections;

/**
 * A system for logging information about objects of different types in an organized way.
 * It's designed to only access files when it needs to, in case the program is forcefully killed.
 * (Think robot shutoff by power switch)
 * It also creates a new folder to log in on each construction.
 */
public class YARLS {
    // Static fields and methods

    /**
     * The single instance of the YARLS object.
     */
    private static YARLS instance;

    /**
     * The base directory of all files that YARLS writes to.
     */
    public static final String baseDirPath = "YARLS/";
    
    /**
    * Get the single instance of the YARLS object.
    * @return The one instance of the YARLS object.
    */
    public static YARLS getInstance() {
        if (instance == null) {
            instance = new YARLS();
        }
        
        return instance;
    }
    
    // --------------------------------------------------------------------------------------------
    // Non-static fields and methods

    public final String logDirPath;
    private HashMap<Object, ObjectLog> objectLogMap;
    private HashMap<Class<?>, Class<? extends ObjectLog>> classMap;
    private YARLSLog mainLog;
    private final Date startTime;
    
    private YARLS() {
        // For creating the prefix. Doesn't have to be accurate to "real" time
        // as long as the relative time is accurate.
        startTime = new Date();

        // Create our main logging folder to hold this run's logs.
        long logNumber = 0;
        File logDir;
        do {
            logDir = new File(baseDirPath + logNumber);
            logNumber++;
        } while (logDir.isDirectory());

        logDirPath = baseDirPath + --logNumber + "/";

        // Create our "main" log.
        mainLog = new YARLSLog(this);

        // Prep the objectLog and class mapping objects.
        objectLogMap = new HashMap<Object, ObjectLog>();
        classMap = new HashMap<Class<?>, Class<? extends ObjectLog>>();
        
        // This is the fun part. We're taking every class that extends ObjectLog (or extends something that extends it)
        // and puts them in a mapping from class of the wanted type (See ObjectLog's getType() method)
        // When this is done, the map will have entries like this:
        // Class<WPI_TalonSRX> maps to Class<WPI_TalonSRXLog>

        // Get a set of the classes that extend ObjectLog
        Set<Class<? extends ObjectLog>> subTypes = new Reflections("io.github.mosadie.yarls").getSubTypesOf(ObjectLog.class);
        Iterator<Class<? extends ObjectLog>> iterator = subTypes.iterator();
        while(iterator.hasNext()) {
            Class<? extends ObjectLog> logClass = iterator.next();
            try {
                // Try to call the static getType() method of that class.
                classMap.put((Class<?>)logClass.getMethod("getType").invoke(null), logClass);
            } catch (Exception e) {
                logException(e);
            }
        }
    }

    /**
     * Get the path of the main log folder directory.
     * @return The path of the main log folder, as a String.
     */
    protected String getLogDirPath() {
        return logDirPath;
    }
    
    /**
     * Get the ObjectLog (or the correct subclass) for a given object.
     * Creates one if one doesn't already exist.
     * @param key The object to get an ObjectLog for.
     * @return The ObjectLog (or correct subclass) for the object.
     */
    public ObjectLog getObjectLog(Object key) {
        if (!objectLogMap.containsKey(key)) {
            objectLogMap.put(key, createObjectLog(key));
        }
        
        return objectLogMap.get(key);
    }
    
    /**
     * Create the ObjectLog (or correct subclass) for a given object.
     * Should only be called by getObjectLog(Object key)
     */
    private ObjectLog createObjectLog(Object object) {
        if (classMap.containsKey(object.getClass())) {
            try {
            Class<? extends ObjectLog> objectLogType = getObjectLogType(object);
            return (ObjectLog)objectLogType.getConstructors()[0].newInstance(object);
            } catch (Exception e) {
                logException(e);
            }
        }
        
        return new ObjectLog(object);
    }

    /**
     * Get the ObjectLog type for the object given.
     * Defaults to ObjectLog, but can be another class that extends ObjectLog.
     * @param object The object who's ObjectLog type you want to know.
     * @return A Class object pointing to the class that extends ObjectLog that best matches your object.
     */
    public Class<? extends ObjectLog> getObjectLogType(Object object) {
        if (classMap.containsKey(object.getClass())) {
            return classMap.get(object.getClass());
        }

        return ObjectLog.class;
    }

    /**
     * Log an exception to the YARLS's exception log.
     * @param exception The exception object to log information about.
     */
    public void logException(Exception exception) {
        mainLog.logException(exception);
    }

    /**
     * Get the time in hours, minutes, seconds, and millaseconds since YARLS was constructed.
     * @return An array of longs representing the time since YARLS was constructed in the form of [hours, minutes, seconds, millaseconds]
     */
    public long[] getTimeSinceStart() {
        long mills = new Date().getTime()-startTime.getTime();
        long totalSeconds = mills/1000;
        mills = mills % 1000;
        long hours = totalSeconds/60/60;
        totalSeconds -= hours*360;
        long minutes = totalSeconds/60;
        totalSeconds -= minutes*60;
        long seconds = totalSeconds;

        return new long[] {hours, minutes, seconds, mills};
    }
}