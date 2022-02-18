package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Toolkit.CT_DigitalInput;

public class StorageSubsystem extends SubsystemBase {
    private WPI_TalonSRX m_conveyorMotor;
    private WPI_TalonFX m_feedMotor;
    private CT_DigitalInput m_proximitySubsystem;
    private boolean m_isBallThere;

    public StorageSubsystem() {
        m_conveyorMotor = new WPI_TalonSRX(Constants.STORAGE_CONVEYOR_MOTOR_CAN_ID);
        m_feedMotor = new WPI_TalonFX(Constants.SHOOTER_FEED_MOTOR_CAN_ID);
        m_proximitySubsystem = new CT_DigitalInput(9);

        m_conveyorMotor.configFactoryDefault();
        m_feedMotor.configFactoryDefault();
    }

    @Override
    public void periodic() {
        m_isBallThere = !m_proximitySubsystem.get();
        SmartDashboard.putBoolean("Ball Present", m_isBallThere);
    }

    public void setConveyorMotor(double speed) {
        m_conveyorMotor.set(ControlMode.PercentOutput, speed);

    }
    public void setFeedMotor(double speed) {
        //System.out.println("Feed motor set");
        m_feedMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopMotors() {
        //System.out.println("storage motors stopped");
        m_conveyorMotor.stopMotor();
        m_feedMotor.stopMotor();
    }

    public boolean isBallThere() {
        return m_isBallThere;
    }
}
