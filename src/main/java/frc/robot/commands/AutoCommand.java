package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class AutoCommand extends SequentialCommandGroup{
    private ShooterVisionSubsystem m_shooterVisionSubsystem;
    private DrivebaseSubsystem m_drivebaseSubsystem;
    private ShooterSubsystem m_shooterSubsystem;
    private StorageSubsystem m_storageSubsystem;
    private AcquisitionSubsystem m_acquisitionSubsystem;
    private static boolean isInAuto = false;

    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    public AutoCommand(ShooterVisionSubsystem shooterVisionSubsystem, DrivebaseSubsystem drivebaseSubsystem, 
                                ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem, 
                                AcquisitionSubsystem acquisitionSubsystem, boolean isHigh){
        m_shooterVisionSubsystem = shooterVisionSubsystem;
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_storageSubsystem = storageSubsystem;
        m_shooterSubsystem = shooterSubsystem;
        m_acquisitionSubsystem = acquisitionSubsystem;
        
        addCommands(
            new InstantCommand(() -> {
                isInAuto = true;
                m_acquisitionSubsystem.setSpinnerMotor(Constants.ACQUIRER_SPINNER_SPEED);
                m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED * 2);
                
            }),
            new DriveCommand(Constants.TO_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, m_drivebaseSubsystem),
            new WaitCommand(0.5), 
            new InstantCommand(() -> {
                m_acquisitionSubsystem.setSpinnerMotor(0);
                m_storageSubsystem.stopMotors();
                
            }),
            new ConditionalCommand(
                // High shoot
                new ShooterCommand(shooterVisionSubsystem, shooterSubsystem, storageSubsystem, drivebaseSubsystem, true, true), 
                // Low Shoot
                new SequentialCommandGroup(
                    new DriveCommand(-Constants.TO_HUB_FROM_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, m_drivebaseSubsystem),
                    new ShooterCommand(shooterVisionSubsystem, shooterSubsystem, storageSubsystem, drivebaseSubsystem, false, false),
                    new DriveCommand(Constants.TO_HUB_FROM_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, m_drivebaseSubsystem)
                ), 
                () -> isHigh
            )
        );
        
    }

    @Override
    public void end(boolean interrupted) {
        isInAuto = false;
    }

    
    public static boolean isInAuto(){
        return isInAuto;
    }
}
