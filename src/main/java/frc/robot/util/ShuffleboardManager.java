package frc.robot.util;

import java.time.Instant;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.commands.HomeClimberActuators;
import frc.robot.commands.AutoCommand.AutoPosition;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.CargoVisionSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class ShuffleboardManager {

    private ShooterSubsystem m_shooterSubsystem;
    private StorageSubsystem m_storageSubsystem;
    private DrivebaseSubsystem m_drivebaseSubsystem;
    private AcquisitionSubsystem m_acquisitionSubsystem;
    private ShooterVisionSubsystem m_shooterVisionSubsystem;
    private ClimberSubsystem m_climberSubsystem;
    private CargoVisionSubsystem m_cargoVisionSubsystem;

    private static SendableChooser<Boolean> m_levelChooser = new SendableChooser<>();
    private static SendableChooser<AutoPosition> m_positionChooser = new SendableChooser<>();
    private static SendableChooser<Boolean> m_goToTerminalChooser = new SendableChooser<>();
    private static SendableChooser<Boolean> m_ballSearchChooser = new SendableChooser<>();

    private static ShuffleboardTab m_rapidReact;
    private static ShuffleboardTab m_autoConfig;
    private static ShuffleboardTab m_debug;

    public ShuffleboardManager(ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem,
                         DrivebaseSubsystem drivebaseSubsystem, AcquisitionSubsystem acquisitionSubsystem, 
                         ShooterVisionSubsystem shooterVisionSubsystem, ClimberSubsystem climberSubsystem,
                         CargoVisionSubsystem cargoVisionSubsystem, ShuffleboardTab rapidReact,
                         ShuffleboardTab autoConfig, ShuffleboardTab debug) {

        m_shooterSubsystem = shooterSubsystem;
        m_storageSubsystem = storageSubsystem;
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_acquisitionSubsystem = acquisitionSubsystem;
        m_shooterVisionSubsystem = shooterVisionSubsystem;
        m_climberSubsystem = climberSubsystem;
        m_cargoVisionSubsystem = cargoVisionSubsystem;

        m_rapidReact = rapidReact;
        m_autoConfig = autoConfig;
        m_debug = debug;
    }

    public void configureShuffleboard() {
        m_levelChooser.setDefaultOption("High", true);
        m_levelChooser.addOption("Low", false);
        m_autoConfig.add("Auto: Level", m_levelChooser)
        .withWidget(BuiltInWidgets.kSplitButtonChooser)
        .withSize(3, 1)
        .withPosition(0, 0);

        m_positionChooser.setDefaultOption("Position 1", AutoPosition.Position1);
        m_positionChooser.addOption("Position 2", AutoPosition.Position2);
        m_positionChooser.addOption("Position 3", AutoPosition.Position3);
        m_autoConfig.add("Auto: Position", m_positionChooser)
        .withWidget(BuiltInWidgets.kSplitButtonChooser)
        .withSize(3, 1)
        .withPosition(0, 1);

        m_goToTerminalChooser.setDefaultOption("Don't go to terminal", false);
        m_goToTerminalChooser.addOption("Go to terminal", true);
        m_autoConfig.add("Auto: Terminal", m_goToTerminalChooser)
        .withWidget(BuiltInWidgets.kSplitButtonChooser)
        .withSize(3, 1)
        .withPosition(0, 2);

        m_ballSearchChooser.setDefaultOption("Search for ball", true);
        m_ballSearchChooser.addOption("Don't search for ball", false);
        m_autoConfig.add("Auto: Ball Hunting", m_ballSearchChooser)
        .withWidget(BuiltInWidgets.kSplitButtonChooser)
        .withSize(3, 1)
        .withPosition(0, 3);

        m_rapidReact.addBoolean("Is In Acquiring Mode", () -> m_drivebaseSubsystem.getDrivingMode() == Constants.ACQUIRING_DRIVING_MODE);
        m_rapidReact.addBoolean("Is Acquirer Spinning", m_acquisitionSubsystem::isAcquiring);

        m_debug.add("Climb", new InstantCommand(() -> m_climberSubsystem.climb()));
        m_debug.add("Retract", new InstantCommand(() -> m_climberSubsystem.retract()));
        m_debug.add("stop Climber Winch", new InstantCommand(() -> m_climberSubsystem.stopClimberWinchMotor()));

        m_debug.add("Hook +", new InstantCommand(() -> m_climberSubsystem.setHooks(-Constants.HOOK_SPEED)));
        m_debug.add("Hook -", new InstantCommand(() -> m_climberSubsystem.setHooks(Constants.HOOK_SPEED)));
        m_debug.add("Stop Hook", new InstantCommand(() -> m_climberSubsystem.stopHook()));

        m_debug.add("Left Climber Actuator Up", new InstantCommand(() -> m_climberSubsystem.actuateLeftUp()));
        m_debug.add("Left Climber Actuator Down", new InstantCommand(() -> m_climberSubsystem.actuateLeftDown()));
        m_debug.add("Left Climber Actuator Stop", new InstantCommand(() -> m_climberSubsystem.stopLeftActuator()));

        m_debug.add("Right Climber Actuator Up", new InstantCommand(() -> m_climberSubsystem.actuateRightUp()));
        m_debug.add("Right Climber Actuator Down", new InstantCommand(() -> m_climberSubsystem.actuateRightDown()));
        m_debug.add("Right Climber Actuator Stop", new InstantCommand(() -> m_climberSubsystem.stopRightActuator()));

        m_debug.add("Both Actuators Up", new InstantCommand(() -> {
            m_climberSubsystem.actuateLeftUp();
            m_climberSubsystem.actuateRightUp();
        }));

        m_debug.add("Both Actuators Down", new InstantCommand(() -> {
            m_climberSubsystem.actuateLeftDown();
            m_climberSubsystem.actuateRightDown();
        }));

        m_debug.add("Both Actuators Stop", new InstantCommand(() -> {
            m_climberSubsystem.stopLeftActuator();
            m_climberSubsystem.stopRightActuator();
        }));

        m_debug.add("Home Actuators Command", new HomeClimberActuators(m_climberSubsystem));
    
        m_debug.add("Run Conveyor", new InstantCommand(() -> m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED)));
        m_debug.add("Stop Conveyor", new InstantCommand(() -> m_storageSubsystem.setConveyorMotor(0)));

        m_debug.add("Run Acquirer Spinner", new InstantCommand(() -> m_acquisitionSubsystem.setSpinnerMotor(Constants.ACQUIRER_SPINNER_SPEED)));
        m_debug.add("Stop Acquirer Spinner", new InstantCommand(() -> m_acquisitionSubsystem.stopSpinnerMotor()));

        m_debug.add("Run Feed Motor", new InstantCommand(() -> m_storageSubsystem.setFeedMotor(Constants.SHOOTER_FEED_SPEED)));
        m_debug.add("Stop Feed Motor", new InstantCommand(() -> m_storageSubsystem.setFeedMotor(0)));

        m_debug.add("Start Shooter Motor High", new InstantCommand(() -> m_shooterSubsystem.setMotors(Constants.HIGH_SHOOT_SPEED)));
        m_debug.add("Start Shooter Motor Low", new InstantCommand(() -> m_shooterSubsystem.setMotors(Constants.LOW_SHOOT_SPEED)));
        m_debug.add("Stop Shooter Motor", new InstantCommand(() -> m_shooterSubsystem.stopMotors()));
    }

    public boolean getAutoLevel() {
        return m_levelChooser.getSelected();
    }

    public AutoPosition getAutoPosition() {
        return m_positionChooser.getSelected();
    }

    public boolean getAutoTerminal() {
        return m_goToTerminalChooser.getSelected();
    }

    public boolean getAutoSearchForBall() {
        return m_ballSearchChooser.getSelected();
    }

    
}
