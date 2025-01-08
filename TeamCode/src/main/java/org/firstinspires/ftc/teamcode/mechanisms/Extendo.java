package org.firstinspires.ftc.teamcode.mechanisms;

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

    public Extendo(HardwareMap hardwareMap) {
        extendoM = hardwareMap.get(DcMotorEx.class, "extendoM");
        //extendoM.setDirection(DcMotorSimple.Direction.REVERSE);
        extendoM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extendoM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        maxPosition = 315; // max encoder postion - get from test file
        targetPosition = 0;

        //set a target position before putting motor in RUN TO POSITION mode
        extendoM.setTargetPosition(0);
        extendoM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void Teleop(Gamepad gamepad2, Telemetry telemetry) {

        // Increase or decrease the target position depending on joystick reading. Change the multiplier to speed up or slow down
        // Remember, Joystick forward is negative!!!!!!
        targetPosition = targetPosition + (int) (10.0 * -gamepad2.right_stick_y);

        // Constrain the target position to the range of the mechanism.
        if (targetPosition < 0) {
            targetPosition = 0;
        } else if (targetPosition > maxPosition) {
            targetPosition = maxPosition;
        }

        // Set motor target to the new value of the targetPosition variable
        extendoM.setTargetPosition(targetPosition);

        // Set motor power to .5 to seek (and hold) the new target position
        extendoM.setPower(0.5);
    }
}




