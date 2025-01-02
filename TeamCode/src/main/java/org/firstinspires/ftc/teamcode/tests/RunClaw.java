package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.R;

import java.util.Locale;

@TeleOp
public class RunClaw extends OpMode {

    ServoImplEx clawS;

    ServoImplEx leftFlipperS;
    ServoImplEx rightFlipperS;

    boolean clawIsOpen;

    @Override
    public void init() {

        clawS = hardwareMap.get(ServoImplEx.class, "clawS");

        leftFlipperS = hardwareMap.get(ServoImplEx.class, "leftFlipperS");
        rightFlipperS = hardwareMap.get(ServoImplEx.class, "rightFlipperS");

        leftFlipperS.setDirection(Servo.Direction.REVERSE);
        //rightFlipperS.setDirection(Servo.Direction.REVERSE);

        clawIsOpen = false;

    }

    @Override
    public void loop() {

        //Write servo position to screen, with instructions for use
        telemetry.addData("Status", "Looping");
        telemetry.addData("To open and close Claw", "Use Gamepad 1 A");
        telemetry.addData("To flip Claw inside", "Use Gamepad 1 X");
        telemetry.addData("To flip Claw outward", "Use Gamepad 1 Y");
        telemetry.addData("Claw servo position", clawS.getPosition());
        telemetry.addData("Flipper Left servo position", leftFlipperS.getPosition());
        telemetry.addData("Flipper Right servo position", rightFlipperS.getPosition());
        telemetry.update();

        if (gamepad1.a && clawIsOpen){
            clawS.setPosition(0);
            clawIsOpen = false;
        }else if (gamepad1.a && !clawIsOpen){
            clawS.setPosition(0.25);
            clawIsOpen = true;
        }

        if (gamepad1.x){
            leftFlipperS.setPosition(0);
            rightFlipperS.setPosition(0);
        } else if (gamepad1.y) {
            leftFlipperS.setPosition(1);
            rightFlipperS.setPosition(1);
        }
    }
}
