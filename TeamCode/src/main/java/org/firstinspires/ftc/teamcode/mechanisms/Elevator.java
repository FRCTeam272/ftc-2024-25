package org.firstinspires.ftc.teamcode.mechanisms;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Elevator {


    DcMotorEx leftSlide, rightSlide;

//    AnalogInput elevPNP;

    TouchSensor slideLS;

//    private PIDFController elevController;
//    public static double elevP = 0.1, elevI = 0, elevD = 0.00001, elevFF = -0.001;

    boolean toggleSlide = true;
    int slideMax = 5;
    int slideMin = 0;

    int elevSlidePos = -2;

    double elevTargetPos = 0;

    double elevPower = 0;


    public Elevator(HardwareMap hardwareMap) {

        leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");

        //leftSlide.setDirection(DcMotor.Direction.REVERSE); //LS reversed worked on the table!
        rightSlide.setDirection(DcMotor.Direction.REVERSE);

        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//      Disabled so that motor encoders don't reset between auton and teleop!!!!!
//        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
        leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

//        leftSlide.setTargetPosition(0);
//        rightSlide.setTargetPosition(0);
//
//        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        slideLS = hardwareMap.get(TouchSensor.class, "slideLS");

    }

    // Move Elevator to Position "Stow"
    public class Stow implements Action { //set to stow position for the beginning of Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            leftSlide.setPower(1);
            rightSlide.setPower(1);

            leftSlide.setTargetPosition(0);
            rightSlide.setTargetPosition(0);

            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            return false;
        }
    }

    public Action stow() {
        return new Stow();
    }

    // Move Elevator to Position "Load from Intake"
    public class Load implements Action { //set height and open claw to load for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {

            leftSlide.setTargetPosition(500);
            rightSlide.setTargetPosition(500);

//            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            return false;
        }
    }

    public Action load() {
        return new Load();
    }

    // Move Elevator to Position "Score in High Basket"
    public class ScoreHigh implements Action { //raise to High Basket for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {

            leftSlide.setTargetPosition(9500);
            rightSlide.setTargetPosition(9500);

//            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            return false;
        }
    }

    public Action scoreHigh() {
        return new ScoreHigh();
    }

    // Move Elevator to Position "Score in Low Basket"
    public class ScoreLow implements Action { //raise to Low Basket for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {

            leftSlide.setTargetPosition(6600);
            rightSlide.setTargetPosition(6600);

//            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            return false;
        }
    }

    public Action scoreLow() {
        return new ScoreLow();
    }

    public void Teleop(Gamepad gamepad2, Gamepad gamepad1, Telemetry telemetry) {
//        if (toggleSlide && (gamepad1.dpad_up || gamepad1.dpad_down)) {  // Only execute once per Button push
//            toggleSlide = false;  // Prevents this section of code from being called again until the Button is released and re-pressed
//            if (gamepad1.dpad_up) {  // If the d-pad up button is pressed
//                elevSlidePos = elevSlidePos + 1; //Increase elev position
//                if (elevSlidePos > slideMax) { //If arm position is above max
//                    elevSlidePos = slideMax; //Cap it at max
//                }
//            } else if (gamepad1.dpad_down) { // If d-pad down button is pressed
//                elevSlidePos = elevSlidePos - 1; //Decrease elev position
//                if (elevSlidePos < slideMin) { //If arm position is below min
//                    elevSlidePos = slideMin; //cap it at minimum
//                }
//            }
//
//        }
//        else if (!gamepad1.dpad_up && !gamepad1.dpad_down) { //if neither button is being pressed
//            toggleSlide = true; // Button has been released, so this allows a re-press to activate the code above.
//        }


        if (gamepad2.x) { // Base Pos
            elevSlidePos = 0;
        } else if (gamepad2.left_trigger > 0.5) { // Low Basket Pos
            elevSlidePos = 4;
        } else if (gamepad2.left_bumper) { // High Basket Pos
            elevSlidePos = 5;
        } else if (gamepad2.y) { // Load Position
            elevSlidePos = 1;
        } else if (gamepad2.b) { //High Bar Score/Safe load Flip
            elevSlidePos = 2;
        } else if (gamepad2.right_bumper) { // High Specimen Pos
            elevSlidePos = 3;
        } else if (gamepad2.right_trigger > 0.5) { // High Specimen Score
            elevSlidePos = 6;
        }
//        else if (gamepad1.a) { // Low Hang Pull Pos
//            elevSlidePos = 4;
//        } else if (gamepad2.right_trigger > 0.5) { // Low Specimen Pos
//            elevSlidePos = 5;
//        } else if (gamepad2.right_bumper) { // High Specimen Pos
//            elevSlidePos = 6;
//        } else if (gamepad2.b && elevSlidePos == 5) { // Low Specimen Pull Pos
//            elevSlidePos = 7;
//        } else if (gamepad2.b && elevSlidePos == 6) { // High Specimen Pull Pos
//            elevSlidePos = 8;
//        }


        GoToPosition(elevSlidePos, telemetry);
    }

    public void GoToPosition(int position, Telemetry telemetry) {

        elevSlidePos = position; // to update in auto,

        switch (elevSlidePos) {
            case -2: // Start Pos
                elevTargetPos = 0;
                break;
            case -1: // Zero Pos
                elevTargetPos = 0;
                break;
            case 0: // Base Pos
                elevTargetPos = 10;
                break;
            case 1: // Load Pos
                elevTargetPos = 500;
                break;
            case 2: // safe flip
                elevTargetPos = 2000;
                break;
            case 3: // High Bar Place 26.75"
                elevTargetPos = 5900;
                break;
            case 4: // Low Basket Score 29"
                elevTargetPos = 6600;
                break;
            case 5: // High Basket Score/Max
                elevTargetPos = 9500;
                break;
            case 6: // High Specimen Score 20.25"
                elevTargetPos = 3700;
                break;

            default:
                throw new IllegalStateException("Unexpected position value: " + position); // todo: remove in comp
        }

        telemetry.addData("elev slide pos case", elevSlidePos);
        telemetry.addData("elev target pos", elevTargetPos);


        if (elevSlidePos != -2) {

            elevPower = 1; // full power
            if (slideLS.isPressed()) {
                leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                leftSlide.setTargetPosition((int) elevTargetPos);
                rightSlide.setTargetPosition((int) elevTargetPos);

            } else {
                leftSlide.setTargetPosition((int) elevTargetPos);
                rightSlide.setTargetPosition((int) elevTargetPos);
            }

            //Never going to happen?
            if (leftSlide.getCurrentPosition() >= 9600 || rightSlide.getCurrentPosition() >= 9600) { //max pos
                elevPower = 0;
            }

            leftSlide.setPower(elevPower);
            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightSlide.setPower(elevPower);
            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }


    public boolean getSlideLimitState(){
        return slideLS.isPressed();
    }
//
//    public void runSlides(double slidePower){
//        slideM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        slideM.setPower(slidePower);
//    }
//
//    public void stopSlides(){
//        slideM.setPower(0);
//        slideM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        slideM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//    }
//


    public void homeSlides() {
        if (slideLS.isPressed()) {
            rightSlide.setPower(0);
            leftSlide.setPower(0);

            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

//            leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else { //test set run to position to -1000 or something to cover where it was in auton

            rightSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            rightSlide.setPower(-0.5);
            leftSlide.setPower(-0.5);
        }
    }

/*        slideM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideM.setPower(-1);
        while (!slideLimit.isPressed()){

        }
        slideM.setPower(0);
        slideM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/

//
    public void resetSlideEncoder(){
        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public double getCurrentHeight(){
        return leftSlide.getCurrentPosition();
    }
}




