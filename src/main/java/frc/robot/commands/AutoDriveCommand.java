package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.util.AutoDrive;

public class AutoDriveCommand extends CommandBase {

    private DrivebaseSubsystem m_drivebaseSubsystem;
    private double m_distanceCM = 0.0; // in centimeters
    private double m_fineSpeed = 0.0;
    private double m_coarseSpeed = 0.0;

    private Runnable m_autoDriveRunnable;
    private Thread m_autoDriveThread;

    /** Creates a new AutoDriveCommand. */
    public AutoDriveCommand(DrivebaseSubsystem drivebaseSubsystem, 
                            double distanceCM, 
                            double fineSpeed, 
                            double coarseSpeed) {
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_distanceCM = distanceCM;
        m_fineSpeed = fineSpeed;
        m_coarseSpeed = coarseSpeed;

        m_autoDriveRunnable = new AutoDrive(m_drivebaseSubsystem, 
                                            m_distanceCM,
                                            m_fineSpeed,
                                            m_coarseSpeed);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        System.out.println("Running auto drive command%");
        m_autoDriveThread = new Thread(m_autoDriveRunnable, "AutoDriveThread"); 
        m_autoDriveThread.start();  
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // do nothing ... code is running in a thread
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        System.out.println("ending auto drive command");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return (!m_autoDriveThread.isAlive());
    }
}
