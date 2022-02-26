// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.ctre.phoenix.sensors.WPI_PigeonIMU;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;

public class TurnCommand extends CommandBase {

  private static WPI_PigeonIMU m_pigeon = new WPI_PigeonIMU(Constants.PIGEON_CAN_ID);

  private DrivebaseSubsystem m_drivebaseSubsystem;
  private int theta;
  private double speed;
  private double newTheta;

  private boolean isDone;

  /**
   * Make the theta negative if turning the opposite direction is desired, leave the
   * speed positive.
   */
  public TurnCommand(DrivebaseSubsystem drivebaseSubsystem, int theta, double speed) {
    m_drivebaseSubsystem = drivebaseSubsystem;
    this.theta = theta;
    this.speed = speed;
    m_pigeon.calibrate();

    System.out.println("Running turn command");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    isDone = false;
    newTheta = theta + m_pigeon.getYaw();
  }

  @Override
  public void execute() {

    double yaw = m_pigeon.getYaw();

    if(yaw < newTheta) {
      m_drivebaseSubsystem.setMove(0, 0, -speed);
    } else {
      m_drivebaseSubsystem.setMove(0, 0, speed);
    }

    if(theta > 0) {
      if(yaw >= newTheta) {
        m_drivebaseSubsystem.stopMotors();
        isDone = true;
      }
    } else {
      if(yaw <= newTheta) {
        m_drivebaseSubsystem.stopMotors();
        isDone = true;
      }
    }

    //System.out.println((int)m_pigeon.getYaw() + "| newTheta: " + newTheta);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    //System.out.println("Ending Turn Command");
    //System.out.println("getYaw: " + m_pigeon.getYaw() + "| newTheta: " + newTheta);
    //m_drivebaseSubsystem.setMove(0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //return Math.abs(m_pigeon.getYaw()) >= Math.abs(newTheta);
    // if(theta > 0) {
    //   return m_pigeon.getYaw() >= newTheta;
    // } else {
    //   return m_pigeon.getYaw() <= newTheta;
    // }
    return isDone;
  }

  public static WPI_PigeonIMU getPigeon() {
      return m_pigeon;
  }
}
