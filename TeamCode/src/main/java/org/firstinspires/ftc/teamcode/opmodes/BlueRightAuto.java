package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Mecanum;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "Right", preselectTeleOp = "BLUE | Auto")
public class BlueRightAuto extends StealthOpMode {
    private Mecanum mecanum;

    @Override
    public void initialize() {
        mecanum = new Mecanum(hardwareMap, new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0)));
        register(mecanum);
        mecanum.resetHeading();
    }

    public void whileWaitingToStart() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public Command getAutoCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> mecanum.resetHeading()),
                mecanum.driveTeleop(() -> 0.0, () -> 0.5, () -> 0.0),
                new WaitCommand(500),
                mecanum.driveTeleop(() -> 0.0, () -> 0.0, () -> 0.0),

                mecanum.driveTeleop(() -> 0.5, () -> 0.0, () -> 0.0),
                new WaitCommand(50),
                mecanum.driveTeleop(() -> 0.0, () -> 0.0, () -> 0.0),

                mecanum.driveTeleop(() -> 0.0, () -> -0.5, () -> 0.0),
                new WaitCommand(500),
                mecanum.driveTeleop(() -> 0.0, () -> 0.0, () -> 0.0),

                //Repeat

                mecanum.driveTeleop(() -> 0.0, () -> 0.5, () -> 0.0),
                new WaitCommand(500),
                mecanum.driveTeleop(() -> 0.0, () -> 0.0, () -> 0.0),

                mecanum.driveTeleop(() -> 0.5, () -> 0.0, () -> 0.0),
                new WaitCommand(50),
                mecanum.driveTeleop(() -> 0.0, () -> 0.0, () -> 0.0),

                mecanum.driveTeleop(() -> 0.0, () -> -0.5, () -> 0.0),
                new WaitCommand(500),
                mecanum.driveTeleop(() -> 0.0, () -> 0.0, () -> 0.0),

                //Park
                mecanum.driveTeleop(() -> 0.5, () -> 0.0, () -> 0.0),
                new WaitCommand(100),
                mecanum.driveTeleop(() -> 0.0, () -> 0.0, () -> 0.0)
        );
    }
}
