package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp
public class ClawShoulderEncoder extends OpMode {

    ServoImplEx clawS;

    CRServo leftFlipperS;
    CRServo rightFlipperS;

    DcMotor flipEncoder; //flipper encoder

    boolean clawIsOpen;
    double clawOpenPos;
    double clawClosedPos;

    int flipPosition;

    Gamepad currentGamepad1;
    Gamepad previousGamepad1;




    @Override
    public void init() {

        clawS = hardwareMap.get(ServoImplEx.class, "clawS");

        leftFlipperS = hardwareMap.get(CRServo.class, "leftFlipperS");
        rightFlipperS = hardwareMap.get(CRServo.class, "rightFlipperS");

        leftFlipperS.setDirection(CRServo.Direction.REVERSE);
        //rightFlipperS.setDirection(CRServo.Direction.REVERSE);

        flipEncoder = hardwareMap.get(DcMotor.class, "rightBack");
        flipEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flipEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        /*RUN_WITHOUT_ENCODER does NOT disable the encoder,
        it just tells it not to use it for built in velocity control*/

        flipPosition = flipEncoder.getCurrentPosition();

        clawIsOpen = false;

        clawOpenPos = 0.5;
        clawClosedPos = 0.2;

        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();

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
        telemetry.addData("Flip encoder position", flipPosition);
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

    }
}

