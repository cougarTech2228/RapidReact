package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class AutonomousCommand extends SequentialCommandGroup{
    private DrivebaseSubsystem m_drivebaseSubsystem;
    private ShooterSubsystem m_shooterSubsystem;
    private StorageSubsystem m_storageSubsystem;
    private AcquisitionSubsystem m_acquisitionSubsystem;
    private static boolean kIsAuto = false;

    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    public AutonomousCommand(DrivebaseSubsystem drivebaseSubsystem, ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem, AcquisitionSubsystem acquisitionSubsystem){
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_storageSubsystem = storageSubsystem;
        m_shooterSubsystem = shooterSubsystem;
        m_acquisitionSubsystem = acquisitionSubsystem;
        addRequirements(drivebaseSubsystem, shooterSubsystem, storageSubsystem, acquisitionSubsystem);

        executeCommand();
    }

    private void executeCommand(){
        addCommands(
            new InstantCommand(() -> {
                kIsAuto = true;
                m_drivebaseSubsystem.setMove(Constants.AUTO_MOVE_SPEED, 0);
                m_acquisitionSubsystem.setSpinnerMotor(Constants.ACQUIRER_SPINNER_SPEED);
                m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED);
                m_shooterSubsystem.setMotors(Constants.HIGH_SHOOT_SPEED);
            })
            , new WaitCommand(Constants.AUTO_MOVE_TIME)
            , new InstantCommand(() -> {m_drivebaseSubsystem.setMove(0, 0);})
            , new ParallelCommandGroup(
                new WaitCommand(.5)
                .andThen(
                    new InstantCommand(() -> {
                    m_acquisitionSubsystem.stopSpinnerMotor(); 
                    kIsAuto = false;
                    })
                    , new ShooterCommand(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem)
                )
            )
        );
    }
    public static boolean getIsAuto(){
        return kIsAuto;
    }
}
