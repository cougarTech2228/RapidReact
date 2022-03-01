package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class AcquisitionSubsystem extends SubsystemBase{
    private WPI_TalonFX m_acquirerMotor;
    private WPI_TalonFX m_deployMotor;
    private boolean m_isAcquiring;


    public AcquisitionSubsystem(){
        m_acquirerMotor = new WPI_TalonFX(Constants.ACQUIRER_SPIN_MOTOR_CAN_ID);
        m_deployMotor = new WPI_TalonFX(Constants.ACQUIRER_DEPLOY_MOTOR_CAN_ID);
        m_isAcquiring = false;

        m_acquirerMotor.configFactoryDefault();
        m_deployMotor.configFactoryDefault();
    }

    @Override
    public void periodic() {}

    public boolean isAcquiring() {
        return m_isAcquiring;
    }

    public void setSpinnerMotor(double percent){
        m_acquirerMotor.set(ControlMode.PercentOutput, percent);
        m_isAcquiring = true;
    }

    public void stopSpinnerMotor(){
        m_acquirerMotor.stopMotor();
        m_isAcquiring = false;
    }

    public double getMotorCurrent() {
        return m_acquirerMotor.getSupplyCurrent();
    }

    public WPI_TalonFX getAcquirerMotor(){
        return m_acquirerMotor;
    }
}
