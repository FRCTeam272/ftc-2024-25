package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.ClawElev;
import org.firstinspires.ftc.teamcode.mechanisms.ClawFloor;
import org.firstinspires.ftc.teamcode.mechanisms.Elevator;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.Flipper;
import org.firstinspires.ftc.teamcode.mechanisms.FloorLift;

@Disabled
@Autonomous(name="Basket_Side_Auton", group="Auto")
public class BasketAuton extends LinearOpMode {

    // Pose to approach basket head on
    public static double depositApproachX = -48;
    public static double depositApproachY = -48;

    // Pose to score in basket
    public static double depositSampleX = -58;
    public static double depositSampleY = -58;
    public static double depositSampleH = Math.toRadians(45);

    // Pose to score in basket
    public static double depositSample1X = -58;
    public static double depositSample1Y = -58;
    public static double depositSample1H = Math.toRadians(45);

    // Pose to score in basket
    public static double depositSample2X = -58;
    public static double depositSample2Y = -58;
    public static double depositSample2H = Math.toRadians(45);

    // Pose to pick up Sample 1
    public static double sample1X = -48.5;
    public static double sample1Y = -49;
    public static double sample1H = Math.toRadians(90);

    // Pose to pick up Sample 2
    public static double sample2X = -59;
    public static double sample2Y = -48;
    public static double sample2H = Math.toRadians(90);

    // Pose to reset localizer for Teleop
    public static double resetX = -50;
    public static double resetY = -50;
    public static double resetH = Math.toRadians(90);

    // Optional Pose to go park (park is approach, parkCreep amount to creep forward to park)
    public static double parkX = -48;
    public static double parkY = -12;
    public static double parkXCreep = 20;
    public static double parkH = Math.toRadians(180);

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize mechanisms
        Pose2d StartPose = new Pose2d(-36, -63.5, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, StartPose);
        ClawElev clawElev = new ClawElev(hardwareMap);
        ClawFloor clawFloor = new ClawFloor(hardwareMap);
        Elevator elevator = new Elevator(hardwareMap);
        Extendo extendo = new Extendo(hardwareMap);
        Flipper flipper = new Flipper(hardwareMap);
        FloorLift floorLift = new FloorLift(hardwareMap);

        // Build Trajectories
        Action approachBasket0 = drive.actionBuilder(StartPose)
                .strafeToLinearHeading(new Vector2d(depositApproachX, depositApproachY), depositSampleH)
                .build();

        Action depositSample0 = drive.actionBuilder(new Pose2d(depositApproachX, depositApproachY, depositSampleH))
                .strafeTo(new Vector2d(depositSampleX, depositSampleY))
                .build();

        Action driveSample1 = drive.actionBuilder(new Pose2d(depositSampleX, depositSampleY, depositSampleH))
                .strafeToLinearHeading(new Vector2d(sample1X, sample1Y), sample1H)
                .build();

        Action approachBasket1 = drive.actionBuilder(new Pose2d(sample1X, sample1Y, sample1H))
                .strafeToLinearHeading(new Vector2d(depositApproachX, depositApproachY), depositSampleH)
                .build();

        Action depositSample1 = drive.actionBuilder(new Pose2d(depositApproachX, depositApproachY, depositSampleH))
                .strafeTo(new Vector2d(depositSample1X, depositSample1Y))
                .build();

        Action driveSample2 = drive.actionBuilder(new Pose2d(depositSample1X, depositSample1Y, depositSampleH))
                .strafeToLinearHeading(new Vector2d(sample2X, sample2Y), sample2H)
                .build();

        Action approachBasket2 = drive.actionBuilder(new Pose2d(sample2X, sample2Y, sample2H))
                .strafeToLinearHeading(new Vector2d(depositApproachX, depositApproachY), depositSampleH)
                .build();

        Action depositSample2 = drive.actionBuilder(new Pose2d(depositApproachX, depositApproachY, depositSampleH))
                .strafeTo(new Vector2d(depositSample2X, depositSample2Y))
                .build();

        Action resetPose = drive.actionBuilder(new Pose2d(depositSample2X, depositSample2Y, depositSampleH))
                .strafeToLinearHeading(new Vector2d(resetX, resetY), resetH)
                .build();

        Action park = drive.actionBuilder(new Pose2d(depositSampleX, depositSampleY, depositSampleH))
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

                // Drive to Basket to score Preload
                clawElev.closeClaw(),
                extendo.stow(),
                new ParallelAction(
                        approachBasket0,
                        elevator.scoreHigh()
                ),

                // Approach basket and flip out to rest on basket
                new ParallelAction(
                        depositSample0,
                        new SequentialAction(
                                flipper.flipOut(),
                                new SleepAction(.5),
                                flipper.flipStop()
                        )
                ),

                // Open claw and flip back inward
                new SequentialAction(
                        clawElev.openClaw(),
                        new SleepAction(.25),
                        new ParallelAction(
                                flipper.flipIn(),
                                new SleepAction(.5),
                                flipper.flipStop()
                        ),

                        // Drive to Sample 1, while lowering Elev
                        new ParallelAction(
                                driveSample1,
                                extendo.load(),
                                elevator.safeFlip(),
                                new SequentialAction(
                                        floorLift.flipOut(),
                                        new SleepAction(.5),
                                        floorLift.flipStop()
                                )
                        )
                ),

                // Close intake claw and flip intake back in, load elevator claw
                new SequentialAction(
                        clawFloor.closeClaw(),
                        new SleepAction(.25),
                        floorLift.flipIn(),
                        new SleepAction(.5),
                        floorLift.flipStop(),
                        new SleepAction(.25),
                        clawFloor.openClaw(),
                        elevator.load(),
                        new SleepAction(.25),
                        clawElev.closeClaw()
                )
//
//                // Drive Sample 1 to basket approach while raising elevator
//                new ParallelAction(
//                        approachBasket1,
//                        elevator.scoreHigh()
//                ),
//                // Approach basket and flip out to rest on basket
//                new ParallelAction(
//                        depositSample1,
//                        new SequentialAction(
//                                flipper.flipOut(),
//                                new SleepAction(.5),
//                                flipper.flipStop()
//                        )
//                )

        ));
    }
}
