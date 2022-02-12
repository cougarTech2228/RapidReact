// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/** An example command that uses an example subsystem. */
public class ShooterCommand extends SequentialCommandGroup{
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final ShooterSubsystem m_shooterSubsystem;
  private final StorageSubsystem m_storageSubsystem;
  private final DrivebaseSubsystem m_drivebaseSubsystem;
  
  //private NetworkTableEntry m_velocityEntry;
  private static boolean kIsShooting = false;
  private int m_typeOfShot;
  /** 1 = high auto
   * 2 = low auto
   * 3 = high manual
   * 4 = low manual
   */

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ShooterCommand(ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem, DrivebaseSubsystem drivebaseSubsystem, int typeOfShot) {
    m_shooterSubsystem = shooterSubsystem;
    m_storageSubsystem = storageSubsystem;
    m_drivebaseSubsystem = drivebaseSubsystem;
    m_typeOfShot = typeOfShot;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooterSubsystem, drivebaseSubsystem, storageSubsystem);

    
  //   m_velocityEntry = Shuffleboard.getTab("Shooter Velocity Adjuster").add("Shooter Velocity", Constants.HIGH_SHOOT_SPEED)
  //   .withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", Constants.SHOOTER_MAX_OUTPUT)).getEntry();
  //  m_velocityEntry.setDefaultNumber(0);
    if(m_drivebaseSubsystem.getDrivingMode() == Constants.ACQUIRING_DRIVING_MODE)
      return;
    switch(m_typeOfShot){
      case 1:
        executeHighAuto();
        break;

      case 2:
        executeLowAuto();
        break;
      
      case 3:
        executeHighManual();
        break;

      case 4:
        executeLowManual();
        break;
      case 5:
        executeAutonomousShoot();
        break;
    }

  }

  private void executeHighAuto(){
    if(!kIsShooting){
      double shootVelocity = m_shooterSubsystem.getCalculatedShooterPercent("High");
      addCommands(
        new InstantCommand(() -> {
          kIsShooting = true;
          m_shooterSubsystem.setMotors(shootVelocity);
        })
        , new AlignToTargetCommand(m_drivebaseSubsystem)
        , new InstantCommand(() -> {          m_storageSubsystem.setDriveMotor();
        })
        , new WaitCommand(.5)
        , new InstantCommand(() -> {m_storageSubsystem.setFeedMotor();})
        , new WaitCommand(Constants.SHOOT_FEED_TIME)
        , new InstantCommand(() -> {
          m_storageSubsystem.stopMotors();
          m_shooterSubsystem.setMotors(Constants.SHOOTER_IDLE_SPEED);
          kIsShooting = false;
        })
        );
    }
  }
  private void executeLowAuto(){
    if(!kIsShooting){
      double shootVelocity = m_shooterSubsystem.getCalculatedShooterPercent("Low");
      addCommands(
        new InstantCommand(() -> {
          kIsShooting = true;
          m_shooterSubsystem.setMotors(shootVelocity);
        })
        , new AlignToTargetCommand(m_drivebaseSubsystem)
        , new InstantCommand(() -> {          m_storageSubsystem.setDriveMotor();
        })
        , new WaitCommand(.5)
        , new InstantCommand(() -> {m_storageSubsystem.setFeedMotor();})
        , new WaitCommand(Constants.SHOOT_FEED_TIME)
        , new InstantCommand(() -> {
          m_storageSubsystem.stopMotors();
          m_shooterSubsystem.setMotors(Constants.SHOOTER_IDLE_SPEED);
          kIsShooting = false;
        })
        );
    }
  }
  private void executeHighManual(){
    if(!kIsShooting){
      double shootVelocity = Constants.HIGH_SHOOT_SPEED;
      addCommands(
        new InstantCommand(() -> {
          kIsShooting = true;
          m_shooterSubsystem.setMotors(shootVelocity);
          m_storageSubsystem.setDriveMotor();
        })
        , new WaitCommand(1)
        , new InstantCommand(() -> {m_storageSubsystem.setFeedMotor();})
        , new WaitCommand(Constants.SHOOT_FEED_TIME)
        , new InstantCommand(() -> {
          m_storageSubsystem.stopMotors();
          m_shooterSubsystem.setMotors(Constants.SHOOTER_IDLE_SPEED);
          kIsShooting = false;
        })
        );
    }
  }
  private void executeLowManual(){
    if(!kIsShooting){
      double shootVelocity = Constants.LOW_SHOOT_SPEED;
      addCommands(
        new InstantCommand(() -> {
          kIsShooting = true;
          m_shooterSubsystem.setMotors(shootVelocity);
        })
        , new InstantCommand(() -> {          m_storageSubsystem.setDriveMotor();
        })
        , new WaitCommand(.5)
        , new InstantCommand(() -> {m_storageSubsystem.setFeedMotor();})
        , new WaitCommand(Constants.SHOOT_FEED_TIME)
        , new InstantCommand(() -> {
          m_storageSubsystem.stopMotors();
          m_shooterSubsystem.setMotors(Constants.SHOOTER_IDLE_SPEED);
          kIsShooting = false;
        })
        );
    }
  }
  public void executeAutonomousShoot(){
    if(!kIsShooting){
      double shootVelocity = Constants.HIGH_SHOOT_SPEED;
      addCommands(
        new InstantCommand(() -> {
          kIsShooting = true;
          m_shooterSubsystem.setMotors(shootVelocity);
        })
        , new InstantCommand(() -> {          m_storageSubsystem.setDriveMotor();
        })
        , new InstantCommand(() -> {m_storageSubsystem.setFeedMotor();})
        , new WaitCommand(6)
        , new InstantCommand(() -> {
          m_storageSubsystem.stopMotors();
          m_shooterSubsystem.setMotors(Constants.SHOOTER_IDLE_SPEED);
          kIsShooting = false;
        })
        );
    }
  }
}
