package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.stealthrobotics.library.Commands;
import org.stealthrobotics.library.StealthSubsystem;

import java.util.function.DoubleSupplier;

public class Giraffe extends StealthSubsystem {

    private final DcMotorEx giraffeMotor1;
    private final DcMotorEx giraffeMotor2;

    private final PIDFController giraffePID;

    private final Number kP = 0.0;
    private final Number kI = 0.0;
    private final Number kD = 0.0;
    private final Number kF = 0.0;
    private final double POSITION_TOLERANCE = 10.0;

    private GiraffeMode mode = GiraffeMode.PID;

    private final DoubleSupplier manualControl;

    private final Telemetry telemetry;

    public Giraffe(HardwareMap john, DoubleSupplier manualControl, Telemetry telemetry) {
        giraffeMotor1 = john.get(DcMotorEx.class, "giraffeMotor1");
        giraffeMotor2 = john.get(DcMotorEx.class, "giraffeMotor2");

        giraffeMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        giraffeMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        giraffePID = new PIDFController(kP.doubleValue(), kI.doubleValue(), kD.doubleValue(), kF.doubleValue());
        giraffePID.setTolerance(POSITION_TOLERANCE);
        this.manualControl = manualControl;
        this.telemetry = telemetry;
    }

    public Giraffe(HardwareMap john, Telemetry telemetry) {
        this(john, () -> 0.0, telemetry);
    }

    private void setGiraffePower(long power) {
        giraffeMotor1.setPower(power);
        giraffeMotor2.setPower(power);
    }

    private void setTargetPosition(int position) {
        giraffePID.setSetPoint(position);
    }

    private void holdPosition() {
        giraffePID.setSetPoint(giraffeMotor1.getCurrentPosition());
    }

    /**
     * Returns a command to go to a position.
     *
     * @param flavor the state to go to
     * @return the command to go to the position
     * First sets the target position, then waits until the PID controller is at the set point. Races with a command to set the giraffe power to the calculated PID output. This command will never end, so it ends when the first command ends, which will be when the PID controller is at the set point.
     */
    public Command setFlavor(GiraffeState flavor) {
        return this.runOnce(() -> setTargetPosition(flavor.getPosition()))
                .andThen(new WaitUntilCommand(giraffePID::atSetPoint));
    }

    /**
     * sets the mode to manual to disable PID use, then cancels the command once the trigger or joystick is released.
     * sets mode back to pid and holds the position.
     * @return the command to manually control the giraffe
     *
     *
     */
    public Command tameGiraffe() {
        return this.runOnce(() -> mode = GiraffeMode.MANUAL).andThen(new WaitUntilCommand(() -> Math.abs((long) manualControl.getAsDouble()) < 0.05));
//                .andThen(this.runOnce(() -> mode = GiraffeMode.PID)).andThen(this.runOnce(this::holdPosition));
    }

    public void toggleHold(){
        if(mode != GiraffeMode.HOLDING){
            mode = GiraffeMode.HOLDING;
        }
        else{
            mode = GiraffeMode.PID;
        }
    }

    /**
     * periodically sets the giraffe power based on the mode.
     * if in pid mode, calculates the power based on the pid controller. if in manual mode, sets the power based on the manual control.
     */
    @Override
    public void periodic(){
        if(mode == GiraffeMode.PID){
            setGiraffePower((long) giraffePID.calculate(giraffeMotor1.getCurrentPosition()));
        }
        if(mode == GiraffeMode.MANUAL){
            setGiraffePower((long) manualControl.getAsDouble());
        }
        if(mode == GiraffeMode.HOLDING){
            setGiraffePower(1);
        }
        telemetry.addData("Mode", mode.name());
    }

    public enum GiraffeState {

        HIGH_BUCKET(0),
        LOW_BUCKET(0),
        HOME(0),
        LOW_RUNG(0),
        HIGH_RUNG(0);

        private int position;

        private GiraffeState(int position) {
            this.position = position;


        }

        public int getPosition() {
            return position;
        }
    }

    private enum GiraffeMode{
        MANUAL,
        PID,
        HOLDING
    }


}
