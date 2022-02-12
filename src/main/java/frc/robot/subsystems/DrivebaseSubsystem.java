package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.commands.AlignToTargetCommand;
import frc.robot.commands.AutonomousCommand;

public class DrivebaseSubsystem extends SubsystemBase {
    private WPI_TalonFX m_rightFront = new WPI_TalonFX(Constants.RIGHT_FRONT_MOTOR_CAN_ID);
    private WPI_TalonFX m_rightBack = new WPI_TalonFX(Constants.RIGHT_REAR_MOTOR_CAN_ID);
    private WPI_TalonFX m_leftFront = new WPI_TalonFX(Constants.LEFT_FRONT_MOTOR_CAN_ID);
    private WPI_TalonFX m_leftBack = new WPI_TalonFX(Constants.LEFT_REAR_MOTOR_CAN_ID);

    private MecanumDrive m_robotDrive;

    private int m_drivingMode = 0;

    public DrivebaseSubsystem() {

        DriverStation.silenceJoystickConnectionWarning(true);

        m_leftFront.configOpenloopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);
        m_leftBack.configOpenloopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);
        m_rightFront.configOpenloopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);
        m_rightBack.configOpenloopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);

        m_leftBack.follow(m_leftFront);
        m_rightBack.follow(m_rightFront);

        m_leftFront.setInverted(true);
        m_leftBack.setInverted(true);

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

        if(!AlignToTargetCommand.getAlignMode() && !AutonomousCommand.getIsAuto())// && !DriverStation.isAutonomous())
        {
        m_robotDrive.driveCartesian(deadband(-Y),
                deadband(X),
                deadband(Z));
        } else {
            m_robotDrive.feed();
        }
    }

    public void turn(double speed){
        m_robotDrive.driveCartesian(0, 0, speed);
    }
    public void setMove(double ySpeed, double xSpeed){
        m_robotDrive.driveCartesian(ySpeed, xSpeed, 0);
    }
    public void setMotorsToCoast(){
        m_leftBack.setNeutralMode(NeutralMode.Coast);
        m_leftFront.setNeutralMode(NeutralMode.Coast);
        m_rightBack.setNeutralMode(NeutralMode.Coast);
        m_rightFront.setNeutralMode(NeutralMode.Coast);
    }
    public void setMotorsToBrake(){
        m_leftBack.setNeutralMode(NeutralMode.Brake);
        m_leftFront.setNeutralMode(NeutralMode.Brake);
        m_rightBack.setNeutralMode(NeutralMode.Brake);
        m_rightFront.setNeutralMode(NeutralMode.Brake);
    }
    //A value of 1 refers to the xbox controller, a value of 20 refers to the joystick.
    public int getControlType(){
        return DriverStation.getJoystickType(0);
    } 
    public void setDrivingMode(int modeNum){
        m_drivingMode = modeNum;
    }
    public int getDrivingMode(){
        return m_drivingMode;
    }
}
