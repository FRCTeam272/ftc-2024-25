package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class ResetMotors extends OpMode {

    static DcMotor rightSlide;
    static DcMotor leftSlide;

    static DcMotorEx extendo;

    static DcMotorEx intakeFlop;

    static DcMotorEx flipEncoder;

    @Override
    public void init() {

        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        extendo = hardwareMap.get(DcMotorEx.class, "extendoM");
        intakeFlop = hardwareMap.get(DcMotorEx.class, "intakeFlop");
        flipEncoder = hardwareMap.get(DcMotorEx.class, "rightBack");

        telemetry.addData("Status", "Init");
        telemetry.addData("Use to reset motor encoders", "before each match");
        telemetry.addData("Claw should be ", "outward facing");
    }

    @Override
    public void loop() {
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeFlop.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeFlop.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        flipEncoder.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        telemetry.addData("Status", "Looping");
        telemetry.addData("All motor encoders", "RESET");
        telemetry.addData("rightSlide Position", rightSlide.getCurrentPosition());
        telemetry.addData("leftSlide Position", leftSlide.getCurrentPosition());
        telemetry.addData("extendo Position", extendo.getCurrentPosition());
        telemetry.addData("intakeLift Position", intakeFlop.getCurrentPosition());
        telemetry.addData("flipEncoder position", flipEncoder.getCurrentPosition());

    }
}
