package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.DMC60;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class ClimberSubsystem extends SubsystemBase {
    private boolean m_isAscending;
    private boolean m_isDescending;

    private WPI_TalonFX m_climberWinch;
    private WPI_TalonFX m_climberSwingMotor;
    private DigitalInput m_upperLimit;
    private DigitalInput m_lowerLimit;
    private DigitalInput m_limitSwingArmHome;
    private DMC60 m_hookControl;

    // When limit is actually triggered, limit.get() is false. Opposite of what you would think.

    public ClimberSubsystem() {
        m_isAscending = false;
        m_isDescending = false;

        m_climberWinch = new WPI_TalonFX(Constants.CLIMBER_WINCH_CAN_ID);
        m_climberSwingMotor = new WPI_TalonFX(Constants.CLIMBER_SWING_ARM_CAN_ID);
        m_upperLimit = new DigitalInput(Constants.CLIMBER_UPPER_LIMIT_DIO);
        m_lowerLimit = new DigitalInput(Constants.CLIMBER_LOWER_LIMIT_DIO);
        m_limitSwingArmHome = new DigitalInput(Constants.SWING_ARM_HOME_LIMIT_DIO);
        m_hookControl = new DMC60(Constants.HOOK_CONTROL_PWM);

        m_climberWinch.setNeutralMode(NeutralMode.Brake);

        RobotContainer.getRapidReactTab().add("Home Switch", !m_limitSwingArmHome.get());
    }

    @Override
    public void periodic() {

        // if(!ClimbSequenceCommand.isClimbing()) {
        //   if (isUpperLimitReached() || isLowerLimitReached()){
        //     stopMotor();
        //   }

        //   if (isSwingArmHomed()){
        //     stopClimberSwingMotor();
        //   }
        // }

        if(m_isAscending && isUpperLimitReached() ||
           m_isDescending && isLowerLimitReached()) {
          stopMotor();
        }
    }
    /**
     * Meant to just be used within the subsystem, along with stopping the motors it modifies the descending/ascending status variables.
     */
    private void stopMotor(){
        m_climberWinch.stopMotor();
        m_isDescending = false;
        m_isAscending = false;
    }

    public void climb() {
        m_isAscending = true;
        if(!isUpperLimitReached())
            m_climberWinch.set(ControlMode.PercentOutput, Constants.CLIMBER_WINCH_MOTOR_SPEED);
    }
    
    public void retract() {
        m_isDescending = true;
        if(!isLowerLimitReached())
            m_climberWinch.set(ControlMode.PercentOutput, -Constants.CLIMBER_WINCH_MOTOR_SPEED);
    }

    public boolean isUpperLimitReached() {
        return !m_upperLimit.get();
    }

    public boolean isLowerLimitReached() {
        return !m_lowerLimit.get();
    }

    public void stopClimberWinchMotor() {
        m_climberWinch.stopMotor();
    }

    public void startClimberSwingMotor(double speed) {
        m_climberSwingMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopClimberSwingMotor() {
        m_climberSwingMotor.stopMotor();
    }

    public void releaseHook() {
        m_hookControl.set(Constants.HOOK_SPEED);
    }

    public void stopHook() {
        m_hookControl.stopMotor();
    }

    public void setHooks(double speed) {
        m_hookControl.set(speed);
    }

    public boolean isSwingArmHomed() {
        return m_limitSwingArmHome.get();
    }

    public WPI_TalonFX getClimberMotor() {
        return m_climberSwingMotor;
    }
}


