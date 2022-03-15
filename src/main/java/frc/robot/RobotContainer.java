// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.commands.AutoCommand;
import frc.robot.commands.AutoCommand.AutoPosition;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.CargoVisionSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.util.ButtonManager;
import frc.robot.util.ShuffleboardManager;
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
  private final static ShuffleboardTab m_autoConfig = Shuffleboard.getTab("Auto Config");
  private final static ShuffleboardTab m_debug = Shuffleboard.getTab("Debug");

  // The robot's subsystems and commands are defined here...
  private static final CargoVisionSubsystem m_cargoVisionSubsystem = new CargoVisionSubsystem();
  private static final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem();
  private static final DrivebaseSubsystem m_drivebaseSubsystem = new DrivebaseSubsystem();
  private static final ShooterVisionSubsystem m_shooterVisionSubsystem = new ShooterVisionSubsystem();
  private static final StorageSubsystem m_storageSubsystem = new StorageSubsystem();
  private static final AcquisitionSubsystem m_acquisitionSubsystem = new AcquisitionSubsystem();
  private static final ClimberSubsystem m_climberSubsystem = new ClimberSubsystem();
  //private static final VoltageMonitorSubystem m_voltageMonitorSubsystem = 
  //new VoltageMonitorSubystem(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, 
                             //m_acquisitionSubsystem, m_shooterVisionSubsystem, m_climberSubsystem);

  private static final ButtonManager m_buttonManager = 
  new ButtonManager(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, m_acquisitionSubsystem, m_shooterVisionSubsystem, m_climberSubsystem, m_cargoVisionSubsystem);

  private static final ShuffleboardManager m_shuffleboardManager = 
  new ShuffleboardManager(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, 
                          m_acquisitionSubsystem, m_shooterVisionSubsystem, m_climberSubsystem, 
                          m_cargoVisionSubsystem, m_rapidReact, m_autoConfig, m_debug);
  
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    m_buttonManager.configureButtonBindings();
  
    m_shuffleboardManager.configureShuffleboard();
  }

  public static String getTeamColor() {
    //return m_teamColorChooser.getSelected();
    if(DriverStation.getAlliance() == Alliance.Red) {
      return "Red";
    } else if(DriverStation.getAlliance() == Alliance.Blue) {
      return "Blue";
    } else {
      return "Invalid Team Color";
    }
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return getAutoCommand(m_shuffleboardManager.getAutoLevel(), m_shuffleboardManager.getAutoPosition(), m_shuffleboardManager.getAutoTarmacSpot(), m_shuffleboardManager.getAutoSearchForBall());
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


  // public static VoltageMonitorSubystem getVoltageMonitorSubsystem() {
  //   return m_voltageMonitorSubsystem;
  // }

  /**
   * Autonomous Command Getters
   */

  public AutoCommand getAutoCommand(boolean isHigh, AutoPosition position, boolean shouldBeInTarmac, boolean searchForBall) {
    return new AutoCommand(m_shooterVisionSubsystem, m_drivebaseSubsystem, m_shooterSubsystem, 
                           m_storageSubsystem, m_acquisitionSubsystem, m_cargoVisionSubsystem, 
                           isHigh, position, shouldBeInTarmac, searchForBall);
  }

  public static ShuffleboardManager getShuffleboardManager() {
    return m_shuffleboardManager;
  }

  public static ShuffleboardTab getRapidReactTab() {
    return m_rapidReact;
  }
  
}
