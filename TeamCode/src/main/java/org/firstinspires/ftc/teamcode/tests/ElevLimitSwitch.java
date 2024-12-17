package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class ElevLimitSwitch extends OpMode {
    DigitalChannel slideLS;

    public void init() {

        //initialize limit switch
        slideLS = hardwareMap.get(DigitalChannel.class, "slideLS");
        slideLS.setMode(DigitalChannel.Mode.INPUT);
    }

    @Override
    public void loop() {
        telemetry.addData("Status", "Looping");
        telemetry.addData("Elev Limit Switch", slideLS.getState()); //print whether or not it is pressed
        telemetry.update();
    }

    @Override
    public void stop() {

    }
}
