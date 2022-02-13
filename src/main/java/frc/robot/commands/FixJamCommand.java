// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class FixJamCommand extends CommandBase {

    private AcquisitionSubsystem m_acquisitionSubsystem;
    private StorageSubsystem m_storageSubsystem;
    private ShooterSubsystem m_shooterSubsystem;

    public FixJamCommand(AcquisitionSubsystem acquisitionSubsystem, StorageSubsystem storageSubsystem, ShooterSubsystem shooterSubsystem) {
        m_acquisitionSubsystem = acquisitionSubsystem;
        m_storageSubsystem = storageSubsystem;
        m_shooterSubsystem = shooterSubsystem;
        
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_acquisitionSubsystem.setSpinnerMotor(-Constants.ACQUIRER_SPINNER_SPEED);
        m_storageSubsystem.setConveyorMotor(-Constants.STORAGE_CONVEYOR_SPEED);
        m_storageSubsystem.setFeedMotor(-Constants.SHOOTER_FEED_SPEED);
        m_shooterSubsystem.setMotors(-Constants.REVERSE_SHOOT_SPEED);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_acquisitionSubsystem.stopSpinnerMotor();
        m_storageSubsystem.stopMotors();
        m_shooterSubsystem.stopMotors();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
