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
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Claw;
import org.firstinspires.ftc.teamcode.mechanisms.Elevator;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;


@Config
@Autonomous
public class BasketAuton extends LinearOpMode {

    // Pose to approach basket head on
    public static double depositApproachX = -48;
    public static double depositApproachY = -48;

    // Pose to score in basket
    public static double depositSampleX = -54;
    public static double depositSampleY = -54;
    public static double depositSampleH = Math.toRadians(45);

    // Pose to pick up Sample 1
    public static double sample1X = -48;
    public static double sample1Y = -49;
    public static double sample1H = Math.toRadians(90);

    // Pose to pick up Sample 2
    public static double sample2X = -60;
    public static double sample2Y = -49;
    public static double sample2H = Math.toRadians(90);

    // Pose to go park (park is approach, parkCreep amount to creep forward to park)
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

        Action driveSample2 = drive.actionBuilder(new Pose2d(depositSampleX,depositSampleY,depositSampleH))
                .strafeToLinearHeading(new Vector2d(sample2X, sample2Y),sample2H)
                .build();

        Action approachBasket2 = drive.actionBuilder(new Pose2d(sample2X, sample2Y, sample2H))
                .strafeToLinearHeading(new Vector2d(depositApproachX, depositApproachY), depositSampleH)
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

                claw.closeClaw(),
                new ParallelAction(
                        approachBasket0,
                        elevator.scoreHigh(),
                        new SequentialAction(
                                claw.flipOut(),
                                new SleepAction(1.2),
                                claw.flipStop()
                        )
                ),


                depositSample,
                claw.openClaw(),

                new ParallelAction(
                        driveSample1,
                        elevator.load(),
                        new SequentialAction(
                                claw.flipIn(),
                                new SleepAction(1.2),
                                claw.flipStop()
                        )
                ),
                intake.lower(),

                new ParallelAction(
                        extendo.extend(), //while extendo slowly moves out
                        new SequentialAction( //intake until sample detected or 2 seconds
                                intake.floorIntake(),
                                new SleepAction(0.25),
                                intake.floorIntake(),
                                new SleepAction(0.25),
                                intake.floorIntake(),
                                new SleepAction(0.25),
                                intake.floorIntake(),
                                new SleepAction(0.25),
                                intake.floorIntake(),
                                new SleepAction(0.25),
                                intake.floorIntake(),
                                new SleepAction(0.25),
                                intake.floorIntake(),
                                new SleepAction(0.25),
                                intake.floorIntake(),
                                new SleepAction(0.25),
                                intake.floorIntake(),
                                intake.stopIntake()

                        )
                ),
                intake.raise(),
                extendo.stow(),



//                //move out of stow
//                claw.closeClaw(),
//                approachBasket0,
//                claw.flipStow(),


                new SleepAction(2),
//
//                //score Preload
//                new ParallelAction(
//                        depositSample,
//                        claw.basketRaise()
//                ),
//
//               new SleepAction(2),
//
//                //drive to first sample while lowering elevator to load
//               // new ParallelAction(
//                //        elevator.load(),
//                        driveSample1,
//               // ),
//
//                new SleepAction(2),
//
//                // lower intake and slowly push out extendo while intakinng
//                intake.lower(),
//                new ParallelAction(
//                        extendo.extend(),
//                        intake.floorIntake()
//                ),
                new SleepAction(2)

        ));
    }
}
