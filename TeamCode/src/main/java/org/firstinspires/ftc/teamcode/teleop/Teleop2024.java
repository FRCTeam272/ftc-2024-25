package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Drawing;
import org.firstinspires.ftc.teamcode.MecanumDrive;

@TeleOp
public class Teleop2024 extends OpMode {

    //public static Params PARAMS = new Params();

    private FtcDashboard dash = FtcDashboard.getInstance();

    private MecanumDrive mecanumDrive;

    private long prev_time = System.currentTimeMillis();
    private double slide_position = 0.0;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        mecanumDrive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

    }

    private boolean ranOnce = false;

    @Override
    public void loop() {

        TelemetryPacket packet = new TelemetryPacket();

        // Update pose estimator
        mecanumDrive.updatePoseEstimate();
        double maxDriveSpeed = 1.0; //add clamps for elevator up, etc
        double maxRotateSpeed = 0.75;
        mecanumDrive.driveWithController(gamepad1, maxDriveSpeed, maxRotateSpeed);

        // Get execution time
        long dt = System.currentTimeMillis() - prev_time;
        prev_time = System.currentTimeMillis();

        telemetry.addData("x", mecanumDrive.pose.position.x);
        telemetry.addData("y", mecanumDrive.pose.position.y);
        telemetry.addData("heading (deg)", Math.toDegrees(mecanumDrive.pose.heading.toDouble()));
        telemetry.update();

        packet.fieldOverlay().setStroke("#3F51B5");
        Drawing.drawRobot(packet.fieldOverlay(), mecanumDrive.pose);

        dash.sendTelemetryPacket(packet);
    }

    @Override
    public void stop() {

    }

    private static double linearDeadband(double raw, double deadband) {
        return Math.abs(raw) < deadband ? 0 : Math.signum(raw) * (Math.abs(raw) - deadband) / (1 - deadband);
    }

    public static double map(double x, double inMin, double inMax, double outMin, double outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }


}
