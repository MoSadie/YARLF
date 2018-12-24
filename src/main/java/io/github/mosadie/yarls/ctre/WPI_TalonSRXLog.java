package io.github.mosadie.yarls.ctre;

import java.io.File;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import io.github.mosadie.yarls.YARLS;

public class WPI_TalonSRXLog extends TalonSRXLog {

    public WPI_TalonSRXLog(WPI_TalonSRX talonSRX) {
        super(talonSRX);
    }

    @Override
    protected void makeLogFolder() {
        this.logFolder = new File(YARLS.getInstance().logDirPath + "WPI_TalonSRX/" + getObject().getBaseID());
        logFolder.mkdirs();
    }

    @Override
    public WPI_TalonSRX getObject() {
        return (WPI_TalonSRX)super.getObject();
    }

    public static Class<?> getType() {
        return WPI_TalonSRX.class;
    }

    @Override
    public void logCurrentStatus(LOG_LEVEL logLevel) {
        String message = "Current Status:";
        message += " Last Speed Value: " +      getObject().get();
        message += " | Motor Output Percent " + getObject().getMotorOutputPercent();
        message += " | Motor Output Voltage " + getObject().getMotorOutputVoltage();
        message += " | Output Current: " +      getObject().getOutputCurrent();
        message += " | Bus Voltage: " +         getObject().getBusVoltage();
        message += " | Temperature " +          getObject().getTemperature();
        message += " | Firmware Version: " +    getObject().getFirmwareVersion();

        log(logLevel, message);
    }
}