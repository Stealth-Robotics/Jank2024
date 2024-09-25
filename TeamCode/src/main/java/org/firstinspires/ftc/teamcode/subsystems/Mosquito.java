package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
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

    private SuckSide side = SuckSide.FRONT;



    public Mosquito(HardwareMap blood){
        legs = blood.get(Servo.class, "legs");
        straw = blood.get(DcMotorEx.class, "straw");
        eyes = blood.get(ColorSensor.class, "eyes");
    }

    private Command setLegState(LegState state) {
        return Commands.runOnce(() -> legs.setPosition(state.getPosition())).andThen(new WaitCommand(250));
    }

    public SuckSide getSide() {
        return side;
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
                .alongWith(new ConditionalCommand(setLegState(LegState.SUCKING_FRONT),
                        setLegState(LegState.SUCKING_BACK), () -> side == SuckSide.FRONT))
                .interruptOn(() -> Math.abs(power.getAsDouble()) < 0.05 || eyes.red() > 100 || eyes.blue() > 100)
                .andThen(setLegState(LegState.HOME))
                .andThen(this.runOnce(() -> straw.setPower(0.0)));
    }

    public enum LegState {
        SUCKING_FRONT(0.0),
        SUCKING_BACK(0.0),
        HOME(1.0);

        private final double position;

        LegState(double position) {
            this.position = position;
        }

        public double getPosition() {
            return position;
        }
    }

    public enum SuckSide {
        FRONT,
        BACK
    }


}
