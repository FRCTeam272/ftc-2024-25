package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.mechanisms.ClawElev;
import org.firstinspires.ftc.teamcode.mechanisms.ClawFloor;
import org.firstinspires.ftc.teamcode.mechanisms.Elevator;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;
import org.firstinspires.ftc.teamcode.mechanisms.Flipper;
import org.firstinspires.ftc.teamcode.mechanisms.FloorLift;

@TeleOp (name= "FTC_14133_2024", group="Iterative Opmode") //labels program in Driver station selection
public class Teleop14133 extends LinearOpMode {

    private Elevator elevator=null;
    private Extendo extendo=null;
    private FloorLift floorLift=null;
    private ClawElev clawElev =null;
    private ClawFloor clawFloor=null;
    private Flipper flipper=null;

    @Override
    public void runOpMode() {

        elevator = new Elevator(hardwareMap);
        extendo = new Extendo(hardwareMap);
        floorLift = new FloorLift(hardwareMap);
        clawElev = new ClawElev(hardwareMap);
        clawFloor = new ClawFloor(hardwareMap);
        flipper = new Flipper(hardwareMap);

        DcMotor FL = hardwareMap.get(DcMotor.class, "leftFront");
        DcMotor FR = hardwareMap.get(DcMotor.class, "rightFront");
        DcMotor BL = hardwareMap.get(DcMotor.class, "leftBack");
        DcMotor BR = hardwareMap.get(DcMotor.class, "rightBack");

        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        BR.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.REVERSE);

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        imu.resetYaw();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Looping");

            elevator.Teleop(gamepad2, gamepad1, flipper, telemetry);
            flipper.updateFlipperAngle(elevator, telemetry);

            extendo.Teleop(gamepad2,floorLift, telemetry);

            floorLift.Teleop(gamepad2,telemetry);

            clawElev.Teleop(gamepad2,telemetry);
            clawFloor.Teleop(gamepad2,telemetry);

            double y = -gamepad1.left_stick_y/2; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x/2;
            double rx = gamepad1.right_stick_x/1.5;

            double fastpower = 3;

            if(gamepad1.left_bumper)  {
                fastpower = 3;
            }
            else {
                fastpower = 1;
            }


            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            if (gamepad1.x) {
                imu.resetYaw();
            }

            telemetry.addLine("Press X to reset orientation");

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            FL.setPower(frontLeftPower/fastpower);
            BL.setPower(backLeftPower/fastpower);
            FR.setPower(frontRightPower/fastpower);
            BR.setPower(backRightPower/fastpower);


        }

    }


}
