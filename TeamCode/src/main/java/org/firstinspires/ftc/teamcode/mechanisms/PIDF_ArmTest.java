package org.firstinspires.ftc.teamcode.mechanisms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

// from YouTube Video "PIDF Loops & Arm Control" by FTC 16739 KookyBotz.
// refer to video for how to tune lift joint of the arm using this file

@Config //so that it can be modified in FTC Dashboard
@TeleOp
public class PIDF_ArmTest extends OpMode {
    private PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static int target = 0;

    private final double ticks_in_degree = 700 / 180.0;  //set depending on motor used

    private DcMotorEx arm_motor;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        arm_motor = hardwareMap.get(DcMotorEx.class, "arm_motor");
    }

    @Override
    public void loop() {
        controller.setPID(p, i, d);
        int armPos = arm_motor.getCurrentPosition();
        double pid = controller.calculate(armPos, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f; //Refer to video desc for link to how to set up this math

        double power = pid+ ff;

        arm_motor.setPower(power);

        telemetry.addData("pos ", armPos);
        telemetry.addData("target ", target);

    }
}
