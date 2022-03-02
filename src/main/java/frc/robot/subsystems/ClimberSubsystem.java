package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.OI;

public class ClimberSubsystem extends SubsystemBase {
    private boolean m_isAscending;
    private boolean m_isDescending;
    private boolean m_allowAscending;
    private boolean m_allowDescending;

    private WPI_TalonFX m_climberMoter;
    private DigitalInput m_upperLimit;
    private DigitalInput m_lowerLimit;
    // When limit is actually triggered, limit.get() is false. Opposite of what you would think.

    public ClimberSubsystem() {
        m_isAscending = false;
        m_isDescending = false;
        m_allowAscending = false;
        m_allowDescending = false;

        m_climberMoter = new WPI_TalonFX(Constants.CLIMBER_WINCH_CAN_ID);
        m_upperLimit = new DigitalInput(Constants.CLIMBER_UPPER_LIMIT_DIO);
        m_lowerLimit = new DigitalInput(Constants.CLIMBER_LOWER_LIMIT_DIO);

        m_climberMoter.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    public void periodic() {
        if (!m_upperLimit.get() && m_isAscending){
            stopMotor();
            OI.setXboxRumbleSpeed(Constants.RUMBLE_SPEED);
            m_allowAscending = false;
        } 
        
        if(m_upperLimit.get()) {
            m_allowAscending = true;
        }

        if (!m_lowerLimit.get() && m_isDescending){
            stopMotor();
            OI.setXboxRumbleSpeed(Constants.RUMBLE_SPEED);
            m_allowDescending = false;
        } 

        if(m_lowerLimit.get()) {
            m_allowDescending = true;
        }

        //SmartDashboard.putBoolean("Allow Acending", m_allowAscending);
        //SmartDashboard.putBoolean("Allow Down", m_allowDescending);
        // SmartDashboard.putBoolean("Upper Limit Switch", m_upperLimit.get());
        // SmartDashboard.putBoolean("Lower Limit Switch", m_lowerLimit.get());
        //SmartDashboard.putBoolean("Is ascending", m_isAscending);
        //SmartDashboard.putBoolean("Is descending", m_isDescending);
    } 
        
    public void setClimberMotor(double speed){
        if (speed == 0){
            stopMotor();
        }
        if (m_allowAscending && speed > 0){
            m_climberMoter.set(TalonFXControlMode.PercentOutput, speed);
            m_isAscending = true;
        }
        if (m_allowDescending && speed < 0){
            m_climberMoter.set(TalonFXControlMode.PercentOutput, speed);
            m_isDescending = true;
        }

        if(!m_allowAscending && speed > 0) {
            OI.setXboxRumbleSpeed(Constants.RUMBLE_SPEED);
        }

        if(!m_allowDescending && speed < 0) {
            OI.setXboxRumbleSpeed(Constants.RUMBLE_SPEED);
        }
    }

    /**
     * Meant to just be used within the subsystem, along with stopping the motors it modifies the descending/ascending status variables.
     */
    private void stopMotor(){
        OI.setXboxRumbleStop();
        m_climberMoter.stopMotor();
        m_isDescending = false;
        m_isAscending = false;
    }

    public WPI_TalonFX getClimberMotor(){
        return m_climberMoter;
    }

}




