package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Claw;
import org.firstinspires.ftc.teamcode.mechanisms.Elevator;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;

public class TestAuton2 extends LinearOpMode {

    enum State {
        IDLE,
        PARK
    }

    State currentState = State.IDLE;

    int elevSlidePos = -1;

    private Claw claw=null;
    private Elevator elevator=null;
    private Extendo extendo=null;
    private Intake intake=null;

    @Override
    public void runOpMode() throws InterruptedException {
        //create drivetrain
        Pose2d initialPose = new Pose2d(0,0,Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        claw = new Claw(hardwareMap);
        elevator = new Elevator(hardwareMap);
        extendo = new Extendo(hardwareMap);
        intake = new Intake(hardwareMap);

        //Build Trajectories Here

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Position during Init", initialPose);
            telemetry.update();
        }

        telemetry.addData("Starting Position", initialPose);
        telemetry.update();
        waitForStart();

        if (isStopRequested()) return;

        //Tighten claw and flip carriage down
        claw.objgrabber.closeClaw();

        claw.objflipper.flipOutside();


    }

}
