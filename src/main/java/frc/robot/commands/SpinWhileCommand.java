// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivebaseSubsystem;

public class SpinWhileCommand extends CommandBase {

  private DrivebaseSubsystem m_drivebaseSubsystem;
  private BooleanSupplier supplier;
  private double speed;

  /** Creates a new spinWhileCommand. */
  public SpinWhileCommand(DrivebaseSubsystem drivebaseSubsystem, double speed, BooleanSupplier supplier) {
   m_drivebaseSubsystem = drivebaseSubsystem;
   this.supplier = supplier;
   this.speed = speed;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_drivebaseSubsystem.setMove(0, 0, speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drivebaseSubsystem.stopMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !supplier.getAsBoolean();
  }

}
