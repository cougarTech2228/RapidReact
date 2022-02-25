package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;

public class AlignToTargetCommand extends CommandBase {
    private boolean needsToTurn = true;
    private static boolean kIsAligning = false;
    private DrivebaseSubsystem m_drivebaseSubsystem;
    private ShooterVisionSubsystem m_shooterVisionSubsystem;

    public AlignToTargetCommand(DrivebaseSubsystem drivebaseSubsystem, ShooterVisionSubsystem shooterVisionSubsystem){
        needsToTurn = true;
        m_drivebaseSubsystem = drivebaseSubsystem;
        m_shooterVisionSubsystem = shooterVisionSubsystem;
        addRequirements(drivebaseSubsystem);
    }

    @Override
    public void initialize() {
        //m_shooterVisionSubsystem.setCameras(Constants.SHOOTING_DRIVING_MODE);
    }

    @Override
    public void execute() {
        double misalignment = ShooterVisionSubsystem.getDeviationFromCenter(); //negative means needs to turn clockwise
        needsToTurn = Math.abs(misalignment) > Constants.MAX_MISALIGNMENT_VALUE;
        if(!needsToTurn)
            return;
        kIsAligning = true;
        if(misalignment < 0)
            m_drivebaseSubsystem.setMove(0, 0, -Constants.ALIGNING_TURN_SPEED);
        else
            m_drivebaseSubsystem.setMove(0, 0, Constants.ALIGNING_TURN_SPEED);
    }

    @Override
    public void end(boolean interrupted){
        kIsAligning = false;
    }

    @Override
    public boolean isFinished(){
        return !needsToTurn;
    }

    public static boolean getIsAligning(){
        return kIsAligning;
    }
}
