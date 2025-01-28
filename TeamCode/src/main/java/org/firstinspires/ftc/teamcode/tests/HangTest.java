package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class HangTest extends OpMode {

    static DcMotor rightSlide;
    static DcMotor leftSlide;

    static  DcMotor extendoM;

    static DcMotorEx intakeLiftM;

    @Override
    public void init() {
        // Elevator Motors
        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
        rightSlide.setDirection(DcMotor.Direction.REVERSE);

        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        //leftSlide.setDirection(DcMotor.Direction.REVERSE);

        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Extendo Motor
        extendoM = hardwareMap.get(DcMotorEx.class, "extendoM");
        extendoM.setDirection(DcMotorSimple.Direction.REVERSE);
        extendoM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extendoM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Lift Motor
        intakeLiftM = hardwareMap.get(DcMotorEx.class, "intakeFlop");
        intakeLiftM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeLiftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    @Override
    public void loop() {

        //Hold Extendo in place
        extendoM.setTargetPosition(0);
        extendoM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }
}
