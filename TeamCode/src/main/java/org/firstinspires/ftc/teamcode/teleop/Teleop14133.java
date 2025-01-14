package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.MecanumDrive.clamp;
import static org.firstinspires.ftc.teamcode.MecanumDrive.map;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Claw;
import org.firstinspires.ftc.teamcode.mechanisms.Elevator;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;


@TeleOp(name= "FTC_14133_2024", group="Iterative Opmode") //labels program in Driver station selection

public class Teleop14133 extends OpMode {

    private MecanumDrive drive=null;

    private Elevator elevator=null;
    private Extendo extendo=null;
    private Intake intake=null;
    private Claw claw=null;

    //@Override
    public void init() {

        drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
        elevator = new Elevator(hardwareMap);
        extendo = new Extendo(hardwareMap);
        intake = new Intake(hardwareMap);
        claw = new Claw(hardwareMap);
    }

    //@Override
    public void init_loop() {
        telemetry.addData("Status", "Init Loop");
        telemetry.update();
    }

    @Override
    public void loop() {

        telemetry.addData("Status", "Looping");

        elevator.Teleop(gamepad2,gamepad1, telemetry);

        extendo.Teleop(gamepad2,telemetry);

        intake.objcatcher.Teleop(gamepad2, telemetry);
        intake.objlift.Teleop(gamepad2,gamepad1, telemetry);

        claw.objgrabber.Teleop(gamepad2,telemetry);
        claw.objflipper.Teleop(gamepad2,telemetry);

        // Update Pose estimate
        drive.updatePoseEstimate();



        //double maxDriveSpeed = 1.0;
        //double maxRotateSpeed = 0.75;

        double maxDriveSpeed = 1.0 - map(clamp(elevator.getCurrentHeight() / 10000.0, 0.0, 1.0), 0.0, 1.0, 0.0, 0.7);
        double maxRotateSpeed = 0.75 - map(clamp(elevator.getCurrentHeight() / 10000.0, 0.0, 1.0), 0.0, 1.0, 0.0, 0.4);
        //drive.driveWithController(gamepad1, maxDriveSpeed, maxRotateSpeed);

        drive.driveFieldCentric(gamepad1);

        telemetry.update();

    }
}
