package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import frc.robot.OI;



public class DrivebaseSubsystem extends SubsystemBase {
    private WPI_TalonSRX m_rightFront = new WPI_TalonSRX(Constants.RIGHT_FRONT_MOTOR_CAN_ID);
    private WPI_TalonSRX m_rightBack = new WPI_TalonSRX(Constants.RIGHT_REAR_MOTOR_CAN_ID);
    private WPI_TalonSRX m_leftFront = new WPI_TalonSRX(Constants.LEFT_FRONT_MOTOR_CAN_ID);
    private WPI_TalonSRX m_leftBack = new WPI_TalonSRX(Constants.LEFT_REAR_MOTOR_CAN_ID);

    private MecanumDrive m_robotDrive;

    public static int drivingMode = 0;
  

    public DrivebaseSubsystem() {

        DriverStation.silenceJoystickConnectionWarning(true);

        m_leftFront.configOpenloopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);
        m_leftBack.configOpenloopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);
        m_rightFront.configOpenloopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);
        m_rightBack.configOpenloopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);

        
        m_leftBack.follow(m_leftFront);
        m_rightBack.follow(m_rightFront);

        m_rightFront.setInverted(true);
        m_rightBack.setInverted(true);

        m_leftFront.setNeutralMode(NeutralMode.Brake);
        m_leftBack.setNeutralMode(NeutralMode.Brake);
        m_rightFront.setNeutralMode(NeutralMode.Brake);
        m_rightBack.setNeutralMode(NeutralMode.Brake);

        m_robotDrive = new MecanumDrive(m_leftFront, m_leftBack, m_rightFront, m_rightBack);
    }

    private double deadband(final double value) {
        /* Upper deadband */
        if (value >= Constants.JOYSTICK_DEADBAND_PERCENTAGE) {
            return value;
        }

        /* Lower deadband */
        if (value <= -Constants.JOYSTICK_DEADBAND_PERCENTAGE) {
            return value;
        }

        /* Inside deadband */
        return 0.0;
    }

    
    @Override
    public void periodic() { 
        // This method will be called once per scheduler run
        
        double X;
        double Y;
        double Z;

        if(getControlType() == 1) {
            Y = -OI.getXboxLeftJoystickY();
            X = OI.getXboxLeftJoystickX();
            Z = OI.getXboxRightJoystickX();
        } else if(getControlType() == 20) {
            Y = OI.getJoystickThrottleY();
            X = OI.getJoystickThrottleX();
            Z = OI.getJoystickThrottleZ();
        } else {
            Y = 0;
            X = 0;
            Z = 0;
        }

        if(drivingMode == 1) {
            Y = -Y;
            X = -X;
        } 

        m_robotDrive.driveCartesian(deadband(Y), deadband(X), deadband(Z));
    }

    //A value of 1 refers to the xbox controller, a value of 20 refers to the joystick.
    public int getControlType(){
        return DriverStation.getJoystickType(0);
    } 
}
