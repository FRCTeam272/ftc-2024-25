package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Disabled
@TeleOp
public class ElevLimitSwitch extends OpMode {
    TouchSensor slideLimit;

    public void init() {

        //initialize limit switch
        slideLimit = hardwareMap.get(TouchSensor.class, "slideLS");
    }

    @Override
    public void loop() {
        telemetry.addData("Status", "Looping");
        telemetry.addData("Elev Limit Switch", slideLimit.isPressed()); //print whether or not it is pressed
        telemetry.update();
    }

    @Override
    public void stop() {

    }
}
