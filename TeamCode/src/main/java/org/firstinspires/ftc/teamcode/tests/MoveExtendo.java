package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;

@Disabled
@TeleOp
public class MoveExtendo extends OpMode {

    static DcMotorEx extendo;
//    TouchSensor extendoLimit;

    int maxPosition;
    int targetPosition;

    public void init() {

        //initialize touch sensor and extendo motor
        //extendoLimit = hardwareMap.get(TouchSensor.class, "extendoLS");

        extendo = hardwareMap.get(DcMotorEx.class, "extendoM");
        //extendo.setDirection(DcMotorSimple.Direction.REVERSE);

        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        extendo.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));

        maxPosition = 315;
        targetPosition = 0;

        //set a target position before putting motor in RUN TO POSITION mode
        extendo.setTargetPosition(0);
        extendo.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void loop() {

        //Write extendo position to screen, with instructions for use
        telemetry.addData("Status", "Looping");
        telemetry.addData("Power set to", "1/3rd for testing purposes");
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
//           if (gamepad1.x){
//               extendo.setPower(0);
//               extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//               extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//           }

        //use GP2 right stick to move extendo
        //extendo.setPower(gamepad1.right_stick_y/3); // run at third power, full power tends to launch things!


       // Increase or decrease the target position depending on joystick reading. Change the multiplier to speed up or slow down
        targetPosition = targetPosition + (int)(4.0 * gamepad1.right_stick_y);

        // Constrain the target position to the range of the mechanism
        if (targetPosition < 0) {
            targetPosition = 0;
        } else if (targetPosition > maxPosition){
            targetPosition = maxPosition;
        }

        // Set motor target to the new value of the targetPosition variable
        extendo.setTargetPosition(targetPosition);

        // Set motor power to 1 to seek (and hold) the new target position
        extendo.setPower(0.5);


    }
    public void stop(){

    }
}
