package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.stealthrobotics.library.Commands;
import org.stealthrobotics.library.StealthSubsystem;

import java.util.function.DoubleSupplier;

public class Mosquito extends StealthSubsystem {

    private final Servo legs;
    private final DcMotorEx straw;
    private final ColorSensor eyes;

    public Mosquito(HardwareMap blood){
        legs = blood.get(Servo.class, "legs");
        straw = blood.get(DcMotorEx.class, "straw");
        eyes = blood.get(ColorSensor.class, "eyes");
    }

    private void setLegState(LegState state) {
        legs.setPosition(state.getPosition());
    }

    /**
     * Returns a command to intake. First pivots intake down and starts running intake.
     * If trigger is released or color sensor detects red or blue, stops intake and pivots intake back up.
     * TODO: tune color values
     * @param power doublesupplier giving trigger
     * @return command to run stuff
     */
    public Command suck(DoubleSupplier power){
        return this.run(() -> straw.setPower(power.getAsDouble()))
                .alongWith(Commands.runOnce(() -> setLegState(LegState.SUCKING)))
                .interruptOn(() -> Math.abs(power.getAsDouble()) < 0.05 || eyes.red() > 100 || eyes.blue() > 100)
                .andThen(this.runOnce(() -> setLegState(LegState.HOME)))
                .andThen(this.runOnce(() -> straw.setPower(0.0)));
    }

    public enum LegState {
        SUCKING(0.0),
        HOME(1.0);

        private final double position;

        LegState(double position) {
            this.position = position;
        }

        public double getPosition() {
            return position;
        }
    }


}
