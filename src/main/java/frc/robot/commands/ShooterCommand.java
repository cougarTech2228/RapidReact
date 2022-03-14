// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.subsystems.ShooterSubsystem.VelocityType;

import java.security.interfaces.DSAParams;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/** An example command that uses an example subsystem. */
public class ShooterCommand extends SequentialCommandGroup{
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final ShooterSubsystem m_shooterSubsystem;
  private final StorageSubsystem m_storageSubsystem;
  private final DrivebaseSubsystem m_drivebaseSubsystem;
  private final ShooterVisionSubsystem m_shooterVisionSubsystem;
  
  private boolean m_isAutoAlign = true;
  
  private static boolean m_isShooting = false;
  private boolean m_isHigh = false, m_isAutonomous = false;
  /** 1 = high auto
   * 2 = low auto
   * 3 = high manual
   * 4 = low manual
   */

  public enum ShotType{
    HIGH_AUTO, HIGH_MANUAL, LOW, SPIT;
  } 
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ShooterCommand(ShooterVisionSubsystem shooterVisionSubsystem, ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem, DrivebaseSubsystem drivebaseSubsystem, ShotType shotType) {
    m_shooterVisionSubsystem = shooterVisionSubsystem;
    m_shooterSubsystem = shooterSubsystem;
    m_storageSubsystem = storageSubsystem;
    m_drivebaseSubsystem = drivebaseSubsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooterSubsystem, drivebaseSubsystem, storageSubsystem);

    executeShots(shotType);
  }
  
  private void executeShots(ShotType shotType){
    if(!m_isShooting){
      //double shootVelocity = Constants.HIGH_SHOOT_SPEED;
      addCommands(
        new InstantCommand(() -> {
          m_isShooting = true;
          double motorSetSpeed = m_shooterSubsystem.getVelocityHigh();
          switch(shotType){
            case HIGH_AUTO:

              if(m_shooterSubsystem.getVelocityType() == VelocityType.Equation) {
                motorSetSpeed = ShooterVisionSubsystem.getMotorSpeedAtDistance();
              }

              if(!(motorSetSpeed > .85 || motorSetSpeed < .45 || Double.isNaN(motorSetSpeed))){
                m_shooterSubsystem.setMotors(motorSetSpeed);
              } else {
                m_shooterSubsystem.setMotors(m_shooterSubsystem.getVelocityHigh());
              }

              break;
            case HIGH_MANUAL:
              
              if(m_shooterSubsystem.getVelocityType() == VelocityType.Equation) {
                motorSetSpeed = ShooterVisionSubsystem.getMotorSpeedAtDistance();
              }

              if(!(motorSetSpeed > .85 || motorSetSpeed < .45 || Double.isNaN(motorSetSpeed))){
                m_shooterSubsystem.setMotors(motorSetSpeed);
              } else {
                m_shooterSubsystem.setMotors(m_shooterSubsystem.getVelocityHigh());
              }


              break;
            case LOW:
              m_shooterSubsystem.setMotors(Constants.LOW_SHOOT_SPEED);
              break;
            case SPIT:
              m_shooterSubsystem.setMotors(Constants.SPIT_SHOOT_SPEED);
              break;
          }
        }));


        addCommands(new InstantCommand(() -> {
          double conveyorSpeed = Constants.STORAGE_CONVEYOR_SPEED;

          if(shotType == ShotType.LOW) {
            conveyorSpeed *= 1.5; // Make low shoot faster between shots
          }

          m_storageSubsystem.setConveyorMotor(conveyorSpeed);
        }));

        if(shotType == ShotType.HIGH_AUTO){
          addCommands(
            new AlignToTargetCommand(m_drivebaseSubsystem, m_shooterVisionSubsystem)
          , new WaitCommand(1));
        }
        else{
          addCommands(new WaitCommand(.5));
        }

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
