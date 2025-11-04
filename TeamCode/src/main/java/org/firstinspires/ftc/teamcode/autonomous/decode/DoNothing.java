package org.firstinspires.ftc.teamcode.autonomous.decode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Config
@Autonomous (name="BlueNearTest", group="Auto")
public class DoNothing extends LinearOpMode{

    //TODO - Coordinate List (Pasted from MeepMeep!)

    // Starting Coordinates
    double startX = -63;
    double startY = -36;
    double startH = Math.toRadians(180);


    // Ending Coordinates
    double endX = -24;
    double endY = -15;
    double endH = Math.toRadians(180);

    @Override
    public void runOpMode() throws InterruptedException {

        // Initializing Robot
        Pose2d StartPose = new Pose2d(startX,startY,startH);
        MecanumDrive drive = new MecanumDrive(hardwareMap, StartPose);

        // TODO Build Trajectories - paste from MeepMeep, separating out by movement,
        // because robot will do other actions timed by where in the trajectory it is

        //drive to preload shooting position
        TrajectoryActionBuilder goToEnd = drive.actionBuilder(StartPose)
               .strafeToLinearHeading(new Vector2d(endX,endY),endH) //drive to preload shooting position
                ;
        Action GoToEnd = goToEnd.build(); //notice the uppercase name of the Action vs the lower case name of the trajectory!





        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Position during Init", StartPose);
            telemetry.update();
        }

        telemetry.addData("Starting Position", StartPose);
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        // TODO Build the actual list of actions with Sequential and Parallel Actions
        // Use SleepAction (the number in () is seconds) as placeholders, but many will need to stay because it will move on to the next action
        // as soon as the prior non-movement action is started


        Actions.runBlocking(new SequentialAction( //overall sequential action that continues for length of Auton



                // drive to prelaunch position
                GoToEnd





        ));

    }
}
