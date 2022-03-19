package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.commands.ShooterCommand.ShotType;
import frc.robot.subsystems.AcquisitionSubsystem;
import frc.robot.subsystems.CargoVisionSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class AutoCommand extends SequentialCommandGroup{
    private static boolean m_isInAuto = false;
    private static ShooterVisionSubsystem m_shooterVisionSubsystem;
    private static StorageSubsystem m_storageSubsystem;
    private static DrivebaseSubsystem m_drivebaseSubsystem;
    private static ShooterSubsystem m_shooterSubsystem;
    private static AcquisitionSubsystem m_acquisitionSubsystem;
    private static CargoVisionSubsystem m_cargoVisionSubsystem;

    private static boolean m_isHigh;
    private static AutoPosition m_position;
    private static boolean m_shouldGoToTerminal;
    private static boolean m_searchForBall;

    // Read left to right from corresponding driver station
    public enum AutoPosition {
        Position1,
        Position2,
        Position3
    }

    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    public AutoCommand(ShooterVisionSubsystem shooterVisionSubsystem, DrivebaseSubsystem drivebaseSubsystem, 
                                ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem, 
                                AcquisitionSubsystem acquisitionSubsystem, CargoVisionSubsystem cargoVisionSubsystem, 
                                boolean isHigh, AutoPosition position, boolean shouldGoToTerminal, boolean searchForBall) {

        m_shooterVisionSubsystem = shooterVisionSubsystem;
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_shooterSubsystem = shooterSubsystem;
        m_storageSubsystem = storageSubsystem;
        m_acquisitionSubsystem = acquisitionSubsystem;
        m_cargoVisionSubsystem = cargoVisionSubsystem;
        m_isHigh = isHigh;
        m_position = position;
        m_shouldGoToTerminal = shouldGoToTerminal;
        m_searchForBall = searchForBall;

        addFirstMoveCommands();

        addShootCommands();
        
        addSecondMoveCommands();

        addTerminalCommands();

        addSearchForBallCommands();
    }

    @Override
    public void end(boolean interrupted) {
        m_shooterVisionSubsystem.setCameras(Constants.ACQUIRING_DRIVING_MODE);
        //m_storageSubsystem.stopMotors();
        AcquiringAssistanceCommand.setAssistingDriver(false);
        m_isInAuto = false;
    }

    public static boolean isInAuto(){
        return m_isInAuto;
    }

    private void addFirstMoveCommands() {

        double firstDistance;

        if(m_position == AutoPosition.Position3) {
            firstDistance = Constants.TO_BALL_DISTANCE_POS3; // Go a shorter distance for position 3
        } else {
            firstDistance = Constants.TO_BALL_DISTANCE_POS12;
        }

        addCommands(
            new InstantCommand(() -> {
                m_isInAuto = true;
                m_shooterVisionSubsystem.setCameras(Constants.SHOOTING_DRIVING_MODE);
                m_acquisitionSubsystem.deployAcquirer();
                m_acquisitionSubsystem.setSpinnerMotor(-Constants.ACQUIRER_SPINNER_SPEED);
                m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED * 2);
                
            }),
            new WaitCommand(0.5),
            new DriveCommand(firstDistance, Constants.AUTO_MOVE_SPEED, m_drivebaseSubsystem),
            new WaitCommand(0.5), 
            new InstantCommand(() -> {
                m_acquisitionSubsystem.setSpinnerMotor(0);
                m_storageSubsystem.stopMotors();
                if(m_position == AutoPosition.Position3) {
                    //m_acquisitionSubsystem.retractAcquirer(); TODO
                }
            })
        );
    }

    private void addShootCommands() {
        if(m_isHigh) {
            addCommands(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, ShotType.HIGH));
        } else {
            addCommands(new DriveCommand(-Constants.TO_HUB_FROM_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, m_drivebaseSubsystem));
            if(m_position == AutoPosition.Position2) {
                addCommands(new AutoAngleTurnCommand(m_drivebaseSubsystem, 30));
            }
            addCommands(
                new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, ShotType.LOW)
            );
        }
    }

    private void addSecondMoveCommands() {
        // If we want the bot to be outside the tarmac, and we did a low shoot, move outside tarmac
        if(!m_isHigh) {
            addCommands(new DriveCommand(Constants.TO_HUB_FROM_BALL_DISTANCE, Constants.AUTO_MOVE_SPEED, m_drivebaseSubsystem));
        }

        // If we just shot high and we are to remain outside the tarmac, move a little more out to ensure taxi point
        if(m_isHigh && m_position != AutoPosition.Position3) {
            int distance = 20;
            addCommands(new DriveCommand(distance, Constants.AUTO_MOVE_SPEED, m_drivebaseSubsystem));
        }
    }

    private void addTerminalCommands() {
        if(m_shouldGoToTerminal && !m_searchForBall) {

            double angle;
            double distance;
            double secondDistance;

            switch(m_position) {
                case Position1:
                    angle = -100;
                    distance = 577;
                    secondDistance = distance;
                    break;
                case Position2:
                    angle = 23.5;
                    distance = 365;
                    secondDistance = distance;
                    break;
                case Position3:
                    angle = 81.5;
                    distance = 615;
                    secondDistance = 100;
                    break;
                default:
                    angle = 0;
                    distance = 0;
                    secondDistance = 0;
                    System.out.println("4th Position????");
                    break;
                
            }

            addCommands(
                new AutoAngleTurnCommand(m_drivebaseSubsystem, angle),
                new InstantCommand(() -> {
                    m_acquisitionSubsystem.setSpinnerMotor(-Constants.ACQUIRER_SPINNER_SPEED);
                    m_storageSubsystem.setConveyorMotor(Constants.STORAGE_CONVEYOR_SPEED * 2);
                    if(m_position == AutoPosition.Position3) {
                        //m_acquisitionSubsystem.deployAcquirer(); TODO
                    }
                }),
                new DriveCommand(distance, Constants.AUTO_MOVE_SPEED, m_drivebaseSubsystem),
                new DriveCommand(-secondDistance, Constants.AUTO_MOVE_SPEED, m_drivebaseSubsystem)
            );

            
            if(m_position == AutoPosition.Position2) {
                addCommands(new ShooterCommand(m_shooterVisionSubsystem, m_shooterSubsystem, m_storageSubsystem, m_drivebaseSubsystem, -angle));
            }
            

            
        }
    }

    private void addSearchForBallCommands() {
        if(m_searchForBall && !m_shouldGoToTerminal) {
            
            double speed;

            // Ensures the bot would spin away from the center line to avoid penalty
            if(m_position == AutoPosition.Position1) {
                speed = -0.15;
            } else {
                speed = 0.15;
            }

            addCommands(
                new SpinWhileCommand(m_drivebaseSubsystem, speed, (() -> m_cargoVisionSubsystem.getBestBall() == null)),
                new AcquiringAssistanceCommand(m_cargoVisionSubsystem, m_drivebaseSubsystem, m_acquisitionSubsystem, m_storageSubsystem, true)//,
                //new SpinWhileCommand(drivebaseSubsystem, -speed, (() -> Double.isNaN(ShooterVisionSubsystem.getDeviationFromCenter()))),
                //new ShooterCommand(shooterVisionSubsystem, shooterSubsystem, storageSubsystem, drivebaseSubsystem, ShotType.HIGH_AUTO)
            );
        }
    }
}



