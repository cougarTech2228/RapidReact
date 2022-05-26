package frc.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;

public class AutoDrive implements Runnable {

    private DrivebaseSubsystem m_drivebaseSubsystem;
    private double m_distanceCM = 0.0; // in centimeters
    private double m_fineSpeed = 0.0;
    private double m_coarseSpeed = 0.0;

    private double m_currentEncoderCount = 0.0;
    private double m_endCount = 0.0;

    private double m_target = 0;

    private final static double WHEEL_CIRCUMFERENCE_CM = 48.5;
    private final static double TICKS_PER_ROTATION = 12228;

    private boolean m_hasStartedMoving;

    public AutoDrive(DrivebaseSubsystem drivebaseSubsystem, double distanceCM, double fineSpeed, double coarseSpeed) {
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_distanceCM = distanceCM;
        m_fineSpeed = fineSpeed;
        m_coarseSpeed = coarseSpeed;
        

        if (m_fineSpeed < 0 || m_fineSpeed > 1) {
            m_fineSpeed = 0;
        }

        if (m_coarseSpeed < 0 || m_coarseSpeed > 1) {
            m_coarseSpeed = 0;
        }
    }

    public void run() {
        double delta = (m_distanceCM / WHEEL_CIRCUMFERENCE_CM) * TICKS_PER_ROTATION;
        m_target = m_drivebaseSubsystem.getEncoderCount() + delta;
        m_hasStartedMoving = false;
        
        m_drivebaseSubsystem.setMotorsToBrake();

        double speed = 0.0;
           
            while (getCondition(m_target)) {

                if(m_drivebaseSubsystem.getEncoderRateOfChange() > 0) {
                    m_hasStartedMoving = true;
                }

                if(m_hasStartedMoving && m_drivebaseSubsystem.getEncoderRateOfChange() == 0) {
                    break;
                }

                double percentageToTarget = (m_target - m_drivebaseSubsystem.getEncoderCount()) / delta;
                if (Math.abs(percentageToTarget) < Constants.COARSE_AUTO_MOVE_THRESHOLD_PERCENTAGE) {
                    speed = m_fineSpeed;
                } else {
                    speed = m_coarseSpeed;
                }
                
                if(m_distanceCM < 0) {
                    speed *= -1;
                }

                m_drivebaseSubsystem.setMove(speed, 0, 0);
                //System.out.println(m_currentEncoderCount);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        m_drivebaseSubsystem.setMotorsToBrake();
        m_drivebaseSubsystem.stopMotors();
        m_drivebaseSubsystem.setMotorsToBrake();
        m_drivebaseSubsystem.configOpenLoopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);
    }

    private boolean getCondition(double target) {
        if(m_distanceCM > 0) {
            return m_drivebaseSubsystem.getEncoderCount() < target;
        } else {
            return m_drivebaseSubsystem.getEncoderCount() > target;
        }
    }

    private double mapf(double x, double in_min, double in_max, double out_min, double out_max) {
        if (((in_max - in_min) + out_min) == 0) {
            // guard against a divide by zero error and just set return value to 0.
            return 0.0;
        } else {
            x = Math.abs(x);
            return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
        }
    }
}