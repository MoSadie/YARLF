package io.github.mosadie.yarlf.ctre;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import io.github.mosadie.yarlf.ObjectLog;
import io.github.mosadie.yarlf.YARLF;

import java.io.File;

public class TalonSRXLog extends ObjectLog {
    
    public TalonSRXLog(TalonSRX talonSRX) {
        super(talonSRX);
    }

    @Override
    protected void makeLogFolder() {
        this.logFolder = new File(YARLF.baseDirPath + "TalonSRX/" + getObject().getBaseID());
        logFolder.mkdirs();
    }

    @Override
    public TalonSRX getObject() {
        return (TalonSRX)super.getObject();
    }

    public static Class<?> getType() {
        return TalonSRX.class;
    }
}