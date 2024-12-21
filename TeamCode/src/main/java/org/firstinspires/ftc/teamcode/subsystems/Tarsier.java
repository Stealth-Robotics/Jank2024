package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class Tarsier extends SubsystemBase {
    private Limelight3A eyes;

    private OliverState state = OliverState.APRILTAG;

    private LLResult result;

    public Tarsier(HardwareMap hardwareMap) {
        eyes = hardwareMap.get(Limelight3A.class, "ll");
    }
    // Oliver be one of these things https://www.istockphoto.com/photo/tarsier-gm457072497-30869922

    public void setState(OliverState state) {
        this.state = state;
        eyes.pipelineSwitch(state.getValue());
    }

    public OliverState getState() {
        return state;
    }

    @Override
    public void periodic() {
        LLResult res = eyes.getLatestResult();
        if(res != null) {
            if(res.isValid()){
                result = res;
            }
        }
    }

    // Returns the latest result from the limelight
    // sets result to null after so that we can only return the latest result once
    public LLResult getResult(){
        LLResult res = result;
        result = null;
        return res;
    }

    public enum OliverState {
        APRILTAG(0),
        BLOCK_DETECTION(1);

        private final int value;
        OliverState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}

// You'll taste it, you'll taste it in time
