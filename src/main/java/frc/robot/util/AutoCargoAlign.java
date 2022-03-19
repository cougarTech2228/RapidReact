package frc.robot.util;


import frc.robot.Constants;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.CargoVisionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.subsystems.StorageSubsystem.BallType;

public class AutoCargoAlign implements Runnable {

    private CargoVisionSubsystem m_cargoVisionSubsystem;
    private DrivebaseSubsystem m_drivebaseSubsystem;
    private AcquisitionSubsystem m_acquisitionSubsystem;
    private StorageSubsystem m_storageSubystem;
  
    private int m_iterationsWithNoBall;
    private boolean m_useColorSensorAsCondition;
  
    public enum AssistanceState {
        ASST_ALIGNING,
        ASST_ALIGNED
    }
  
    public AssistanceState m_assistanceState;
  
    public AutoCargoAlign(CargoVisionSubsystem cargoVisionSubsystem, 
                            DrivebaseSubsystem drivebaseSubsystem, 
                            AcquisitionSubsystem acquisitionSubsystem,
                            StorageSubsystem storageSubsystem, 
                            boolean useColorSensorAsCondition) {
        m_cargoVisionSubsystem = cargoVisionSubsystem;
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_acquisitionSubsystem = acquisitionSubsystem;
        m_storageSubystem = storageSubsystem;
        m_useColorSensorAsCondition = useColorSensorAsCondition;
    }

    public void run() {
        System.out.println("Initiating Auto Cargo Alignment...");

        m_drivebaseSubsystem.configOpenLoopRamp(0.0);
        m_drivebaseSubsystem.resetHeading();
        m_drivebaseSubsystem.setMotorsToBrake();

        m_iterationsWithNoBall = 0;
        m_assistanceState = AssistanceState.ASST_ALIGNING;

        if (m_useColorSensorAsCondition) {
            while (m_storageSubystem.getCurrentBall() == BallType.None) { //&& 
                  //(m_iterationsWithNoBall < (Constants.ASSISTANCE_LOST_BALL_TIME * 50))) {    
                    getBall();
                   }
        } else {
            while (m_iterationsWithNoBall < (Constants.ASSISTANCE_LOST_BALL_TIME * 50)) {
                getBall();
            }
        }

        //System.out.println("is red ball? " + (m_storageSubystem.getCurrentBall() == BallType.RedBall));
        //System.out.println("is blue ball? " + (m_storageSubystem.getCurrentBall() == BallType.BlueBall));
        //System.out.println("no balls? " + (m_storageSubystem.getCurrentBall() == BallType.None));

        m_acquisitionSubsystem.stopSpinnerMotor();
        m_storageSubystem.stopMotors();
        m_drivebaseSubsystem.configOpenLoopRamp(Constants.OPEN_RAMP_SECONDS_TO_FULL);
        
        System.out.println("Auto Cargo Alignment complete");
    }

    private void getBall() {
        Balls ball = m_cargoVisionSubsystem.getBestBall();

        if(ball != null) {
            m_iterationsWithNoBall = 0;
            // Calculate where the center of the ball is on the x axis
            double mid = ((ball.getBox().getXmax() - ball.getBox().getXmin()) / 2.0) + ball.getBox().getXmin();
            // Get the center of the screen (center of robot)
            double center = m_cargoVisionSubsystem.getWidth() / 2.0;
            // Calculate the distance of the ball from the center.
            double offset = mid - center;
    
            switch(m_assistanceState) {
                case ASST_ALIGNING:
                    if(Math.abs(offset) < Constants.ASSISTANCE_RANGE) {
                        m_drivebaseSubsystem.setMove(0, 0, 0);
                        m_assistanceState = AssistanceState.ASST_ALIGNED;
                    } else {
                        if(ball.getSurfaceArea() < Constants.ASSISTANCE_SA_RANGE) {
                            if(offset < 0)
                                m_drivebaseSubsystem.setMove(0, 0, -Constants.ASSISTANCE_TURN_SPEED);
                            else  
                                m_drivebaseSubsystem.setMove(0, 0, Constants.ASSISTANCE_TURN_SPEED);
                        } else {
                            if(offset < 0) 
                                m_drivebaseSubsystem.setMove(0, -Constants.ASSISTANCE_STRAFE_SPEED, 0);
                            else
                                m_drivebaseSubsystem.setMove(0, Constants.ASSISTANCE_STRAFE_SPEED, 0);
                        }
                    }
                    break;
                case ASST_ALIGNED:
                    if(Math.abs(offset) < Constants.ASSISTANCE_RANGE) {
                        if(ball.getSurfaceArea() < Constants.ASSISTANCE_SA_RANGE) {
                            m_drivebaseSubsystem.setMove(Constants.ASSISTANCE_FORWARD_FAST_SPEED, 0, 0);
                        } else {
                            m_acquisitionSubsystem.setSpinnerMotor(Constants.ACQUIRER_SPINNER_SPEED);
                            m_storageSubystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED * 2);
                            m_drivebaseSubsystem.setMove(Constants.ASSISTANCE_FORWARD_SLOW_SPEED, 0, 0);
                        }
                    } else {
                        m_assistanceState = AssistanceState.ASST_ALIGNING;
                    }
                    break;
                default:
                    System.out.println("Auto Cargo Alignment - Unknown State");
            }
        } else {
            m_iterationsWithNoBall++;
        }    
    }
}

