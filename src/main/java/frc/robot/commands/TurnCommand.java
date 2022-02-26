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
  private double startingTheta;
  private double newTheta;

  /** Creates a new TurnCommand. */
  public TurnCommand(DrivebaseSubsystem drivebaseSubsystem, int theta, double speed) {
    m_drivebaseSubsystem = drivebaseSubsystem;
    this.theta = -theta;
    this.speed = speed;
    m_pigeon.calibrate();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startingTheta = m_pigeon.getYaw();
    System.out.println("Starting at: " + startingTheta);
    System.out.println("Theta was: " + theta);
    newTheta = theta + startingTheta;
    System.out.println("Theta is now: " + newTheta);
    
  }

  @Override
  public void execute() {
    if(m_pigeon.getYaw() < newTheta) {
      m_drivebaseSubsystem.setMove(0, 0, -speed);
    } else {
      m_drivebaseSubsystem.setMove(0, 0, speed);
    }

    System.out.println((int)m_pigeon.getYaw());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drivebaseSubsystem.setMove(0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(m_pigeon.getYaw()) >= Math.abs(newTheta);
  }

  public static WPI_PigeonIMU getPigeon() {
      return m_pigeon;
  }
}
