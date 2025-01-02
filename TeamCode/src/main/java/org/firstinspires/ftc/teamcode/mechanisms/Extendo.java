package org.firstinspires.ftc.teamcode.mechanisms;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Extendo {
    DcMotorEx extendoM;

    // AnalogInput armPNP;

    TouchSensor extendoLimit;

    private PIDFController extendoController;
    public static double extendoP = 0.1, extendoI = 0, extendoD = 0.00001, extendoFF = -0.001;

/* boolean toggleArm = true;
 int armMax = 5;
 int armMin = 0;*/

    int extendoPos = -1;

    double extendoTargetPos = 0;

    // final double degpervoltage = 270/3.3;

    double extendoPower = 1;

    //final double extendoCountsPerInch = 19970.0/5.0; //Counts Per Inch

    // final double slideConverter = 62.1/287.5;


    public Extendo(HardwareMap hardwareMap){
        extendoM = hardwareMap.get(DcMotorEx.class, "extendoM");
        extendoM.setDirection(DcMotorSimple.Direction.REVERSE);
        extendoM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extendoM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);




        // armPNP = hardwareMap.get(AnalogInput.class, "armPNP");

        extendoLimit = hardwareMap.get(TouchSensor.class, "extendoLS");

        extendoController = new PIDFController(extendoP, extendoI, extendoD, extendoFF);
    }

    public void Teleop(Gamepad gamepad2, Telemetry telemetry){

        extendoPower = 0.5; // run at half power, full power tends to launch things!
        if (extendoLimit.isPressed()) {
            extendoM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        } else {
            extendoM.setPower(gamepad2.right_stick_y * extendoPower);
        }

        if (extendoM.getCurrentPosition() >= 262) { //max position is 268, so set to something less
            extendoPower = 0;
            extendoM.setTargetPosition(260);
        }

        }

//        if (gamepad2.b){ // Stow Pos
//            extendoPos = 0;
//        }else if (gamepad2.a){ // Minimum Pickup Pos
//            extendoPos = 1;
//        }else if (gamepad2.x){ // Maximum Pickup Pos
//            extendoPos = 2;
//        }
//
//
//        GoToPosition(extendoPos, telemetry);


    public void GoToPosition(int position, Telemetry telemetry) {

        extendoPos = position; // to update in auto, redundant in teleop

        switch (extendoPos) {
            case 0: // Stow Pos
                extendoTargetPos = 0;
                break;
            case 1: // Minimum Pickup Pos
                extendoTargetPos = 100;
                break;
            case 2: // Maximum Pickup Pos
                extendoTargetPos = 260;
                break;

            default:
                throw new IllegalStateException("Unexpected position value: " + position); // todo: remove in comp
        }

        //slideTargetPos *= slideConverter;
        telemetry.addData("extendo current postion", extendoTargetPos);

        if (extendoPos != -1) {

            extendoPower = 0.5; // run at half power, full power tends to launch things!
            if (extendoLimit.isPressed()) {
                extendoM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                extendoM.setTargetPosition(0);
            } else {
                extendoM.setTargetPosition((int) extendoTargetPos);
            }

            if (extendoM.getCurrentPosition() >= 262) { //max position is 268, so set to something less
                extendoPower = 0;

            }

            extendoM.setPower(extendoPower);
            extendoM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        // double armPower = (armController.calculate(getArmAngle(), armTargetPos));

        // double currentIntake = intake.objpivot.getIntakeAngle(telemetry);
        // double targetIntake = intake.objpivot.getIntakeTargetPos();

 /* if ((!((currentIntake > (targetIntake-5)) && (currentIntake < (targetIntake+5)))) && armSlidePos == 0){
 armPower = 0;
 //telemetry.addData("inside", "arm stopper");
 }

 armPower *= 0.5;

 //telemetry.addData("inside range", !((currentIntake > (targetIntake-5)) && (currentIntake < (targetIntake+5))));
 //telemetry.addData("on arm pos 0", armSlidePos != 0);
 armL.setPower(armPower);
 armR.setPower(armPower);

 }

 public void setArmPos(int tarPos){
 double armPower = (armController.calculate(getArmAngle(), tarPos));

 armL.setPower(armPower);
 armR.setPower(armPower);
 } */
    }
        public boolean getExtendoLimitState(){
            return extendoLimit.isPressed();
        }

        public void runExtendo(double extendoPower){
            extendoM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            extendoM.setPower(extendoPower);
        }

        public void stopExtendo(){
            extendoM.setPower(0);
            extendoM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            extendoM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        public void homeExtendo(){
            if (extendoLimit.isPressed()){
                extendoM.setPower(0);
                extendoM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                extendoM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }else {
                extendoM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                extendoM.setPower(-1);
            }

/* slideM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
 slideM.setPower(-1);
 while (!slideLimit.isPressed()){

 }
 slideM.setPower(0);
 slideM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
 slideM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/
        }

        public void resetExtendoEncoder(){
            extendoM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        public double getExtendoPosition(){
            return extendoM.getCurrentPosition();
        }

//        public double getExtendoPos(){
//            return extendoPos;
//        }


    }

