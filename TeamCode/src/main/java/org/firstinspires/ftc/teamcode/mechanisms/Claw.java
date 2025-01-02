package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Claw {

    //Instantiate motor variables
    ServoImplEx clawS;

    ServoImplEx leftFlipperS;
    ServoImplEx rightFlipperS;

    boolean clawIsOpen;

    public Grabber objgrabber;
    public Flipper objflipper;

    public Claw(HardwareMap hardwareMap) { //motor mapping
        clawS = hardwareMap.get(ServoImplEx.class, "clawS");

        leftFlipperS = hardwareMap.get(ServoImplEx.class, "leftFlipperS");
        rightFlipperS = hardwareMap.get(ServoImplEx.class, "rightFlipperS");

        leftFlipperS.setDirection(Servo.Direction.REVERSE);
        //rightFlipperS.setDirection(Servo.Direction.REVERSE);

        clawIsOpen = false; //toggle so that we can use one button for this

        objgrabber = new Grabber();
        objflipper = new Flipper();

    }

    public class Grabber {

        public void openClaw() { //open claw for Auto
            clawS.setPosition(0);
        }

        public void closeClaw() { //close claw for Auto
            clawS.setPosition(0.25);
        }

        public void Teleop(Gamepad gamepad2, Telemetry telemetry) {

            if (gamepad2.a && clawIsOpen) { //close claw
                clawS.setPosition(0);
                clawIsOpen = false;
            } else if (gamepad2.a && !clawIsOpen) { //open claw
                clawS.setPosition(0.25);
                clawIsOpen = true;
            }

            telemetry.addData("Claw servo position", clawS.getPosition());
            telemetry.update();
        }
    }

    public class Flipper {

        public void flipInside() {
            leftFlipperS.setPosition(0);
            rightFlipperS.setPosition(0);
        }

        public void flipOutside() {
            leftFlipperS.setPosition(1);
            rightFlipperS.setPosition(1);
        }

        public void Teleop(Gamepad gamepad2, Telemetry telemetry) {
            if (gamepad2.x) {
                leftFlipperS.setPosition(0);
                rightFlipperS.setPosition(0);
            } else if (gamepad2.y) {
                leftFlipperS.setPosition(1);
                rightFlipperS.setPosition(1);
            }

            telemetry.addData("Flipper Left servo position", leftFlipperS.getPosition());
            telemetry.addData("Flipper Right servo position", rightFlipperS.getPosition());
            telemetry.update();
        }
    }
}
