package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
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
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;

@Disabled
@Config
@Autonomous (name="Right_Side_Park_Only_Auton", group="Auto")
public class ParkAuton extends LinearOpMode {

    // Coordinates to move away from wall slightly
    public static double move1X = 12;
    public static double move1Y = -60;

    // Park coordinates
    public static double parkX = 54;
    public static double parkY = -60;

    @Override
    public void runOpMode() {

        // Initializing robot
        Pose2d StartPose = new Pose2d(12, -63.5, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, StartPose);
        ClawElev clawElev = new ClawElev(hardwareMap);
        Extendo extendo = new Extendo(hardwareMap);

        // Build trajectory
        Action goPark = drive.actionBuilder(StartPose)
                .strafeTo(new Vector2d(move1X, move1Y)) // move off wall
                .strafeTo(new Vector2d(parkX, parkY)) // move sideways into Obs Zone
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
                goPark
        ));

    }
}
