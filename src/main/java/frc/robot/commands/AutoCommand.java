package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.commands.ShooterCommand.ShotType;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.CargoVisionSubsystem;
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
                                AcquisitionSubsystem acquisitionSubsystem, CargoVisionSubsystem cargoVisionSubsystem,
                                boolean isHigh, AutoPosition position, boolean shouldBeOutsideTarmac, boolean searchForBall) {


        addCommands(
            new InstantCommand(() -> {
                m_isInAuto = true;
                acquisitionSubsystem.deployAcquirer();
                acquisitionSubsystem.setSpinnerMotor(Constants.ACQUIRER_SPINNER_SPEED);
                storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED * 2);
                
            }),
            new WaitCommand(0.5),
            new DriveCommand(Constants.TO_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, drivebaseSubsystem),
            new WaitCommand(0.5), 
            new InstantCommand(() -> {
                acquisitionSubsystem.setSpinnerMotor(0);
                storageSubsystem.stopMotors();
            })
        );

        if(isHigh) {
            addCommands(new ShooterCommand(shooterVisionSubsystem, shooterSubsystem, storageSubsystem, drivebaseSubsystem, ShotType.HIGH_AUTO));
        } else {
            addCommands(new DriveCommand(-Constants.TO_HUB_FROM_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, drivebaseSubsystem));
            if(position == AutoPosition.Position2) {
                addCommands(new TurnCommand(drivebaseSubsystem, 30, 0.1));
            }
            addCommands(
                new ShooterCommand(shooterVisionSubsystem, shooterSubsystem, storageSubsystem, drivebaseSubsystem, ShotType.LOW)
            );
        }
        
        // If we want the bot to be outside the tarmac, and we did a low shoot, move outside tarmac
        if(shouldBeOutsideTarmac && !isHigh) {
            addCommands(new DriveCommand(Constants.TO_HUB_FROM_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, drivebaseSubsystem));
        }

        // If we want the bot to be inside the tarmac, and we did a high shoot, move inside tarmac.
        if(!shouldBeOutsideTarmac && isHigh) {
            addCommands(new DriveCommand(-Constants.TO_HUB_FROM_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, drivebaseSubsystem));
        }

        if(searchForBall && shouldBeOutsideTarmac) {
            
            double speed;

            // Ensures the bot would spin away from the center line to avoid penalty
            if(position == AutoPosition.Position1) {
                speed = -0.15;
            } else {
                speed = 0.15;
            }

            addCommands(
                new SpinWhileCommand(drivebaseSubsystem, speed, (() -> cargoVisionSubsystem.getBestBall() == null)),
                new AcquiringAssistanceCommand(cargoVisionSubsystem, drivebaseSubsystem, acquisitionSubsystem, storageSubsystem),
                new SpinWhileCommand(drivebaseSubsystem, -speed, (() -> Double.isNaN(ShooterVisionSubsystem.getDeviationFromCenter())))
            );
            if(isHigh){
                addCommands(new ShooterCommand(shooterVisionSubsystem, shooterSubsystem, storageSubsystem, drivebaseSubsystem, ShotType.HIGH_AUTO));
            }
            else{
                addCommands(new ShooterCommand(shooterVisionSubsystem, shooterSubsystem, storageSubsystem, drivebaseSubsystem, ShotType.LOW));
            }
        }
        
    }

    @Override
    public void end(boolean interrupted) {
        m_isInAuto = false;
    }

    
    public static boolean isInAuto(){
        return m_isInAuto;
    }
}
