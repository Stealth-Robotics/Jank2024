package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.subsystems.Armadillo;
import org.firstinspires.ftc.teamcode.subsystems.Giraffe;
import org.firstinspires.ftc.teamcode.subsystems.Leopard;
import org.firstinspires.ftc.teamcode.subsystems.Mosquito;
import org.stealthrobotics.library.opmodes.StealthOpMode;

import java.util.function.DoubleSupplier;

public class Teleop extends StealthOpMode {
    GamepadEx driverGamepad;
    GamepadEx operatorGamepad;
    private Follower follower;
    private Leopard leopard;
    private Giraffe giraffe;
    private Armadillo armadillo;
    private Mosquito mosquito;

    @Override
    public void whileWaitingToStart() {
        CommandScheduler.getInstance().run();

    }

    @Override
    public void initialize() {
        DoubleSupplier giraffeControl = () -> operatorGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                operatorGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
        follower = new Follower(hardwareMap);
        leopard = new Leopard(hardwareMap, follower);
        giraffe = new Giraffe(hardwareMap, giraffeControl);
        armadillo = new Armadillo(hardwareMap);
        mosquito = new Mosquito(hardwareMap);


        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);


        register(leopard, giraffe, armadillo);

        leopard.setDefaultCommand(leopard.sprintTeleop(driverGamepad::getLeftY, driverGamepad::getLeftX, driverGamepad::getRightX));


        //Giraffe manual control. when either trigger is pressed, the giraffe will move.
        // because of a condition in the command in the subsystem, the command will be unscheduled once the trigger is released.
        new Trigger(() -> Math.abs(giraffeControl.getAsDouble()) > 0.05)
                .whenActive(giraffe.tameGiraffe(), true);

        //Stow preset
        new Trigger(() -> operatorGamepad.getButton(GamepadKeys.Button.A))
                .whenActive(giraffe.setFlavor(Giraffe.GiraffeState.HOME)
                        .alongWith(armadillo.setArmadilloState(Armadillo.ArmadilloState.STOW))
                        .andThen(armadillo.setMouthState(Armadillo.MouthState.OPEN)), true);

        new Trigger(() -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05
                || driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05)
                .whenActive(mosquito.suck(() -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                        driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)), true);


    }

    @SuppressWarnings("unused")
    //sets the camera to the red prop processor if alliance is red
    @TeleOp(name = "RED | Tele-Op", group = "Red")
    public static class RedTeleop extends Teleop {


    }

    //sets the camera to the blue prop processor if alliance is blue
    @SuppressWarnings("unused")
    @TeleOp(name = "BLUE | Tele-Op", group = "Blue")
    public static class BlueTeleop extends Teleop {

    }
}
