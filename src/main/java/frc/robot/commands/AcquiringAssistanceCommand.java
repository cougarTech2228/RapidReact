// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.CargoVisionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.util.Balls;

public class AcquiringAssistanceCommand extends CommandBase {

  private CargoVisionSubsystem m_cargoVisionSubsystem;
  private DrivebaseSubsystem m_driveBaseSubsystem;
  private AcquisitionSubsystem m_acquisitionSubsystem;
  private StorageSubsystem m_storageSubystem;

  private static boolean m_isAssistingDriver;

  private int m_iterationsWithNoBall;

  public enum AssistanceState {
      ASST_START,
      ASST_ALIGNING,
      ASST_ALIGNED
  }

  public AssistanceState m_assistanceState;

  /** Creates a new AcquiringAssistanceCommand. */
  public AcquiringAssistanceCommand(CargoVisionSubsystem cargoVisionSubsystem, DrivebaseSubsystem drivebaseSubsystem, 
                                    AcquisitionSubsystem acquisitionSubsystem, StorageSubsystem storageSubsystem) {
    m_cargoVisionSubsystem = cargoVisionSubsystem;
    m_driveBaseSubsystem = drivebaseSubsystem;
    m_acquisitionSubsystem = acquisitionSubsystem;
    m_storageSubystem = storageSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_isAssistingDriver = true;
    m_iterationsWithNoBall = 0;
    m_assistanceState = AssistanceState.ASST_ALIGNING;
  }

  

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Balls ball = m_cargoVisionSubsystem.getBestBall();

    if(ball != null) {

        m_iterationsWithNoBall = 0;
        // Calculate where the center of the ball is on the x axis
        double mid = ((ball.getBox().getXmax() - ball.getBox().getXmin()) / 2.0) + ball.getBox().getXmin();
        // Get the center of the screen (center of robot)
        double center = m_cargoVisionSubsystem.getWidth() / 2.0;
        // Calculate the distance of the ball from the center.
        double offset = mid - center;

        switch(m_assistanceState) {
            case ASST_ALIGNING:
                if(Math.abs(offset) < Constants.ASSISTANCE_RANGE) {
                    m_driveBaseSubsystem.setMove(0, 0, 0);
                    m_assistanceState = AssistanceState.ASST_ALIGNED;
                } else {
                    if(ball.getSurfaceArea() < Constants.ASSISTANCE_SA_RANGE) {
                        if(offset < 0)
                            m_driveBaseSubsystem.setMove(0, 0, -Constants.ASSISTANCE_TURN_SPEED);
                        else  
                            m_driveBaseSubsystem.setMove(0, 0, Constants.ASSISTANCE_TURN_SPEED);
                    } else {
                        if(offset < 0) 
                            m_driveBaseSubsystem.setMove(0, -Constants.ASSISTANCE_STRAFE_SPEED, 0);
                        else
                            m_driveBaseSubsystem.setMove(0, Constants.ASSISTANCE_STRAFE_SPEED, 0);
                    }
                }
                break;
            case ASST_ALIGNED:
                if(Math.abs(offset) < Constants.ASSISTANCE_RANGE) {
                    if(ball.getSurfaceArea() < Constants.ASSISTANCE_SA_RANGE) {
                        m_driveBaseSubsystem.setMove(Constants.ASSISTANCE_FORWARD_FAST_SPEED, 0, 0);
                    } else {
                        m_acquisitionSubsystem.setSpinnerMotor(Constants.ACQUIRER_SPINNER_SPEED);
                        m_storageSubystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED * 2);
                        m_driveBaseSubsystem.setMove(Constants.ASSISTANCE_FORWARD_SLOW_SPEED, 0, 0);
                    }
                } else {
                    m_assistanceState = AssistanceState.ASST_ALIGNING;
                }
                break;
            default:
                System.out.println("Unknown state");
        }
    } else {
        m_iterationsWithNoBall++;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_acquisitionSubsystem.stopSpinnerMotor();
    m_storageSubystem.stopMotors();

    m_isAssistingDriver = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Stop when the robot loses the ball for n iterations

     
    // When the ball isn't seen for n seconds. Either due to the ball rolling out of robot's
    // vision or if the robot acquired the ball
    return m_iterationsWithNoBall >= (Constants.ASSISTANCE_LOST_BALL_TIME * 50);
  }

  public static boolean isAssistingDriver() {
    return m_isAssistingDriver;
  }
}
