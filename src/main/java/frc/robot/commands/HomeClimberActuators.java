// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.util.ButtonManager;

public class HomeClimberActuators extends CommandBase {

    private ClimberSubsystem m_climberSubsystem;
    private boolean m_isLeftHomed;
    private boolean m_isRightHomed;

    /** Creates a new HomeClimberActuators. */
    public HomeClimberActuators(ClimberSubsystem climberSubsystem) {
        m_climberSubsystem = climberSubsystem;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_climberSubsystem.actuateLeftUp();
        m_climberSubsystem.actuateRightUp();

        m_isLeftHomed = false;
        m_isRightHomed = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if(m_climberSubsystem.isLeftActuatorLimitHit()) {
            m_climberSubsystem.stopLeftActuator();
            m_isLeftHomed = true;
        }

        if(m_climberSubsystem.isRightActuatorLimitHit()) {
            m_climberSubsystem.stopRightActuator();
            m_isRightHomed = true;
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        ButtonManager.m_hasRetractedArms = false;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_isLeftHomed && m_isRightHomed;
    }
}
