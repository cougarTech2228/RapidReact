package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.WPI_PigeonIMU;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.RobotContainer;
import frc.robot.commands.AcquiringAssistanceCommand;
import frc.robot.commands.AlignToTargetCommand;
import frc.robot.commands.AutoCommand;

public class DrivebaseSubsystem extends SubsystemBase {
    private WPI_TalonFX m_rightFront = new WPI_TalonFX(Constants.RIGHT_FRONT_MOTOR_CAN_ID);
    private WPI_TalonFX m_rightBack = new WPI_TalonFX(Constants.RIGHT_REAR_MOTOR_CAN_ID);
    private WPI_TalonFX m_leftFront = new WPI_TalonFX(Constants.LEFT_FRONT_MOTOR_CAN_ID);
    private WPI_TalonFX m_leftBack = new WPI_TalonFX(Constants.LEFT_REAR_MOTOR_CAN_ID);

    private MecanumDrive m_robotDrive;

    private int m_drivingMode = Constants.ACQUIRING_DRIVING_MODE;

    private WPI_PigeonIMU m_pigeon = new WPI_PigeonIMU(Constants.PIGEON_CAN_ID);

    public DrivebaseSubsystem() {

        DriverStation.silenceJoystickConnectionWarning(true);

        configOpenLoopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);

        m_leftBack.follow(m_leftFront);
        m_rightBack.follow(m_rightFront);

        m_leftFront.setInverted(true);
        m_leftBack.setInverted(true);

        m_leftFront.setNeutralMode(NeutralMode.Brake);
        m_leftBack.setNeutralMode(NeutralMode.Brake);
        m_rightFront.setNeutralMode(NeutralMode.Brake);
        m_rightBack.setNeutralMode(NeutralMode.Brake);

        m_robotDrive = new MecanumDrive(m_leftFront, m_leftBack, m_rightFront, m_rightBack);
        m_pigeon.calibrate();

        RobotContainer.getRapidReactTab().add("is Aligning", AlignToTargetCommand.getIsAligning());
        RobotContainer.getRapidReactTab().add("Is in auto", AutoCommand.isInAuto());
        RobotContainer.getRapidReactTab().add("Is assisting driver", AcquiringAssistanceCommand.isAssistingDriver());
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
            Y = OI.getXboxLeftJoystickY();
            X = OI.getXboxLeftJoystickX();
            Z = OI.getXboxRightJoystickX();
        } else if(getControlType() == 20) {
            Y = -OI.getJoystickThrottleY();
            X = OI.getJoystickThrottleX();
            Z = OI.getJoystickThrottleZ();
        } else {
            Y = 0;
            X = 0;
            Z = 0;
        }

        if(m_drivingMode == Constants.SHOOTING_DRIVING_MODE) {
            Y = -Y;
            X = -X;
        } 

        if(!AlignToTargetCommand.getIsAligning() && 
           !AutoCommand.isInAuto() && 
           !AcquiringAssistanceCommand.isAssistingDriver()) {
               
            m_robotDrive.driveCartesian(deadband(-Y), deadband(X), deadband(Z));
        } else {
            m_robotDrive.feed();
        }

      }

    public void feed() {
        m_robotDrive.feed();
    }

    public void setMove(double y, double x, double z) {
        m_robotDrive.driveCartesian(y, x, z); 
    }

    public void stopMotors() {
        m_leftBack.stopMotor();
        m_leftFront.stopMotor();
        m_rightBack.stopMotor();
        m_rightFront.stopMotor();
    }

    public void setMotorsToCoast() {
        m_leftBack.setNeutralMode(NeutralMode.Coast);
        m_leftFront.setNeutralMode(NeutralMode.Coast);
        m_rightBack.setNeutralMode(NeutralMode.Coast);
        m_rightFront.setNeutralMode(NeutralMode.Coast);
    }

    public void setMotorsToBrake() {
        m_leftBack.setNeutralMode(NeutralMode.Brake);
        m_leftFront.setNeutralMode(NeutralMode.Brake);
        m_rightBack.setNeutralMode(NeutralMode.Brake);
        m_rightFront.setNeutralMode(NeutralMode.Brake);
    }

    // A value of 1 refers to the xbox controller, a value of 20 refers to the joystick.
    public int getControlType() {
        return DriverStation.getJoystickType(0);
    }

    public void setDrivingMode(int modeNum) {
        m_drivingMode = modeNum;
    }

    public int getDrivingMode() {
        return m_drivingMode;
    }

    public double getEncoderCount() {
        return m_leftBack.getSelectedSensorPosition();
    }

    public  WPI_PigeonIMU getPigeon() {
        return m_pigeon;
    }

    public WPI_TalonFX getRightFront() {
        return m_rightFront;
    }
    
    public WPI_TalonFX getRightBack() {
        return m_rightBack;
    }
    
    public WPI_TalonFX getLeftFront() {
        return m_leftFront;
    }
    
    public WPI_TalonFX getLeftBack() {
        return m_leftBack;
    }

    public void configOpenLoopRamp(double secondsToFull) {
        m_leftFront.configOpenloopRamp(secondsToFull);
        m_leftBack.configOpenloopRamp(secondsToFull);
        m_rightFront.configOpenloopRamp(secondsToFull);
        m_rightBack.configOpenloopRamp(secondsToFull);
    }

    public void resetHeading() {
        m_pigeon.setYaw(0.0);
        m_pigeon.setFusedHeading(0.0);
    }

    public double getYaw() {
        double[] ypr= new double[3];
        m_pigeon.getYawPitchRoll(ypr);
        return Math.IEEEremainder(ypr[0], 360.0d);
    }
}
