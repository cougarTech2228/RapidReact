// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj.shuffleboard.WidgetType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.AutoCommand;
import frc.robot.commands.AutoCommand.AutoPosition;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.CargoVisionSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.util.ButtonManager;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private final static ShuffleboardTab m_rapidReact = Shuffleboard.getTab("Rapid React");

  // The robot's subsystems and commands are defined here...
  private static final CargoVisionSubsystem m_cargoVisionSubsystem = new CargoVisionSubsystem();
  private static final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem();
  private static final DrivebaseSubsystem m_drivebaseSubsystem = new DrivebaseSubsystem();
  private static final ShooterVisionSubsystem m_shooterVisionSubsystem = new ShooterVisionSubsystem();
  private static final StorageSubsystem m_storageSubsystem = new StorageSubsystem();
  private static final AcquisitionSubsystem m_acquisitionSubsystem = new AcquisitionSubsystem();
  private static final ClimberSubsystem m_climberSubsystem = new ClimberSubsystem();

  private static final ButtonManager m_buttonManager = 
  new ButtonManager(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, m_acquisitionSubsystem, m_shooterVisionSubsystem, m_climberSubsystem, m_cargoVisionSubsystem);

  private final static SendableChooser<String> m_teamColorChooser = new SendableChooser<>();
  private final static SendableChooser<Command> m_autoChooser = new SendableChooser<>();

  private static ComplexWidget m_autoWidget;

  private static ComplexWidget m_autoLevelWidget;
  private static SendableChooser<Boolean> m_levelChooser = new SendableChooser<>();

  private static ComplexWidget m_autoPositionWidget;
  private static SendableChooser<AutoPosition> m_positionChooser = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    m_buttonManager.configureButtonBindings();
    //m_shooterVisionSubsystem.setCameras(Constants.ACQUIRING_DRIVING_MODE);
    m_rapidReact.add("Team Color Chooser", m_teamColorChooser)
    .withSize(2, 1)
    .withPosition(0, 1);
    m_teamColorChooser.setDefaultOption("Red", Constants.RED_TEAM);
    m_teamColorChooser.addOption("Blue", Constants.BLUE_TEAM);

    // m_rapidReact.add("Auto Chooser", m_autoChooser)
    // .withSize(2, 1)
    // .withPosition(0, 2);
    // m_autoChooser.setDefaultOption("High Position 1/3", getAutoCommand(true, false));
    // m_autoChooser.addOption("Low Position 1/3", getAutoCommand(false, false));
    // m_autoChooser.addOption("High Position 2", getAutoCommand(true, true));
    // m_autoChooser.addOption("Low Position 2", getAutoCommand(false, true));

    //m_autoWidget = m_rapidReact.add("Auto Split Button whatever", m_autoChooser).withWidget(BuiltInWidgets.kSplitButtonChooser);
  
    m_levelChooser.setDefaultOption("\tHigh\t", true);
    m_levelChooser.addOption("\tLow\t", false);
    m_autoLevelWidget = m_rapidReact.add("Auto: Level", m_levelChooser)
    .withWidget(BuiltInWidgets.kSplitButtonChooser)
    .withSize(3, 1)
    .withPosition(0, 2);

    m_positionChooser.setDefaultOption("Position 1", AutoPosition.Position1);
    m_positionChooser.addOption("Position 2", AutoPosition.Position2);
    m_positionChooser.addOption("Position 3", AutoPosition.Position3);
    m_autoPositionWidget = m_rapidReact.add("Auto: Position", m_positionChooser)
    .withWidget(BuiltInWidgets.kSplitButtonChooser)
    .withSize(3, 1)
    .withPosition(0, 3);

  }

  public static String getTeamColor() {
    return m_teamColorChooser.getSelected();
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return getAutoCommand(m_levelChooser.getSelected(), m_positionChooser.getSelected());
  }

  /**
   * Subsystem Getters
   * 
   * Should only be used when a subsystem is needed in Robot.java, otherwise
   * the subsystem should be passed through a constructor. 
   */

  public static DrivebaseSubsystem getDrivebaseSubsystem() {
      return m_drivebaseSubsystem;
  }

  public static ShooterVisionSubsystem getShooterVisionSubsystem() {
      return m_shooterVisionSubsystem;
  }

  /**
   * Autonomous Command Getters
   */

  public AutoCommand getAutoCommand(boolean isHigh, AutoPosition position) {
    return new AutoCommand(m_shooterVisionSubsystem, m_drivebaseSubsystem, m_shooterSubsystem, m_storageSubsystem, m_acquisitionSubsystem, isHigh, position);
  }

  public static ShuffleboardTab getRapidReactTab() {
      return m_rapidReact;
  }
  
}
