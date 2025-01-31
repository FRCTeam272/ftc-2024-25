package org.firstinspires.ftc.teamcode.mechanisms;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ClawElev {

    //Instantiate motor variables
    private ServoImplEx clawS;

    boolean clawIsOpen;
    double clawOpenPos;
    double clawClosedPos;

    Gamepad currentGamepad2;
    Gamepad previousGamepad2;

    public ClawElev(HardwareMap hardwareMap) { //motor mapping
        clawS = hardwareMap.get(ServoImplEx.class, "clawS");

        clawIsOpen = false; //toggle so that we can use one button for this

        clawOpenPos = 0.2;
        clawClosedPos = 0;

        currentGamepad2 = new Gamepad();
        previousGamepad2 = new Gamepad();
    }

    public void Teleop(Gamepad gamepad2, Telemetry telemetry) {
        // Store the gamepad values from the previous loop iteration in
        // previousGamepad1/2 to be used in this loop iteration.
        // This is equivalent to doing this at the end of the previous
        // loop iteration, as it will run in the same order except for
        // the first/last iteration of the loop.
        previousGamepad2.copy(currentGamepad2);

        // Store the gamepad values from this loop iteration in
        // currentGamepad1/2 to be used for the entirety of this loop iteration.
        // This prevents the gamepad values from changing between being
        // used and stored in previousGamepad1/2.
        currentGamepad2.copy(gamepad2);

        //Rising edge detector toggle switch for claw
        if (currentGamepad2.a && !previousGamepad2.a && clawIsOpen) {
            clawS.setPosition(clawClosedPos);
            clawIsOpen = false;
        } else if (currentGamepad2.a && !previousGamepad2.a && !clawIsOpen) {
            clawS.setPosition(clawOpenPos);
            clawIsOpen = true;
        }

        telemetry.addData("Claw servo position", clawS.getPosition());
        telemetry.addData("Claw is open", clawIsOpen);
        telemetry.update();
    }

    public boolean IsClawOpen() {
        return clawIsOpen;
    }

    // Open Claw for Auton
    public class OpenClaw implements Action { //open claw for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            clawS.setPosition(clawOpenPos);
            clawIsOpen = true;
            return false;
        }
    }

    public Action openClaw() {
        return new OpenClaw();
    }

    // Close Claw for Auton
    public class CloseClaw implements Action { //open claw for Auto
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            clawS.setPosition(clawClosedPos);
            clawIsOpen = false;
            return false;
        }
    }

    public Action closeClaw() {
        return new CloseClaw();
    }
}
