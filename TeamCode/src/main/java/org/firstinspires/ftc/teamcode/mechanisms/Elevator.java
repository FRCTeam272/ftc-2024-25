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
    DcMotorEx slideM;
    DcMotorEx armL, armR;

    AnalogInput armPNP;

    TouchSensor slideLimit;

    private PIDFController armController;
    public static double armP = 0.1, armI = 0, armD = 0.00001, armFF = -0.001;

/*    boolean toggleArm = true;
    int armMax = 5;
    int armMin = 0;*/

    int armSlidePos = -2;

    double armTargetPos = 0;
    double slideTargetPos = 0;

    final double degpervoltage = 270/3.3;

    double slidePower = 1;

    final double slideCountsPerInch = 19970.0/5.0; //Counts Per Inch

    final double slideConverter = 62.1/287.5;


    public Elevator(HardwareMap hardwareMap){
        slideM = hardwareMap.get(DcMotorEx.class, "slideM");
        slideM.setDirection(DcMotorSimple.Direction.REVERSE);
        slideM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armL = hardwareMap.get(DcMotorEx.class, "armLM");
        armR = hardwareMap.get(DcMotorEx.class, "armRM");

        armL.setDirection(DcMotorSimple.Direction.FORWARD);
        armR.setDirection(DcMotorSimple.Direction.FORWARD);

        armPNP = hardwareMap.get(AnalogInput.class, "armPNP");

        slideLimit = hardwareMap.get(TouchSensor.class, "slideLS");

        armController = new PIDFController(armP, armI, armD, armFF);
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


        if (gamepad2.b){ // Pickup Pos
            armSlidePos = 0;
        }else if (gamepad2.a){ // Low Place Pos
            armSlidePos = 1;
        }else if (gamepad2.x){ // Medium Place Pos
            armSlidePos = 2;
        }else if (gamepad2.y){ // High Place Pos
            armSlidePos = 3;
        }else if (gamepad2.dpad_right){ // Truss Travel Pos
            armSlidePos = -2;
        }else if (gamepad2.dpad_up){ // Climb Up Pos
            armSlidePos = 4;
        }else if (gamepad2.dpad_down){ // Climb Down Pos
            armSlidePos = 5;
        }


        GoToPosition(armSlidePos, intake, telemetry);
    }

    public void GoToPosition(int position, Intake intake, Telemetry telemetry){

        armSlidePos = position; // to update in auto, redundant in teleop

        switch (armSlidePos){
            case -4: //auto pixel stack intake
                armTargetPos = 104.4;
                slideTargetPos = 512;
                break;
            case -3:
                armTargetPos = 71; //auto place
                slideTargetPos = 5013;
                break;
            case -2: //tele truss pass
                armTargetPos = 93;
                slideTargetPos = 4730;
                break;
            case -1: //auto truss pass
                armTargetPos = 93;
                slideTargetPos = 0;
                break;
            case 0: // Intake
                armTargetPos = 102.35;
                slideTargetPos = 4463;
                break;
            case 1: // Low Place
                armTargetPos = 68;
                slideTargetPos = 4730;
                break;
            case 2: // Medium Place
                armTargetPos = 63;
                slideTargetPos = 7172;
                break;
            case 3: // High Place
                armTargetPos = 61;
                slideTargetPos = 9389;
                break;
            case 4: // Climb High
                armTargetPos = 37;
                slideTargetPos = 8656;
                break;
            case 5: // Climb Low
                armTargetPos = 37;
                slideTargetPos = 4707;
                break;
            default:
                throw new IllegalStateException("Unexpected position value: " + position); // todo: remove in comp
        }

        //slideTargetPos *= slideConverter;
        telemetry.addData("arm target angle", getArmAngle());
        telemetry.addData("arm current angle", armTargetPos);

        if (armSlidePos != -1) {

            slidePower = 1;
            if (slideLimit.isPressed()) {
                slideM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                slideM.setTargetPosition(355);
            } else {
                slideM.setTargetPosition((int) slideTargetPos);
            }

            if (slideM.getCurrentPosition() >= 9500){
                slidePower = 0;
            }

            slideM.setPower(slidePower);
            slideM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        double armPower = (armController.calculate(getArmAngle(), armTargetPos));

//        double currentIntake = intake.objpivot.getIntakeAngle(telemetry);
//        double targetIntake = intake.objpivot.getIntakeTargetPos();
//
//        if ((!((currentIntake > (targetIntake-5)) && (currentIntake < (targetIntake+5)))) && armSlidePos == 0){
//            armPower = 0;
//            //telemetry.addData("inside", "arm stopper");
//        }
//
//        armPower *= 0.5;

        //telemetry.addData("inside range", !((currentIntake > (targetIntake-5)) && (currentIntake < (targetIntake+5))));
        //telemetry.addData("on arm pos 0", armSlidePos != 0);
        armL.setPower(armPower);
        armR.setPower(armPower);

    }

    public void setArmPos(int tarPos){
        double armPower = (armController.calculate(getArmAngle(), tarPos));

        armL.setPower(armPower);
        armR.setPower(armPower);
    }

    public boolean getSlideLimitState(){
        return slideLimit.isPressed();
    }

    public void runSlides(double slidePower){
        slideM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideM.setPower(slidePower);
    }

    public void stopSlides(){
        slideM.setPower(0);
        slideM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void homeSlides(){
        if (slideLimit.isPressed()){
            slideM.setPower(0);
            slideM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            slideM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }else {
            slideM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slideM.setPower(-1);
        }

/*        slideM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideM.setPower(-1);
        while (!slideLimit.isPressed()){

        }
        slideM.setPower(0);
        slideM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/
    }

    public void resetSlideEncoder(){
        slideM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public double getArmAngle(){
        double PNPVoltage = armPNP.getVoltage();
        return degpervoltage*PNPVoltage;
    }

    public double getSlideLenght(){
        return slideM.getCurrentPosition();
    }

    public double getArmSlidePos(){
        return armSlidePos;
    }


}
