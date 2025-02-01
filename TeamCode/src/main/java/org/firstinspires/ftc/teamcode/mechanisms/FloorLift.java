package org.firstinspires.ftc.teamcode.mechanisms;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FloorLift {
    //Instantiate motor variables
    private CRServo aLiftS;
    private CRServo bLiftS;

    private DcMotorEx liftEncoder;

    //PIDF variables and constants
    private PIDController controller;

    public static double p = 0.002, i = 0.001, d = 0.0001;
    public static double f = 0.1;

    public static double target = 0; // 0 to 6000

    private final double ticks_in_degrees = 8192 / 360.0;

    double liftTargetPos = 0;
    int intakePos = 0;

    int liftPos = 2;

    double targetLift = 0;

    double loadPos = -1980; // load position
    double upPos = 0; //lift straight up all the way;
    double downPos = 2250; //lift on floor;

    public FloorLift (HardwareMap hardwareMap) { //motor mapping
        controller  = new PIDController(p, i, d);
        //telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftEncoder = hardwareMap.get(DcMotorEx.class, "intakeFlop");
        //liftEncoder.setDirection(DcMotorSimple.Direction.REVERSE);

        aLiftS = hardwareMap.get(CRServo.class, "aFlipS");
        bLiftS = hardwareMap.get(CRServo.class, "bFlipS");

        aLiftS.setDirection(CRServo.Direction.REVERSE);
        bLiftS.setDirection(CRServo.Direction.REVERSE);

    }

    public void Teleop(Gamepad gamepad2, Telemetry telemetry) {
        if (gamepad2.dpad_up) { // up position
            intakePos = 0;
        } else if (gamepad2.dpad_left) { //down position
            intakePos = 1;
        } else if (gamepad2.dpad_right) { //load position
            intakePos = -1;
        }

        GoToPosition(intakePos, telemetry);


    }

    public void GoToPosition (int position, Telemetry telemetry) {
        liftPos = position;

        switch (liftPos) {
            case 1: // Claw on Floor
                liftTargetPos = downPos;
                break;
            case 0: // Claw Upright
                liftTargetPos = upPos;
                break;
            case -1: // Inner load position
                liftTargetPos = loadPos;
                break;
            case 2: // Start Pos
                liftTargetPos = upPos;

            default:
                throw new IllegalStateException("Unexpected position value: " + position);
        }


        // PIDF Setup from FTC 16379 KookyBotz "PIDF Loops & Arm Control" on YouTube
        if (liftPos != 2) { //if not in start position

            // if the lift is on the floor, cut power to the servos
            if ((liftPos == 1) && ( liftEncoder.getCurrentPosition() >= (liftTargetPos - 100))){
                bLiftS.setPower(0);
                aLiftS.setPower(0);
            }
            // if lift is in load position, cut power to the servos
            else if ((liftPos == -1) && (liftEncoder.getCurrentPosition() <= (liftTargetPos + 100))){
                bLiftS.setPower(0);
                aLiftS.setPower(0);
            }
            else{
                controller.setPID(p, i, d);
                double armPos = liftEncoder.getCurrentPosition();
                double pid = controller.calculate(armPos, liftTargetPos);
                double ff = Math.cos(Math.toRadians(liftTargetPos / ticks_in_degrees)) * f;

                double power = pid + ff;

                bLiftS.setPower(power);
                aLiftS.setPower(power);
            }
            telemetry.addData("flipper pos ", liftEncoder.getCurrentPosition());
            telemetry.addData("flipper target ", liftTargetPos);
        }
    }
    public double getLiftAngle() { return liftEncoder.getCurrentPosition(); }

    public int getLiftPos() { return liftPos; }

    // Stop flip motors, for auton
    public class FlipStop implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {

            aLiftS.setPower(0); //0 power stops flip
            bLiftS.setPower(0);

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

            aLiftS.setPower(-1); //negative power flips outward
            bLiftS.setPower(-1);

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

            aLiftS.setPower(1); //positive power flips inward
            bLiftS.setPower(1);

            return false;
        }
    }
    public Action flipIn() {
        return new FlipIn();
    }
}
