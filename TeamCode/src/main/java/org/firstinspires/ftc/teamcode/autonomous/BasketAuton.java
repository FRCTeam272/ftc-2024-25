package org.firstinspires.ftc.teamcode.autonomous;

import android.graphics.LinearGradient;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;

@Autonomous
public class BasketAuton extends LinearOpMode {

    private Extendo extendo=null;
    private Intake intake=null;

    @Override
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(-24,-63,Math.toRadians(90));
        /* Start centered on first tile line to the left of center.
        * Drives Left 24" and forward 12" while turning to face with elevator toward basket*/

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        Extendo extendo = new Extendo(hardwareMap);
        Intake intake = new Intake(hardwareMap);

        //Build trajectories here
        TrajectoryActionBuilder toBasket0 = drive.actionBuilder(initialPose)
                .strafeToSplineHeading(new Vector2d(-48,-48), Math.toRadians(45))
                .strafeTo(new Vector2d(-50,-50))
                .waitSeconds(1);
        Action trajectoryActionCloseOut = toBasket0.fresh()
                .build();

        TrajectoryActionBuilder toSample1 = drive.actionBuilder(new Pose2d(-50,-50,Math.toRadians(45)))
                .splineTo(new Vector2d(-48,-35), Math.toRadians(90))
                .waitSeconds(1);

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Position during Init", initialPose);
            telemetry.update();
        }

        telemetry.addData("Starting Position", initialPose);
        telemetry.update();
        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        toBasket0.build(),
                        toSample1.build(),
                        trajectoryActionCloseOut
                )
        );

    }
}