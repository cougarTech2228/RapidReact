package frc.robot.util;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class ButtonManager {
  
    private static OI m_oi = new OI();

    private ShooterSubsystem m_shooterSubsystem;
    private StorageSubsystem m_storageSubsystem;
    private DrivebaseSubsystem m_drivebaseSubsystem;
    private AcquisitionSubsystem m_acquisitionSubsystem;
    private ShooterVisionSubsystem m_shooterVisionSubsystem;

    public ButtonManager(ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem,
     DrivebaseSubsystem drivebaseSubsystem, AcquisitionSubsystem acquisitionSubsystem, ShooterVisionSubsystem shooterVisionSubsystem) {

        m_shooterSubsystem = shooterSubsystem;
        m_storageSubsystem = storageSubsystem;
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_acquisitionSubsystem = acquisitionSubsystem;
        m_shooterVisionSubsystem = shooterVisionSubsystem;
    }

    public void configureButtonBindings() {
        Button rightTrigger = new Button(OI::getXboxRightTriggerPressed);
        Button leftTrigger = new Button(OI::getXboxLeftTriggerPressed);
        Button rightBumper = new Button(OI::getXboxRightBumper);
        Button leftBumper = new Button(OI::getXboxLeftBumper);

        Button aButton = new Button(OI::getXboxAButton);
        Button bButton = new Button(OI::getXboxBButton);
        Button xButton = new Button(OI::getXboxXButton);
        Button yButton = new Button(OI::getXboxYButton);

        Button dpadUp = new Button(OI::getXboxDpadUp);
        Button dpadDown = new Button(OI::getXboxDpadDown);
        Button dpadLeft = new Button(OI::getXboxDpadLeft);
        Button dpadRight = new Button(OI::getXboxDpadRight);
        Button startButton = new Button(OI::getXboxStartButton);
        Button backButton = new Button(OI::getXboxBackButton);

        dpadUp.whenPressed(new ShooterCommand(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, 1)); //high auto
        dpadLeft.whenPressed(new ShooterCommand(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, 3)); //high manual
        dpadDown.whenPressed(new ShooterCommand(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, 2)); //low auto
        dpadRight.whenPressed(new ShooterCommand(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, 4)); // low manual

        aButton.whenPressed(
            new ConditionalCommand(
                new InstantCommand(() -> {m_acquisitionSubsystem.stopSpinnerMotor(); m_storageSubsystem.stopMotors();}),
                new InstantCommand(() -> {m_acquisitionSubsystem.setSpinnerMotor(Constants.ACQUIRER_SPINNER_SPEED); m_storageSubsystem.setDriveMotor();}),
                m_acquisitionSubsystem :: isAcquiring));
        
        bButton.whenPressed(new AutonomousCommand(m_drivebaseSubsystem, m_shooterSubsystem, m_storageSubsystem, m_acquisitionSubsystem));

        xButton.whenPressed(new InstantCommand(() -> {
            if(m_drivebaseSubsystem.getDrivingMode() == Constants.SHOOTING_DRIVING_MODE){
                m_drivebaseSubsystem.setDrivingMode(Constants.ACQUIRING_DRIVING_MODE);
                m_shooterVisionSubsystem.setCameras(Constants.ACQUIRING_DRIVING_MODE);
            }    
            else{
                m_drivebaseSubsystem.setDrivingMode(Constants.SHOOTING_DRIVING_MODE);
                m_shooterVisionSubsystem.setCameras(Constants.SHOOTING_DRIVING_MODE);
            }
        }));
    }

}
