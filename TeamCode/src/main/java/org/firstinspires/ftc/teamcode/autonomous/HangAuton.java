package org.firstinspires.ftc.teamcode.autonomous;

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
import org.firstinspires.ftc.teamcode.mechanisms.ClawElev;
import org.firstinspires.ftc.teamcode.mechanisms.Elevator;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.Flipper;
import org.firstinspires.ftc.teamcode.mechanisms.FloorLift;

@Autonomous(name="Left_Side_Hang_Only_Auton", group="Auto")
public class HangAuton extends LinearOpMode {

    // Hang coordinates
    public static double hangX = -36;
    public static double hangY = -11;
    public static double hangXCreep = 10;
    public static double hangH = Math.toRadians(180);

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
                .strafeToLinearHeading(new Vector2d(hangX, hangY), hangH)
                .strafeTo(new Vector2d(hangX + hangXCreep, hangY))
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
                extendo.load(),
                new ParallelAction(
                        goHang,
                        elevator.safeFlip(),
                        new SequentialAction(
                                new SleepAction(4),
                                flipper.flipOut(),
                                new SleepAction(1.25),
                                flipper.flipStop()
                        )
                ),
                new SleepAction(3)

        ));
    }
}
