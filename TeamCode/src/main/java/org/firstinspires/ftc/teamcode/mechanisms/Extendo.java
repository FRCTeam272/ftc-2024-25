package org.firstinspires.ftc.teamcode.mechanisms;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Extendo {
    DcMotorEx extendoM;

    int maxPosition;
    int targetPosition;
    int loadPosition;

    public Extendo(HardwareMap hardwareMap) {
        extendoM = hardwareMap.get(DcMotorEx.class, "extendoM");
        extendoM.setDirection(DcMotorSimple.Direction.REVERSE);
        extendoM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extendoM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        maxPosition = 315; // max encoder postion - get from test file
        targetPosition = 0;
        loadPosition = 185;

        //set a target position before putting motor in RUN TO POSITION mode
        extendoM.setTargetPosition(0);
        extendoM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    // Stow Extendo for Auton
    public class Stow implements Action { //open claw for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            extendoM.setPower(1);
            extendoM.setTargetPosition(0);
            return false;
        }
    }

    public Action stow() {
        return new Stow();
    }

    // Slowly push out Extendo for Auton
    public class Extend implements Action { //open claw for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            extendoM.setPower(0.10);
            extendoM.setTargetPosition(300);
            return false;
        }
    }

    public Action extend() {
        return new Extend();
    }

    public void Teleop(Gamepad gamepad2, FloorLift floorLift, Telemetry telemetry) {

        if (floorLift.getLiftPos() == 2) { //don't extend at start
            extendoM.setTargetPosition(0);
        }

        else if (floorLift.getLiftPos() <= 0){ // if loading or upright, go to safe flip position
            extendoM.setTargetPosition(loadPosition);
        }

        else if (floorLift.getLiftPos() ==1) { // if down on floor, put extendo to driver control

            // Increase or decrease the target position depending on joystick reading. Change the multiplier to speed up or slow down
            // Remember, Joystick forward is negative!!!!!!
            targetPosition = targetPosition + (int) (17.0 * -gamepad2.right_stick_y); //was 10 during last practice

            // Constrain the target position to the range of the mechanism.
            if (targetPosition < 0) {
                targetPosition = 0;
            } else if (targetPosition > maxPosition) {
                targetPosition = maxPosition;
            }

            // Set motor target to the new value of the targetPosition variable
            extendoM.setTargetPosition(targetPosition);
        }
            // Set motor power to .5 to seek (and hold) the new target position
            extendoM.setPower(0.5);

    }
}




