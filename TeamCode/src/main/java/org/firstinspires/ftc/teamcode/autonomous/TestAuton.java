package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.IntakeOld;

@Disabled
@Autonomous
public class TestAuton extends LinearOpMode {

    private Extendo extendo=null;
    private IntakeOld intake=null;

    @Override
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(0,0,Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        Extendo extendo = new Extendo(hardwareMap);
        IntakeOld intake = new IntakeOld(hardwareMap);

        //Build trajectories here
        TrajectoryActionBuilder toBasket = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(24,2))
                .waitSeconds(1);
        Action trajectoryActionCloseOut = toBasket.fresh()
                .strafeTo(new Vector2d(25,2))
                .build();

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
                      toBasket.build(),
                      trajectoryActionCloseOut
              )
        );

    }
}
