package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;
import frc.robot.util.AutoAngleTurn;

public class AutoAngleTurnCommand extends CommandBase {

    private DrivebaseSubsystem m_drivebaseSubsystem;
    private ShooterVisionSubsystem m_shooterVisionSubsystem;
    private double m_targetAngle = 0.0;
    private Runnable m_autoTurnRunnable;
    private Thread m_autoAngleTurnThread;

    /** Creates a new AutoAngleTurnCommand. */
    public AutoAngleTurnCommand(DrivebaseSubsystem drivebaseSubsystem, 
                                double targetAngle) {
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_targetAngle = targetAngle; // Maximum is 180.0

        m_autoTurnRunnable = new AutoAngleTurn(m_drivebaseSubsystem, m_targetAngle);
    }

    public AutoAngleTurnCommand(DrivebaseSubsystem drivebaseSubsystem, 
                                ShooterVisionSubsystem shooterVisionSubsystem) {
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_shooterVisionSubsystem = shooterVisionSubsystem;

        m_autoTurnRunnable = new AutoAngleTurn(m_drivebaseSubsystem, m_shooterVisionSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_autoAngleTurnThread = new Thread(m_autoTurnRunnable, "AutoAngleTurnThread"); 
        m_autoAngleTurnThread.start();  
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // do nothing ... code is running in a thread
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        // do nothing
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return (!m_autoAngleTurnThread.isAlive());
    }
}
