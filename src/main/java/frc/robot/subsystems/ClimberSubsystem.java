package frc.robot.subsystems;
import java.util.Map;
import java.util.ResourceBundle.Control;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.DMC60;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.RobotContainer;

public class ClimberSubsystem extends SubsystemBase {
    private boolean m_isAscending;
    private boolean m_isDescending;
    private boolean m_allowAscending;
    private boolean m_allowDescending;
    private double m_homeEncoderCount;

    private WPI_TalonFX m_climberWinch;
    private WPI_TalonFX m_climberSwingMotor;
    private DigitalInput m_upperLimit;
    private DigitalInput m_lowerLimit;
    private DigitalInput m_limitSwingArmHome;
    private DMC60 m_hookControl;

    private SimpleWidget m_swingSpeedWidget;
    private SimpleWidget m_swingWaitWidget;

    // When limit is actually triggered, limit.get() is false. Opposite of what you would think.

    public ClimberSubsystem() {
        m_isAscending = false;
        m_isDescending = false;
        m_allowAscending = false;
        m_allowDescending = false;

        m_climberWinch = new WPI_TalonFX(Constants.CLIMBER_WINCH_CAN_ID);
        m_climberSwingMotor = new WPI_TalonFX(Constants.CLIMBER_SWING_ARM_CAN_ID);
        m_upperLimit = new DigitalInput(Constants.CLIMBER_UPPER_LIMIT_DIO);
        m_lowerLimit = new DigitalInput(Constants.CLIMBER_LOWER_LIMIT_DIO);
        m_limitSwingArmHome = new DigitalInput(Constants.SWING_ARM_HOME_LIMIT_DIO);
        m_hookControl = new DMC60(Constants.HOOK_CONTROL_PWM);

        m_climberWinch.setNeutralMode(NeutralMode.Brake);

        m_swingSpeedWidget = RobotContainer.getRapidReactTab()
        .add("Swing Speed changer", Constants.CLIMBER_SWING_ARM_MOTOR_SPEED)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 1));

        m_swingWaitWidget = RobotContainer.getRapidReactTab()
        .add("Swing Wait changer", 4)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 6));

    }

    @Override
    public void periodic() {

          // if (isUpperLimitReached() || isLowerLimitReached()){
          //   stopMotor();
          // }

          // if (isSwingArmHomed()){
          //   stopClimberSwingMotor();
          // }
    }
    /**
     * Meant to just be used within the subsystem, along with stopping the motors it modifies the descending/ascending status variables.
     */
    private void stopMotor(){
        OI.setXboxRumbleStop();
        m_climberWinch.stopMotor();
        m_isDescending = false;
        m_isAscending = false;
    }

    public WPI_TalonFX getCilmberWinchMoter(){
        return m_climberWinch;
    }

    public WPI_TalonFX getClimberSwingArmMoter(){
        return m_climberSwingMotor;
    }

    public void climb(){
      m_climberWinch.set(ControlMode.PercentOutput, Constants.CLIMBER_WINCH_MOTOR_SPEED);
    }
    
    public void retract(){
      m_climberWinch.set(ControlMode.PercentOutput, -Constants.CLIMBER_WINCH_MOTOR_SPEED);
    }

    public boolean isUpperLimitReached(){
      return !m_upperLimit.get();
    }

    public boolean isLowerLimitReached(){
      return !m_lowerLimit.get();
    }

    public void stopClimberWinchMotor(){
      m_climberWinch.stopMotor();
    }

    public void startClimberSwingMotor(double speed){
      m_climberSwingMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopClimberSwingMotor(){
      m_climberSwingMotor.stopMotor();
    }

    public void releaseHook(){
      m_hookControl.set(Constants.HOOK_SPEED);
    }

    public void stopHook(){
      m_hookControl.stopMotor();;
    }

    public void setHooks(double speed){
      m_hookControl.set(speed);
    }

    public boolean isSwingArmHomed(){
      return m_limitSwingArmHome.get();
    }

    public double getSwingSpeed() {
      return m_swingSpeedWidget.getEntry().getDouble(0.1);
    }

    public double getWaitTime() {
      return m_swingWaitWidget.getEntry().getDouble(4);
    }

    public WPI_TalonFX getClimberMotor() {
      return m_climberSwingMotor;
    }
}


