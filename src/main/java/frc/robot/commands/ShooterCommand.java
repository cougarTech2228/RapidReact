// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;
import frc.robot.subsystems.StorageSubsystem;

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
  
  private static boolean kIsShooting = false;
  private boolean m_isHigh = false, m_isAutonomous = false;
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
  public ShooterCommand(ShooterVisionSubsystem shooterVisionSubsystem, ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem, DrivebaseSubsystem drivebaseSubsystem, boolean isHigh, boolean isAutoAlign) {
    m_shooterVisionSubsystem = shooterVisionSubsystem;
    m_shooterSubsystem = shooterSubsystem;
    m_storageSubsystem = storageSubsystem;
    m_drivebaseSubsystem = drivebaseSubsystem;
    m_isHigh = isHigh;
    m_isAutoAlign = isAutoAlign;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooterSubsystem, drivebaseSubsystem, storageSubsystem);

    if(m_isHigh){
      if(m_isAutoAlign){
        executeHighAuto();
      }
      else{
        executeHighManual();
      }
    }
    else{
      if(m_isAutoAlign){
        executeLowAuto();
      }
      else{
        executeLowManual();
      }
    }
  }
  public ShooterCommand(ShooterVisionSubsystem shooterVisionSubsystem, ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem, DrivebaseSubsystem drivebaseSubsystem) {
    m_shooterVisionSubsystem = shooterVisionSubsystem;
    m_shooterSubsystem = shooterSubsystem;
    m_storageSubsystem = storageSubsystem;
    m_drivebaseSubsystem = drivebaseSubsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooterSubsystem, drivebaseSubsystem, storageSubsystem);

    executeAutonomousShoot();
  }
  private void executeHighAuto(){
    if(!kIsShooting){
      //double shootVelocity = m_shooterSubsystem.getCalculatedShooterPercent("High");
      double shootVelocity = Constants.HIGH_SHOOT_SPEED;
      //double shootVelocity = m_shooterSubsystem.getVelocityHighTable().getDouble(Constants.HIGH_SHOOT_SPEED);
      addCommands(
        new InstantCommand(() -> {
          kIsShooting = true;
          m_shooterSubsystem.setMotors(shootVelocity);
          m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED);
        })
        , new AlignToTargetCommand(m_drivebaseSubsystem, m_shooterVisionSubsystem)
        , new WaitCommand(1)
        , new InstantCommand(() -> {
          m_storageSubsystem.setFeedMotor(Constants.SHOOTER_FEED_SPEED);
        })
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
      //double shootVelocity = m_shooterSubsystem.getCalculatedShooterPercent("Low");
      double shootVelocity = Constants.LOW_SHOOT_SPEED;
      //double shootVelocity = m_shooterSubsystem.getVelocityLowTable().getDouble(Constants.LOW_SHOOT_SPEED);
      addCommands(
        new InstantCommand(() -> {
          kIsShooting = true;
          m_shooterSubsystem.setMotors(shootVelocity);
        })
        , new AlignToTargetCommand(m_drivebaseSubsystem, m_shooterVisionSubsystem)
        , new InstantCommand(() -> {          m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED);
        })
        , new WaitCommand(.5)
        , new InstantCommand(() -> {m_storageSubsystem.setFeedMotor(Constants.SHOOTER_FEED_SPEED);})
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
      //double shootVelocity = m_shooterSubsystem.getVelocityHighTable().getDouble(Constants.HIGH_SHOOT_SPEED);
      addCommands(
        new InstantCommand(() -> {
          kIsShooting = true;
          m_shooterSubsystem.setMotors(shootVelocity);
          m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED);
        })
        , new WaitCommand(1)
        , new InstantCommand(() -> {m_storageSubsystem.setFeedMotor(Constants.SHOOTER_FEED_SPEED);})
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
      //double shootVelocity = m_shooterSubsystem.getVelocityLowTable().getDouble(Constants.LOW_SHOOT_SPEED);
      addCommands(
        new InstantCommand(() -> {
          kIsShooting = true;
          m_shooterSubsystem.setMotors(shootVelocity);
        })
        , new InstantCommand(() -> {          m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED);
        })
        , new WaitCommand(.5)
        , new InstantCommand(() -> {m_storageSubsystem.setFeedMotor(Constants.SHOOTER_FEED_SPEED);})
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
          m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED);
        })
        , new AlignToTargetCommand(m_drivebaseSubsystem, m_shooterVisionSubsystem)
        , new WaitCommand(1)
        , new InstantCommand(() -> {m_storageSubsystem.setFeedMotor(Constants.SHOOTER_FEED_SPEED);})
        , new WaitCommand(6)
        , new InstantCommand(() -> {
          m_storageSubsystem.stopMotors();
          m_shooterSubsystem.setMotors(Constants.SHOOTER_IDLE_SPEED);
          kIsShooting = false;
        })
        );
    }
  }

  @Override
  public void end(boolean interrupted){
    kIsShooting = false;
    m_shooterSubsystem.stopMotors();
    m_storageSubsystem.stopMotors();
  }

}
