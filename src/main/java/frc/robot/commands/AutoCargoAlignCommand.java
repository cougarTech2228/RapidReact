package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.CargoVisionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.util.AutoCargoAlign;

public class AutoCargoAlignCommand extends CommandBase {

    private CargoVisionSubsystem m_cargoVisionSubsystem;
    private DrivebaseSubsystem m_drivebaseSubsystem;
    private AcquisitionSubsystem m_acquisitionSubsystem;
    private StorageSubsystem m_storageSubsystem;
    private boolean m_useColorSensorAsCondition;
    private Thread m_autoCargoAlignThread;

    /** Creates a new AutoCargoAlignCommand. */
    public AutoCargoAlignCommand(CargoVisionSubsystem cargoVisionSubsystem, DrivebaseSubsystem drivebaseSubsystem,
            AcquisitionSubsystem acquisitionSubsystem, StorageSubsystem storageSubsystem,
            boolean useColorSensorAsCondition) {
        m_cargoVisionSubsystem = cargoVisionSubsystem;
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_acquisitionSubsystem = acquisitionSubsystem;
        m_storageSubsystem = storageSubsystem;
        m_useColorSensorAsCondition = useColorSensorAsCondition;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Runnable autoCargoAlignRunnable = new AutoCargoAlign(m_cargoVisionSubsystem, 
                                                             m_drivebaseSubsystem, 
                                                             m_acquisitionSubsystem, 
                                                             m_storageSubsystem, 
                                                             m_useColorSensorAsCondition);
        m_autoCargoAlignThread = new Thread(autoCargoAlignRunnable, "AutoCargoAlignThread"); 
        m_autoCargoAlignThread.start();
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
        return (!m_autoCargoAlignThread.isAlive());
    }
}
