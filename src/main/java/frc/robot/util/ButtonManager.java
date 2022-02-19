package frc.robot.util;

import javax.swing.UIDefaults.ActiveValue;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.commands.AlignToTargetCommand;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.FixJamCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
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
    private ClimberSubsystem m_climberSubsystem;
    private SequentialCommandGroup m_rumbleCommand;

    private boolean m_isAutoAlignment = false;

    public ButtonManager(ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem,
                         DrivebaseSubsystem drivebaseSubsystem, AcquisitionSubsystem acquisitionSubsystem, 
                         ShooterVisionSubsystem shooterVisionSubsystem, ClimberSubsystem climberSubsystem) {

        m_shooterSubsystem = shooterSubsystem;
        m_storageSubsystem = storageSubsystem;
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_acquisitionSubsystem = acquisitionSubsystem;
        m_shooterVisionSubsystem = shooterVisionSubsystem;
        m_climberSubsystem = climberSubsystem;
        m_rumbleCommand = new SequentialCommandGroup(
            new InstantCommand(() -> OI.setXboxRumbleSpeed(.5)),
            new WaitCommand(1),
            new InstantCommand(() -> OI.setXboxRumbleStop()));

;
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

        dpadUp.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, true, true)); // high auto
        dpadDown.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, false, true)); // low auto
        dpadLeft.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, true, false)); // high manual
        dpadRight.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, false, false)); // low manual

        aButton.whenPressed(() -> m_storageSubsystem.setFeedMotor(Constants.SHOOTER_FEED_SPEED));
        aButton.whenReleased(() -> m_storageSubsystem.setFeedMotor(0));

        yButton.whenPressed(() -> m_shooterSubsystem.setMotors(Constants.LOW_SHOOT_SPEED));
        yButton.whenReleased(() -> m_shooterSubsystem.setMotors(0));


        // dpadUp.whenPressed(
        //     new ConditionalCommand(
        //         new ShooterCommand(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, true, true), 
        //         new ShooterCommand(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, true, false), 
        //         () -> m_isAutoAlignment
        //     )
        // );

        // dpadDown.whenPressed(
        //     new ConditionalCommand(
        //         new ShooterCommand(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, false, true), 
        //         new ShooterCommand(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, false, false), 
        //         () -> m_isAutoAlignment
        //     )
        // );


        rightBumper.whenPressed(
            new ConditionalCommand(
                new InstantCommand(() -> {
                    m_acquisitionSubsystem.stopSpinnerMotor(); 
                    m_storageSubsystem.stopMotors();
                }),
                new InstantCommand(() -> {
                    m_acquisitionSubsystem.setSpinnerMotor(Constants.ACQUIRER_SPINNER_SPEED); 
                    m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED);
                }),
                m_acquisitionSubsystem :: isAcquiring));
        
        // yButton.whenPressed(new InstantCommand(() -> {
        //     if(m_isAutoAlignment){
        //         m_isAutoAlignment = false;
        //         SmartDashboard.putBoolean("Auto Alignment", m_isAutoAlignment);
        //     }
        //     else{
        //         m_isAutoAlignment = true;
        //         SmartDashboard.putBoolean("Auto Alignment", m_isAutoAlignment);
        //     }
        //     m_rumbleCommand.schedule();

        // }));
        bButton.whenPressed(new InstantCommand(() -> {
            if(m_drivebaseSubsystem.getDrivingMode() == Constants.SHOOTING_DRIVING_MODE){
                m_drivebaseSubsystem.setDrivingMode(Constants.ACQUIRING_DRIVING_MODE);
                m_shooterVisionSubsystem.setCameras(Constants.ACQUIRING_DRIVING_MODE);
                SmartDashboard.putBoolean("Is in shooter mode", false);
            }    
            else{
                m_drivebaseSubsystem.setDrivingMode(Constants.SHOOTING_DRIVING_MODE);
                m_shooterVisionSubsystem.setCameras(Constants.SHOOTING_DRIVING_MODE);
                SmartDashboard.putBoolean("Is in shooter mode", true);
            }
        }));

        rightTrigger.whenPressed(new InstantCommand(() -> 
        {
            m_climberSubsystem.setClimberMotor(Constants.CLIMBER_MOTOR_SPEED);
            //OI.setXboxRumbleSpeed(.5);
        }));
        rightTrigger.whenReleased(new InstantCommand(() -> {
            m_climberSubsystem.setClimberMotor(0);
            //OI.setXboxRumbleStop();       
        }));
        leftTrigger.whenPressed(new InstantCommand(() -> 
        {
            m_climberSubsystem.setClimberMotor(-Constants.CLIMBER_MOTOR_SPEED);
            //OI.setXboxRumbleSpeed(.5);
        }));
        leftTrigger.whenReleased(new InstantCommand(() -> {
            m_climberSubsystem.setClimberMotor(0);
            //OI.setXboxRumbleStop();      
        }));

        backButton.whenPressed(() -> m_climberSubsystem.setClimberMotor(0));

        xButton.whenHeld(new FixJamCommand(m_acquisitionSubsystem, m_storageSubsystem, m_shooterSubsystem));
    }

}
