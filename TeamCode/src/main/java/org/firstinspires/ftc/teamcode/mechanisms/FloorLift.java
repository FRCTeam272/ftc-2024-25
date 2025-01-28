package org.firstinspires.ftc.teamcode.mechanisms;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FloorLift {
    //Instantiate motor variables
    private CRServo leftLiftS;
    private CRServo rightLiftS;

    private DcMotorEx liftEncoder;

    //PIDF variables and constants
    private PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static double target = 0; // 0 to 6000

    private final double ticks_in_degrees = 8192 / 360.0;

    double liftTargetPos = 0;
    double intakePos = 0;

    double targetLift = 0;

    double upPos = 0; //lift up all the way;
    double downPos = 1000; //lift on floor;

    public FloorLift (HardwareMap hardwareMap) { //motor mapping
        controller  = new PIDController(p, i, d);
        //telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        liftEncoder = hardwareMap.get(DcMotorEx.class, "intakeFlop");
        //liftEncoder.setDirection(DcMotorSimple.Direction.REVERSE);

        leftLiftS = hardwareMap.get(CRServo.class, "leftLiftS");
        rightLiftS = hardwareMap.get(CRServo.class, "rightLiftS");

        //leftLiftS.setDirection(CRServo.Direction.REVERSE);
        rightLiftS.setDirection(CRServo.Direction.REVERSE);

    }

    public void Teleop(Gamepad gamepad2, Telemetry telemetry) {
        if (gamepad2.dpad_right) { // up position
            intakePos = upPos;
        }else if (gamepad2.dpad_left) { //down position
            intakePos = downPos;
        }

        GoToPosition (intakePos, telemetry);


    }

    public void GoToPosition (double angle, Telemetry telemetry) {
        // PIDF Setup from FTC 16379 KookyBotz "PIDF Loops & Arm Control" on YouTube
        liftTargetPos = angle;
        controller.setPID(p, i, d);
        double armPos = liftEncoder.getCurrentPosition();
        double pid = controller.calculate(armPos, liftTargetPos);
        double ff = Math.cos(Math.toRadians(liftTargetPos / ticks_in_degrees)) * f;

        double power = pid + ff;

        rightLiftS.setPower(power);
        leftLiftS.setPower(power);

        telemetry.addData("flipper pos ", armPos);
        telemetry.addData("flipper target ", liftTargetPos);
    }

    public double getLiftAngle() { return liftEncoder.getCurrentPosition(); }
}
