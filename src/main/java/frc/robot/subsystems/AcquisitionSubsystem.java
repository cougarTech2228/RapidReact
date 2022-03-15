package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Toolkit.CT_DigitalInput;

public class AcquisitionSubsystem extends SubsystemBase{
    private WPI_TalonFX m_acquirerMotor;
    private WPI_TalonFX m_deployMotor;
    private boolean m_isAcquiring;
    private boolean m_isAcquirerDeployed;
    private CT_DigitalInput m_acquirerDownLimit;
    private CT_DigitalInput m_acquirerUpLimit;
    
    public AcquisitionSubsystem(){
        m_acquirerMotor = new WPI_TalonFX(Constants.ACQUIRER_SPIN_MOTOR_CAN_ID);
        m_deployMotor = new WPI_TalonFX(Constants.ACQUIRER_DEPLOY_MOTOR_CAN_ID);
        m_acquirerDownLimit = new CT_DigitalInput(Constants.AQUIRER_LOWER_LIMIT_DIO);
        m_acquirerUpLimit = new CT_DigitalInput(Constants.AQUIRER_UPPER_LIMIT_DIO);
        m_isAcquiring = false;
        m_isAcquirerDeployed = false;

        m_acquirerMotor.configFactoryDefault();
        m_deployMotor.configFactoryDefault();
        m_deployMotor.setNeutralMode(NeutralMode.Brake);

        m_acquirerDownLimit.setMethodToRun(() -> {
            m_deployMotor.stopMotor();
            m_isAcquirerDeployed = true;
        });

        m_acquirerUpLimit.setMethodToRun(() -> {
            m_deployMotor.stopMotor();
            m_isAcquirerDeployed = false;
        });
    }

    @Override
    public void periodic() {
        m_acquirerUpLimit.runWhenTripped();
        m_acquirerDownLimit.runWhenTripped();

        RobotContainer.getRapidReactTab().add("Upper limit", m_acquirerUpLimit.get());
        RobotContainer.getRapidReactTab().add("Lower limit", m_acquirerDownLimit.get());
    }

    public void setSpinnerMotor(double percent){
        m_acquirerMotor.set(ControlMode.PercentOutput, percent);
        m_isAcquiring = true;
    }

    public void stopSpinnerMotor(){
        m_acquirerMotor.stopMotor();
        m_isAcquiring = false;
    }

    public void deployAcquirer() {
        m_acquirerDownLimit.resetMethodToRun();
        m_deployMotor.set(Constants.ACQUIRER_DEPLOY_SPEED);
    }

    public void retractAcquirer() {
        m_acquirerUpLimit.resetMethodToRun();
        m_deployMotor.set(-Constants.ACQUIRER_RETRACT_SPEED);
    }

    public boolean isAcquirerDeployed() {
        return m_isAcquirerDeployed;
    }

    public boolean isAcquiring() {
        return m_isAcquiring;
    }

    public double getMotorCurrent() {
        return m_acquirerMotor.getSupplyCurrent();
    }

    public WPI_TalonFX getAcquirerMotor(){
        return m_acquirerMotor;
    }
}
