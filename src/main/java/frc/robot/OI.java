package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class OI {
    
    private static PS4Controller m_ps4controller;

    public OI() {
        m_ps4controller = new PS4Controller(0);
    }

    public static boolean getCircleButton() {
        return m_ps4controller.getCircleButton();
    }

    public static boolean getTriangleButton() {
        return m_ps4controller.getTriangleButton();
    }

    public static boolean getSquareButton() {
        return m_ps4controller.getSquareButton();
    }

    public static boolean getCrossButton() {
        return m_ps4controller.getCrossButton();
    }

    public static boolean getShareButton() {
        return m_ps4controller.getShareButton();
    }

    public static boolean getOptionsButton() {
        return m_ps4controller.getOptionsButton();
    }

    // They got weird bindings in their library. Their L2 is really R2
    public static boolean getR2Button() {
        return m_ps4controller.getL2Axis() == 1;
    }

    public static boolean getR1Button() {
        return m_ps4controller.getR1Button();
    }

    // They got weird bindings in their library. Their Right X is really L2
    public static boolean getL2Button() {
        return m_ps4controller.getRightX() == 1;
    }

    public static boolean getL1Button() {
        return m_ps4controller.getL1Button();
    }

    public static boolean getLeftJoystickPressed() {
        return m_ps4controller.getL3Button();
    }

    public static boolean getRightJoystickPressed() {
        return m_ps4controller.getR3Button();
    }

    // They got weird bindings in their library. Their Right X is really L2
    public static double getL2Axis() {
        return m_ps4controller.getRightX();
    }

    // They got weird bindings in their library. Their L2 is really R2
    public static double getR2Axis() {
        return m_ps4controller.getL2Axis();
    }

    public static double getRightJoysickX() {
        return m_ps4controller.getR2Axis();
    }

    public static double getRightJoystickY() {
        return m_ps4controller.getRightY();
    }

    public static double getLeftJoystickX() {
        return m_ps4controller.getLeftX();
    }

    public static double getLeftJoystickY() {
        return m_ps4controller.getLeftY();
    }

    public static boolean getDpadUp() {
        int pov = m_ps4controller.getPOV(0);
        return (((pov >= 0) && (pov <= 45)) || ((pov >= 315) && (pov <= 360)));
    }

    public static boolean getDpadRight() {
        int pov = m_ps4controller.getPOV(0);
        return ((pov >= 45) && (pov <= 135));
    }

    public static boolean getDpadDown() {
        int pov = m_ps4controller.getPOV(0);
        return ((pov >= 135) && (pov <= 225));
    }

    public static boolean getDpadLeft() {
        int pov = m_ps4controller.getPOV(0);
        return ((pov >= 225) && (pov <= 315));
    }

    public static boolean getTouchpadPressed() {
        return m_ps4controller.getTouchpad();
    }

    public static void setRumbleSpeed(double rumbleSpeed) {
        m_ps4controller.setRumble(RumbleType.kLeftRumble, rumbleSpeed);
        m_ps4controller.setRumble(RumbleType.kRightRumble, rumbleSpeed);
    }

    public static void setLeftRumbleSpeed(double rumbleSpeed) {
        m_ps4controller.setRumble(RumbleType.kLeftRumble, rumbleSpeed);
    }

    public static void setRightRumbleSpeed(double rumbleSpeed) {
        m_ps4controller.setRumble(RumbleType.kRightRumble, rumbleSpeed);
    }

    public static void setRumbleStop() {
        setRumbleSpeed(0);
    }

}
