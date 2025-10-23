package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.ClawElev;
import org.firstinspires.ftc.teamcode.mechanisms.ClawFloor;
import org.firstinspires.ftc.teamcode.mechanisms.Drive;
import org.firstinspires.ftc.teamcode.mechanisms.Elevator;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.Flipper;
import org.firstinspires.ftc.teamcode.mechanisms.FloorLift;

@Disabled
@TeleOp(name= "FTC_14133_2024", group="Iterative Opmode") //labels program in Driver station selection

public class Teleop14133Old extends OpMode {

    //private MecanumDrive drive=null;
    private Drive drive;

    private Elevator elevator=null;
    private Extendo extendo=null;
//    private Intake intake=null;
    private FloorLift floorLift=null;
    private ClawElev clawElev =null;
    private ClawFloor clawFloor=null;
    private Flipper flipper=null;

    //@Override
    public void init() {

        //drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
        drive = new Drive (hardwareMap);

        elevator = new Elevator(hardwareMap);
        extendo = new Extendo(hardwareMap);
//        intake = new Intake(hardwareMap);
        floorLift = new FloorLift(hardwareMap);
        clawElev = new ClawElev(hardwareMap);
        clawFloor = new ClawFloor(hardwareMap);
        flipper = new Flipper(hardwareMap);
    }

    //@Override
    public void init_loop() {
        telemetry.addData("Status", "Init Loop");
        telemetry.update();
    }

    @Override
    public void loop() {

        telemetry.addData("Status", "Looping");

        elevator.Teleop(gamepad2, gamepad1, flipper, telemetry);
        flipper.updateFlipperAngle(elevator, telemetry);

        extendo.Teleop(gamepad2,floorLift, telemetry);

//        intake.objcatcher.Teleop(gamepad2, telemetry);
//        intake.objlift.Teleop(gamepad2,gamepad1, telemetry);

        floorLift.Teleop(gamepad2,telemetry);

        clawElev.Teleop(gamepad2,telemetry);
        clawFloor.Teleop(gamepad2,telemetry);




        // Update Pose estimate
        //drive.updatePoseEstimate();



        //double maxDriveSpeed = 1.0;
        //double maxRotateSpeed = 0.75;

        //double maxDriveSpeed = 1.0 - map(clamp(elevator.getCurrentHeight() / 10000.0, 0.0, 1.0), 0.0, 1.0, 0.0, 0.7);
        //double maxRotateSpeed = 0.75 - map(clamp(elevator.getCurrentHeight() / 10000.0, 0.0, 1.0), 0.0, 1.0, 0.0, 0.4);
        //drive.driveWithController(gamepad1, maxDriveSpeed, maxRotateSpeed);

        //drive.driveFieldCentric(gamepad1);
        drive.Teleop(gamepad1,telemetry);

        telemetry.update();

    }
}
