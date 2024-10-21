package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTestingAutonBasket {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)

                // Basic Square
                 // Set bot starting location
                 .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-24, -63, Math.toRadians(180)))

                         //Paste Trajectory here
                         //Basket Side
                        //Drive to basket
                         .splineToLinearHeading(new Pose2d(-32,-40, Math.toRadians(180)),Math.toRadians(180))
                         .splineToLinearHeading(new Pose2d(-56,-56, Math.toRadians(225)), Math.toRadians(225))
                         //score in basket
                         .waitSeconds(1)
                         //drive to 1st sample
                         .lineToSplineHeading(new Pose2d(-48,-35,Math.toRadians(90)))
                         //pickup sample
                         .waitSeconds(1)
                         //drive to basket
                         .lineToSplineHeading(new Pose2d(-56,-56,Math.toRadians(225)))
                         // drive to 2nd sample
                         .lineToSplineHeading(new Pose2d(-58,-35,Math.toRadians(90)))
                         //pickup sample
                         .waitSeconds(1)
                         //drive to basket
                         .lineToSplineHeading(new Pose2d(-56,-56,Math.toRadians(225)))
                         //drive to 3rd sample
                         .lineToSplineHeading(new Pose2d(-58,-25,Math.toRadians(180)))
                         //pickup sample
                         .waitSeconds(1)
                         //drive to basket
                         .lineToSplineHeading(new Pose2d(-56,-56,Math.toRadians(225)))
                         //drive to submersible
                         .waitSeconds(1)
                         .lineTo(new Vector2d(-52,-48))




                         .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}