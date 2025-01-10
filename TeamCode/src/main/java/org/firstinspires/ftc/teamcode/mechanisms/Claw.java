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
    private ServoImplEx clawS;

    private CRServo leftFlipperS;
    private CRServo rightFlipperS;

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

    // Open Claw for Auton
    public class OpenClaw implements Action { //open claw for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
               clawS.setPosition(clawOpenPos);
               return false;
        }
    }

    public Action openClaw() {
        return new OpenClaw();
    }

    // Close Claw for Auton
    public class CloseClaw implements Action { //open claw for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            clawS.setPosition(clawClosedPos);
            return false;
        }
    }

    public Action closeClaw() {
        return new CloseClaw();
    }

    // Flip Outward from inner hard stop for Auton
    // 1200 from hard stop, 800 from stow
    public class FlipOut implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {

                leftFlipperS.setPower(-1); //negative power flips outward
                rightFlipperS.setPower(-1);

            return false;
        }
    }

    public Action flipOut() {
        return new FlipOut();
    }

    // Flip Inward from outer hard stop for Auton
    // 1200 from hard stop
    public class FlipIn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {

            leftFlipperS.setPower(1); //positive power flips inward
            rightFlipperS.setPower(1);

            return false;
        }
    }

    public Action flipIn() {
        return new FlipIn();
    }

    // Flip Inward very lightly to maintain height for scoring
    // full speed for 400ms, then at least 1 sec at this speed
    public class FlipInSlow implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {

            leftFlipperS.setPower(.08); //positive power flips inward
            rightFlipperS.setPower(.08);

            return false;
        }
    }

    public Action flipInSlow() {
        return new FlipInSlow();
    }

    // Stop flip motors, for auton
    public class FlipStop implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {

            leftFlipperS.setPower(0); //0 power stops flip
            rightFlipperS.setPower(0);

            return false;
        }
    }

    public Action flipStop() {
        return new FlipStop();
    }

    // For Auton, 2.5 seconds long
    // Raise Grabber and partially hold it up while opening Claw to score in the high basket, then continue flip to load position
    public class BasketRaise implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            ElapsedTime flipTimer = new ElapsedTime();
            flipTimer.reset();

            // Powers flipper just enough to raise it, then tries to hold it at low power for a second.
            while (flipTimer.milliseconds() <= 400) { // partially raise claw
                leftFlipperS.setPower(1);
                rightFlipperS.setPower(1);
            }
            while (flipTimer.milliseconds() >=400 && flipTimer.milliseconds() <= 600) { // hold claw up
                leftFlipperS.setPower(0.08);
                rightFlipperS.setPower(0.08);
            }
            while (flipTimer.milliseconds() >=600 && flipTimer.milliseconds() <= 2000) { // open claw while continuing to hold up
                clawS.setPosition(clawOpenPos);
                leftFlipperS.setPower(0.08);
                rightFlipperS.setPower(0.08);
            }
            while (flipTimer.milliseconds() >=2000 && flipTimer.milliseconds() <= 2500) { // continue flip to load position
                leftFlipperS.setPower(1);
                rightFlipperS.setPower(1);
            }
            leftFlipperS.setPower(0); //let claw drop inward, open to get next Sample
            rightFlipperS.setPower(0);
            return false;
        }
    }

    public Action basketRaise() {
        return new BasketRaise();
    }

    public class Grabber {

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
