package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

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

    public Giraffe(HardwareMap john) {
        giraffeMotor1 = john.get(DcMotorEx.class, "giraffeMotor1");
        giraffeMotor2 = john.get(DcMotorEx.class, "giraffeMotor2");

        giraffePID = new PIDFController(kP.doubleValue(), kI.doubleValue(), kD.doubleValue(), kF.doubleValue());
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
                .andThen(new WaitUntilCommand(giraffePID::atSetPoint))
                .raceWith(Commands.run(() -> setGiraffePower((long) giraffePID.calculate(giraffeMotor1.getCurrentPosition()))));
    }

    public Command tameGiraffe(DoubleSupplier power) {
        return this.run(() -> setGiraffePower((long) power.getAsDouble())).interruptOn(() -> Math.abs((long) power.getAsDouble()) < 0.05)
                .whenFinished(this::holdPosition);

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


}
