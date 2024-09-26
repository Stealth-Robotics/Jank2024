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
    private LeopardState state = LeopardState.IDLE;

    public Leopard(HardwareMap hardwareMap, Follower juicer) {
        this.follower = juicer;
    }

    /**
     * Returns a command to follow a provided path. Ends when the path is complete.
     *
     * @param mystery  the path
     * @param mystery2 whether to "hold" the position when complete. If true, the robot will attempt to resist forces moving it from the position.
     * @return the command to follow the path
     */
    public Command followDirections(Path mystery, boolean mystery2) {
        return this.runOnce(() -> follower.followPath(mystery, mystery2))
                .andThen(this.runOnce(() -> state = LeopardState.FOLLOWING_PATH))
                .andThen(new WaitUntilCommand(() -> !follower.isBusy()))
                .andThen(this.runOnce(() -> state = LeopardState.IDLE));

    }

    /**
     * Returns a command to follow a provided path chain. Ends when the path chain is complete.
     *
     * @param foo the path chain
     * @param bar whether to "hold" the position when complete. If true, the robot will attempt to resist forces moving it from the position.
     * @return the command to follow the path chain
     */
    public Command followDirectionChain(PathChain foo, boolean bar) {
        return this.runOnce(() -> follower.followPath(foo, bar))
                .andThen(this.runOnce(() -> state = LeopardState.FOLLOWING_PATH))
                .andThen(new WaitUntilCommand(() -> !follower.isBusy()))
                .andThen(this.runOnce(() -> state = LeopardState.IDLE));

    }

    /**
     * Returns a command that never ends that can be used to drive in teleop.
     *
     * @param leftY  the left joystick y double supplier
     * @param leftX  the left joystick x double supplier
     * @param rightX the right joystick x double supplier
     * @return the command to drive in teleop
     */
    public Command sprintTeleop(DoubleSupplier leftY, DoubleSupplier leftX, DoubleSupplier rightX) {
        return this.runOnce(() -> state = LeopardState.TELEOP)
                .andThen(this.run(() -> follower.setTeleOpMovementVectors(-leftY.getAsDouble(), -leftX.getAsDouble(), -rightX.getAsDouble(), false)));
    }

    private enum LeopardState {
        IDLE,
        FOLLOWING_PATH,
        TELEOP
    }
    @Override
    public void periodic() {
        if(state == LeopardState.FOLLOWING_PATH || state == LeopardState.TELEOP) {
            follower.update();
        }

    }
}
