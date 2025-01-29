package org.firstinspires.ftc.teamcode.mechanisms;

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
    double downPos = 2100; //lift on floor;

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
        if (liftPos != -2) {
            controller.setPID(p, i, d);
            double armPos = liftEncoder.getCurrentPosition();
            double pid = controller.calculate(armPos, liftTargetPos);
            double ff = Math.cos(Math.toRadians(liftTargetPos / ticks_in_degrees)) * f;

            double power = pid + ff;

            bLiftS.setPower(power);
            aLiftS.setPower(power);

            telemetry.addData("flipper pos ", armPos);
            telemetry.addData("flipper target ", liftTargetPos);
        }
    }
    public double getLiftAngle() { return liftEncoder.getCurrentPosition(); }

    public int getLiftPos() { return liftPos; }
}
