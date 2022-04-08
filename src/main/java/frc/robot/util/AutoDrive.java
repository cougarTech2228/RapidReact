package frc.robot.util;

import java.sql.Driver;

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

    private final static double WHEEL_CIRCUMFERENCE_CM = 48.5;
    private final static double TICKS_PER_ROTATION = 12228;


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
        m_currentEncoderCount = m_drivebaseSubsystem.getEncoderCount();
        m_drivebaseSubsystem.setMotorsToBrake();
        //m_drivebaseSubsystem.configOpenLoopRamp(0.0);
        double speed = 0.0;
        double delta;

        if (m_distanceCM > 0) {
            delta = ((m_distanceCM / WHEEL_CIRCUMFERENCE_CM) * TICKS_PER_ROTATION);
            m_endCount = m_currentEncoderCount + delta;
        } else {
            delta = ((-m_distanceCM / WHEEL_CIRCUMFERENCE_CM) * TICKS_PER_ROTATION);
            m_endCount = m_currentEncoderCount - delta;
        }

        

        if(m_distanceCM > 0) {            
            while (m_currentEncoderCount < m_endCount && DriverStation.isAutonomous()) {
                //Current: 1007330.0| End Count: 1007701.3608247422 
                double foo = (m_endCount - m_currentEncoderCount) / delta;
                if (Math.abs(foo) < Constants.COARSE_AUTO_MOVE_THRESHOLD_PERCENTAGE) {
                    speed = m_fineSpeed;
                } else {
                    // speed = mapf(Math.abs(m_currentEncoderCount / m_endCount), 
                    //              Constants.COARSE_AUTO_MOVE_THRESHOLD_PERCENTAGE,
                    //              1.0,
                    //              m_fineSpeed,
                    //              m_coarseSpeed);
                    speed = m_coarseSpeed;
                }
                
                m_drivebaseSubsystem.setMove(speed, 0, 0);
                m_currentEncoderCount = m_drivebaseSubsystem.getEncoderCount();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        else {
            while (m_currentEncoderCount > m_endCount && DriverStation.isAutonomous()) {
                double foo = (m_currentEncoderCount - m_endCount) / delta;
                if (Math.abs(foo) < Constants.COARSE_AUTO_MOVE_THRESHOLD_PERCENTAGE) {
                    speed = m_fineSpeed;
                } else {
                    // speed = mapf(Math.abs(m_currentEncoderCount / m_endCount), 
                    //              Constants.COARSE_AUTO_MOVE_THRESHOLD_PERCENTAGE,
                    //              1.0,
                    //              m_fineSpeed,
                    //              m_coarseSpeed);
                    speed = m_coarseSpeed;
                }

                m_drivebaseSubsystem.setMove(-speed, 0, 0);
                m_currentEncoderCount = m_drivebaseSubsystem.getEncoderCount();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        m_drivebaseSubsystem.setMotorsToBrake();
        m_drivebaseSubsystem.stopMotors();
        m_drivebaseSubsystem.setMotorsToBrake();
        m_drivebaseSubsystem.configOpenLoopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);
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