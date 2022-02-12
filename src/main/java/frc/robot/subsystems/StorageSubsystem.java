package frc.robot.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.Toolkit.CT_DigitalInput;

public class StorageSubsystem extends SubsystemBase {
    WPI_TalonSRX m_storageDrive = new WPI_TalonSRX(Constants.STORAGE_DRIVE_MOTOR_CAN_ID);
    WPI_TalonFX m_shooterFeed = new WPI_TalonFX(Constants.SHOOTER_FEED_MOTOR_CAN_ID);
    CT_DigitalInput proximitySensor = new CT_DigitalInput(9);
    private boolean m_isBallThere;
    public StorageSubsystem(){
        m_storageDrive.configFactoryDefault();
        m_shooterFeed.configFactoryDefault();
    }

    @Override
    public void periodic(){
        m_isBallThere = !proximitySensor.get();
        SmartDashboard.putBoolean("Ball Present", m_isBallThere);
    }

    public void setDriveMotor(){
        m_storageDrive.set(ControlMode.PercentOutput, Constants.STORAGE_DRIVE_SPEED);

    }
    public void setFeedMotor(){
        m_shooterFeed.set(ControlMode.PercentOutput, Constants.SHOOTER_FEED_SPEED);
    }

    public void stopMotors(){
        m_storageDrive.stopMotor();
        m_shooterFeed.stopMotor();
    }
    public boolean isBallThere(){
        return m_isBallThere;
    }
}
