// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.subsystems.AcquisitionSubsystem;
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
  private final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem();
  private static final DrivebaseSubsystem m_drivebaseSubsystem = new DrivebaseSubsystem();
  private final ShooterVisionSubsystem m_shooterVisionSubsystem = new ShooterVisionSubsystem();
  private final StorageSubsystem m_storageSubsystem = new StorageSubsystem();
  private final AcquisitionSubsystem m_acquisitionSubsystem = new AcquisitionSubsystem();
  private final ButtonManager m_buttonManager = 
  new ButtonManager(m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, m_acquisitionSubsystem, m_shooterVisionSubsystem);
  private final AutonomousCommand m_autoCommand = 
  new AutonomousCommand(m_drivebaseSubsystem, m_shooterSubsystem, m_storageSubsystem, m_acquisitionSubsystem);

  //private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    m_buttonManager.configureButtonBindings();
    m_shooterVisionSubsystem.setCameras(Constants.ACQUIRING_DRIVING_MODE);
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }
  public static DrivebaseSubsystem getDrivebaseSubsystem(){
    return m_drivebaseSubsystem;
  }
}
