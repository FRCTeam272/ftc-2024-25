package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

@TeleOp
public class RunIntake extends OpMode {

    static DcMotorEx intakeFlop;

    static CRServo leftIntake;
    static CRServo rightIntake;

    DistanceSensor sensorDistance;

    public void init() {

        intakeFlop = hardwareMap.get(DcMotorEx.class, "intakeFlop");
        intakeFlop.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeFlop.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeFlop.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftIntake = hardwareMap.get(CRServo.class, "leftIntake");
        rightIntake = hardwareMap.get(CRServo.class, "rightIntake");

        leftIntake.setDirection(CRServo.Direction.REVERSE);
        rightIntake.setDirection(CRServo.Direction.FORWARD);

        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensorColor");

    }
    public void loop() {

        //Write flopMotor position to screen, with instructions for use
        telemetry.addData("Status", "Looping");
        telemetry.addData("Power set to", "1/3rd for testing purposes");
        telemetry.addData("To Move FlopMotor", "Use Gamepad 1 Right Stick");
        telemetry.addData("move intake inward", "Use Gamepad  A");
        telemetry.addData("move intake outward", "Use Gamepad  Y");
        telemetry.addData("To Reset FlopMotor Encoder", "Use Gamepad 1 X");
        telemetry.addData("flop position", intakeFlop.getCurrentPosition());
        telemetry.addData("Distance (cm)", String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
        telemetry.update();

        // To reset encoder without a limit switch attached, gamepad1 x
        if (gamepad1.x) {
            intakeFlop.setPower(0);
            intakeFlop.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            intakeFlop.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        //use GP1 right stick to move Flop
        intakeFlop.setPower(-gamepad1.right_stick_y / 3);

        if (gamepad1.a) {
            leftIntake.setPower(1);
            rightIntake.setPower(1);
        }

        else if (gamepad1.y) {
            leftIntake.setPower(-1);
            rightIntake.setPower(-1);
        }

        else if (!gamepad1.a && !gamepad1.y) {
            leftIntake.setPower(0);
            rightIntake.setPower(0);
        }
    }

    public void stop(){

    }

}