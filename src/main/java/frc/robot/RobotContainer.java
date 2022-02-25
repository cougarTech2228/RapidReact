// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.AutoCommand;
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

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    m_buttonManager.configureButtonBindings();
    //m_shooterVisionSubsystem.setCameras(Constants.ACQUIRING_DRIVING_MODE);
    SmartDashboard.putData("Team Color Chooser", m_teamColorChooser);
    m_teamColorChooser.setDefaultOption("Red", Constants.RED_TEAM);
    m_teamColorChooser.addOption("Blue", Constants.BLUE_TEAM);

    SmartDashboard.putData("Auto Chooser", m_autoChooser);
    m_autoChooser.setDefaultOption("High Auto", getAutoCommand(true));
    m_autoChooser.addOption("Low Auto", getAutoCommand(false));
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
    return m_autoChooser.getSelected();
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

  public AutoCommand getAutoCommand(boolean isHigh) {
    return new AutoCommand(m_shooterVisionSubsystem, m_drivebaseSubsystem, m_shooterSubsystem, m_storageSubsystem, m_acquisitionSubsystem, isHigh);
  }
  
}
