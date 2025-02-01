package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
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
@Autonomous (name="Basket_Side_Auton", group="Auto")
public class BasketAutonExperiment extends LinearOpMode {

    // This enum defines our "state"
    // This is essentially just defines the steps our program will take
    enum State {
        DRIVEBASKET_0,  // First, drive to basket for preload
        SCORE_0,        // Then, score the first sample
        DRIVESAMPLE_1,  // Then drive to sample 1
        TRAJECTORY_3,   // Then, we follow another lineTo() trajectory
        WAIT_1,         // Then we're gonna wait a second
        TURN_2,         // Finally, we're gonna turn again
        IDLE            // Our bot will enter the IDLE state when done
    }

    // We define the current state we're on
    // Default to IDLE
    State currentState = State.IDLE;

    // Pose to approach basket head on
    public static double depositApproachX = -48;
    public static double depositApproachY = -48;

    // Pose to score in basket
    public static double depositSampleX = -58 ;
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

        Action driveSample1 = drive.actionBuilder(new Pose2d(depositSampleX,depositSampleY,depositSampleH))
                .strafeToLinearHeading(new Vector2d(sample1X, sample1Y),sample1H)
                .build();

        Action approachBasket1 = drive.actionBuilder(new Pose2d(sample1X, sample1Y, sample1H))
                .strafeToLinearHeading(new Vector2d(depositApproachX, depositApproachY), depositSampleH)
                .build();

        Action depositSample1 = drive.actionBuilder(new Pose2d(depositApproachX, depositApproachY, depositSampleH))
                .strafeTo(new Vector2d(depositSample1X, depositSample1Y))
                .build();

        Action driveSample2 = drive.actionBuilder(new Pose2d(depositSample1X,depositSample1Y,depositSampleH))
                .strafeToLinearHeading(new Vector2d(sample2X, sample2Y),sample2H)
                .build();

        Action approachBasket2 = drive.actionBuilder(new Pose2d(sample2X, sample2Y, sample2H))
                .strafeToLinearHeading(new Vector2d(depositApproachX, depositApproachY), depositSampleH)
                .build();

        Action depositSample2 = drive.actionBuilder(new Pose2d(depositApproachX, depositApproachY, depositSampleH))
                .strafeTo(new Vector2d(depositSample2X, depositSample2Y))
                .build();

        Action resetPose = drive.actionBuilder(new Pose2d(depositSample2X, depositSample2Y,depositSampleH))
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

        // set the current state to the first one and run movements & trajectories
        currentState = State.DRIVEBASKET_0;

        extendo.setTargetPosition(0);
        elevator.GoToPosition(2, telemetry);
        Actions.runBlocking(new SequentialAction(
                clawElev.closeClaw(),
                approachBasket0,
                depositSample0,
                clawElev.openClaw()

        ));

        while (opModeIsActive() && !isStopRequested()) {
            // Our state machine logic
            // You can have multiple switch statements running together for multiple state machines
            // in parallel. This is the basic idea for subsystems and commands.

            // We essentially define the flow of the state machine through this switch statement
            switch (currentState) {
                case DRIVEBASKET_0:
                    if (clawElev.IsClawOpen()) {
                        currentState = State.DRIVESAMPLE_1;
                        Actions.runBlocking(new SequentialAction(
                                driveSample1
                        ));
                        floorLift.GoToPosition(1,telemetry);
                        elevator.GoToPosition(-2, telemetry);
                    }
                    break;
            }

            flipper.updateFlipperAngle(elevator, telemetry);

        }
    }
}
