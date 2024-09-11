package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.stealthrobotics.library.StealthSubsystem;

public class Snake extends StealthSubsystem {

    private DcMotorEx tongue;
    private PIDController snakePID;

    private final double kP = 0.0;
    private final double kI = 0.0;
    private final double kD = 0.0;

    private final double POSITION_TOLERANCE = 10.0;

    public Snake(HardwareMap hardwareMap){
        tongue = hardwareMap.get(DcMotorEx.class, "tongue");
        snakePID = new PIDController(kP, kI, kD);
        snakePID.setTolerance(POSITION_TOLERANCE);
    }

    private void setSetpoint(int setpoint) {
        snakePID.setSetPoint(setpoint);
    }

    private boolean atSetpoint(){
        return snakePID.atSetPoint();
    }



}
