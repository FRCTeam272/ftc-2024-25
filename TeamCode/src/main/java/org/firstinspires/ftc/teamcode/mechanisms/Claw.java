package org.firstinspires.ftc.teamcode.mechanisms;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Claw {

    //Instantiate motor variables
    ServoImplEx clawS;

    CRServo leftFlipperS;
    CRServo rightFlipperS;

    boolean clawIsOpen;
    double clawOpenPos;
    double clawClosedPos;

    Gamepad currentGamepad2;
    Gamepad previousGamepad2;

    public Grabber objgrabber;
    public Flipper objflipper;

    public Claw(HardwareMap hardwareMap) { //motor mapping
        clawS = hardwareMap.get(ServoImplEx.class, "clawS");

        leftFlipperS = hardwareMap.get(CRServo.class, "leftFlipperS");
        rightFlipperS = hardwareMap.get(CRServo.class, "rightFlipperS");

        leftFlipperS.setDirection(CRServo.Direction.REVERSE);
        //rightFlipperS.setDirection(Servo.Direction.REVERSE);

        clawIsOpen = false; //toggle so that we can use one button for this

        clawOpenPos = 0.2;
        clawClosedPos = -0.2;

        currentGamepad2 = new Gamepad();
        previousGamepad2 = new Gamepad();

        objgrabber = new Grabber();
        objflipper = new Flipper();

    }

    public class OpenClaw implements Action { //open claw for Auto
        private boolean init = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!init) {
                clawS.setPosition(0.2);
                if (clawS.getPosition() == 0.2){
                    init = true; }
            }
            return false;
        }
    }

    public Action openClaw() {
        return new OpenClaw();
    }

    public class Grabber {

        public class OpenClaw implements Action { //open claw for Auto
            private boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!init) {
                    clawS.setPosition(0.2);
                    init = true;
                }
                return clawS.getPosition() > 0.1;
            }
        }

        public Action openClaw() {
            return new OpenClaw();
        }
//
//        public class CloseClaw implements Action { //close claw for Auto
//            @Override
//            public boolean run(@NonNull TelemetryPacket packet) {
//                clawS.setPosition(-0.2);
//                return false;
//            }
//        }
//
//        public Action closeClaw() {
//            return new CloseClaw();
//        }

//        public Action openClaw() {
//            return new Action() {
//                private boolean init = false;
//
//                @Override
//                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//                    if(!init) {
//                        clawS.setPosition(0.2);
//                        init = true;
//                    }
//                    return true;
//                }
//            };
//        }


        public void Teleop(Gamepad gamepad2, Telemetry telemetry) {

            // Store the gamepad values from the previous loop iteration in
            // previousGamepad1/2 to be used in this loop iteration.
            // This is equivalent to doing this at the end of the previous
            // loop iteration, as it will run in the same order except for
            // the first/last iteration of the loop.
            previousGamepad2.copy(currentGamepad2);

            // Store the gamepad values from this loop iteration in
            // currentGamepad1/2 to be used for the entirety of this loop iteration.
            // This prevents the gamepad values from changing between being
            // used and stored in previousGamepad1/2.
            currentGamepad2.copy(gamepad2);

            //Rising edge detector toggle switch for claw
            if (currentGamepad2.a && !previousGamepad2.a && clawIsOpen) {
                clawS.setPosition(clawClosedPos);
                clawIsOpen = false;
            } else if (currentGamepad2.a && !previousGamepad2.a && !clawIsOpen) {
                clawS.setPosition(clawOpenPos);
                clawIsOpen = true;
            }

            telemetry.addData("Claw servo position", clawS.getPosition());
            telemetry.addData("Claw is open", clawIsOpen);
            telemetry.update();
        }
    }

    public class Flipper {

        public void flipInside() { //powers briefly to send to inward facing hard stop

            ElapsedTime flipTimer = new ElapsedTime();
            flipTimer.reset();

            while (flipTimer.milliseconds() <= 1000) {
                leftFlipperS.setPower(1);
                rightFlipperS.setPower(1);
            }
            leftFlipperS.setPower(0);
            rightFlipperS.setPower(0);
        }

        public void flipOutside() { //powers briefly to flip to outward facing hard stop

            ElapsedTime flipTimer = new ElapsedTime();
            flipTimer.reset();

            while (flipTimer.milliseconds() <= 500) {
                leftFlipperS.setPower(-1);
                rightFlipperS.setPower(-1);
            }
            leftFlipperS.setPower(0);
            rightFlipperS.setPower(0);
        }

        public void Teleop(Gamepad gamepad2, Telemetry telemetry) {
            if (gamepad2.left_stick_y == 0) {
                rightFlipperS.setPower(0);
                leftFlipperS.setPower(0);
            } else if (gamepad2.left_stick_y != 0) {
                rightFlipperS.setPower(-gamepad2.left_stick_y);
                leftFlipperS.setPower(-gamepad2.left_stick_y);

//            telemetry.addData("Flipper Left servo position", leftFlipperS.getPosition());
//            telemetry.addData("Flipper Right servo position", rightFlipperS.getPosition());
                telemetry.update();
            }
        }
    }
}
