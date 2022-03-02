package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class AcquisitionSubsystem extends SubsystemBase{
    private WPI_TalonFX m_acquirerMotor;
    private WPI_TalonFX m_deployMotor;
    private boolean m_isAcquiring;
    private DigitalInput m_AquirerDownLimit;
    private DigitalInput m_AquirerUpLimit;
    private final static double AQUIERER_DEPLOY_SPEED = 0.2;
    private final static double RAISEING_DIFF = 0.1;


    public AcquisitionSubsystem(){
        m_acquirerMotor = new WPI_TalonFX(Constants.ACQUIRER_SPIN_MOTOR_CAN_ID);
        m_deployMotor = new WPI_TalonFX(Constants.ACQUIRER_DEPLOY_MOTOR_CAN_ID);
        m_AquirerDownLimit = new DigitalInput(Constants.AQUIRER_LOWER_LIMIT_DIO);
        m_AquirerUpLimit = new DigitalInput(Constants.AQUIRER_UPPER_LIMIT_DIO);
        m_isAcquiring = false;

        m_acquirerMotor.configFactoryDefault();
        m_deployMotor.configFactoryDefault();


        SmartDashboard.putData("Lower Acquierer", new InstantCommand(() -> {
            lowerAcquierer();
        }));
        SmartDashboard.putData("Raise Acquierer", new InstantCommand(() -> {
            raiseAcquierer();
        }));
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
    
    public void lowerAcquierer(){
        System.out.println("Lowering Acquierer");
        while(!m_AquirerDownLimit.get()){
            m_deployMotor.set(AQUIERER_DEPLOY_SPEED);
        }
        m_deployMotor.stopMotor();
    }

    public void raiseAcquierer(){
        System.out.println("Raiseing Acquierer");
        while(!m_AquirerUpLimit.get()){
            m_deployMotor.set(-(AQUIERER_DEPLOY_SPEED+RAISEING_DIFF));
        }
        m_deployMotor.stopMotor();
    }
}
