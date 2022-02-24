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
	public static final int MAX_MISALIGNMENT_VALUE = 2;
	public static final double ALIGNING_TURN_SPEED = .075;
	public static final float SHOOT_FEED_TIME = 2.0f;
	public static final double SHOOTER_IDLE_SPEED = 0;
	public static final double AUTO_MOVE_SPEED = .2;
	public static final double AUTO_MOVE_TIME = 1.6;

    public final static int kTimeoutMs = 30;

	public static final double OPEN_RAMP_SECONDS_TO_FULL = 1.0;
	public static final double JOYSTICK_DEADBAND_PERCENTAGE = 0.1;

	public static final int ACQUIRER_DEPLOY_MOTOR_CAN_ID = 22;
	public static final int ACQUIRER_SPIN_MOTOR_CAN_ID = 21;
	public static final int STORAGE_CONVEYOR_MOTOR_CAN_ID = 23;
	public static final int SHOOTER_FEED_MOTOR_CAN_ID = 24;
	public static final int CLIMBER_CAN_ID = 45;
	
	public static final double STORAGE_CONVEYOR_SPEED = .25;
	public static final double SHOOTER_FEED_SPEED = .3;
	public static final double ACQUIRER_SPINNER_SPEED = -.80;
	public static final double CLIMBER_MOTOR_SPEED = 1;

	public static final double HIGH_SHOOT_SPEED = .63;
	public static final double LOW_SHOOT_SPEED = .40;
	public static final double REVERSE_SHOOT_SPEED = .2;

	public static final double RUMBLE_SPEED = 0.3;

	public static final int SHOOTING_DRIVING_MODE = 1;
	public static final int ACQUIRING_DRIVING_MODE = 0;

	public static final int CLIMBER_LOWER_LIMIT_DIO = 0;
	public static final int CLIMBER_UPPER_LIMIT_DIO = 1;

	public static final String RED_TEAM = "RedCargo";
	public static final String BLUE_TEAM = "BlueCargo";

	public static final double CONFIDENCE_LIMIT = 0.65;

	public static final double ASSISTANCE_TURN_RANGE = 20;
	public static final double ASSISTANCE_STRAFE_RANGE = 20;

	public static final double ASSISTANCE_SA_RANGE = 600;

	public static final int ASSISTANCE_NO_MOVEMENT = -1;
	public static final int ASSISTANCE_TURN = 0;
	public static final int ASSISTANCE_STRAFE = 1;
	public static final int ASSISTANCE_FORWARD_FAST = 0;
	public static final int ASSISTANCE_FORWARD_SLOW = 1;

	public static final double ASSISTANCE_TURN_SPEED = 0.1;
	public static final double ASSISTANCE_STRAFE_SPEED = 0.25;
	public static final double ASSISTANCE_FORWARD_FAST_SPEED = 0.25;
	public static final double ASSISTANCE_FORWARD_SLOW_SPEED = 0.15;

	public static final int ASSISTANCE_LOST_BALL_TIME = 1;
	
}
