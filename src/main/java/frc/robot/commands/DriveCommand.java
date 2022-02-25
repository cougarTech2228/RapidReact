package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivebaseSubsystem;

public class DriveCommand extends CommandBase {
    private double distanceCM;
    private DrivebaseSubsystem driveBase;
    private double speed;
    private boolean isDone = false;

    private final static double WHEEL_DIAMETER_CM = 48.5;
    private final static double TICKS_PER_ROTATION = 12228;

    /**
     * 
     * @param distance Distance in CM to drive, positive values drive forward,
     *                 negative values drive backwards
     * @param speed speed from 0 -> 1
     * @param drivebase
     * @throws Exception
     */
    public DriveCommand(double distanceCM, double speed, DrivebaseSubsystem drivebase) {
        this.distanceCM = distanceCM;
        this.driveBase = drivebase;
        this.speed = speed; 

        if (speed < 0 || speed > 1) {
            speed = 0;
        }
    }

    @Override
    public void execute() {
        isDone = false;
        double currentEncoderCount = driveBase.getEncoderCount();
        double endCount;
        if (distanceCM > 0) {
            endCount = currentEncoderCount + ((distanceCM / WHEEL_DIAMETER_CM) * TICKS_PER_ROTATION);
            while(currentEncoderCount < endCount){
                driveBase.setMove(speed, 0, 0);
                currentEncoderCount = driveBase.getEncoderCount();
            }
        } else {
            endCount = currentEncoderCount - ((-distanceCM / WHEEL_DIAMETER_CM) * TICKS_PER_ROTATION);
            while(currentEncoderCount > endCount){
                driveBase.setMove(-speed, 0, 0);
                currentEncoderCount = driveBase.getEncoderCount();
            }
        }
        driveBase.setMove(0, 0, 0);
        driveBase.setMotorsToBrake();
        isDone = true;
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }
}

