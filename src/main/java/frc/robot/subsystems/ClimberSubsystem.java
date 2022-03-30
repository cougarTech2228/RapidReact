package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.DMC60;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class ClimberSubsystem extends SubsystemBase {
    private boolean m_isAscending;
    private boolean m_isDescending;

    private boolean m_isLeftAscending;
    private boolean m_isRightAscending;
    private boolean m_isLeftDescending;
    private boolean m_isRightDescending;

    private WPI_TalonFX m_winch;
    private WPI_TalonSRX m_leftActuator;
    private WPI_TalonSRX m_rightActuator;
    private DigitalInput m_upperLimit;
    private DigitalInput m_lowerLimit;
    private DigitalInput m_leftActuatorLimit;
    private DigitalInput m_rightActuatorLimit;
    private DMC60 m_hookControl;
    private boolean m_hasRetractedArms;

    // When limit is actually triggered, limit.get() is false. Opposite of what you would think.

    public ClimberSubsystem() {
        m_isAscending = false;
        m_isDescending = false;

        m_isLeftAscending = false;
        m_isLeftDescending = false;
        m_isRightAscending = false;
        m_isRightDescending = false;
        m_hasRetractedArms = false;

        m_winch = new WPI_TalonFX(Constants.CLIMBER_WINCH_CAN_ID);
        m_leftActuator = new WPI_TalonSRX(Constants.CLIMBER_LEFT_ACTUATOR_CAN_ID);
        m_rightActuator = new WPI_TalonSRX(Constants.CLIMBER_RIGHT_ACTUATOR_CAN_ID);
        m_upperLimit = new DigitalInput(Constants.CLIMBER_UPPER_LIMIT_DIO);
        m_lowerLimit = new DigitalInput(Constants.CLIMBER_LOWER_LIMIT_DIO);
        m_leftActuatorLimit = new DigitalInput(Constants.LEFT_ACTUATOR_LIMIT_DIO);
        m_rightActuatorLimit = new DigitalInput(Constants.RIGHT_ACTUATOR_LIMIT_DIO);
        m_hookControl = new DMC60(Constants.HOOK_CONTROL_PWM);

        configureTalon(m_leftActuator);
        configureTalon(m_rightActuator);

        m_winch.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    public void periodic() {

        if(isLeftActuatorLimitHit() && m_isLeftAscending) {
            stopLeftActuator();
        }

        if(isRightActuatorLimitHit() && m_isRightAscending) {
            stopRightActuator();
        }

        if(m_isAscending && isUpperLimitReached() ||
           m_isDescending && isLowerLimitReached()) {
          stopMotor();
        }

        SmartDashboard.putBoolean("Left Actuator Limit", isLeftActuatorLimitHit());
        SmartDashboard.putBoolean("Right Actuator Limit", isRightActuatorLimitHit());
        SmartDashboard.putBoolean("Lower Limit", isLowerLimitReached());
        SmartDashboard.putBoolean("Upper Limit", isUpperLimitReached());
    }
    /**
     * Meant to just be used within the subsystem, along with stopping the motors it modifies the descending/ascending status variables.
     */
    private void stopMotor(){
        m_winch.stopMotor();
        m_isDescending = false;
        m_isAscending = false;
    }

    public void climb() {
        m_isAscending = true;
        if(!isUpperLimitReached())
            m_winch.set(ControlMode.PercentOutput, Constants.CLIMBER_WINCH_MOTOR_SPEED);
    }
    
    public void retract() {
        m_isDescending = true;
        if(!isLowerLimitReached())
            m_winch.set(ControlMode.PercentOutput, -Constants.CLIMBER_WINCH_MOTOR_SPEED);
    }

    public void actuateLeftUp() {
        m_isLeftAscending = true;
        if(!isLeftActuatorLimitHit())
            m_leftActuator.set(ControlMode.PercentOutput, Constants.ACTUATOR_SPEED);
    }

    public void actuateRightUp() {
        m_isRightAscending = true;
        if(!isRightActuatorLimitHit())
            m_rightActuator.set(ControlMode.PercentOutput, Constants.ACTUATOR_SPEED);
    }

    public void actuateLeftDown() {
        m_isLeftDescending = true;
        m_leftActuator.set(ControlMode.PercentOutput, -Constants.ACTUATOR_SPEED);
    }

    public void actuateRightDown() {
        m_isRightDescending = true;
        m_rightActuator.set(ControlMode.PercentOutput, -Constants.ACTUATOR_SPEED);
    }

    public void stopLeftActuator() {
        m_isLeftDescending = false;
        m_isLeftAscending = false;
        m_leftActuator.stopMotor();
    }

    public void stopRightActuator() {
        m_isRightAscending = false;
        m_isRightDescending = false;
        m_rightActuator.stopMotor();
    }

    public boolean isUpperLimitReached() {
        return !m_upperLimit.get();
    }

    public boolean isLowerLimitReached() {
        return !m_lowerLimit.get();
    }

    public void stopClimberWinchMotor() {
        m_winch.stopMotor();
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

    public boolean isLeftActuatorLimitHit() {
        return m_leftActuatorLimit.get();
    }

    public boolean isRightActuatorLimitHit() {
        return m_rightActuatorLimit.get();
    }

    private void configureTalon(WPI_TalonSRX talon) {
        talon.configPeakCurrentLimit(30);
        talon.configPeakCurrentDuration(100);
        talon.configContinuousCurrentLimit(20);
        talon.enableCurrentLimit(true);
    }

    public void hasRetractedArms() {
        
    }
}


