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
@Autonomous (name="BlueFarTest", group="Auto")
    public class BlueFarTest extends LinearOpMode {


        //TODO - Coordinate List (Pasted from MeepMeep!)


        // Starting Coordinates
        double startX = 62;
        double startY = -15;
        double startH = Math.toRadians(180);


        // Shoot Preload
        double preloadX = 58;
        double preloadY = -15;
        double preloadH = Math.toRadians(210);


        // Pickup Load1
        double load1X = 35;
        double load1Y = -30;
        double load1H = Math.toRadians(270);


        // Drive to Pickup Load1
        double getload1X = 35;
        double getload1Y = -52;
        double getload1H = Math.toRadians(270);


        // turn to drive Load1
        double turnload1X = 35;
        double turnload1Y = -28;
        double turnload1H = Math.toRadians(180);


        // drive to shoot Load1
        double driveload1X = -25;
        double driveload1Y = -28;
        double driveload1H = Math.toRadians(180);


        // turn to shoot Load1
        double turntoshootload1X = -26;
        double turntoshootload1Y = -28;
        double turntoshootload1H = Math.toRadians(225);


        //get into position to get load 2
        double load2X = -12;
        double load2Y = -28;
        double load2H = Math.toRadians(270);


        //get load 2
        double getload2X = -12;
        double getload2Y = -52;
        double getload2H = Math.toRadians(270);


        //shoot load 2
        double shootload2X = -26;
        double shootload2Y = -28;
        double shootload2H = Math.toRadians(225);


        //get into position to get load 3
        double load3X = 12;
        double load3Y = -28;
        double load3H = Math.toRadians(270);


        //get load 3
        double getload3X = 12;
        double getload3Y = -52;
        double getload3H = Math.toRadians(270);


        //turn turn drive load 3 to shooting postion
        double turnload3X = 12;
        double turnload3Y = -28;
        double turnload3H = Math.toRadians(180);


        //shoot load 3
        double shootload3X = -26;
        double shootload3Y = -28;
        double shootload3H = Math.toRadians(225);


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


            //turn to drive load one
            TrajectoryActionBuilder turnload1 = intakeload1.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(turnload1X, turnload1Y), turnload1H) //drive to position to loading 1st set of artifacts
                    ;
            Action Turnload1 = turnload1.build();


            //drive load one to shooting position
            TrajectoryActionBuilder drivetoshootload1 = turnload1.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(driveload1X, driveload1Y), driveload1H) //drive to position to loading 1st set of artifacts
                    ;
            Action Drivetoshootload1 = drivetoshootload1.build();


            //turn to shoot load one
            TrajectoryActionBuilder turntoshootload1 = drivetoshootload1.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(turntoshootload1X, turntoshootload1Y), turntoshootload1H) //drive to position to loading 1st set of artifacts
                    ;
            Action Turntoshootload1 = turntoshootload1.build();


            //drive to position to get load 2
            TrajectoryActionBuilder gotointakeload2 = turntoshootload1.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(load2X, load2Y), load2H) //drive to position to loading 1st set of artifacts
                    ;
            Action Gotointakeload2 = gotointakeload2.build();


            //intake load 2                                                      :D
            TrajectoryActionBuilder intakeload2 = gotointakeload2.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(getload2X, getload2Y), getload2H) //drive to position to loading 1st set of artifacts
                    ;
            Action Intakeload2 = intakeload2.build();


            //shoot load 2
            TrajectoryActionBuilder shootload2 = intakeload2.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(shootload2X, shootload2Y), shootload2H) //drive to position to loading 1st set of artifacts
                    ;
            Action Shootload2 = shootload2.build();


            //intake load 3         ;D
            TrajectoryActionBuilder gotointakeload3 = shootload2.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(load3X, load3Y), load3H) //drive to position to loading 1st set of artifacts
                    ;
            Action Gotointakeload3 = gotointakeload3.build();


            TrajectoryActionBuilder getload3 = gotointakeload3.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(getload3X, getload3Y), getload3H) //drive to position to loading 1st set of artifacts
                    ;
            Action Getload3 = getload3.build();


            //turn load 3 tpo drive to shooting position
            TrajectoryActionBuilder turnload3 = getload3.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(turnload3X, turnload3Y), turnload3H) //drive to position to loading 1st set of artifacts
                    ;
            Action Turnload3 = turnload3.build();


            //shoot load 3
            TrajectoryActionBuilder shootload3 = turnload3.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
                    .strafeToLinearHeading(new Vector2d(shootload3X, shootload3Y), shootload3H) //drive to position to loading 1st set of artifacts
                    ;
            Action Shootload3 = shootload3.build();


            TrajectoryActionBuilder gotoend = shootload3.endTrajectory().fresh() // instead of StartPose, it works from where the last trajectory ended
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
                            new SleepAction(0.25), // placeholder for spinning up shooter flywheel Action
                            GoToShootPreload
                    ),


                    // shoot 3 Artifacts from far position
                    new SleepAction(0.25), //placeholder for ShootArtifactFar


                    // drive to intake Load 1
                    GoToIntakeLoad1,


                    new ParallelAction(
                            Intakeload1,


                            // spin up Intake
                            new SleepAction(0.25) //spin up Intake Placeholder
                            // Drive forward intaking Artifacts)
                    ),


                    Turnload1,


                    Drivetoshootload1,


                    new ParallelAction(
                            Turntoshootload1,
                            new SleepAction(0.25) //placeholder for spinning up launcher
                    ),


                    new SleepAction(0.25),//placeholder for launcher


                    Gotointakeload2,


                    new ParallelAction(
                            Intakeload2,
                            new SleepAction(0.25)// place holder for spinning intake              ;D
                    ),


                    new ParallelAction(
                            Shootload2,
                            new SleepAction(0.25)//placeholder for spinning up launcher
                    ),


                    new SleepAction(0.25),//placeholder for launcher


                    Gotointakeload3,


                    new ParallelAction(
                            Getload3,
                            new SleepAction(0.25)//placeholder for spinning intake
                    ),


                    Turnload3,


                    new ParallelAction(
                            Shootload3,
                            new SleepAction(0.25)//place holder for spinning up the launcher
                    ),


                    new SleepAction(0.25), //placeholder for launcher


                    Gotoend


            ));


        }
    }
