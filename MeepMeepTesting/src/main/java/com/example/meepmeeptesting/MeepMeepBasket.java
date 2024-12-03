package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBasket {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        //Set Bot Starting Location
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-33, -63, Math.toRadians(90)))
                //Paste Trajectory here
                //Basket Side
                //Drive to basket
                .strafeToSplineHeading(new Vector2d(-48,-48), Math.toRadians(45))
                .strafeTo(new Vector2d(-56,-56))
                //score in basket
                .waitSeconds(1)

                //drive to 1st sample
                .splineTo(new Vector2d(-48,-35), Math.PI/2)
                 //pickup sample
                .waitSeconds(1)

                //drive backward to basket
                .setReversed(true) //to drive backward
                .splineToSplineHeading(new Pose2d(-56,-56, Math.toRadians(45)),Math.toRadians(225))

                // drive forward to 2nd sample
                .setReversed(false) //to drive forward
                .splineTo(new Vector2d(-58,-35), Math.PI/2)
                //pickup sample
                .waitSeconds(1)

                //drive to basket
                .setReversed(true) //to drive backward
                .splineToSplineHeading(new Pose2d(-56,-56, Math.toRadians(45)),Math.toRadians(225))

                 //drive to 3rd sample
                /* .lineToSplineHeading(new Pose2d(-58,-25,Math.toRadians(180)))
                 //pickup sample
                 .waitSeconds(1)

                 //drive to basket
                 .lineToSplineHeading(new Pose2d(-56,-56,Math.toRadians(225)))

                 //drive to submersible
                 .waitSeconds(1)
                 .lineTo(new Vector2d(-52,-48)) */

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}

