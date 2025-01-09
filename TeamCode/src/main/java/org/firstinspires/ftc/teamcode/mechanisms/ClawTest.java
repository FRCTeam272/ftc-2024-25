package org.firstinspires.ftc.teamcode.mechanisms;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class ClawTest {

    private ServoImplEx clawS;

    public ClawTest(HardwareMap hardwareMap) {
        clawS = hardwareMap.get(ServoImplEx.class, "clawS");
    }

    public class OpenClaw implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            clawS.setPosition(.5);
            return false;

        }
    }

        public Action openClaw() {
            return new OpenClaw();
        }
    }
}
