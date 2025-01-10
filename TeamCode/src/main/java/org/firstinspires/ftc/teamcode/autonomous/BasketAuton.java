package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Claw;
import org.firstinspires.ftc.teamcode.mechanisms.Elevator;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;


@Config
@Autonomous (name="Basket_Side_Auton", group="Auto")
public class BasketAuton extends LinearOpMode {

    // Pose to approach basket head on
    public static double depositApproachX = -48;
    public static double depositApproachY = -48;

    // Pose to score in basket
    public static double depositSampleX = -58;
    public static double depositSampleY = -58;
    public static double depositSampleH = Math.toRadians(45);

    // Pose to pick up Sample 1
    public static double sample1X = -48;
    public static double sample1Y = -49;
    public static double sample1H = Math.toRadians(90);

    // Pose to pick up Sample 2
    public static double sample2X = -58;
    public static double sample2Y = -49;
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
    public void runOpMode() {

        Pose2d StartPose = new Pose2d(-36, -63.5, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, StartPose);
        Claw claw = new Claw(hardwareMap);
        Elevator elevator = new Elevator(hardwareMap);
        Extendo extendo = new Extendo(hardwareMap);
        Intake intake = new Intake(hardwareMap);

        Action approachBasket0 = drive.actionBuilder(StartPose)
                .strafeToLinearHeading(new Vector2d(depositApproachX, depositApproachY), depositSampleH)
                .build();

        Action depositSample = drive.actionBuilder(new Pose2d(depositApproachX, depositApproachY, depositSampleH))
                .strafeTo(new Vector2d(depositSampleX, depositSampleY))
                .build();

        Action driveSample1 = drive.actionBuilder(new Pose2d(depositSampleX,depositSampleY,depositSampleH))
                .strafeToLinearHeading(new Vector2d(sample1X, sample1Y),sample1H)
                .build();

        Action approachBasket1 = drive.actionBuilder(new Pose2d(sample1X, sample1Y, sample1H))
                .strafeToLinearHeading(new Vector2d(depositApproachX, depositApproachY), depositSampleH)
                .build();

        Action depositSample1 = drive.actionBuilder(new Pose2d(depositApproachX, depositApproachY, depositSampleH))
                .strafeTo(new Vector2d(depositSampleX, depositSampleY))
                .build();

        Action driveSample2 = drive.actionBuilder(new Pose2d(depositSampleX,depositSampleY,depositSampleH))
                .strafeToLinearHeading(new Vector2d(sample2X, sample2Y),sample2H)
                .build();

        Action approachBasket2 = drive.actionBuilder(new Pose2d(sample2X, sample2Y, sample2H))
                .strafeToLinearHeading(new Vector2d(depositApproachX, depositApproachY), depositSampleH)
                .build();

        Action depositSample2 = drive.actionBuilder(new Pose2d(depositApproachX, depositApproachY, depositSampleH))
                .strafeTo(new Vector2d(depositSampleX, depositSampleY))
                .build();

        Action resetPose = drive.actionBuilder(new Pose2d(depositSampleX, depositSampleY,depositSampleH))
                .strafeToLinearHeading(new Vector2d(resetX,resetY),resetH)
                .build();

        Action park = drive.actionBuilder(new Pose2d(depositSampleX,depositSampleY,depositSampleH))
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

                // Score Preload
                claw.closeClaw(),
                new ParallelAction(
                        approachBasket0,
                        elevator.scoreLow(),
                        new SequentialAction(
                                claw.flipOut(),
                                new SleepAction(2),
                                claw.flipStop()
                        )
                ),
                depositSample,
                claw.openClaw(),

                // Drive to Sample 1, while lowering elev and flipping claw in
                new ParallelAction(
                        driveSample1,
                        elevator.load(),
                        new SequentialAction(
                                claw.flipIn(),
                                new SleepAction(2),
                                claw.flipStop()
                        )
                ),

                // Intake Sample 1, lower intake, intake while extending slowly
                intake.lower(),

                new ParallelAction(
                        extendo.extend(),
                        new SequentialAction(
                                intake.floorIntake(),
                                new SleepAction(0.35),
                                intake.floorIntake(),
                                new SleepAction(0.35),
                                intake.floorIntake(),
                                new SleepAction(0.35),
                                intake.floorIntake(),
                                new SleepAction(0.35),
                                intake.floorIntake(),
                                new SleepAction(0.35),
                                intake.floorIntake(),
                                intake.stopIntake()

                        )
                ),
                intake.raise(),

                // Drive Sample 1 to Basket Approach,
                // while loading claw from intake, raise Elev, and flip out
                new ParallelAction(
                        approachBasket1,
                        new SequentialAction(
                                intake.loadIntake(),
                                extendo.stow(),
                                new SleepAction(3),
                                intake.stopIntake(),
                                claw.closeClaw(),
                                new SleepAction(0.5),
                                elevator.scoreLow(),
                                new SleepAction(0.5),
                                claw.flipOut(),
                                new SleepAction(2),
                                claw.flipStop()
                        )
                ),

                //Score Sample 1
                new SequentialAction(
                        depositSample1,
                        claw.openClaw()
                ),

                // Drive to Sample 2, while lowering elev and flipping claw in
                new ParallelAction(
                        driveSample2,
                        elevator.load(),
                        new SequentialAction(
                                claw.flipIn(),
                                new SleepAction(2),
                                claw.flipStop()
                        )
                ),

        // Intake Sample 2, lower intake, intake while extending slowly
        intake.lower(),

                new ParallelAction(
                        extendo.extend(), //while extendo slowly moves out
                        new SequentialAction( //intake until sample detected or 2 seconds
                                intake.floorIntake(),
                                new SleepAction(0.35),
                                intake.floorIntake(),
                                new SleepAction(0.35),
                                intake.floorIntake(),
                                new SleepAction(0.35),
                                intake.floorIntake(),
                                new SleepAction(0.35),
                                intake.floorIntake(),
                                new SleepAction(0.35),
                                intake.floorIntake(),
                                intake.stopIntake()

                        )
                ),
                intake.raise(),

                // Drive Sample 2 to Basket Approach,
                // while loading claw from intake, raise Elev, and flip out
                new ParallelAction(
                        approachBasket2,
                        new SequentialAction(
                                intake.loadIntake(),
                                extendo.stow(),
                                new SleepAction(3),
                                intake.stopIntake(),
                                claw.closeClaw(),
                                new SleepAction(0.5),
                                elevator.scoreLow(),
                                new SleepAction(0.5),
                                claw.flipOut(),
                                new SleepAction(2),
                                claw.flipStop()
                        )
                ),

                //Score Sample 2
                new SequentialAction(
                        depositSample2,
                        claw.openClaw()
                ),

                // move back and turn slightly to have intake forward for teleop
                resetPose

        ));


    }
}
