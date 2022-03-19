package frc.robot.util;

import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;

public class AutoAngleTurn implements Runnable {

    private DrivebaseSubsystem m_drivebaseSubsystem;
    private ShooterVisionSubsystem m_shooterVisionSubsystem;
    private double m_targetAngle = 0.0;

    public AutoAngleTurn(DrivebaseSubsystem drivebaseSubsystem, 
                         double targetAngle) {
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_targetAngle = targetAngle; // Maximum is 180.0
    }

    public AutoAngleTurn(DrivebaseSubsystem drivebaseSubsystem, ShooterVisionSubsystem shooterVisionSubsystem) {
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_shooterVisionSubsystem = shooterVisionSubsystem;
    }

    public void run() {

        if(m_shooterVisionSubsystem != null) { 
            double deviation = m_shooterVisionSubsystem.getDeviationFromCenter() * Constants.INCHES_PER_PIXEL;

            m_targetAngle = Math.toDegrees(Math.atan(deviation / Constants.OUTER_TARMAC_DISTANCE));
        }

        System.out.println("Auto Angle Started: " + m_targetAngle);

        if (Math.abs(m_targetAngle) <= Constants.MAX_ANGLE_TO_TURN) {

            double currentHeading = 0.0;

            m_drivebaseSubsystem.configOpenLoopRamp(0.0);
            m_drivebaseSubsystem.resetHeading();
            m_drivebaseSubsystem.setMotorsToBrake();

            currentHeading = m_drivebaseSubsystem.getYaw();

            double delta = currentHeading - m_targetAngle;

            while (Math.abs(delta) > Constants.ACCEPTABLE_AUTO_TURN_OFFSET) {

                if (Math.abs(delta) > Constants.COARSE_ANGLE_THRESHOLD) {
                    double speed = mapf(Math.abs(delta), Constants.COARSE_ANGLE_THRESHOLD,
                    Constants.MAX_ANGLE_TO_TURN,
                    Constants.FINE_AUTO_TURN_SPEED,
                    Constants.COURSE_AUTO_TURN_SPEED);

                    if (delta < 0) {
                        m_drivebaseSubsystem.setMove(0, 0, -speed);
                    } else {
                        m_drivebaseSubsystem.setMove(0, 0, speed);
                    }
                } else {
                    if (delta < 0) {
                        m_drivebaseSubsystem.setMove(0, 0, -Constants.FINE_AUTO_TURN_SPEED);
                    } else {
                        m_drivebaseSubsystem.setMove(0, 0, Constants.FINE_AUTO_TURN_SPEED);
                    }
                }

                currentHeading = m_drivebaseSubsystem.getYaw();
                delta = m_targetAngle - currentHeading;
            }

            m_drivebaseSubsystem.stopMotors();
            m_drivebaseSubsystem.configOpenLoopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);
            System.out.println("Auto Angle complete: " + currentHeading);
        } else {
            System.out.println("Error: Target angle exceeds maximum.");
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
