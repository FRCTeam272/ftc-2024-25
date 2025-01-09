package org.firstinspires.ftc.teamcode.mechanisms;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Intake {

    //Instantiate motor variables
    static DcMotorEx intakeLiftM;

    static CRServo leftIntake;
    static CRServo rightIntake;

    DistanceSensor sensorDistance;

    AnalogInput flopIntakePNP;

    private PIDController intakeLiftPController;
    public static double p = 0.1, i = 0, d = 0; // todo: tune intake pivot pid

    boolean possession = false; // Variable telling whether we have possession of a game piece or not

    final double intakeSpeed = 1;

    double intakeTargetPos = 0; // todo determine starting position
    int intakePos = 0;
    double intakeLiftPower = 0;

    boolean toggleIntake = true;
    boolean togglePossession = true;

    public Catcher objcatcher;
    public Lift objlift;

    boolean toggleManualIntake = true;
    int manualIntakeOn = 1;

    public Intake(HardwareMap hardwareMap){ //motor mapping
        intakeLiftM = hardwareMap.get(DcMotorEx.class, "intakeFlop");
        intakeLiftM.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeLiftM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeLiftM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeLiftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftIntake = hardwareMap.get(CRServo.class, "leftIntake");
        rightIntake = hardwareMap.get(CRServo.class, "rightIntake");

        leftIntake.setDirection(CRServo.Direction.REVERSE);
        rightIntake.setDirection(CRServo.Direction.FORWARD);

        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensorColor");

        intakeLiftM.setTargetPosition(0);
        intakeLiftM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        objcatcher = new Catcher();
        objlift = new Lift();

        }

    // Lower Intake for Auton
    public class Lower implements Action { //open claw for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intakeLiftM.setTargetPosition(800);
            while (intakeLiftM.getCurrentPosition() <= 799) {
                intakeLiftM.setPower(1);
            }
            intakeLiftM.setPower(0);
            return false;
        }
    }

    public Action lower() {
        return new Lower();
    }

    // Raise Intake for Auton
    public class Raise implements Action { //open claw for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intakeLiftM.setTargetPosition(0);
            return false;
        }
    }

    public Action raise() {
        return new Raise();
    }

    // Run Intake Inward for 5 seconds Auton and then lift Intake
    public class FloorIntake implements Action { //open claw for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            ElapsedTime intakeTimer = new ElapsedTime();
            intakeTimer.reset();

            while (sensorDistance.getDistance(DistanceUnit.CM) < 4 || intakeTimer.milliseconds() <= 3500 ){
                leftIntake.setPower(1);
                rightIntake.setPower(1);
            }
            leftIntake.setPower(0);
            rightIntake.setPower(0);
            intakeLiftM.setTargetPosition(0);
            while (intakeLiftM.getCurrentPosition() >= 1) {
                intakeLiftM.setPower(-1);
            }
            intakeLiftM.setPower(0);
            return false;
        }
    }

    public Action floorIntake() {
        return new FloorIntake();
    }

    // Run Intake Inward for 5 seconds or until sample has gone through for Auton
    public class LoadIntake implements Action { //open claw for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            ElapsedTime intakeTimer = new ElapsedTime();
            intakeTimer.reset();

            while (objcatcher.getPossession() || intakeTimer.milliseconds() <= 5000 ){
                leftIntake.setPower(1);
                rightIntake.setPower(1);
            }
            leftIntake.setPower(0);
            rightIntake.setPower(0);
            return false;
        }
    }

    public Action loadIntake() {
        return new LoadIntake();
    }

    public class Catcher {

        public void runIntake(double intakeState){ // 1 is intake, 0 is off, -1 is outtake
            leftIntake.setPower(intakeSpeed *intakeState);
            rightIntake.setPower(intakeSpeed *intakeState);
        }

        public void Teleop(Gamepad gamepad2, Telemetry telemetry) { //Code to be run in Op Mode void Loop at top level

            int intakeState = 0;

            possession = sensorDistance.getDistance(DistanceUnit.CM) < 3; //todo set distance

            if (togglePossession && possession) { //Only execute once per possession
                togglePossession = false;
            }else if (possession) {
                togglePossession = true;
            }


            if (toggleIntake && gamepad2.dpad_down) {  // Only execute once per Button push
                toggleIntake = false;  // Prevents this section of code from being called again until the Button is released and re-pressed
            } else if (!gamepad2.dpad_down) { //if neither button is being pressed
                toggleIntake = true; // Button has been released, so this allows a re-press to activate the code above.
            }

            if (!toggleIntake){ //Intake
                intakeState = 1;
            }else if (gamepad2.dpad_up){ //Reverse Intake
                intakeState = -1;
//            }else if (!toggleIntake && !togglePossession) { //Stop Intake if there is a piece in it, once per possession
//                intakeState = 0;


            }

            runIntake(intakeState);
            telemetry.addData("Possession", possession);
            telemetry.update();


        }

        public boolean getPossession() {
            possession = sensorDistance.getDistance(DistanceUnit.CM) < 3; //todo set distance
            return possession; //returns variable from distance beam break
        }
    }

    public class Lift {
        public void Teleop(Gamepad gamepad2, Telemetry telemetry) {
            if (gamepad2.dpad_right) { // up position
                intakePos = 0;
            }else if (gamepad2.dpad_left) { //down position
                intakePos = 2;
            }

            GoToPosition (intakePos, telemetry);


        }

        public void GoToPosition(int position, Telemetry telemetry){

            intakePos = position;

            switch (intakePos){
                case 0: // up position
                    intakeTargetPos = 0;
                    break;
                case 1: // mid position
                    intakeTargetPos = 200;
                    break;
                case 2: // down position
                    intakeTargetPos = 800;
                    break;
                default:
                    throw new IllegalStateException("Unexpected position value: " + position); // todo: remove in comp
            }

            telemetry.addData("Intake Target Position", intakeTargetPos);
            telemetry.update();

            if (intakePos != -1) {

                intakeLiftPower = 1;

                intakeLiftM.setTargetPosition((int) intakeTargetPos);

                if (intakeLiftM.getCurrentPosition() >= 1000){
                    intakeLiftPower = 0;
                }

                intakeLiftM.setPower(intakeLiftPower);
                intakeLiftM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
        }
        }
    }