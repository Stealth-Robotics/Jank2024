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

@Autonomous(name = "Park", preselectTeleOp = "BLUE | Auto")
public class ParkAuto extends StealthOpMode {
    private Mecanum mecanum;

    @Override
    public void initialize() {
        mecanum = new Mecanum(hardwareMap, new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0)));
        register(mecanum);
    }

    public void whileWaitingToStart() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public Command getAutoCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> mecanum.resetHeading()),
                mecanum.driveTeleop(() -> 0.0, () -> -0.5, () -> 0.0).withTimeout(800),
                mecanum.driveTeleop(() -> 0.0, () -> 0.0, () -> 0.0)
        );
    }
}
