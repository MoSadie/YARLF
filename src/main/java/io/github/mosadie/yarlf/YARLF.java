package io.github.mosadie.yarlf;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.reflections.Reflections;

public class YARLF {
    private static YARLF instance;
    public static final String baseDirPath = "YARLF/";
    
    /**
    * Get the single instance of the YARLF object.
    * @return The one instance of the YARLF object.
    */
    public static YARLF getInstance() {
        if (instance == null) {
            instance = new YARLF();
        }
        
        return instance;
    }
    
    // --------------------------------------------------------------------------------------------
    public final String logDirPath;
    private HashMap<Object, ObjectLog> objectLogMap;
    private HashMap<Class<?>, Class<? extends ObjectLog>> classMap;
    private YARLFLog mainLog;
    private final Date startTime;
    
    private YARLF() {
        startTime = new Date();
        long logNumber = 0;
        File logDir;
        do {
            logDir = new File(baseDirPath + logNumber);
            logNumber++;
        } while (logDir.isDirectory());

        logDirPath = baseDirPath + --logNumber + "/";

        mainLog = new YARLFLog(this);

        objectLogMap = new HashMap<Object, ObjectLog>();
        classMap = new HashMap<Class<?>, Class<? extends ObjectLog>>();
        
        Set<Class<? extends ObjectLog>> subTypes = new Reflections("io.github.mosadie.yarlf").getSubTypesOf(ObjectLog.class);
        Iterator<Class<? extends ObjectLog>> iterator = subTypes.iterator();
        while(iterator.hasNext()) {
            Class<? extends ObjectLog> logClass = iterator.next();
            try {
                classMap.put((Class<?>)logClass.getMethod("getType").invoke(null), logClass);
            } catch (Exception e) {
                logException(e);
            }
        }
    }

    protected String getLogDirPath() {
        return logDirPath;
    }
    
    public ObjectLog getObjectLog(Object key) {
        if (!objectLogMap.containsKey(key)) {
            objectLogMap.put(key, createObjectLog(key));
        }
        
        return objectLogMap.get(key);
    }
    
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

    public Class<? extends ObjectLog> getObjectLogType(Object object) {
        if (classMap.containsKey(object.getClass())) {
            return classMap.get(object.getClass());
        }

        return ObjectLog.class;
    }

    public void logException(Exception e) {
        mainLog.logException(e);
    }

    public long[] getTimeSinceStart() {
        long mills = new Date().getTime()-startTime.getTime();
        long totalSeconds = mills/1000;
        long hours = totalSeconds/60/60;
        totalSeconds -= hours*360;
        long minutes = totalSeconds/60;
        totalSeconds -= minutes*60;
        long seconds = totalSeconds;

        return new long[] {hours, minutes, seconds};
    }
}