package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.ClawOld;
import org.firstinspires.ftc.teamcode.mechanisms.Elevator;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.IntakeOld;

@Disabled
@Autonomous
public class TestAuton2 extends LinearOpMode {

//    enum State {
//        IDLE,
//        PARK
//    }

   // State currentState = State.IDLE;

    int elevSlidePos = -1;

    private ClawOld claw=null;
    private Elevator elevator=null;
    private Extendo extendo=null;
    private IntakeOld intake=null;

    @Override
    public void runOpMode() throws InterruptedException {
        //create drivetrain
        Pose2d initialPose = new Pose2d(0,0,Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        claw = new ClawOld(hardwareMap);
        elevator = new Elevator(hardwareMap);
        extendo = new Extendo(hardwareMap);
        intake = new IntakeOld(hardwareMap);

        //Build Trajectories Here

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Position during Init", initialPose);
            telemetry.update();
        }

        telemetry.addData("Starting Position", initialPose);
        telemetry.update();
        waitForStart();

        //if (isStopRequested()) return;

        Actions.runBlocking(
                //Tighten claw and flip carriage down
                claw.openClaw()

        );






    }

}
