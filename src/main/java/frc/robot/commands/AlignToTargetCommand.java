package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.ShooterVisionSubsystem;

public class AlignToTargetCommand extends CommandBase {
    private boolean needsToTurn;
    private static boolean kIsAligning;
    private DrivebaseSubsystem m_drivebaseSubsystem;
    public AlignToTargetCommand(DrivebaseSubsystem drivebaseSubsystem){
        needsToTurn = true;
        m_drivebaseSubsystem = drivebaseSubsystem;
        addRequirements(drivebaseSubsystem);
    }

    @Override
    public void execute(){
        double misalignment = ShooterVisionSubsystem.getDeviationFromCenter(); //negative means needs to turn clockwise
        needsToTurn = Math.abs(misalignment) > Constants.MAX_MISALIGNMENT_VALUE;
        if(!needsToTurn)
            return;
        kIsAligning = true;
        if(misalignment < 0)
            m_drivebaseSubsystem.turn(Constants.ALIGNING_TURN_SPEED);
        else
            m_drivebaseSubsystem.turn(-Constants.ALIGNING_TURN_SPEED);
    }

    @Override
    public void end(boolean interrupted){
        kIsAligning = false;
    }

    @Override
    public boolean isFinished(){
        return needsToTurn;
    }

    public static boolean getAlignMode(){
        return kIsAligning;
    }
}
