package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class AutoCommand extends SequentialCommandGroup{
    private static boolean m_isInAuto = false;

    // Read left to right from corresponding driver station
    public enum AutoPosition {
        Position1,
        Position2,
        Position3
    }

    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    public AutoCommand(ShooterVisionSubsystem shooterVisionSubsystem, DrivebaseSubsystem drivebaseSubsystem, 
                                ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem, 
                                AcquisitionSubsystem acquisitionSubsystem, boolean isHigh, AutoPosition position){
        
        addCommands(
            new InstantCommand(() -> {
                m_isInAuto = true;
                acquisitionSubsystem.setSpinnerMotor(Constants.ACQUIRER_SPINNER_SPEED);
                storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED * 2);
                
            }),
            new DriveCommand(Constants.TO_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, drivebaseSubsystem),
            new WaitCommand(0.5), 
            new InstantCommand(() -> {
                acquisitionSubsystem.setSpinnerMotor(0);
                storageSubsystem.stopMotors();
                
            }),
            new ConditionalCommand(
                // High shoot
                new ShooterCommand(shooterVisionSubsystem, shooterSubsystem, storageSubsystem, drivebaseSubsystem, true, true), 
                // Low Shoot
                new SequentialCommandGroup(
                    new DriveCommand(-Constants.TO_HUB_FROM_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, drivebaseSubsystem),
                    new ConditionalCommand(
                        // Position 2 requires the bot to turn a bit towards the hub
                        new SequentialCommandGroup(
                            new InstantCommand(() -> drivebaseSubsystem.setMove(0, 0, -0.1)),
                            new WaitCommand(2),
                            new InstantCommand(() -> drivebaseSubsystem.setMove(0, 0, 0))
                        ), 
                        // If its not position 2 then nothing else changes
                        new InstantCommand(), 
                        () -> (position == AutoPosition.Position2)
                    ),
                    new ShooterCommand(shooterVisionSubsystem, shooterSubsystem, storageSubsystem, drivebaseSubsystem, false, false),
                    new DriveCommand(Constants.TO_HUB_FROM_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, drivebaseSubsystem)
                ), 
                () -> isHigh
            )
        );
        
    }

    @Override
    public void end(boolean interrupted) {
        m_isInAuto = false;
    }

    
    public static boolean isInAuto(){
        return m_isInAuto;
    }
}
