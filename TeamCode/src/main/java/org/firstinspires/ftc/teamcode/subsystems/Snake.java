package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.controller.PIDController;
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

    private SnakeMode mode = SnakeMode.PID;

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

    /**
     * Returns a command to move the snake to a position. sets PID target, then periodic handles setting power.
     * ends when at setpoint
     * @param position the position to move to
     * @return the command to move the snake
     */
    public Command snakeToPosition(SnakePosition position){
        return this.runOnce(() -> setSetpoint(position.getPosition())).andThen(new WaitUntilCommand(this::atSetpoint));
    }

    @Override
    public void periodic() {
        if(mode == SnakeMode.PID){
            tongue.setPower(snakePID.calculate(tongue.getCurrentPosition()));
        }
    }

    private enum SnakeMode {
        PID,
        MANUAL
    }

    public enum SnakePosition {
        INTAKING(0),
        HOME(1);

        private final int position;

        SnakePosition(int position){
            this.position = position;
        }

        public int getPosition(){
            return position;
        }
    }
}
