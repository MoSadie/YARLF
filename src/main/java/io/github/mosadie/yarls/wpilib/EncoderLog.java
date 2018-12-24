package io.github.mosadie.yarls.wpilib;

import java.io.File;

import edu.wpi.first.wpilibj.Encoder;
import io.github.mosadie.yarls.ObjectLog;
import io.github.mosadie.yarls.YARLS;

public class EncoderLog extends ObjectLog {

    public EncoderLog(Encoder encoder) {
        super(encoder);
    }

    @Override
    protected void makeLogFolder() {
        this.logFolder = new File(YARLS.getInstance().logDirPath + "Encoder/" + getObject().getFPGAIndex()); //TODO Check if this is unique.
        logFolder.mkdirs();
    }

    @Override
    public Encoder getObject() {
        return (Encoder)super.getObject();
    }

    public static Class<?> getType() {
        return Encoder.class;
    }

    @Override
    public void logCurrentStatus(LOG_LEVEL logLevel) {
        String message = "Current Status:";
        message += " Count " +   getObject().get();
        message += " | Distance " + getObject().getDistance();
        message += " | Distance rate: " + getObject().getRate();
        message += " | Raw Count: " +         getObject().getRaw();

        log(logLevel, message);
    }
}