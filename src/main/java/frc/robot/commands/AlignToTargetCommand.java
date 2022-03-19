package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;

public class AlignToTargetCommand extends CommandBase {
    private boolean needsToTurn = true;
    private static boolean isAligning = false;
    private DrivebaseSubsystem m_drivebaseSubsystem;
    private ShooterVisionSubsystem m_shooterVisionSubsystem;
    private double m_misalignment = 0.0;

    public AlignToTargetCommand(DrivebaseSubsystem drivebaseSubsystem, ShooterVisionSubsystem shooterVisionSubsystem){
        needsToTurn = true;
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_shooterVisionSubsystem = shooterVisionSubsystem;
        addRequirements(drivebaseSubsystem);
    }

    @Override
    public void initialize() {
        System.out.println("Starting Align Command...");
        m_drivebaseSubsystem.configOpenLoopRamp(0.0);
        m_drivebaseSubsystem.setMotorsToBrake();

        //Do we need to uncomment this for this command to work in a stand-alone environment (i.e, hooked to the Y button)?
        m_shooterVisionSubsystem.setCameras(Constants.SHOOTING_DRIVING_MODE);
    }

    @Override
    public void execute() {
        // negative misalignment value means needs to turn clockwise
        m_misalignment = m_shooterVisionSubsystem.getDeviationFromCenter(); 

        needsToTurn = Math.abs(m_misalignment) > Constants.ACCEPTABLE_ALIGNMENT_OFFSET;

        if (needsToTurn && (!Double.isNaN(m_misalignment))) {
            isAligning = true;
            if (m_misalignment < -Constants.COARSE_ALIGNMENT_THRESHOLD) {
                double speed = mapf(m_misalignment, (double)Constants.COARSE_ALIGNMENT_THRESHOLD, 
                                    (double)Constants.MAX_DETECTED_ALIGNMENT_RANGE, 
                                    Constants.FINE_ALIGNMENT_TURN_SPEED, 
                                    Constants.COARSE_ALIGNMENT_TURN_SPEED);

                m_drivebaseSubsystem.setMove(0, 0, -speed);
                System.out.println("< -Constants.COARSE_ALIGNMENT_THRESHOLD: " + (-speed));
                //m_drivebaseSubsystem.setMove(0, 0, -Constants.COARSE_ALIGNMENT_TURN_SPEED);
            }
            else if (m_misalignment < -Constants.ACCEPTABLE_ALIGNMENT_OFFSET) {
                m_drivebaseSubsystem.setMove(0, 0, -Constants.FINE_ALIGNMENT_TURN_SPEED);
                //System.out.println("< -Constants.ACCEPTABLE_ALIGNMENT_OFFSET");
            }
            else if (m_misalignment  > Constants.COARSE_ALIGNMENT_THRESHOLD) {
                double speed = mapf(m_misalignment, (double)Constants.COARSE_ALIGNMENT_THRESHOLD, 
                                    (double)Constants.MAX_DETECTED_ALIGNMENT_RANGE, 
                                    Constants.FINE_ALIGNMENT_TURN_SPEED, 
                                    Constants.COARSE_ALIGNMENT_TURN_SPEED);

                m_drivebaseSubsystem.setMove(0, 0, speed);
                //System.out.println("> Constants.COARSE_ALIGNMENT_THRESHOLD: " + speed);
                //m_drivebaseSubsystem.setMove(0, 0, Constants.COARSE_ALIGNMENT_TURN_SPEED);
            }
            else if (m_misalignment > Constants.ACCEPTABLE_ALIGNMENT_OFFSET) {
                m_drivebaseSubsystem.setMove(0, 0, Constants.FINE_ALIGNMENT_TURN_SPEED);
                //System.out.println(" > Constants.ACCEPTABLE_ALIGNMENT_OFFSET");
            }
            else {
                System.out.println("Error: Invalid handling of misalignment value");
            }
        }
    }

    @Override
    public void end(boolean interrupted){
        isAligning = false;
        m_drivebaseSubsystem.stopMotors();
        m_drivebaseSubsystem.configOpenLoopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);
        System.out.println("Alignment Complete: " + m_misalignment);
    }

    @Override
    public boolean isFinished(){
        return !needsToTurn;
    }

    public static boolean getIsAligning(){
        return isAligning;
    }

    private double mapf(double x, double in_min, double in_max, double out_min, double out_max) {
        if (((in_max - in_min) + out_min) == 0) {
            // guard against a divide by zero error and just set speed to 0.
            return 0.0;
        } else {
            x = Math.abs(x);
            return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
        }
    }
}
