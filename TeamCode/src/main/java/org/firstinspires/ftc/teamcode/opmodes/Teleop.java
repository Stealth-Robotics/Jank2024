package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Armadillo;
import org.firstinspires.ftc.teamcode.subsystems.Giraffe;
import org.firstinspires.ftc.teamcode.subsystems.Leopard;
import org.firstinspires.ftc.teamcode.subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.subsystems.Mosquito;
import org.firstinspires.ftc.teamcode.subsystems.Snake;
import org.stealthrobotics.library.Commands;
import org.stealthrobotics.library.opmodes.StealthOpMode;

import java.util.function.DoubleSupplier;

public class Teleop extends StealthOpMode {
    GamepadEx driverGamepad;
    GamepadEx operatorGamepad;
    private Follower follower;
//    private Leopard leopard;
    private Giraffe giraffe;
    private Armadillo armadillo;
    private Mosquito mosquito;
    private Snake snake;
    private Mecanum mecanum;

    Limelight3A ll;

    @Override
    public void whileWaitingToStart() {
        CommandScheduler.getInstance().run();

    }

    @Override
    public void initialize() {
        ll = hardwareMap.get(Limelight3A.class,"limelight");
        
        DoubleSupplier giraffeControl = () -> operatorGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                operatorGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
        DoubleSupplier mosquitoControl = () -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
//        follower = new Follower(hardwareMap);
////        leopard = new Leopard(hardwareMap, follower);
//        giraffe = new Giraffe(hardwareMap, giraffeControl);
//        armadillo = new Armadillo(hardwareMap);
//        mosquito = new Mosquito(hardwareMap);
        mecanum = new Mecanum(hardwareMap, new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0)));


        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);


        register(mecanum);

//        leopard.setDefaultCommand(leopard.sprintTeleop(driverGamepad::getLeftY, driverGamepad::getLeftX, driverGamepad::getRightX));

//        mecanum.setDefaultCommand(mecanum.driveTeleop(driverGamepad::getLeftX, driverGamepad::getLeftY, driverGamepad::getRightX));
        mecanum.setDefaultCommand(mecanum.driveTeleop(() -> driverGamepad.getLeftX(), () -> driverGamepad.getLeftY(), () -> driverGamepad.getRightX()));

        new Trigger(() -> driverGamepad.getButton(GamepadKeys.Button.DPAD_DOWN)).whenActive(new InstantCommand(() -> mecanum.resetHeading()));


        //Giraffe manual control. when either trigger is pressed, the giraffe will move.
        // because of a condition in the command in the subsystem, the command will be unscheduled once the trigger is released.
//        new Trigger(() -> Math.abs(giraffeControl.getAsDouble()) > 0.05)
//                .whenActive(giraffe.tameGiraffe(), true);
//
//        //Stow preset
//        new Trigger(() -> operatorGamepad.getButton(GamepadKeys.Button.A))
//                .whenActive(giraffe.setFlavor(Giraffe.GiraffeState.HOME)
//                        .alongWith(armadillo.setArmadilloState(Armadillo.ArmadilloState.STOW))
//                        .andThen(armadillo.setMouthState(Armadillo.MouthState.OPEN)), true);
//
//        new Trigger(() -> Math.abs(mosquitoControl.getAsDouble()) > 0.05)
//                .whenActive(mosquito.suck(mosquitoControl), true);
//
//        new Trigger(() -> mosquitoControl.getAsDouble() == 0.0)
//                .whenActive(new ConditionalCommand(snake.snakeToPosition(Snake.SnakePosition.INTAKING),
//                        Commands.none(), () -> mosquito.getSide() == Mosquito.SuckSide.FRONT), true);




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
