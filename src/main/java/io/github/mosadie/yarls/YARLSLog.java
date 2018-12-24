package io.github.mosadie.yarls;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class YARLSLog extends ObjectLog {
    protected File exceptionLog;

    public YARLSLog(YARLS yarlf) {
        super(yarlf);
    }

    @Override
    protected void makeLogFolder() {
        this.logFolder = new File(getObject().getLogDirPath() + "YARLF/");
        logFolder.mkdir();
    }

    @Override
    public YARLS getObject() {
        return (YARLS)super.getObject();
    }

    public static Class<?> getType() {
        return YARLS.class;
    }

    @Override
    protected void createLogFiles() {
        super.createLogFiles();
        try {
            exceptionLog = new File(logFolder, "exceptions.txt");
            exceptionLog.createNewFile();
        } catch (IOException e) {
            logException(e);
        }
    }

    public void logException(Exception exception) {
        if (exceptionLog.exists()) {
            try (FileWriter output = new FileWriter(exceptionLog)) {
                output.write(getPrefix() + "Exception: " + exception.toString() + "\n" + getPrefix() + "Stacktrace: " + Arrays.toString(exception.getStackTrace()) + "\n");
            } catch (Exception e) {
                e.printStackTrace(); // And feel sad... :(
            }
        }
    }
}