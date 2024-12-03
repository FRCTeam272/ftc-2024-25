package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp
public class MoveExtendo extends OpMode {

    static DcMotorEx extendo;
    TouchSensor extendoLimit;

       public void init() {

           //initialize touch sensor and extendo motor
           extendoLimit = hardwareMap.get(TouchSensor.class, "extendoLS");

           extendo = hardwareMap.get(DcMotorEx.class, "extendoM");
           extendo.setDirection(DcMotorSimple.Direction.REVERSE);

           extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
           extendo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

       public void loop() {

           //Write extendo position to screen, with instructions for use
           telemetry.addData("Status", "Looping");
           telemetry.addData("To Move Extendo", "Use Gamepad 2 Right Stick");
           telemetry.addData("To Fully Retract Extendo", "Use Gamepad 2 A");
           telemetry.addData("extendo position", extendo.getCurrentPosition());
           telemetry.update();

           //Use A button to retract until limit switch is pressed, then reset encoder
           if (gamepad2.a){
               extendo.setPower(-1);
               while (!extendoLimit.isPressed()){

               }
               extendo.setPower(0);
               extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
               extendo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
           }

           //use GP2 right stick to move extendo
           extendo.setPower(gamepad2.right_stick_y);
    }
    public void stop(){

    }
}
