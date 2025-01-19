package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;


@TeleOp
public class MoveElevTogether extends OpMode {
    static DcMotor rightSlide;
    static DcMotor leftSlide;


    TouchSensor slideLimit;


    public void init() {


        //initialize touch sensor and extendo motor


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

        slideLimit = hardwareMap.get(TouchSensor.class, "slideLS");


    }


    public void loop() {


        //Write motor position to screen, with instructions for use
        telemetry.addData("Status", "Looping");
        telemetry.addData("Power set to", "50% for testing purposes");
        telemetry.addData("To Move Elevator", "Use Gamepad 1 Left Stick");
        telemetry.addData("To reset Encoders", "Use Gamepad 1 A");
        telemetry.addData("right slide position", rightSlide.getCurrentPosition());
        telemetry.addData("left slide position", leftSlide.getCurrentPosition());
        telemetry.addData("Elev Limit Switch", slideLimit.isPressed()); //print whether or not it is pressed
        telemetry.update();


        //Use A button to retract until limit switch is pressed, then reset encoder
        if (gamepad1.a){

            while (!slideLimit.isPressed()){
                rightSlide.setPower(-0.5);
                leftSlide.setPower(-0.5);

            }
            rightSlide.setPower(0);
            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            leftSlide.setPower(0);
            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }


//        double selectedValue = -1;
//
//            if(gamepad1.a){
//                selectedValue = 0;
//            } else if(gamepad1.b){
//                selectedValue = 20;
//            } else if(gamepad1.x){
//                selectedValue = 50;
//            }
//
//            if(selectedValue != -1){
//                // internally these will log at nearly the same time like 1/60 of a second
//                leftSlide.setPower(selectedValue);
//                rightSlide.setPower(selectedValue);
//            }


        //use GP1 right stick to move right motor
        rightSlide.setPower(-gamepad1.left_stick_y); //half power
        leftSlide.setPower(-gamepad1.left_stick_y); //half power
    }
    public void stop(){


    }

}
