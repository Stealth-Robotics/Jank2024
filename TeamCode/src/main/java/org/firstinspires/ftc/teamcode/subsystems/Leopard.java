package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.stealthrobotics.library.Commands;
import org.stealthrobotics.library.StealthSubsystem;

import java.util.function.DoubleSupplier;

public class Leopard extends StealthSubsystem {

    private final Follower follower;

    public Leopard(HardwareMap hardwareMap, Follower follower) {
        this.follower = follower;
    }

    public Command followPath(Path path, boolean holdEnd) {
        return this.runOnce(() -> follower.followPath(path, holdEnd)).raceWith(Commands.run(follower::update))
                .andThen(new WaitUntilCommand(() -> !follower.isBusy()));
    }

    public Command followPathChain(PathChain pathChain, boolean holdEnd) {
        return this.runOnce(() -> follower.followPath(pathChain, holdEnd)).raceWith(Commands.run(follower::update))
                .andThen(new WaitUntilCommand(() -> !follower.isBusy()));
    }

    public Command driveTeleop(DoubleSupplier leftY, DoubleSupplier leftX, DoubleSupplier rightX) {
        return this.run(() -> follower.setTeleOpMovementVectors(-leftY.getAsDouble(), -leftX.getAsDouble(), -rightX.getAsDouble()))
                .alongWith(Commands.run(follower::update));
    }

    @Override
    public void periodic() {
        follower.update();
    }
}
