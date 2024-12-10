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
//    TouchSensor extendoLimit;

       public void init() {

           //initialize touch sensor and extendo motor
           //extendoLimit = hardwareMap.get(TouchSensor.class, "extendoLS");

           extendo = hardwareMap.get(DcMotorEx.class, "extendoM");
           extendo.setDirection(DcMotorSimple.Direction.REVERSE);

           extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
           extendo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

       public void loop() {

           //Write extendo position to screen, with instructions for use
           telemetry.addData("Status", "Looping");
           telemetry.addData("Power set to", "50% for testing purposes");
           telemetry.addData("To Move Extendo", "Use Gamepad 1 Right Stick");
          // telemetry.addData("To Fully Retract Extendo and reset", "Use Gamepad 2 A");
           telemetry.addData("To Reset Encoder", "Use Gamepad 1 X");
           telemetry.addData("extendo position", extendo.getCurrentPosition());
           telemetry.update();

           //Use A button to retract until limit switch is pressed, then reset encoder
//           if (gamepad1.a){
//               extendo.setPower(-0.5);
//               while (!extendoLimit.isPressed()){
//
//               }
//               extendo.setPower(0);
//               extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//               extendo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//           }

           // To reset encoder without a limit switch attached, gamepad2 x
           if (gamepad1.x){
               extendo.setPower(0);
               extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
               extendo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
           }

           //use GP2 right stick to move extendo
           extendo.setPower(gamepad1.right_stick_y/2); // run at half power, full power tends to launch things!
    }
    public void stop(){

    }
}
