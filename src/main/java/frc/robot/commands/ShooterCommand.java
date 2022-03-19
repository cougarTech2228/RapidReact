// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;
import frc.robot.subsystems.StorageSubsystem;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/** An example command that uses an example subsystem. */
public class ShooterCommand extends SequentialCommandGroup{
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final ShooterSubsystem m_shooterSubsystem;
  private final StorageSubsystem m_storageSubsystem;
  private final DrivebaseSubsystem m_drivebaseSubsystem;
  private final ShooterVisionSubsystem m_shooterVisionSubsystem;

  private double m_angle;
  private static boolean m_isShooting = false;

  public enum ShotType{
    LUDICROUS, HIGH, LOW, SPIT
  } 
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ShooterCommand(ShooterVisionSubsystem shooterVisionSubsystem, ShooterSubsystem shooterSubsystem, 
                        StorageSubsystem storageSubsystem, DrivebaseSubsystem drivebaseSubsystem, 
                        ShotType shotType) {
    m_shooterVisionSubsystem = shooterVisionSubsystem;
    m_shooterSubsystem = shooterSubsystem;
    m_storageSubsystem = storageSubsystem;
    m_drivebaseSubsystem = drivebaseSubsystem;
    // Use addRequirements() here to declare subsystem dependencies.

    executeShots(shotType, false);
  }

  public ShooterCommand(ShooterVisionSubsystem shooterVisionSubsystem, ShooterSubsystem shooterSubsystem, 
                        StorageSubsystem storageSubsystem, DrivebaseSubsystem drivebaseSubsystem, 
                        double angle) {
    m_shooterVisionSubsystem = shooterVisionSubsystem;
    m_shooterSubsystem = shooterSubsystem;
    m_storageSubsystem = storageSubsystem;
    m_drivebaseSubsystem = drivebaseSubsystem;
    m_angle = angle;

    executeShots(ShotType.HIGH, true);
  }

  public ShooterCommand(ShooterVisionSubsystem shooterVisionSubsystem, ShooterSubsystem shooterSubsystem, 
                        StorageSubsystem storageSubsystem, DrivebaseSubsystem drivebaseSubsystem) {
    m_shooterVisionSubsystem = shooterVisionSubsystem;
    m_shooterSubsystem = shooterSubsystem;
    m_storageSubsystem = storageSubsystem;
    m_drivebaseSubsystem = drivebaseSubsystem;
    m_angle = 0;

    executeShots(ShotType.HIGH, true);
  }
  
  private void executeShots(ShotType shotType, boolean doVisionAlignment){
    if(!m_isShooting){
      addCommands(
        new InstantCommand(() -> {
          m_isShooting = true;
          switch(shotType){
            case LUDICROUS:
              m_shooterSubsystem.setMotors(1);
              break;
            case HIGH:
              m_shooterSubsystem.setMotors(m_shooterSubsystem.getVelocityHigh());
              break;
            case LOW:
              m_shooterSubsystem.setMotors(Constants.LOW_SHOOT_SPEED);
              break;
            case SPIT:
              m_shooterSubsystem.setMotors(Constants.SPIT_SHOOT_SPEED);
              break;
          }
        }));
        
        if(doVisionAlignment) {
          if(m_angle == 0) {
            addCommands(new AutoAngleTurnCommand(m_drivebaseSubsystem, m_shooterVisionSubsystem));
          } else {
            addCommands(new AutoAngleTurnCommand(m_drivebaseSubsystem, m_angle));
          }
        }

        addCommands(new WaitCommand(0.5));

        addCommands(new InstantCommand(() -> {
          double conveyorSpeed = Constants.STORAGE_CONVEYOR_SPEED;

          if(shotType == ShotType.LOW) {
            conveyorSpeed *= 1.5; // Make low shoot faster between shots
          }

          m_storageSubsystem.setConveyorMotor(conveyorSpeed);
        }));

        addCommands(
          new InstantCommand(() -> {
            m_storageSubsystem.setFeedMotor(Constants.SHOOTER_FEED_SPEED);
          })
        );

        addCommands(
          new WaitCommand(Constants.SHOOT_FEED_TIME), 
          new InstantCommand(() -> {
            m_storageSubsystem.stopMotors();
            m_shooterSubsystem.setMotors(Constants.SHOOTER_IDLE_SPEED);
            m_isShooting = false;
          })
        );

    }
  }
 
  @Override
  public void end(boolean interrupted){
    m_isShooting = false;
    m_shooterSubsystem.stopMotors();
    m_storageSubsystem.stopMotors();
  }

}
