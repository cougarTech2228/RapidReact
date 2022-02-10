package frc.robot.util;

import java.util.Map;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.RobotContainer;



import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.OI;
import frc.robot.subsystems.DrivebaseSubsystem;

public class ButtonManager {
    private final OI m_oi = new OI();
    
    public ButtonManager() {

        configureButtonBindings();
        
    }

    public void configureButtonBindings() {
        Button button5 = new Button(OI::getJoystickButton5);
        Button xboxx = new Button(OI::getXboxXButton);

        button5.whenPressed(
            new InstantCommand(() -> {
                if(DrivebaseSubsystem.drivingMode == 1){
                    DrivebaseSubsystem.drivingMode = 0;
                } else {
                    DrivebaseSubsystem.drivingMode = 1;
                }
               
    
            })
        );

        xboxx.whenPressed(
            new InstantCommand(() -> {
                if(DrivebaseSubsystem.drivingMode == 1){
                    DrivebaseSubsystem.drivingMode = 0;
                } else {
                    DrivebaseSubsystem.drivingMode = 1;
                }
            })
        );
    }

}
