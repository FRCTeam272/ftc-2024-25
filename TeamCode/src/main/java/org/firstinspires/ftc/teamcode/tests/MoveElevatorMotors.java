package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@Disabled
@TeleOp
public class MoveElevatorMotors extends OpMode {
    static DcMotor rightSlide;
    static DcMotor leftSlide;


    //TouchSensor slideLimit;


    public void init() {


        //initialize touch sensor and motors


        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");
        rightSlide.setDirection(DcMotor.Direction.REVERSE);


        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        //leftSlide.setDirection(DcMotor.Direction.REVERSE);


        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }


    public void loop() {


        //Write extendo position to screen, with instructions for use
        telemetry.addData("Status", "Looping");
        telemetry.addData("Power set to", "50% for testing purposes");
        telemetry.addData("To Move Right Slide Motor", "Use Gamepad 1 Right Stick");
        telemetry.addData("To Move Left Slide Motot", "Use Gamepad 1 Left Stick");
        telemetry.addData("To reset Encoders", "Use Gamepad 1 A");
        telemetry.addData("right slide position", rightSlide.getCurrentPosition());
        telemetry.addData("left slide position", leftSlide.getCurrentPosition());
        telemetry.update();


        //Use A button to retract until limit switch is pressed, then reset encoder
        if (gamepad1.a){
//            rightSlide.setPower(-1);
//            while (!slideLimit.isPressed()){
//
//
//            }
            rightSlide.setPower(0);
            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            leftSlide.setPower(0);
            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }



        //use GP1 right stick to move right motor
        rightSlide.setPower(gamepad1.right_stick_y/2); //half power
        //use GP1 left stick to move left motor
        leftSlide.setPower(gamepad1.left_stick_y/2); //half power
    }
    public void stop(){


    }

}
