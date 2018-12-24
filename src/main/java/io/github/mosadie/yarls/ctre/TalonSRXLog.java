package io.github.mosadie.yarls.ctre;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import io.github.mosadie.yarls.ObjectLog;
import io.github.mosadie.yarls.YARLS;

import java.io.File;

public class TalonSRXLog extends ObjectLog {
    
    public TalonSRXLog(TalonSRX talonSRX) {
        super(talonSRX);
    }

    @Override
    protected void makeLogFolder() {
        this.logFolder = new File(YARLS.getInstance().logDirPath + "TalonSRX/" + getObject().getBaseID());
        logFolder.mkdirs();
    }

    @Override
    public TalonSRX getObject() {
        return (TalonSRX)super.getObject();
    }

    public static Class<?> getType() {
        return TalonSRX.class;
    }

    @Override
    public void logCurrentStatus(LOG_LEVEL logLevel) {
        String message = "Current Status:";
        message += " Motor Output Percent " +   getObject().getMotorOutputPercent();
        message += " | Motor Output Voltage " + getObject().getMotorOutputVoltage();
        message += " | Output Current: " +      getObject().getOutputCurrent();
        message += " | Bus Voltage: " +         getObject().getBusVoltage();
        message += " | Temperature " +          getObject().getTemperature();
        message += " | Firmware Version: " +    getObject().getFirmwareVersion();

        log(logLevel, message);
    }
}