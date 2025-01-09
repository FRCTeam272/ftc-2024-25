package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.ClawTest;

@Config
@Autonomous
public class BasketAuto1 extends LinearOpMode {

    // Pose to approach basket head on
    public static double depositApproachX = -48;
    public static double depositApproachY = -48;

    // Pose to score in basket
    public static double depositSampleX = -56;
    public static double depositSampleY = -56;
    public static double depositSampleH = Math.toRadians(45);

    // Amount to drive forward while intaking
    public static double  sampleSweepY = 3;

    // Pose to pick up Sample 1
    public static double sample1X = -49;
    public static double sample1Y = -36;
    public static double sample1H = Math.toRadians(90);

    // Pose to pick up Sample 2
    public static double sample2X = -60;
    public static double sample2Y = -36;
    public static double sample2H = Math.toRadians(90);

    // Pose to go park (park is approach, parkCreep amount to creep forward to park)
    public static double parkX = -48;
    public static double parkY = -12;
    public static double parkXCreep = 20;
    public static double parkH = Math.toRadians(180);

    @Override
    public void runOpMode() {

        Pose2d StartPose = new Pose2d(-36, -63, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, StartPose);
        ClawTest claw = new ClawTest(hardwareMap);

        Action depositSample = drive.actionBuilder(StartPose)
                .strafeToLinearHeading(new Vector2d(depositApproachX, depositApproachY), depositSampleH)
                .strafeTo(new Vector2d(depositSampleX, depositSampleY))
                .build();

        Action firstSample = drive.actionBuilder(drive.pose)
                .strafeToLinearHeading(new Vector2d(sample1X, sample1Y),sample1H)
                .build();

        Action firstSampleSweep = drive.actionBuilder(drive.pose)
                .strafeTo(new Vector2d(sample1X, sample1Y+sampleSweepY))
                .build();

        Action secondSample = drive.actionBuilder(drive.pose)
                .strafeToLinearHeading(new Vector2d(sample2X, sample2Y),sample2H)
                .build();

        Action secondSampleSweep = drive.actionBuilder(drive.pose)
                .strafeTo(new Vector2d(sample2X, sample2Y+sampleSweepY))
                .build();

        Action park = drive.actionBuilder(drive.pose)
                .strafeToLinearHeading(new Vector2d(parkX, parkY), parkH)
                .strafeTo(new Vector2d(parkX + parkXCreep, parkY))
                .build();

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Position during Init", StartPose);
            telemetry.update();
        }

        telemetry.addData("Starting Position", StartPose);
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(new SequentialAction(

                //score Preload
                depositSample,
                claw.openClaw(),
                new SleepAction(1),

                //drive to first sample
                firstSample
        ));
    }
}
