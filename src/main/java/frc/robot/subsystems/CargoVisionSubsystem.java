// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.OI;
import frc.robot.RobotContainer;
import frc.robot.commands.AcquiringAssistanceCommand;
import frc.robot.util.Balls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;

public class CargoVisionSubsystem extends SubsystemBase {

  private NetworkTable m_cargoVisionTable = NetworkTableInstance.getDefault().getTable("ML");
  private NetworkTableEntry m_jsonStringEntry = m_cargoVisionTable.getEntry("detections");
  private NetworkTableEntry m_cameraResolution = m_cargoVisionTable.getEntry("resolution");

  private String m_jsonString;

  private GsonBuilder m_gsonBuilder;
  private Gson m_gson;

  private boolean m_isRumbling;
  private ArrayList<Balls> m_balls;
  

  /** Creates a new CargoVisionSubsystem. */
  public CargoVisionSubsystem() {
    m_gsonBuilder = new GsonBuilder();
    m_gsonBuilder.setPrettyPrinting();

    m_gson = m_gsonBuilder.create();

    m_isRumbling = false;
    
  }

  @Override
  public void periodic() {
    
    m_jsonString = m_jsonStringEntry.getString("[]");

    if(m_jsonString.length() > 0 && !m_jsonString.equals("[]"))
      m_balls = new ArrayList<Balls>(Arrays.asList(m_gson.fromJson(m_jsonString, Balls[].class)));
    else {
      if(m_balls != null) {
        m_balls.clear();
      }
    } 
    
    Balls ball = getBestBall();

    if(ball != null) {
      SmartDashboard.putNumber("Surface area", ball.getSurfaceArea());
      if(!m_isRumbling || AcquiringAssistanceCommand.isAssistingDriver()) {
        OI.setXboxRumbleSpeed(0.1);
        m_isRumbling = true;
      }
    } else {
      if(m_isRumbling) {
        OI.setXboxRumbleStop();
        m_isRumbling = false;
      }
    }

    // This method will be called once per scheduler run
  }

  public List<Balls> getCargoDetections() {
      return m_balls;
  }

  public Balls getBestBall() {
    if(m_balls != null)
    if(m_balls.size() > 0) {
        Balls bestBall = null;
        for (int i = 0; i < m_balls.size(); i++) {
            if(m_balls.get(i).getLabel().equals(RobotContainer.getTeamColor())) {
                if(bestBall == null) {
                  bestBall = m_balls.get(i);
                } else if(m_balls.get(i).getSurfaceArea() > bestBall.getSurfaceArea()) {
                    bestBall = m_balls.get(i);
                }
            }
        }
        return bestBall;
    }
    return null;
    
    
  }

  public int getWidth() {
    String width = m_cameraResolution.getString("320, 240").split(",")[0];
    return Integer.valueOf(width);
   }

  

}


