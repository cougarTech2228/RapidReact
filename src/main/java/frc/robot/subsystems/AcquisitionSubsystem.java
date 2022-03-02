package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Toolkit.CT_DigitalInput;

public class AcquisitionSubsystem extends SubsystemBase{
    private WPI_TalonFX m_acquirerMotor;
    private WPI_TalonFX m_deployMotor;
    private boolean m_isAcquiring;
    private boolean m_isAcquirerDeployed;
    private CT_DigitalInput m_AquirerDownLimit;
    private CT_DigitalInput m_AquirerUpLimit;
    private final static double ACQUIERER_DEPLOY_SPEED = 0.4;
    private final static double ACQUIRER_RETRACT_SPEED = 0.4;


    public AcquisitionSubsystem(){
        m_acquirerMotor = new WPI_TalonFX(Constants.ACQUIRER_SPIN_MOTOR_CAN_ID);
        m_deployMotor = new WPI_TalonFX(Constants.ACQUIRER_DEPLOY_MOTOR_CAN_ID);
        m_AquirerDownLimit = new CT_DigitalInput(Constants.AQUIRER_LOWER_LIMIT_DIO);
        m_AquirerUpLimit = new CT_DigitalInput(Constants.AQUIRER_UPPER_LIMIT_DIO);
        m_isAcquiring = false;
        m_isAcquirerDeployed = false;

        m_acquirerMotor.configFactoryDefault();
        m_deployMotor.configFactoryDefault();
        m_deployMotor.setNeutralMode(NeutralMode.Brake);


        // SmartDashboard.putData("Lower Acquierer", new InstantCommand(() -> {
        //     lowerAcquierer();
        // }));
        // SmartDashboard.putData("Raise Acquierer", new InstantCommand(() -> {
        //     raiseAcquierer();
        // }));

        //m_AquirerDownLimit.setMethodToRun(() -> m_deployMotor.stopMotor());
        //m_AquirerUpLimit.setMethodToRun(() -> m_deployMotor.stopMotor());

        m_AquirerDownLimit.setMethodToRun(() -> {
            System.out.println("acquirer down limit hit");
            m_deployMotor.stopMotor();
            m_isAcquirerDeployed = true;
            
        });
        m_AquirerUpLimit.setMethodToRun(() -> {
            System.out.println("acquirer up limit hit");
            m_deployMotor.stopMotor();
            m_isAcquirerDeployed = false;
        });
    }

    @Override
    public void periodic() {
        m_AquirerUpLimit.runWhenTripped();
        m_AquirerDownLimit.runWhenTripped();
    }

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
    
    // public void lowerAcquierer(){
    //     System.out.println("Lowering Acquierer");
    //     m_isAcquirerDeployed = true;
    //     while(!m_AquirerDownLimit.get()){
    //         m_deployMotor.set(AQUIERER_DEPLOY_SPEED);
    //     }
    //     m_deployMotor.stopMotor();
    // }

    // public void raiseAcquierer(){
    //     System.out.println("Raiseing Acquierer");
    //     m_isAcquirerDeployed = false;
    //     while(!m_AquirerUpLimit.get()){
    //         m_deployMotor.set(-(AQUIERER_DEPLOY_SPEED+RAISEING_DIFF));
    //     }
    //     m_deployMotor.stopMotor();
    // }

    public void deployAcquirer() {
        m_AquirerDownLimit.resetMethodToRun();
        m_deployMotor.set(ACQUIERER_DEPLOY_SPEED);
        
    }

    public void retractAcquirer() {
        m_AquirerUpLimit.resetMethodToRun();
        m_deployMotor.set(-ACQUIRER_RETRACT_SPEED);
    }

    public boolean isAcquirerDeployed() {
        return m_isAcquirerDeployed;
    }
}
