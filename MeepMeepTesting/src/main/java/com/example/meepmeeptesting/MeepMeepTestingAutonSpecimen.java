package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTestingAutonSpecimen {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)

                // Basic Square
                 // Set bot starting location
                 .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(24, -63, Math.toRadians(90)))

                         //Paste Trajectory here
                         //Specimen Side
                        //Drive to Chambers
                         .lineToConstantHeading(new Vector2d(0,-33))
                         //score specimen
                         .waitSeconds(1)
                         .lineTo(new Vector2d(12,-33))
//                         .lineToSplineHeading(new Pose2d(48,-12,Math.toRadians(90)))
                         .splineToConstantHeading(new Vector2d(36,-28),Math.toRadians(90))
                         .splineToConstantHeading(new Vector2d(46,-10),Math.toRadians(0))
                         //push sample to obs zone
                         .waitSeconds(1)
                         .lineToConstantHeading(new Vector2d(46,-60))
                         //drive to 2nd sample
                         .waitSeconds(1)
                         .lineToConstantHeading(new Vector2d(46,-28))
                         .splineToConstantHeading(new Vector2d(56,-10),Math.toRadians(0))
                         //push sample to obs zone
                         .waitSeconds(1)
                         .lineToConstantHeading(new Vector2d(56,-60))
                         //drive to 3rd sample
                         .waitSeconds(1)
                         .lineToConstantHeading(new Vector2d(56,-28))
                         .splineToConstantHeading(new Vector2d(62,-10),Math.toRadians(0))
                         //push sample to obs zone
                         .waitSeconds(1)
                         .lineToConstantHeading(new Vector2d(62,-60))






                         .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}