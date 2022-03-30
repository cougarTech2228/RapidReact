package frc.robot.util;

import java.time.Instant;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.commands.AutoAngleTurnCommand;
import frc.robot.commands.AutoDriveCommand;
import frc.robot.commands.FixJamCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.ShooterCommand.ShotType;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.CargoVisionSubsystem;
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
    private CargoVisionSubsystem m_cargoVisionSubsystem;
    public static boolean m_hasRetractedArms;

    public ButtonManager(ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem,
                         DrivebaseSubsystem drivebaseSubsystem, AcquisitionSubsystem acquisitionSubsystem, 
                         ShooterVisionSubsystem shooterVisionSubsystem, ClimberSubsystem climberSubsystem,
                         CargoVisionSubsystem cargoVisionSubsystem) {

        m_shooterSubsystem = shooterSubsystem;
        m_storageSubsystem = storageSubsystem;
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_acquisitionSubsystem = acquisitionSubsystem;
        m_shooterVisionSubsystem = shooterVisionSubsystem;
        m_climberSubsystem = climberSubsystem;
        m_cargoVisionSubsystem = cargoVisionSubsystem;
        m_hasRetractedArms = false;
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

        dpadUp.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, ShotType.HIGH)); // high auto
        dpadDown.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, ShotType.LOW)); // low
        dpadLeft.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, ShotType.HIGH)); // high manual
        dpadRight.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, ShotType.SPIT)); // spit

        aButton.whenPressed(
            new ConditionalCommand(
                new InstantCommand(() -> m_acquisitionSubsystem.retractAcquirer()),
                new InstantCommand(() -> m_acquisitionSubsystem.deployAcquirer()),
                m_acquisitionSubsystem :: isAcquirerDeployed
            )
        );

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
                m_acquisitionSubsystem :: isAcquiring
            )
        );
        
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

        xButton.whenHeld(new FixJamCommand(m_acquisitionSubsystem, m_storageSubsystem, m_shooterSubsystem));

        //startButton.whenPressed(new ClimbSequenceCommand(m_climberSubsystem));

        leftTrigger.whenPressed(new InstantCommand(() -> {
            if(!m_hasRetractedArms) {

            }
            m_climberSubsystem.climb();
        }));

        leftTrigger.whenPressed(new ConditionalCommand(
            new InstantCommand(() -> m_climberSubsystem.climb()), 
            new SequentialCommandGroup(
                new InstantCommand(() -> {
                    m_climberSubsystem.actuateLeftDown();
                    m_climberSubsystem.actuateRightDown();
                    OI.setXboxRumbleSpeed(0.5);
                }),
                new WaitCommand(2.2),
                new InstantCommand(() -> {
                    m_climberSubsystem.stopLeftActuator();
                    m_climberSubsystem.stopRightActuator();
                    OI.setXboxRumbleStop();
                    m_hasRetractedArms = true;
                })
            ), 
            () -> m_hasRetractedArms
        ));

        leftTrigger.whenReleased(new InstantCommand(() -> m_climberSubsystem.stopClimberWinchMotor()));

        rightTrigger.whenPressed(new InstantCommand(() -> m_climberSubsystem.retract()));
        rightTrigger.whenReleased(new InstantCommand(() -> m_climberSubsystem.stopClimberWinchMotor()));

                                                              
        yButton.whenPressed(new AutoAngleTurnCommand(m_drivebaseSubsystem, m_shooterVisionSubsystem));   
        
        backButton.whenPressed(new SequentialCommandGroup(
            new InstantCommand(() -> {
                m_climberSubsystem.actuateLeftDown();
                m_climberSubsystem.actuateRightDown();
                OI.setXboxRumbleSpeed(0.5);
            }),
            new WaitCommand(2.2),
            new InstantCommand(() -> {
                m_climberSubsystem.stopLeftActuator();
                m_climberSubsystem.stopRightActuator();
                OI.setXboxRumbleStop();
            })
        ));
    }
}
