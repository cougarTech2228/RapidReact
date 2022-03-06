package frc.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.commands.AcquiringAssistanceCommand;
import frc.robot.commands.ClimbSequenceCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.FixJamCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.TurnCommand;
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

        dpadUp.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, ShotType.HIGH_AUTO)); // high auto
        dpadDown.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, ShotType.LOW)); // low
        dpadLeft.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, ShotType.HIGH_MANUAL)); // high manual
        dpadRight.toggleWhenPressed(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, ShotType.SPIT)); // spit

        //aButton.whenPressed(() -> m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED));
        //aButton.whenReleased(() -> m_storageSubsystem.setConveyorMotor(0));

        //yButton.whenPressed(() -> m_shooterSubsystem.setMotors(Constants.LOW_SHOOT_SPEED));
        //yButton.whenReleased(() -> m_shooterSubsystem.setMotors(0));

        //startButton.whenPressed(() -> m_storageSubsystem.setFeedMotor(Constants.SHOOTER_FEED_SPEED));
        //startButton.whenReleased(() -> m_storageSubsystem.setFeedMotor(0));

        //startButton.whenPressed(new TurnCommand(m_drivebaseSubsystem, 90, 0.1));
        //startButton.whenPressed(new DriveCommand(100, 0.2, m_drivebaseSubsystem));

        //startButton.whenPressed(new spinWhileCommand(m_drivebaseSubsystem, 0.15, OI::getXboxAButton));
            
        leftBumper.toggleWhenPressed(new AcquiringAssistanceCommand(m_cargoVisionSubsystem, m_drivebaseSubsystem, m_acquisitionSubsystem, m_storageSubsystem, false));

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

        xButton.whenHeld(new FixJamCommand(m_acquisitionSubsystem, m_storageSubsystem, m_shooterSubsystem));

        startButton.whenPressed(new ClimbSequenceCommand(m_climberSubsystem));

        SmartDashboard.putData("Climb",new InstantCommand(() -> m_climberSubsystem.climb()));
        SmartDashboard.putData("Retract",new InstantCommand(() -> m_climberSubsystem.retract()));
        SmartDashboard.putData("stop Climber Winch", new InstantCommand(() -> m_climberSubsystem.stopClimberWinchMotor()));

        SmartDashboard.putData("Hook +",new InstantCommand(() -> m_climberSubsystem.setHooks(-Constants.HOOK_SPEED)));
        SmartDashboard.putData("Hook -",new InstantCommand(() -> m_climberSubsystem.setHooks(Constants.HOOK_SPEED)));
        SmartDashboard.putData("Stop Hook",new InstantCommand(() -> m_climberSubsystem.stopHook()));

        SmartDashboard.putData("Climber Arm Swing towards limit",new InstantCommand(() -> m_climberSubsystem.startClimberSwingMotor(Constants.CLIMBER_SWING_ARM_MOTOR_SPEED)));
        SmartDashboard.putData("Climber Arm Swing away limit",new InstantCommand(() -> m_climberSubsystem.startClimberSwingMotor(-Constants.CLIMBER_SWING_ARM_MOTOR_SPEED)));
        SmartDashboard.putData("Stop Climber Arm",new InstantCommand(() -> m_climberSubsystem.stopClimberSwingMotor()));

        SmartDashboard.putData(
            "Swing Arm Test", 
            new SequentialCommandGroup(
                new InstantCommand(() -> m_climberSubsystem.startClimberSwingMotor(-m_climberSubsystem.getSwingSpeed())),
                new WaitCommand(m_climberSubsystem.getWaitTime()),
                new InstantCommand(() -> m_climberSubsystem.stopClimberSwingMotor())  
            )
        );
    }

}
