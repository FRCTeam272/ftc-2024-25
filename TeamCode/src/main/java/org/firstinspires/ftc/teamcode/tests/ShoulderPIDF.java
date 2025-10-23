package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
@Disabled
@Config
@TeleOp
public class ShoulderPIDF extends OpMode {

    private PIDController controller;

    public static double p = 0.001, i = 0, d = 0.0001;
    public static double f = 0.07;

    public static int target = 0; // 0 to 6000

    private final double ticks_in_degrees = 8192 / 360.0; //was 700 / 180.0

    private DcMotorEx flipEncoder;

    private CRServo leftFlipperS;
    private CRServo rightFlipperS;

    @Override
    public void init() {
        controller  = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        flipEncoder = hardwareMap.get(DcMotorEx.class, "rightBack");

        leftFlipperS = hardwareMap.get(CRServo.class, "leftFlipperS");
        rightFlipperS = hardwareMap.get(CRServo.class, "rightFlipperS");

        leftFlipperS.setDirection(CRServo.Direction.REVERSE);
        //rightFlipperS.setDirection(CRServo.Direction.REVERSE);

        flipEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flipEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        controller.setPID(p, i, d);
        int armPos = flipEncoder.getCurrentPosition();
        double pid = controller.calculate(armPos, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;

        double power = pid + ff;

        rightFlipperS.setPower(power);
        leftFlipperS.setPower(power);

        telemetry.addData("pos ", armPos);
        telemetry.addData("target ", target);
        telemetry.addData("power", power);
        telemetry.addData("press Enter", "to change value in Dashboard");
        telemetry.update();





    }
}
