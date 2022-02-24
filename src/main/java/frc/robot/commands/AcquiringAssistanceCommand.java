// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.RobotContainer;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.CargoVisionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.subsystems.StorageSubsystem.BallType;
import frc.robot.util.Balls;

public class AcquiringAssistanceCommand extends CommandBase {

  private CargoVisionSubsystem m_cargoVisionSubsystem;
  private DrivebaseSubsystem m_driveBaseSubsystem;
  private AcquisitionSubsystem m_acquisitionSubsystem;
  private StorageSubsystem m_storageSubystem;

  private static boolean m_isAssistingDriver;
  private boolean m_isAlignedToBall;

  private int m_iterationsWithNoBall;

  private boolean m_isLinedUpToBall;

  private int m_horizontalAlignState;
  private int m_verticalAlignState;

  private double m_startTime;

  /** Creates a new AcquiringAssistanceCommand. */
  public AcquiringAssistanceCommand(CargoVisionSubsystem cargoVisionSubsystem, DrivebaseSubsystem drivebaseSubsystem, 
                                    AcquisitionSubsystem acquisitionSubsystem, StorageSubsystem storageSubsystem) {
    m_cargoVisionSubsystem = cargoVisionSubsystem;
    m_driveBaseSubsystem = drivebaseSubsystem;
    m_acquisitionSubsystem = acquisitionSubsystem;
    m_storageSubystem = storageSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("fdjfgkajdgffd");
    m_isAssistingDriver = true;
    m_isAlignedToBall = false;
    m_iterationsWithNoBall = 0;
    m_horizontalAlignState = Constants.ASSISTANCE_NO_MOVEMENT;
    m_verticalAlignState = Constants.ASSISTANCE_NO_MOVEMENT;
  }

  /**
   * ROBOT ASSISTANCE SEQUENCE
   * 1) Check the distance away the ball is from the robot.
   *    a. If the ball is far from the robot go to step 2
   *    b. If the ball is close to the robot go to step 3
   * 
   * 2) Ball is far away so to be efficient, robot will turn towards the ball.
   *    a. If the robot is not lined up, keep turning the correct direction until it is. Repeat this step.
   *    b. If the robot is lined up move fast towards the ball. Will eventually go to 1b)
   * 
   * 2.5) If the ball is to far to the side of the camera go to step 2
   * 
   * 3) Ball is close to the robot, robot will strafe to be aligned with ball.
   *    a. If the robot is not aligned, keep strafing until it is. Repeat this step.
   *    b. If the robot is aligned to the ball, move slowly towards the ball. 
   * 
   * End) When the robot has either lost sight of the ball for n seconds or if the ball is acquired.
   * 
   */

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    Balls ball = m_cargoVisionSubsystem.getBestBall();

      if(ball != null) { // Make sure there is a ball to be looking at
        m_iterationsWithNoBall = 0;
        double surfaceArea = ball.getSurfaceArea();

        // If the ball is far enough away (measured with surface area), go into turn mode
        if(surfaceArea < Constants.ASSISTANCE_SA_RANGE) {
          m_horizontalAlignState = Constants.ASSISTANCE_TURN; // turn
        } else { // Otherwise go into strafing mode
          m_verticalAlignState = Constants.ASSISTANCE_NO_MOVEMENT;
          m_horizontalAlignState = Constants.ASSISTANCE_STRAFE; // strafe
        }

        // Calculate where the center of the ball is on the x axis
        double mid = (ball.getBox().getXmax() + ball.getBox().getXmin()) / 2.0;
        // Get the center of the screen (center of robot)
        double center = m_cargoVisionSubsystem.getWidth() / 2.0;
        // Calculate the distance of the ball from the center.
        double offset = mid - center;

        // If the robot needs to turn to face the ball,
        if(m_horizontalAlignState == Constants.ASSISTANCE_TURN) {
          m_isLinedUpToBall = Math.abs(offset) < Constants.ASSISTANCE_TURN_RANGE;

          // If lined up to the ball, bot needs to go forward fast to the ball
          if(m_isLinedUpToBall) {
            m_driveBaseSubsystem.turn(0);
            m_verticalAlignState = Constants.ASSISTANCE_FORWARD_FAST;
          } else { // Otherwise turn the corresponding way toward the ball
            if(offset < 0)
              m_driveBaseSubsystem.turn(-Constants.ASSISTANCE_TURN_SPEED);
            else  
              m_driveBaseSubsystem.turn(Constants.ASSISTANCE_TURN_SPEED);
          }
        }

        /**
         * A check to see if the ball is nearing the edge of the camera when the robot is in turn mode.
         * This would probably happen when the ball is rolling as the robot is aligning to it.
         * This will put the robot back in the turn state so it readjusts another time before reaching
         * the strafe phase of the alignment process.
         */
        // if(Math.abs(offset) > Constants.ASSISTANCE_NEWTURN_RANGE){

        //   m_horizontalAlignState = Constants.ASSISTANCE_TURN;
        //   m_verticalAlignState = Constants.ASSISTANCE_NO_MOVEMENT;
        // }

        // If the robot needs to strafe to align to the ball,
        if(m_horizontalAlignState == Constants.ASSISTANCE_STRAFE) {
          m_isAlignedToBall = Math.abs(offset) < Constants.ASSISTANCE_STRAFE_RANGE;

          // If the robot is aligned to the ball, bot needs to go slow towards the ball.
          if(m_isAlignedToBall) {
            m_verticalAlignState = Constants.ASSISTANCE_FORWARD_SLOW;
          } else { // Otherwise the robot needs to strafe
            if(offset < 0) {
              m_driveBaseSubsystem.setMove(0, -Constants.ASSISTANCE_STRAFE_SPEED);
            } else {
              m_driveBaseSubsystem.setMove(0, Constants.ASSISTANCE_STRAFE_SPEED);
            }
          }
        }

        // If the bot needs to go fast, done after the bot has turned towards the ball at a farther distance
        if(m_verticalAlignState == Constants.ASSISTANCE_FORWARD_FAST) {
          m_driveBaseSubsystem.setMove(Constants.ASSISTANCE_FORWARD_FAST_SPEED, 0);
        }

        // If the bot needs to go slow, done after the bot has strafed into alignment with the ball at a closer distance
        if(m_verticalAlignState == Constants.ASSISTANCE_FORWARD_SLOW) {
          m_startTime = Timer.getFPGATimestamp();
          m_acquisitionSubsystem.setSpinnerMotor(Constants.ACQUIRER_SPINNER_SPEED);
          // Faster conveyor speed so the ball gets in the robot faster
          m_storageSubystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED * 2);
          m_driveBaseSubsystem.setMove(Constants.ASSISTANCE_FORWARD_SLOW_SPEED, 0);
        }
        
      } else {
        System.out.println(m_iterationsWithNoBall);
        // If the ball is not seen, add an iteration to this variable as part of the end condition.
        m_iterationsWithNoBall++;
      }

    
    SmartDashboard.putBoolean("Should be turning", (m_horizontalAlignState == 0));
    SmartDashboard.putBoolean("Should be strafing", (m_horizontalAlignState == 1));
    SmartDashboard.putBoolean("Should be forward fast", (m_verticalAlignState == 0));
    SmartDashboard.putBoolean("Should be forward slow", (m_verticalAlignState == 1));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
   System.out.println("end assistance: " + m_iterationsWithNoBall);
    m_acquisitionSubsystem.stopSpinnerMotor();
    m_storageSubystem.stopMotors();

    m_isAssistingDriver = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Stop when the robot loses the ball for n iterations

     
    // When the ball isn't seen for n seconds. Either due to the ball rolling out of robot's
    // vision or if the robot acquired the ball
    return m_iterationsWithNoBall >= (Constants.ASSISTANCE_LOST_BALL_TIME * 50);

    
  }

  public static boolean isAssistingDriver() {
    return m_isAssistingDriver;
  }
}
