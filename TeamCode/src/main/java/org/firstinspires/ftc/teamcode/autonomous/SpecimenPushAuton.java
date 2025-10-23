package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.ClawElev;
import org.firstinspires.ftc.teamcode.mechanisms.Extendo;

@Disabled
@Config
@Autonomous (name="Right_Side_Sample_Push_Auton", group="Auto")
public class SpecimenPushAuton extends LinearOpMode {

    // Pose to clear Submersible and turn sideways
    public static double moveClearX = 12;
    public static double moveClearY = -60;
    public static double moveClearH = Math.toRadians(90);

    public static double moveClear2X = 36;
    public static double moveClear2Y = -36;

    // Coord to pass Submersible
    public static double movePastX = 36;
    public static double movePastY = -12;

    // Coord to position for Sample1
    public static double sample1X = 46;
    public static double sample1Y = -12;

    // Coord to push to Obs for Sample1
    public static double sample1pushX = 46;
    public static double sample1pushY = -58;

    // Coord to clear for Sample2
    public static double sample2clearX = 45;
    public static double sample2clearY = -12;

    // Coord to position for Sample2
    public static double sample2X = 55;
    public static double sample2Y = -12;

    // Coord to push to Obs for Sample2
    public static double sample2pushX = 55;
    public static double sample2pushY = -58;

    // Coord to clear for Sample3
    public static double sample3clearX = 55;
    public static double sample3clearY = -12;

    // Coord to position for Sample3
    public static double sample3X = 61;
    public static double sample3Y = -12;

    // Coord to push to Obs for Sample3
    public static double sample3pushX = 57;
    public static double sample3pushY = -58;

    @Override
    public void runOpMode() {

        Pose2d StartPose = new Pose2d(12, -63.5, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, StartPose);
        ClawElev clawElev = new ClawElev(hardwareMap);
        Extendo extendo = new Extendo(hardwareMap);

        Action pushSamples = drive.actionBuilder(StartPose)
                .strafeTo(new Vector2d(moveClearX, moveClearY)) // move away from wall
                .strafeTo(new Vector2d(moveClear2X, moveClear2Y))
                .strafeTo(new Vector2d(movePastX, movePastY)) // move past Sub
                .strafeTo(new Vector2d(sample1X, sample1Y)) // move over to in back of sample 1
                .strafeTo(new Vector2d(sample1pushX,sample1pushY)) //push sample 1 to Obs Zone
                .strafeTo(new Vector2d(sample2clearX,sample2clearY)) //move back behind samples
                .strafeTo(new Vector2d(sample2X, sample2Y)) //move over to in back of sample 2
                .strafeTo(new Vector2d(sample2pushX, sample2pushY)) //push sample 2 to Obs Zone
                .strafeTo(new Vector2d(sample3clearX,sample3clearY)) //move back behind samples
                .strafeTo(new Vector2d(sample3X,sample3Y)) //move over to in back of sample 3
                .strafeTo(new Vector2d(sample3pushX,sample3pushY)) //push sample 3 to ObsZone, Park
                .build();

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Position during Init", StartPose);
            telemetry.update();
        }

        telemetry.addData("Starting Position", StartPose);
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(new SequentialAction(
                clawElev.closeClaw(),
                extendo.stow(),
                pushSamples
        ));

    }
}
