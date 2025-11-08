package org.firstinspires.ftc.teamcode.autonomous.decode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Config
@Autonomous (name="BlueFarTest", group="Auto")
    public class BlueFarTestOnly2 extends LinearOpMode {


        //TODO - Coordinate List (Pasted from MeepMeep!)


        // Starting Coordinates
        double startX = 62;
        double startY = -15;
        double startH = Math.toRadians(180);


        // Shoot Preload
        double preloadX = 58;
        double preloadY = -15;
        double preloadH = Math.toRadians(210);

        // Shoot Load1
    double launchload1X = 58;
    double launchload1Y = -15;
    double launchload1H = Math.toRadians(210);

        // Pickup Load1
        double load1X = 36;
        double load1Y = -30;
        double load1H = Math.toRadians(270);


        // Drive to Pickup Load1
        double getload1X = 36;
        double getload1Y = -52;
        double getload1H = Math.toRadians(270);

    // Drive to pickup load two
    double driveload2X = 36;
    double driveload2Y = -60;
    double driveload2H = Math.toRadians(0);




    //go to end position
        double endX = 0;
        double endY = -28;
        double endH = Math.toRadians(270);


        @Override
        public void runOpMode() throws InterruptedException {


            // Initializing Robot
            Pose2d StartPose = new Pose2d(startX, startY, startH);
            MecanumDrive drive = new MecanumDrive(hardwareMap, StartPose);


            // TODO Build Trajectories - paste from MeepMeep, separating out by movement,
            // because robot will do other actions timed by where in the trajectory it is


            //drive to preload shooting position
            TrajectoryActionBuilder goToShootPreload = drive.actionBuilder(StartPose)
                    .strafeToLinearHeading(new Vector2d(preloadX, preloadY), preloadH) //drive to preload shooting position
                    ;
            Action GoToShootPreload = goToShootPreload.build(); //notice the uppercase name of the Action vs the lower case name of the trajectory!


            //drive to position to loading 1st set of artifacts
            TrajectoryActionBuilder goToIntakeLoad1 = goToShootPreload.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(load1X, load1Y), load1H) //drive to position to loading 1st set of artifacts
                    ;
            Action GoToIntakeLoad1 = goToIntakeLoad1.build();


            //get load one
            TrajectoryActionBuilder intakeload1 = goToIntakeLoad1.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(getload1X, getload1Y), getload1H) //drive to position to loading 1st set of artifacts
                    ;
            Action Intakeload1 = intakeload1.build();

//launch load one
            TrajectoryActionBuilder launchload1 = intakeload1.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(launchload1X, launchload1Y), launchload1H) //drive to position to loading 1st set of artifacts
                    ;
            Action Launchload1 = launchload1.build();


            TrajectoryActionBuilder driveload2 = launchload1.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(driveload2X, driveload2Y), driveload2H) //drive to position to loading 1st set of artifacts
                    ;
            Action Driveload2 = driveload2.build();

            TrajectoryActionBuilder gotoend = driveload2.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(endX, endY), endH) //drive to position to loading 1st set of artifacts
                    ;
            Action Gotoend = gotoend.build();


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


                    // drive to shoot preload while spinning up motor wheel at the same time
                    new ParallelAction(
                            //new SleepAction(0.25), // placeholder for spinning up shooter flywheel Action
                            GoToShootPreload
                    ),


//                    // shoot 3 Artifacts from far position
//                    new SleepAction(0.25), //placeholder for ShootArtifactFar


                    // drive to intake Load 1
                    GoToIntakeLoad1,


                    new ParallelAction(
                            Intakeload1


                            // spin up Intake
                            //new SleepAction(0.25) //spin up Intake Placeholder
                            // Drive forward intaking Artifacts)
                    ),

                    Launchload1,


                    Driveload2,



                    //new SleepAction(0.25), //placeholder for launcher


                    Gotoend


            ));


        }
    }
