package org.firstinspires.ftc.teamcode.mechanisms;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Flipper {

    //Instantiate motor variables
    private CRServo leftFlipperS;
    private CRServo rightFlipperS;

    private DcMotorEx flipEncoder;

    //PIDF variables and constants
    private PIDController controller;

    public static double p = 0.001, i = 0, d = 0.0001;
    public static double f = 0.07;

    public static double target = 0; // 0 to 6000

    public static double safeFlipHeight = 1150;

    private final double ticks_in_degrees = 8192 / 360.0;

    double flipTargetPos = 0;

    double targetFlip = 0;

    double innerPos = -6050; //flipper against inner stop;
    double outerPos = 0; //flipper against outer stop;
    double highScorePos = -1000; //flipper up to score in High Basket

    public Flipper(HardwareMap hardwareMap) { //motor mapping
        controller  = new PIDController(p, i, d);
        //telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        flipEncoder = hardwareMap.get(DcMotorEx.class, "rightBack");
        //flipEncoder.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFlipperS = hardwareMap.get(CRServo.class, "leftFlipperS");
        rightFlipperS = hardwareMap.get(CRServo.class, "rightFlipperS");

        //leftFlipperS.setDirection(CRServo.Direction.REVERSE);
        rightFlipperS.setDirection(CRServo.Direction.REVERSE);

    }

    public void GoToPosition (double angle, Elevator elevator, Telemetry telemetry) {

        // turn off servos if at inner hard stop
        if ((elevator.getElevSlidePos() == 1) && (getFlipperAngle() < (innerPos + 50))) {
            rightFlipperS.setPower(0);
            leftFlipperS.setPower(0);
        }
        // turn off servos if at outer hard stop
        else if (((elevator.getElevSlidePos() == 0) || (elevator.getElevSlidePos() == 4) || (elevator.getElevSlidePos() == -1)) && (getFlipperAngle() > (outerPos - 50))) {
            rightFlipperS.setPower(0);
            leftFlipperS.setPower(0);
        }
        else { //run servos
            // PIDF Setup from FTC 16379 KookyBotz "PIDF Loops & Arm Control" on YouTube
            flipTargetPos = angle;
            controller.setPID(p, i, d);
            double armPos = flipEncoder.getCurrentPosition();
            double pid = controller.calculate(armPos, flipTargetPos);
            double ff = Math.cos(Math.toRadians(flipTargetPos / ticks_in_degrees)) * f;

            double power = pid + ff;

            rightFlipperS.setPower(power);
            leftFlipperS.setPower(power);
        }



        telemetry.addData("flipper pos ", flipEncoder.getCurrentPosition());
        telemetry.addData("flipper target ", flipTargetPos);
    }

    public void updateFlipperAngle(Elevator elevator, Telemetry telemetry) {
        // run in loop for Teleop and Auton to have Flipper set position by where the Elevator is going
        if (elevator.getElevSlidePos() != -3) {

            // If elevator is going to high basket position and higher than safe flip height
            if ((elevator.getElevSlidePos() == 2) && (elevator.getCurrentHeight() >= safeFlipHeight)) {
                targetFlip = highScorePos;
            }
            // If elevator is going to load position  and higher than safe flip height or arm is already inside
            else if ((elevator.getElevSlidePos() == 1) && ((elevator.getCurrentHeight() >= safeFlipHeight) || (getFlipperAngle() <= -3000))) {
                targetFlip = innerPos;
            }
            // If elevator is going to any other position with arm in front and higher than safe flip height or arm is already outside
            else if (((elevator.getElevSlidePos() >= 3) || (elevator.getElevSlidePos() == 0)) && ((elevator.getCurrentHeight() >= safeFlipHeight) || (getFlipperAngle() >= -3000))) {
                targetFlip = outerPos;
            }
            // If elevator is going to safe load position to flip Out
            else if ((elevator.getElevSlidePos() == -1) && (elevator.getCurrentHeight() >= safeFlipHeight)) {
                targetFlip = outerPos;
            }
            // If elevator is going to safe load position to flip IN
            else if ((elevator.getElevSlidePos() == -2) && (elevator.getCurrentHeight() >= safeFlipHeight)) {
                targetFlip = innerPos;
            }

            GoToPosition(targetFlip, elevator, telemetry);
        }
    }

    public double getFlipperAngle() { return flipEncoder.getCurrentPosition(); }

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

    // Flip Outward from inner hard stop for Auton
    public class FlipOut implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {

            leftFlipperS.setPower(1); //positive power flips outward
            rightFlipperS.setPower(1);

            return false;
        }
    }
    public Action flipOut() {
        return new FlipOut();
    }

    // Flip Inward from outer hard stop for Auton
    public class FlipIn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {

            leftFlipperS.setPower(-1); //negative power flips inward
            rightFlipperS.setPower(-1);

            return false;
        }
    }
    public Action flipIn() {
        return new FlipIn();
    }


}
