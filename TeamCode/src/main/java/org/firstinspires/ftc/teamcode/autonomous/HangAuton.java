package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
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
import org.firstinspires.ftc.teamcode.mechanisms.Elevator;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.Flipper;
import org.firstinspires.ftc.teamcode.mechanisms.FloorLift;

@Disabled
@Autonomous(name="Left_Side_Hang_Only_Auton", group="Auto")
public class HangAuton extends LinearOpMode {

    // Coordinates to move to clear Sub fins
    public static double move1X = -36;
    public static double move1Y = -12;

    // Hang coordinates
    public static double hangX = -28;
    public static double hangY = -12;

    @Override
    public void runOpMode() throws InterruptedException {

        // Initializing robot
        Pose2d StartPose = new Pose2d(-36, -63.5, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, StartPose);
        ClawElev clawElev = new ClawElev(hardwareMap);
        Extendo extendo = new Extendo(hardwareMap);
        Elevator elevator = new Elevator(hardwareMap);
        Flipper flipper = new Flipper(hardwareMap);


        // Build trajectory
        Action goHang = drive.actionBuilder(StartPose)
                .strafeTo(new Vector2d(move1X, move1Y)) // move to clear Sub fins
                .strafeTo(new Vector2d(hangX, hangY)) // move to Sub for Hang
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
                clawElev.closeClaw(),
                extendo.stow(),
                goHang,
                elevator.scoreLow(),
                new SequentialAction(
                        flipper.flipOut(),
                        new SleepAction(2),
                        flipper.flipStop()
                )

        ));
    }
}
