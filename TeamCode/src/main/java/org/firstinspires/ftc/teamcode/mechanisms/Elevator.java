package org.firstinspires.ftc.teamcode.mechanisms;
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
    int slideMax = 4;
    int slideMin = 0;

    int elevSlidePos = -1;

    double elevTargetPos = 0;


//    final double degpervoltage = 270/3.3;

    double elevPower = 0;

//    final double slideCountsPerInch = 19970.0 / 5.0; //Counts Per Inch
//
//    final double slideConverter = 62.1 / 287.5;


    public Elevator(HardwareMap hardwareMap) {

        leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");

        leftSlide.setDirection(DcMotor.Direction.REVERSE); //LS reversed worked on the table!
        //rightSlide.setDirection(DcMotor.Direction.REVERSE);

        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        //armPNP = hardwareMap.get(AnalogInput.class, "armPNP");

        slideLS = hardwareMap.get(TouchSensor.class, "slideLS");

       // elevController = new PIDFController(elevP, elevI, elevD, elevFF);
    }

    public void Teleop(Gamepad gamepad2, Gamepad gamepad1, Telemetry telemetry) {
        if (toggleSlide && (gamepad1.dpad_up || gamepad1.dpad_down)) {  // Only execute once per Button push
            toggleSlide = false;  // Prevents this section of code from being called again until the Button is released and re-pressed
            if (gamepad1.dpad_up) {  // If the d-pad up button is pressed
                elevSlidePos = elevSlidePos + 1; //Increase elev position
                if (elevSlidePos > slideMax) { //If arm position is above max
                    elevSlidePos = slideMax; //Cap it at max
                }
            } else if (gamepad1.dpad_down) { // If d-pad down button is pressed
                elevSlidePos = elevSlidePos - 1; //Decrease elev position
                if (elevSlidePos < slideMin) { //If arm position is below min
                    elevSlidePos = slideMin; //cap it at minimum
                }
            }

        }
        else if (!gamepad2.dpad_up && !gamepad2.dpad_down) { //if neither button is being pressed
            toggleSlide = true; // Button has been released, so this allows a re-press to activate the code above.
        }


        if (gamepad2.x) { // Base Pos
            elevSlidePos = 0;
        } else if (gamepad2.left_trigger > 0.5) { // Low Basket Pos
            elevSlidePos = 3;
        } else if (gamepad2.left_bumper) { // High Basket Pos
            elevSlidePos = 4;
        } else if (gamepad1.b) { // Load Position
            elevSlidePos = 2;
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
            case -1: // Start Pos
                elevTargetPos = 0;
                break;
            case 0: // Base Pos
                elevTargetPos = 10;
                break;
            case 1: // High Bar Pull Down  20.25"
                elevTargetPos = 3600;
                break;
            case 2: // High Bar Place 26.75"
                elevTargetPos = 6100;
                break;
            case 3: // Low Basket Score 29"
                elevTargetPos = 6500;
                break;
            case 4: // High Basket Score/Max
                elevTargetPos = 9500;
                break;

            default:
                throw new IllegalStateException("Unexpected position value: " + position); // todo: remove in comp
        }

        telemetry.addData("elev slide pos case", elevSlidePos);
        telemetry.addData("elev target pos", elevTargetPos);


        if (elevSlidePos != -1) {

            elevPower = 0.5; // half power
            if (slideLS.isPressed()) {
                leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                leftSlide.setTargetPosition(10);
                rightSlide.setTargetPosition(10);

            } else {
                leftSlide.setTargetPosition((int) elevTargetPos);
                rightSlide.setTargetPosition((int) elevTargetPos);
            }

            if (leftSlide.getCurrentPosition() >= 9600 || rightSlide.getCurrentPosition() >= 9600) { //max pos
                elevPower = 0;
            }

            leftSlide.setPower(elevPower);
            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightSlide.setPower(elevPower);
            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

//        double elevPower = (armController.calculate(getArmAngle(), armTargetPos));
//
//        double currentIntake = intake.objpivot.getIntakeAngle(telemetry);
//        double targetIntake = intake.objpivot.getIntakeTargetPos();
//
//        if ((!((currentIntake > (targetIntake-5)) && (currentIntake < (targetIntake+5)))) && armSlidePos == 0){
//            elevPower = 0;
//            //telemetry.addData("inside", "arm stopper");
//        }
//
//        elevPower *= 0.5;
//
//        //telemetry.addData("inside range", !((currentIntake > (targetIntake-5)) && (currentIntake < (targetIntake+5))));
//        //telemetry.addData("on arm pos 0", armSlidePos != 0);
//        armL.setPower(elevPower);
//        armR.setPower(elevPower);
//
//    }
//
//    public void setArmPos(int tarPos){
//        double armPower = (armController.calculate(getArmAngle(), tarPos));
//
//        armL.setPower(armPower);
//        armR.setPower(armPower);
//    }
//
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

            rightSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//            leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {

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




