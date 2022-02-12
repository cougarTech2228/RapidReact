package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.ser.std.MapProperty;

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
    DrivebaseSubsystem m_drivebaseSubsystem;
    ShooterSubsystem m_shooterSubsystem;
    StorageSubsystem m_storageSubsystem;
    AcquisitionSubsystem m_acquisitionSubsystem;
    static boolean kIsAuto = false;
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
            new InstantCommand(() -> {kIsAuto = true;
                m_drivebaseSubsystem.setMove(Constants.AUTO_MOVE_SPEED, 0);
            m_acquisitionSubsystem.setSpinnerMotor(Constants.ACQUIRER_SPINNER_SPEED);
        m_storageSubsystem.setDriveMotor();
        m_shooterSubsystem.setMotors(Constants.HIGH_SHOOT_SPEED);})
            , new WaitCommand(Constants.AUTO_MOVE_TIME)
            , new InstantCommand(() -> {m_drivebaseSubsystem.setMove(0, 0);})
            , new ParallelCommandGroup(
            new WaitCommand(.5).andThen(new InstantCommand(() -> {m_acquisitionSubsystem.stopSpinnerMotor(); kIsAuto = false;}))
            , new ShooterCommand(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, 5)
            )
        );
    }
    public static boolean getIsAuto(){
        return kIsAuto;
    }
}
