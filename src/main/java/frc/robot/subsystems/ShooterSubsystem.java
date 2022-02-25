// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class ShooterSubsystem extends SubsystemBase {
  private WPI_TalonFX m_shooterMaster;
  private WPI_TalonFX m_shooterFollower;

  private NetworkTableEntry m_velocityHighEntry;
  private NetworkTableEntry m_velocityLowEntry;

  private AnalogInput ultrasonic = new AnalogInput(0);

  /** Creates a new ExampleSubsystem. */
  public ShooterSubsystem() {
    m_shooterMaster = new WPI_TalonFX(Constants.SHOOTER_MASTER_CAN_ID);
    m_shooterFollower = new WPI_TalonFX(Constants.SHOOTER_FOLLOWER_CAN_ID);
    configMotor(m_shooterMaster);
    configMotor(m_shooterFollower);  
    m_shooterFollower.follow(m_shooterMaster);

    m_shooterMaster.setInverted(false);
    m_shooterFollower.setInverted(true);

  //   m_velocityHighEntry = Shuffleboard.getTab("Shooter Velocity Adjuster").add("Shooter High Velocity", Constants.HIGH_SHOOT_SPEED)
  //   .withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 0.7)).getEntry();
  //  //m_velocityHighEntry.setDefaultNumber(Constants.HIGH_SHOOT_SPEED);

  //  m_velocityLowEntry = Shuffleboard.getTab("Shooter Velocity Adjuster").add("Shooter Low Velocity", Constants.LOW_SHOOT_SPEED)
  //   .withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 0.45)).getEntry();
  //  m_velocityLowEntry.setDefaultNumber(Constants.LOW_SHOOT_SPEED);
  }

  @Override
  public void periodic() {
      SmartDashboard.putNumber("Shooter Ultrasonic", getUltrasonicInches());
  }

  public void setMotors(double percent){
    m_shooterMaster.set(ControlMode.PercentOutput, percent);
  }
  public void stopMotors(){
    m_shooterMaster.stopMotor();
  }

   private void configMotor(WPI_TalonFX motor){

        motor.configFactoryDefault();
        motor.setSensorPhase(false);
        motor.configVoltageCompSaturation(11);

        motor.configPeakOutputForward(+1.0, Constants.kTimeoutMs);
        motor.configPeakOutputReverse(-1.0, Constants.kTimeoutMs);

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.supplyCurrLimit.enable = true;
        config.supplyCurrLimit.triggerThresholdCurrent = Constants.SHOOTER_CURRENT_LIMIT;
        config.supplyCurrLimit.triggerThresholdTime = Constants.SHOOTER_CURRENT_DURATION;
        config.supplyCurrLimit.currentLimit = Constants.SHOOTER_CONTINUOUS_CURRENT_LIMIT;
        motor.configAllSettings(config);
    }

public double getCalculatedShooterPercent(String shotType){
  double distance = ShooterVisionSubsystem.getDistanceFt();
  if(shotType.equals("High")){
    return .5f; // replace with an equation
  }
  else{
    return .5f; // replace with an equation
  }
}

public double getUltrasonicInches() {
  double rawValue = ultrasonic.getValue();
  double voltageScaleFactor = 5.0 / RobotController.getVoltage5V();
  double currentDistanceInches = rawValue * voltageScaleFactor * 0.0492;
  return currentDistanceInches;
}

  // public NetworkTableEntry getVelocityHighTable() {
  //   return m_velocityHighEntry;
  // }

  // public NetworkTableEntry getVelocityLowTable() {
  //   return m_velocityLowEntry;
  // }
}
