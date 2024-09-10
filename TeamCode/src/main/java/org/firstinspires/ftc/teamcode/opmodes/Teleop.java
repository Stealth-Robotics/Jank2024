package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.subsystems.Giraffe;
import org.firstinspires.ftc.teamcode.subsystems.Leopard;
import org.stealthrobotics.library.opmodes.StealthOpMode;

public class Teleop extends StealthOpMode {
    private Follower follower;
    private Leopard leopard;
    private Giraffe giraffe;

    GamepadEx driverGamepad;
    GamepadEx operatorGamepad;

    @Override
    public void whileWaitingToStart() {
        CommandScheduler.getInstance().run();

    }

    @Override
    public void initialize() {
        follower = new Follower(hardwareMap);
        leopard = new Leopard(hardwareMap, follower);
        giraffe = new Giraffe(hardwareMap);

        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);


        register(leopard, giraffe);

        leopard.setDefaultCommand(leopard.sprintTeleop(driverGamepad::getLeftY, driverGamepad::getLeftX, driverGamepad::getRightX));


        //Giraffe manual control. when either trigger is pressed, the giraffe will move.
        // because of a condition in the command in the subsystem, the command will be unscheduled once the trigger is released.
        new Trigger(() -> (operatorGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05)
                || (operatorGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05))
                .whenActive(giraffe.tameGiraffe(() -> operatorGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                        operatorGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)), true);


    }
}
