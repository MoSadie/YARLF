package io.github.mosadie.yarls.navx;

import java.io.File;

import com.kauailabs.navx.frc.AHRS;

import io.github.mosadie.yarls.ObjectLog;
import io.github.mosadie.yarls.YARLS;

public class NavXLog extends ObjectLog {

    public NavXLog(AHRS navx) {
        super(navx);
    }

    @Override
    protected void makeLogFolder() {
        this.logFolder = new File(YARLS.getInstance().logDirPath + "NavX/");
        logFolder.mkdirs();
    }

    @Override
    public AHRS getObject() {
        return (AHRS)super.getObject();
    }

    public static Class<?> getType() {
        return AHRS.class;
    }

    @Override
    public void logCurrentStatus(LOG_LEVEL logLevel) {
        String message = "Current Status:";
        message += " Is Connected: " +       getObject().isConnected();
        message += " | Is Calibrating: " +   getObject().isCalibrating();
        message += " | Firmware Version: " + getObject().getFirmwareVersion();
        message += " | Yaw: " +              getObject().getYaw();
        message += " | Pitch: " +            getObject().getPitch();
        message += " | Roll: " +             getObject().getRoll();

        log(logLevel, message);
    }
}