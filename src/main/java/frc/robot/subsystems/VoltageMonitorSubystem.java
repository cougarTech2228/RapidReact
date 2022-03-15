// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalOutput;


public class VoltageMonitorSubystem extends SubsystemBase {
  

    private ShooterSubsystem m_shooterSubsystem;
    private StorageSubsystem m_storageSubsystem;
    private DrivebaseSubsystem m_drivebaseSubsystem;
    private AcquisitionSubsystem m_acquisitionSubsystem;
    private ShooterVisionSubsystem m_shooterVisionSubsystem;
    private ClimberSubsystem m_climberSubsystem;
    private DigitalOutput m_buzzDigitalOutput;
    
  /** Creates a new VoltageMonitorSubystem. */
  public VoltageMonitorSubystem(ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem,
  DrivebaseSubsystem drivebaseSubsystem, AcquisitionSubsystem acquisitionSubsystem, 
  ShooterVisionSubsystem shooterVisionSubsystem, ClimberSubsystem climberSubsystem) {

    m_shooterSubsystem = shooterSubsystem;
    m_storageSubsystem = storageSubsystem;
    m_drivebaseSubsystem = drivebaseSubsystem;
    m_acquisitionSubsystem = acquisitionSubsystem;
    m_shooterVisionSubsystem = shooterVisionSubsystem;
    m_climberSubsystem = climberSubsystem;
    m_buzzDigitalOutput = new DigitalOutput(9);
  }

  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    if(m_acquisitionSubsystem.getAcquirerMotor().getSelectedSensorVelocity() == 0 && 
       m_climberSubsystem.getClimberMotor().getSelectedSensorVelocity() == 0 && 
       m_drivebaseSubsystem.getRightFront().getSelectedSensorVelocity() == 0 && 
       m_drivebaseSubsystem.getRightBack().getSelectedSensorVelocity() == 0 && 
       m_drivebaseSubsystem.getLeftFront().getSelectedSensorVelocity() == 0 && 
       m_drivebaseSubsystem.getLeftBack().getSelectedSensorVelocity() == 0 && 
       m_shooterSubsystem.getShooterMaster().getSelectedSensorVelocity() == 0 && 
       m_shooterSubsystem.getShooterFollower().getSelectedSensorVelocity() == 0 && 
       m_storageSubsystem.getConveyorMotor().getSelectedSensorVelocity() == 0 && 
       m_storageSubsystem.getFeedMotor().getSelectedSensorVelocity() == 0) { //&& 
       //m_shooterVisionSubsystem.getVoltage() <= 12){
       
        m_buzzDigitalOutput.set(true);
       } else{
        m_buzzDigitalOutput.set(false);
       }
  }

  public void stopBuzzer() {
    m_buzzDigitalOutput.set(false);
  }
   

  
}
