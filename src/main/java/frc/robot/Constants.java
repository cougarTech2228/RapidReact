// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final int RIGHT_FRONT_MOTOR_CAN_ID = 11;
	public static final int RIGHT_REAR_MOTOR_CAN_ID = 12;
	public static final int LEFT_FRONT_MOTOR_CAN_ID = 13;
	public static final int LEFT_REAR_MOTOR_CAN_ID = 14;

	public static final int SHOOTER_MASTER_CAN_ID = 41;
	public static final int SHOOTER_FOLLOWER_CAN_ID = 42;
	public static final double SHOOTER_MAX_OUTPUT = 1;

    public static final int SHOOTER_CURRENT_LIMIT = 40;
	public static final float SHOOTER_CURRENT_DURATION = 1.5f;
	public static final int SHOOTER_CONTINUOUS_CURRENT_LIMIT = 35;
	public static final int MAX_MISALIGNMENT_VALUE = 30;
	public static final double ALIGNING_TURN_SPEED = .3;
	public static final float SHOOT_FEED_TIME = 2.0f;
	public static final double SHOOTER_IDLE_SPEED = 0;
	public static final double AUTO_MOVE_SPEED = .2;
	public static final double AUTO_MOVE_TIME = 2.0;

    public final static int kTimeoutMs = 30;

	public static final double OPEN_RAMP_SECONDS_TO_FULL = 2.0;
	public static final double JOYSTICK_DEADBAND_PERCENTAGE = 0.1;

	public static final int ACQUIRER_DEPLOY_MOTOR_CAN_ID = 22;
	public static final int ACQUIRER_SPIN_MOTOR_CAN_ID = 21;
	public static final int STORAGE_DRIVE_MOTOR_CAN_ID = 23;
	public static final int SHOOTER_FEED_MOTOR_CAN_ID = 24;

	public static final double STORAGE_DRIVE_SPEED = .15;
	public static final double SHOOTER_FEED_SPEED = .51;
	public static final double ACQUIRER_SPINNER_SPEED = -.80;

	public static final double HIGH_SHOOT_SPEED = .72;
	public static final double LOW_SHOOT_SPEED = .55;

	public static final int SHOOTING_DRIVING_MODE = 1;
	public static final int ACQUIRING_DRIVING_MODE = 0;
}
