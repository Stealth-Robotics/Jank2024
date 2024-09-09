package org.firstinspires.ftc.teamcode.paths;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

public class BluePaths {
    public static PathChain blueBucketSidePath1;
    private final Pose BUCKET_SCORING_POSE = new Pose(15.36, 128.46, Math.toRadians(135));
    private final Pose BUCKET_SIDE_START_POSE = new Pose(9.5, 104.7, Math.toRadians(0));
    Follower follower;

    public BluePaths(Follower follower) {
        this.follower = follower;
    }

    public void buildPaths() {
        blueBucketSidePath1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(BUCKET_SIDE_START_POSE), new Point(31, 104, Point.CARTESIAN),
                        new Point(BUCKET_SCORING_POSE)))
                .setLinearHeadingInterpolation(BUCKET_SIDE_START_POSE.getHeading(), BUCKET_SCORING_POSE.getHeading())
                .build();
    }
}
