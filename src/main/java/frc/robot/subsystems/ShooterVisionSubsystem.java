package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterVisionSubsystem extends SubsystemBase{
    static NetworkTable m_shooterVisionTable = NetworkTableInstance.getDefault().getTable("Hub");
    static NetworkTableEntry m_averageHeight = m_shooterVisionTable.getEntry("averageHeight"); //last 10 distances calculated as a rolling average
    static NetworkTableEntry m_deviationFromCenter = m_shooterVisionTable.getEntry("deviationFromCenter");
    static NetworkTableEntry m_currentCamera = m_shooterVisionTable.getEntry("currentCamera");

    private DigitalOutput cameraSwitch = new DigitalOutput(Constants.CAMERA_SWITCH_DIO);

    public static double getMotorSpeedAtDistance(){
        //System.out.println(m_averageHeight.getDouble(260) + " " + (.55 + .001689 * (m_averageHeight.getDouble(260) - 239)));
        return (.74 + .00096 * (m_averageHeight.getDouble(260) - 443));
    }
    
    public static double getDeviationFromCenter(){
        return m_deviationFromCenter.getDouble(-1.0);
    }

    public void setCameras(int mode){
        if(mode == Constants.SHOOTING_DRIVING_MODE){
            m_currentCamera.setString("Target");
            cameraSwitch.set(true);
        }
        else{
            m_currentCamera.setString("Ball");
            cameraSwitch.set(false);
        }
    }

    @Override
    public void periodic() {}
}
