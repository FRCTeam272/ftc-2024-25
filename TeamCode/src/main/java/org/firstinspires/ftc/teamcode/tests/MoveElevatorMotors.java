package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp
public class MoveElevatorMotors extends OpMode {
    static DcMotorEx rightSlide;
    static DcMotorEx leftSlide;


    //TouchSensor slideLimit;


    public void init() {


        //initialize touch sensor and extendo motor


        rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");
        rightSlide.setDirection(DcMotorSimple.Direction.REVERSE);


        leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
        leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);


        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }


    public void loop() {


        //Write extendo position to screen, with instructions for use
        telemetry.addData("Status", "Looping");
        telemetry.addData("To Move Right Slide Motor", "Use Gamepad 2 Right Stick");
        telemetry.addData("To Move Left Slide Motot", "Use Gamepad 2 Left Stick");
        telemetry.addData("right slide position", rightSlide.getCurrentPosition());
        telemetry.addData("left slide position", leftSlide.getCurrentPosition());
        telemetry.update();


        //Use A button to retract until limit switch is pressed, then reset encoder
//        if (gamepad2.a){
//            rightSlide.setPower(-1);
//            while (!slideLimit.isPressed()){
//
//
//            }
//            rightSlide.setPower(0);
//            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }




        //use GP2 right stick to move right motor
        rightSlide.setPower(gamepad2.right_stick_y);
        //use GP2 left stick to move left motor
        leftSlide.setPower(gamepad2.left_stick_y);
    }
    public void stop(){


    }

}
