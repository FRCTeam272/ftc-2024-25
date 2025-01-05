package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.config.Config;
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
    double clawOpenPos;
    double clawClosedPos;

    double flipperLoadPos;
    double flipperBarPos;
    double flipperBasketPos;

    @Override
    public void init() {

        clawS = hardwareMap.get(ServoImplEx.class, "clawS");

        leftFlipperS = hardwareMap.get(ServoImplEx.class, "leftFlipperS");
        rightFlipperS = hardwareMap.get(ServoImplEx.class, "rightFlipperS");

       // leftFlipperS.setDirection(Servo.Direction.REVERSE);
        rightFlipperS.setDirection(Servo.Direction.REVERSE);

        clawIsOpen = false;

        clawOpenPos = 0.5;
        clawClosedPos = 0;

        flipperLoadPos = 1;
        flipperBarPos = 0;
        flipperBasketPos = 0.35;

    }

    @Override
    public void loop() {

        //Write servo position to screen, with instructions for use
        telemetry.addData("Status", "Looping");
        telemetry.addData("To open and close Claw", "Use Gamepad 1 A");
        telemetry.addData("To flip Claw inside", "Use Gamepad 1 X");
        telemetry.addData("To flip Claw outward", "Use Gamepad 1 Y");
        telemetry.addData("To flip Claw to basket", "Use Gamepad 1 B");
        telemetry.addData("Claw servo position", clawS.getPosition());
        telemetry.addData("Claw is open", clawIsOpen);
        telemetry.addData("Flipper Left servo position", leftFlipperS.getPosition());
        telemetry.addData("Flipper Right servo position", rightFlipperS.getPosition());
        telemetry.update();

        if (gamepad1.a && clawIsOpen){
            clawS.setPosition(clawClosedPos);
            clawIsOpen = false;
        }else if (gamepad1.a && !clawIsOpen){
            clawS.setPosition(clawOpenPos);
            clawIsOpen = true;
        }

        if (gamepad1.x){
            leftFlipperS.setPosition(flipperLoadPos);
            rightFlipperS.setPosition(flipperLoadPos);
        } else if (gamepad1.y) {
            leftFlipperS.setPosition(flipperBarPos);
            rightFlipperS.setPosition(flipperBarPos);
        } else if (gamepad1.b) {
            leftFlipperS.setPosition(flipperBasketPos);
            rightFlipperS.setPosition(flipperBasketPos);
        }
    }
}
