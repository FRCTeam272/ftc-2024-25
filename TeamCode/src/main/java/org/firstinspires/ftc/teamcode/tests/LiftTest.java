package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
@Disabled
@TeleOp
public class LiftTest extends OpMode {
    private DcMotorEx liftEncoder;

    private CRServo aLiftS;
    private CRServo bLiftS;


    @Override
    public void init() {
        liftEncoder = hardwareMap.get(DcMotorEx.class, "intakeFlop");

        aLiftS = hardwareMap.get(CRServo.class, "aFlipS");
        bLiftS = hardwareMap.get(CRServo.class, "bFlipS");

        aLiftS.setDirection(CRServo.Direction.REVERSE);
        bLiftS.setDirection(CRServo.Direction.REVERSE);



        liftEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        bLiftS.setPower(gamepad2.left_stick_y);
        aLiftS.setPower(gamepad2.left_stick_y);

        telemetry.addData("pos ", liftEncoder.getCurrentPosition());

    }
}
