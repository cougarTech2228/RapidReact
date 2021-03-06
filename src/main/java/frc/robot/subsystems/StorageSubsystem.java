package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class StorageSubsystem extends SubsystemBase {
    private WPI_TalonSRX m_conveyorMotor;
    private WPI_TalonFX m_feedMotor;

    private ColorSensorV3 m_ballColorSensor;

    public enum BallType {
        RedBall,
        BlueBall,
        None;
    }

    public StorageSubsystem() {
        m_conveyorMotor = new WPI_TalonSRX(Constants.STORAGE_CONVEYOR_MOTOR_CAN_ID);
        m_feedMotor = new WPI_TalonFX(Constants.SHOOTER_FEED_MOTOR_CAN_ID);

        m_conveyorMotor.configFactoryDefault();
        m_feedMotor.configFactoryDefault();
        m_ballColorSensor = new ColorSensorV3(Port.kMXP);
    }

    public BallType getCurrentBall() {
        BallType ball = BallType.None;
        if (m_ballColorSensor.getProximity() > Constants.BALL_PRESENT_PROX_THRESHOLD) {
            if (m_ballColorSensor.getRed() > m_ballColorSensor.getBlue()) {
                ball = BallType.RedBall;
            } else {
                ball = BallType.BlueBall;
            }
        }
        return ball;
    }

    @Override
    public void periodic() {}

    public void setConveyorMotor(double speed) {
        m_conveyorMotor.set(ControlMode.PercentOutput, speed);
    }

    public void setFeedMotor(double speed) {
        m_feedMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopMotors() {
        m_conveyorMotor.stopMotor();
        m_feedMotor.stopMotor();
    }

    public WPI_TalonSRX getConveyorMotor(){
        return m_conveyorMotor;
    }
    
    public WPI_TalonFX getFeedMotor(){
        return m_feedMotor;
    }
}
