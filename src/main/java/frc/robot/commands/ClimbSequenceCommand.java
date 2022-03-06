// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.subsystems.ClimberSubsystem;

public class ClimbSequenceCommand extends SequentialCommandGroup {
  /** Creates a new ClimbSequenceCommand. */
  public ClimbSequenceCommand(ClimberSubsystem climberSubsystem) {

    addCommands(
      new InstantCommand(() -> climberSubsystem.climb()),
      new WaitUntilCommand(climberSubsystem::isUpperLimitReached),
      new InstantCommand(() -> climberSubsystem.stopClimberWinchMotor()),

      new WaitUntilCommand(OI::getXboxBackButton),

      new InstantCommand(() -> climberSubsystem.retract()),
      new WaitUntilCommand(climberSubsystem::isLowerLimitReached),
      new InstantCommand(() -> climberSubsystem.stopClimberWinchMotor()),

      new InstantCommand(() -> climberSubsystem.startClimberSwingMotor(-Constants.CLIMBER_SWING_ARM_MOTOR_SPEED)),
      new WaitCommand(1),
      new InstantCommand(() -> climberSubsystem.stopClimberSwingMotor())//,
      // new InstantCommand(() -> climberSubsystem.climb()),
      // new WaitCommand(1),
      // new InstantCommand(() -> climberSubsystem.stopClimberWinchMotor()),
      // new InstantCommand(() -> climberSubsystem.startClimberSwingMotor(Constants.CLIMBER_SWING_ARM_MOTOR_SPEED)),
      // new WaitCommand(1),
      // new InstantCommand(() -> climberSubsystem.stopClimberSwingMotor()),
      // new InstantCommand(() -> climberSubsystem.climb()),
      // new WaitUntilCommand(climberSubsystem::isUpperLimitReached),
      // new InstantCommand(() -> climberSubsystem.climb())
    );
  }
}