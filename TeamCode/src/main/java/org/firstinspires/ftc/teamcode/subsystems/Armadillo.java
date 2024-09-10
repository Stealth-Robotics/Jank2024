package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.stealthrobotics.library.StealthSubsystem;

public class Armadillo extends StealthSubsystem {

    private final Servo armadilloServo;
    private final Servo mouthServo;

    public Armadillo(HardwareMap joseph){
        armadilloServo = joseph.get(Servo.class, "armadilloServo");
        mouthServo = joseph.get(Servo.class, "mouthServo");
    }

    //TODO: tune wait time to accurately reflect time it takes to move to position
    public Command setArmadilloState(ArmadilloState state) {
        return this.runOnce(() -> armadilloServo.setPosition(state.getPosition())).andThen(new WaitCommand(250));
    }

    public Command setMouthState(MouthState state) {
        return this.runOnce(() -> mouthServo.setPosition(state.getPosition())).andThen(new WaitCommand(250));
    }

    public enum ArmadilloState {
        TRANSFER(0.0),
        SCORE_BUCKET(1.0),
        SCORE_CLIP(0.5),
        STOW(0.0);

        private final double position;

        ArmadilloState(double position) {
            this.position = position;
        }

        public double getPosition() {
            return position;
        }
    }

    public enum MouthState {
        OPEN(0.0),
        CLOSE(1.0);

        private final double position;

        MouthState(double position) {
            this.position = position;
        }

        public double getPosition() {
            return position;
        }
    }
}
