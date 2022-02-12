package frc.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterVisionSubsystem extends SubsystemBase{
    static NetworkTable m_shooterVisionTable = NetworkTableInstance.getDefault().getTable("Hub");
    static NetworkTableEntry m_distanceFt = m_shooterVisionTable.getEntry("distanceFeet"); //last 10 distances calculated as a rolling average
    static NetworkTableEntry m_deviationFromCenter = m_shooterVisionTable.getEntry("deviationFromCenter");
    static NetworkTableEntry m_currentCamera = m_shooterVisionTable.getEntry("currentCamera");
    PowerDistribution m_pDp = new PowerDistribution();

    public static double getDistanceFt(){
        return m_distanceFt.getDouble(-1.0);
    }
    public static double getDeviationFromCenter(){
        return m_deviationFromCenter.getDouble(0);
    }

    public void setCameras(int mode){
        if(mode == Constants.SHOOTING_DRIVING_MODE){
            m_currentCamera.setString("Target");
            m_pDp.setSwitchableChannel(true);
        }
        else{
            m_currentCamera.setString("Ball");
            m_pDp.setSwitchableChannel(false);
        }
    }
}
