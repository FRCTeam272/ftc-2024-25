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

    @Override
    public void init() {

        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        extendo = hardwareMap.get(DcMotorEx.class, "extendoM");
        intakeFlop = hardwareMap.get(DcMotorEx.class, "intakeFlop");

        telemetry.addData("Status", "Init");
        telemetry.addData("Use to reset motor encoders", "during driver practice");
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

        telemetry.addData("Status", "Looping");
        telemetry.addData("All motor encoders", "RESET");
        telemetry.addData("rightSlide Position", rightSlide.getCurrentPosition());
        telemetry.addData("leftSlide Position", leftSlide.getCurrentPosition());
        telemetry.addData("extendo Position", extendo.getCurrentPosition());
        telemetry.addData("intakeLift Position", intakeFlop.getCurrentPosition());

    }
}
