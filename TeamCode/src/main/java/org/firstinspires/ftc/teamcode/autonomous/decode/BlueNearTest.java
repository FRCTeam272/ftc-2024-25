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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Config
@Autonomous (name="BlueNearTest", group="Auto")
public class BlueNearTest extends LinearOpMode{

    //TODO - Coordinate List (Pasted from MeepMeep!)

    // Starting Coordinates
    double startX = -50;
    double startY = -44;
    double startH = Math.toRadians(235);

    // Pickup Load1
    double pickup1X = -12;
    double pickup1Y = -24;
    double pickup1H = Math.toRadians(270);

    // Intake Load1
    double intake1X = -12;
    double intake1Y = -52;
    double intake1H = Math.toRadians(270);

    // Launch Load1
    double launch1X = -50;
    double launch1Y = -44;
    double launch1H = Math.toRadians(235);

    // Pickup Load2
    double pickup2X = 12;
    double pickup2Y= -24;
    double pickup2H = Math.toRadians(270);

    // Intake Load2
    double intake2X = 12;
    double intake2Y= -52;
    double intake2H = Math.toRadians(270);

    // Launch Load2
    double launch2X = -50;
    double launch2Y = -44;
    double launch2H = Math.toRadians(235);

    // Pickup Load3
    double pickup3X = 35.5;
    double pickup3Y= -24;
    double pickup3H = Math.toRadians(270);

    // Intake Load3
    double intake3X = 35.5;
    double intake3Y= -52;
    double intake3H = Math.toRadians(270);

    // Ending Coordinates
    double endX = -20;
    double endY = -40;
    double endH = Math.toRadians(235);

    @Override
    public void runOpMode() throws InterruptedException {

        // Initializing Robot
        Pose2d StartPose = new Pose2d(startX,startY,startH);
        MecanumDrive drive = new MecanumDrive(hardwareMap, StartPose);

        // TODO Build Trajectories - paste from MeepMeep, separating out by movement,
        // because robot will do other actions timed by where in the trajectory it is

        //drive to preload shooting position
//        TrajectoryActionBuilder goToShootPreload = drive.actionBuilder(StartPose)
//                .strafeToLinearHeading(new Vector2d(preloadX,preloadY),preloadH) //drive to preload shooting position
//                ;
//        Action GoToShootPreload = goToShootPreload.build(); //notice the uppercase name of the Action vs the lower case name of the trajectory!

        //drive to position to load 1st set of artifacts
        TrajectoryActionBuilder goPickupLoad1 = drive.actionBuilder((StartPose)) // instead of StartPose, it works from where the last trajectory ended
                .strafeToLinearHeading(new Vector2d(pickup1X,pickup1Y),pickup1H) //drive to position to loading 1st set of artifacts
                ;
        Action GoPickupLoad1 = goPickupLoad1.build();

        //intake 1st set of artifacts
        TrajectoryActionBuilder intakeLoad1 = goPickupLoad1.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(intake1X, intake1Y),intake1H) //drive forward to intake 1st load
                ;
        Action IntakeLoad1 = intakeLoad1.build();

        //drive back to launch load 1
        TrajectoryActionBuilder driveToLaunch1 = intakeLoad1.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(launch1X, launch1Y),launch1H) //drive to launch point
                ;
        Action DriveToLaunch1 = driveToLaunch1.build();

        //drive to position to load 2nd set of artifacts
        TrajectoryActionBuilder goPickupLoad2 = driveToLaunch1.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(pickup2X, pickup2Y),pickup2H) //drive to position to load 2nd set of artifacts
                ;
        Action GoPickupLoad2 = goPickupLoad2.build();

        //intake 2nd set of artifacts
        TrajectoryActionBuilder intakeLoad2 = goPickupLoad2.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(intake2X, intake2Y),intake2H) //drive forward to intake load 2
                ;
        Action IntakeLoad2 = intakeLoad2.build();

        //drive back to launch load 2
        TrajectoryActionBuilder driveToLaunch2 = intakeLoad2.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(launch2X, launch2Y),launch2H) //drive to launch point
                ;
        Action DriveToLaunch2 = driveToLaunch2.build();

        //drive to position to load 3rd set of artifacts
        TrajectoryActionBuilder goPickupLoad3 = driveToLaunch2.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(pickup3X,pickup3Y),pickup3H) //drive to position to loading 3rd set of artifacts
                ;
        Action GoPickupLoad3 = goPickupLoad3.build();

        //intake 2nd set of artifacts
        TrajectoryActionBuilder intakeLoad3 = goPickupLoad3.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(intake3X, intake3Y),intake3H) //drive forward to intake load 3
                ;
        Action IntakeLoad3 = intakeLoad3.build();

        //drive to ending position
        TrajectoryActionBuilder endingPose = intakeLoad3.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(endX, endY),endH)//end
                ;
        Action EndingPose = endingPose.build();



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

                // shoot preload
                new ParallelAction(
                        new SleepAction(3) // placeholder for intake and launcher

                ),

                // drive to intake starting point for Load 1
                GoPickupLoad1,

                // intake Load 1
                new ParallelAction(
                        new SleepAction(1), // placeholder for intake
                        IntakeLoad1
                ),

                // drive to launch Load 1
                DriveToLaunch1,

                // launch Load 1
                new SleepAction(1), // placeholder for launcher

                // drive to intake starting point for Load 2
                GoPickupLoad2,

                // intake Load 2
                new ParallelAction(
                        new SleepAction(1), // placeholder for intake
                        IntakeLoad2
                ),

                // drive to launch Load 2
                DriveToLaunch2,

                // launch Load 2
                new SleepAction(1), // placeholder for launcher

                // drive to intake starting point for Load 3
                GoPickupLoad3,

                // intake Load 3
                new ParallelAction(
                        new SleepAction(1), // placeholder for intake
                        IntakeLoad3
                ),

                // drive to ending position
                EndingPose



        ));

    }
}
