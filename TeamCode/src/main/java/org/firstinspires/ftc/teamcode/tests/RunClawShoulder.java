package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
@TeleOp
public class RunClawShoulder extends OpMode {

    ServoImplEx clawS;

    CRServo leftFlipperS;
    CRServo rightFlipperS;

    boolean clawIsOpen;
    double clawOpenPos;
    double clawClosedPos;

    Gamepad currentGamepad1;
    Gamepad previousGamepad1;

    ElapsedTime flipTimer;




    @Override
    public void init() {

        clawS = hardwareMap.get(ServoImplEx.class, "clawS");

        leftFlipperS = hardwareMap.get(CRServo.class, "leftFlipperS");
        rightFlipperS = hardwareMap.get(CRServo.class, "rightFlipperS");

        leftFlipperS.setDirection(CRServo.Direction.REVERSE);
        //rightFlipperS.setDirection(CRServo.Direction.REVERSE);

        clawIsOpen = false;

        clawOpenPos = 0.2;
        clawClosedPos = 0;

        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();

        flipTimer = new ElapsedTime();

    }

    @Override
    public void loop() {

        // Store the gamepad values from the previous loop iteration in
        // previousGamepad1/2 to be used in this loop iteration.
        // This is equivalent to doing this at the end of the previous
        // loop iteration, as it will run in the same order except for
        // the first/last iteration of the loop.
        previousGamepad1.copy(currentGamepad1);

        // Store the gamepad values from this loop iteration in
        // currentGamepad1/2 to be used for the entirety of this loop iteration.
        // This prevents the gamepad values from changing between being
        // used and stored in previousGamepad1/2.
        currentGamepad1.copy(gamepad1);

        //Write servo position to screen, with instructions for use
        telemetry.addData("Status", "Looping");
        telemetry.addData("To open and close Claw", "Use Gamepad 1 A");
        telemetry.addData("To flip Claw", "Use Gamepad 1 left stick");
        telemetry.addData("Claw servo position", clawS.getPosition());
        telemetry.addData("Claw is open", clawIsOpen);
        telemetry.update();

        //Rising edge detector toggle switch for claw
        if (currentGamepad1.a && !previousGamepad1.a && clawIsOpen){
            clawS.setPosition(clawClosedPos);
            clawIsOpen = false;
        }else if (currentGamepad1.a && !previousGamepad1.a && !clawIsOpen){
            clawS.setPosition(clawOpenPos);
            clawIsOpen = true;
        }

        if (gamepad1.left_stick_y == 0){
            rightFlipperS.setPower(0);
            leftFlipperS.setPower(0);
        } else if (gamepad1.left_stick_y != 0) {
            rightFlipperS.setPower(gamepad1.left_stick_y);
            leftFlipperS.setPower(gamepad1.left_stick_y);
        }

        if (gamepad1.x){ //Flip Inward
            flipTimer.reset();

            while (flipTimer.milliseconds() <= 1200){
                leftFlipperS.setPower(1); //inward flip positive
                rightFlipperS.setPower(1);
            }
            leftFlipperS.setPower(0);
            rightFlipperS.setPower(0);
        }

        if (gamepad1.y) { //Flip Outward
            flipTimer.reset();

            while (flipTimer.milliseconds() <= 1250) {
                leftFlipperS.setPower(-1); //outward flip negative
                rightFlipperS.setPower(-1);
            }
            leftFlipperS.setPower(0);
            rightFlipperS.setPower(0);
        }

        if (gamepad1.b) { // Flip Out from stow
            flipTimer.reset();

            while (flipTimer.milliseconds() <= 1000) {
                leftFlipperS.setPower(-1); //outward flip negative
                rightFlipperS.setPower(-1);
            }
            leftFlipperS.setPower(0);
            rightFlipperS.setPower(0);
        }

        if (gamepad1.right_bumper) { //flip up and barely hold to score in basket
            flipTimer.reset();

            while (flipTimer.milliseconds() <= 400) {
                leftFlipperS.setPower(1); //inward flip postive
                rightFlipperS.setPower(1);
            }
            while (flipTimer.milliseconds() >= 400 && flipTimer.milliseconds() <= 2000) {
                leftFlipperS.setPower(0.08);
                rightFlipperS.setPower(0.08);
            }
            leftFlipperS.setPower(0);
            rightFlipperS.setPower(0);

        }
    }

}


