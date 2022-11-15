package org.firstinspires.ftc.teamcode.rasky.components;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.rasky.utilities.Button;
import org.firstinspires.ftc.teamcode.rasky.utilities.WrappedMotor;

/**
 * The class that operates the lift.
 *
 * @author Lucian
 * @version 1.2
 */
public class LiftSystem {
    Gamepad gamepad;
    HardwareMap hardwareMap;
    public WrappedMotor liftMotor;

    double tolerance = 10;
    double speed = 1;

    public LiftSystem(HardwareMap hardwareMap, Gamepad gamepad) {
        this.gamepad = gamepad;
        this.hardwareMap = hardwareMap;
    }

    /**
     * Call this method before using the object.
     */
    public void Init() {
        liftMotor = new WrappedMotor(hardwareMap);
        liftMotor.Init("liftMotor", true, false, true);
        liftMotor.setTolerance(tolerance);
        liftMotor.setSpeed(speed);
        liftMotor.setEncoderDirection(1);
        liftMotor.holdMode(true);
    }

    //Lift positions in encoder ticks
    enum LiftPositions {
        HIGH_JUNCTION(1415),
        MEDIUM_JUNCTION(975),
        LOW_JUNCTION(575),
        GROUND_JUNCTION(55),
        STARTING_POS(0);

        double position = 0;

        LiftPositions(double value) {
            this.position = value;
        }
    }

    Button resetButton = new Button();
    Button groundButton = new Button();
    Button lowButton = new Button();
    Button midButton = new Button();
    Button highButton = new Button();
    LiftPositions state = LiftPositions.STARTING_POS;
    int toggleStates = 0;

    public void run() {

        resetButton.updateButton(gamepad.b);
        groundButton.updateButton(gamepad.dpad_down);
        lowButton.updateButton(gamepad.dpad_right);
        midButton.updateButton(gamepad.dpad_left);
        highButton.updateButton(gamepad.dpad_up);

        if (resetButton.press()) {
            state = LiftPositions.STARTING_POS;
            toggleStates = 0;
        }
        else if(groundButton.press()) {
            state = LiftPositions.GROUND_JUNCTION;
            toggleStates = 1;
        }
        else if (lowButton.press()) {
            state = LiftPositions.LOW_JUNCTION;
            toggleStates = 2;
        }
        else if(midButton.press()) {
            state = LiftPositions.MEDIUM_JUNCTION;
            toggleStates = 3;
        }
        else if (highButton.press()) {
            state = LiftPositions.HIGH_JUNCTION;
            toggleStates = 4;
        }

        liftMotor.setTargetPosition(state.position);
        liftMotor.updatePosition();
    }

    public void showInfo(Telemetry telemetry) {
        telemetry.addData("Lift NrState: ", toggleStates);
        telemetry.addData("Lift State: ", state);

        telemetry.addData("Lift TargetPos: ", liftMotor.targetPosition);
        telemetry.addData("Lift Current Position: ", liftMotor.currentPosition);

        telemetry.addData("Motor Speed: ", speed);
        telemetry.addData("Lift Encoder: ", liftMotor.motor.getCurrentPosition());

        telemetry.addData("Position Tolerance: ", tolerance);
        telemetry.addData("Motor Direction: ", liftMotor.direction);
    }

}
