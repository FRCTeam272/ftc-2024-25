package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTestingAutonExample {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)

                // Basic Square
                 // Set bot starting location
                 .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(0, 0, 0))

                         //Paste Trajectory here

                         .splineTo(new Vector2d(10, 10), 0)
                         .turn(Math.toRadians(90))
                         .splineTo(new Vector2d(25, -15), 0)
                         .waitSeconds(3)
                         .turn(Math.toRadians(45))
                         .forward(10)
                         .strafeRight(5)
                         .turn(Math.toRadians(90))
                         .strafeLeft(5)
                         .waitSeconds(1)
                         .splineToLinearHeading(new Pose2d(-10, -10, Math.toRadians(45)), 0)

                        //End of Trajectory
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}