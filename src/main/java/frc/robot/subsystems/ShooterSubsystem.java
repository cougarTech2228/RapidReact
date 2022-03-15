// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;


public class ShooterSubsystem extends SubsystemBase {
    private WPI_TalonFX m_shooterMaster;
    private WPI_TalonFX m_shooterFollower;

    public enum VelocityType {
        Equation,
        Constant
    }

    private SimpleWidget m_velocityWidget;
    private static SendableChooser<VelocityType> m_velocityTypeChooser = new SendableChooser<>();

    /** Creates a new ExampleSubsystem. */
    public ShooterSubsystem() {
        m_shooterMaster = new WPI_TalonFX(Constants.SHOOTER_MASTER_CAN_ID);
        m_shooterFollower = new WPI_TalonFX(Constants.SHOOTER_FOLLOWER_CAN_ID);
        configMotor(m_shooterMaster);
        configMotor(m_shooterFollower);  
        m_shooterFollower.follow(m_shooterMaster);

        m_shooterMaster.setInverted(false);
        m_shooterFollower.setInverted(true);

        m_velocityWidget = RobotContainer.getRapidReactTab()
        .add(("Shooter High Velocity, Default is " + Constants.HIGH_SHOOT_SPEED), Constants.HIGH_SHOOT_SPEED)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0.50, "max", 0.8))
        .withSize(2, 1)
        .withPosition(4, 0);

        m_velocityTypeChooser.setDefaultOption("Equation", VelocityType.Equation);
        m_velocityTypeChooser.addOption("Constant", VelocityType.Constant);
        RobotContainer.getRapidReactTab()
        .add("Shooting Velocity Type", m_velocityTypeChooser)
        .withWidget(BuiltInWidgets.kSplitButtonChooser);

    }

    @Override
    public void periodic() {}

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

    public WPI_TalonFX getShooterMaster(){
        return m_shooterMaster;
    }

    public WPI_TalonFX getShooterFollower(){
        return m_shooterFollower;
    }

    public double getVelocityHigh() {
        return m_velocityWidget.getEntry().getDouble(Constants.HIGH_SHOOT_SPEED);
    }

    public VelocityType getVelocityType() {
        return m_velocityTypeChooser.getSelected();
    }

  
}
