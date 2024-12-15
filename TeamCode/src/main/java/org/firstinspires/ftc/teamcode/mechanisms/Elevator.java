package org.firstinspires.ftc.teamcode.mechanisms;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Elevator {


    DcMotorEx leftSlide, rightSlide;

    AnalogInput elevPNP;

    TouchSensor slideLS;

    private PIDFController elevController;
    public static double elevP = 0.1, elevI = 0, elevD = 0.00001, elevFF = -0.001;

/*    boolean toggleArm = true;
    int armMax = 5;
    int armMin = 0;*/

    int elevSlidePos = -2;

    double elevTargetPos = 0;


    // final double degpervoltage = 270/3.3;

    double elevPower = 1;

    final double slideCountsPerInch = 19970.0/5.0; //Counts Per Inch

    final double slideConverter = 62.1/287.5;


    public void Elev(HardwareMap hardwareMap){ //TODO added "void" when commenting out, remove once fixed

        leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");

        leftSlide.setDirection(DcMotor.Direction.FORWARD);
        rightSlide.setDirection(DcMotor.Direction.REVERSE);

        leftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //armPNP = hardwareMap.get(AnalogInput.class, "armPNP");

        slideLS = hardwareMap.get(TouchSensor.class, "slideLS");

        elevController = new PIDFController(elevP, elevI, elevD, elevFF);
    }

    public void Teleop(Gamepad gamepad2, Telemetry telemetry, Intake intake){
/*        if (toggleArm && (gamepad2.dpad_up || gamepad2.dpad_down)) {  // Only execute once per Button push
            toggleArm = false;  // Prevents this section of code from being called again until the Button is released and re-pressed
            if (gamepad2.dpad_down) {  // If the d-pad up button is pressed
                armSlidePos = armSlidePos - 1; //Increase Arm position
                if (armSlidePos > armMax) { //If arm position is above 3
                    armSlidePos = armMax; //Cap it at 3
                }
            } else if (gamepad2.dpad_up) { // If d-pad down button is pressed
                armSlidePos = armSlidePos + 1; //Decrease arm position
                if (armSlidePos < armMin) { //If arm position is below -3
                    armSlidePos = armMin; //cap it at -3
                }
            }

        }
        else if (!gamepad2.dpad_up && !gamepad2.dpad_down) { //if neither button is being pressed
            toggleArm = true; // Button has been released, so this allows a re-press to activate the code above.
        }*/


        if (gamepad2.b){ // Stow Pos
            elevSlidePos = 0;
        }else if (gamepad2.a){ // Grab Pos
            elevSlidePos = 1;
        }else if (gamepad2.x){ // Low Basket Pos
            elevSlidePos = 2;
        }else if (gamepad2.y){ // High Basket Pos
            elevSlidePos = 3;
        }else if (gamepad2.dpad_right){ // Low Hang Pos
            elevSlidePos = 4;
        }else if (gamepad2.dpad_up){ // Low Specimen Pos
            elevSlidePos = 5;
        }else if (gamepad2.dpad_down){ // High Specimen Pos
            elevSlidePos = 6;
        }


        GoToPosition(elevSlidePos, intake, telemetry);
    }

    public void GoToPosition(int position, Intake intake, Telemetry telemetry){

        elevSlidePos = position; // to update in auto, redundant in teleop

        switch (elevSlidePos){
            case 0: // Stow Pos
                elevTargetPos = 104.4;
                break;
            case 1:
                elevTargetPos = 71; // Grab Pos
                break;
            case 2: // Low Basket Pos
                elevTargetPos = 93;
                break;
            case 3: // High Basket Pos
                elevTargetPos = 93;
                break;
            case 4: // Low Hang Pos
                elevTargetPos = 102.35;
                break;
            case 5: // Low Specimen
                elevTargetPos = 68;
                break;
            case 6: // High Specimen Pos
                elevTargetPos = 63;
                break;
           /* case 3: // High Place
                elevTargetPos = 61;
                break;
            case 4: // Climb High
                elevTargetPos = 37;
                break;
            case 5: // Climb Low
                elevTargetPos = 37;
                elevTargetPos = 4707;
                break; */
            default:
                throw new IllegalStateException("Unexpected position value: " + position); // todo: remove in comp
        }

        //slideTargetPos *= slideConverter;
        // telemetry.addData("arm target angle", getArmAngle());
        telemetry.addData("elev target pos", elevTargetPos);

        if (elevSlidePos != -1) {

            this.elevPower = 0.5; // half power - A.S.
            if (slideLS.isPressed()) {
                leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                leftSlide.setTargetPosition(355);
                rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightSlide.setTargetPosition(355);
            } else {
                leftSlide.setTargetPosition((int) elevTargetPos);
                rightSlide.setTargetPosition((int) elevTargetPos);
            }

            if (leftSlide.getCurrentPosition() >= 9500||rightSlide.getCurrentPosition() >= 9500){ //max pos
                this.elevPower = 0;
            }

            leftSlide.setPower(this.elevPower);
            leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightSlide.setPower(this.elevPower);
            rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
//    public boolean getSlideLimitState(){
//        return slideLimit.isPressed();
//    }
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
//    public void homeSlides(){
//        if (slideLimit.isPressed()){
//            slideM.setPower(0);
//            slideM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            slideM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        }else {
//            slideM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            slideM.setPower(-1);
//        }
//
///*        slideM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        slideM.setPower(-1);
//        while (!slideLimit.isPressed()){
//
//        }
//        slideM.setPower(0);
//        slideM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        slideM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/
//    }
//
//    public void resetSlideEncoder(){
//        slideM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//    }
//
//    public double getArmAngle(){
//        double PNPVoltage = armPNP.getVoltage();
//        return degpervoltage*PNPVoltage;
//    }
//
//    public double getSlideLength(){
//        return slideM.getCurrentPosition();
//    }
//
//    public double getArmSlidePos(){
//        return armSlidePos;
//    }
//

}}
