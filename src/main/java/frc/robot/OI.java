package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */

public class OI {
    
    private static final int kXboxChannel = 0;
    private static final int kJoystickChannel = 0;

    private static MecanumDrive m_robotDrive;
    private static Joystick m_stick;  
    private static XboxController m_xboxController;
   
    public OI() {
        m_stick = new Joystick(kJoystickChannel);
        m_xboxController = new XboxController(kXboxChannel);
    }

    public static boolean getJoystickTrigger() {
        return m_stick.getTrigger();
    }

    public static boolean getJoystickButton11() {
        return m_stick.getRawButton(11);
    }

    public static boolean getJoystickButton12() {
        return m_stick.getRawButton(12);
    }

    public static boolean getJoystickButton10() {
        return m_stick.getRawButton(10);
    }

    public static boolean getJoystickButton9() {
        return m_stick.getRawButton(9);
    }

    public static boolean getJoystickButton8() {
        return m_stick.getRawButton(8);
    }

    public static boolean getJoystickButton7() {
        return m_stick.getRawButton(7);
    }

    public static boolean getJoystickButton6() {
        return m_stick.getRawButton(6);
    }

    public static boolean getJoystickButton5() {
        return m_stick.getRawButton(5);
    }

    public static boolean getJoystickButton4() {
        return m_stick.getRawButton(4);
    }

    public static boolean getJoystickButton3() {
        return m_stick.getRawButton(3);
    }

    public static boolean getJoystickButton2() {
        return m_stick.getRawButton(2);
    }
   
    public static boolean getJoystickHatUp() {
        return m_stick.getPOV() == 0;
    }

    public static boolean getJoystickHatUpRight() {
        return m_stick.getPOV() == 45;
    }

    public static boolean getJoystickHatRight() {
        return m_stick.getPOV() == 90;
    }

    public static boolean getJoystickHatRightDown() {
        return m_stick.getPOV() == 135;
    }

    public static boolean getJoystickHatDown() {
        return m_stick.getPOV() == 180;
    }

    public static boolean getJoystickHatDownLeft() {
        return m_stick.getPOV() == 225;
    }

    public static boolean getJoystickHatLeft() {
        return m_stick.getPOV() == 270;
    }

    public static boolean getJoystickHatUpLeft() {
        return m_stick.getPOV() == 315;
    }

    // When the slider is pushed closest to the -, a value of -1.0 is returned, 1.0 is returned at the plus.
    public static double getJoystickSliderValue() {
        return -m_stick.getThrottle();
    }

    // Pushing the joystick fully left returns a value of -1.0, pushing it fully right returns a value of 1.0.
    public static double getJoystickThrottleX() {
        return m_stick.getX();
    }

    // Pushing the joystick fully forwards returns a value of 1.0, pushing it fully backwards returns a value of -1.0.
    public static double getJoystickThrottleY() {       
        return -m_stick.getY();
    }

    // Twisting the joystick fully left returns a value of -1.0, twisting it fully right returns a value of 1.0.
    public static double getJoystickThrottleZ() {
        return m_stick.getZ();
    }

    public static Joystick getJoystick() {
        return m_stick;
    }

    public static boolean getXboxAButton() {
        return m_xboxController.getAButton();
    }

    public static boolean getXboxBButton() {
        return m_xboxController.getBButton();
    }

    public static boolean getXboxXButton() {
        return m_xboxController.getXButton();
    }

    public static boolean getXboxYButton() {
        return m_xboxController.getYButton();
    }

    public static boolean getXboxStartButton() {
        return m_xboxController.getStartButton();
    }

    public static boolean getXboxBackButton() {
        return m_xboxController.getBackButton();
    }

    public static boolean getXboxRightBumper() {
        return m_xboxController.getRightBumper();
    }

    public static boolean getXboxLeftBumper() {
        return m_xboxController.getLeftBumper();
    }

    public static boolean getXboxLeftJoystickPress() {
        return m_xboxController.getLeftStickButton();
    }

    public static boolean getXboxRightJoystickPress() {
        return m_xboxController.getRightStickButton();
    }

    public static double getXboxRightTrigger() {
        return m_xboxController.getRightTriggerAxis();
    }

    public static boolean getXboxRightTriggerPressed() {
        return m_xboxController.getRightTriggerAxis() > 0.2;
    }

    public static double getXboxLeftTrigger() {
        return m_xboxController.getLeftTriggerAxis();
    }

    public static boolean getXboxLeftTriggerPressed() {
        return m_xboxController.getLeftTriggerAxis() > 0.2;
    }

    public static double getXboxRightJoystickX() {
        return m_xboxController.getRightX();
    }

    public static double getXboxRightJoystickY() {
        return m_xboxController.getRightY();
    }

    public static double getXboxLeftJoystickX() {
        return m_xboxController.getLeftX();
    }

    public static double getXboxLeftJoystickY() {
        return m_xboxController.getLeftY();
    }

    public static boolean getXboxDpadUp() {
        int pov = m_xboxController.getPOV(0);
        return (((pov >= 0) && (pov <= 45)) || ((pov >= 315) && (pov <= 360)));
    }

    public static boolean getXboxDpadRight() {
        int pov = m_xboxController.getPOV(0);
        return ((pov >= 45) && (pov <= 135));
    }

    public static boolean getXboxDpadDown() {
        int pov = m_xboxController.getPOV(0);
        return ((pov >= 135) && (pov <= 225));
    }

    public static boolean getXboxDpadLeft() {
        int pov = m_xboxController.getPOV(0);
        return ((pov >= 225) && (pov <= 315));
    }

    public static void setXboxRumbleSpeed(double rumbleSpeed) {
        m_xboxController.setRumble(RumbleType.kLeftRumble, rumbleSpeed);
        m_xboxController.setRumble(RumbleType.kRightRumble, rumbleSpeed);
    }

    public static void setXboxLeftRumbleSpeed(double rumbleSpeed) {
        m_xboxController.setRumble(RumbleType.kLeftRumble, rumbleSpeed);
    }

    public static void setXboxRightRumbleSpeed(double rumbleSpeed) {
        m_xboxController.setRumble(RumbleType.kRightRumble, rumbleSpeed);
    }

    public static void setXboxRumbleStop() {
        OI.setXboxRumbleSpeed(0);
    }
    
    public static XboxController getXboxController() {
        return m_xboxController;
    }

   // DriverStation.getJoystickName(m_stick)){
        //return 

   // }

}
