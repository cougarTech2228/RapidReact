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

	// Can IDS

    public static final int RIGHT_FRONT_MOTOR_CAN_ID = 11;
	public static final int RIGHT_REAR_MOTOR_CAN_ID = 12;
	public static final int LEFT_FRONT_MOTOR_CAN_ID = 13;
	public static final int LEFT_REAR_MOTOR_CAN_ID = 14;

	public static final int SHOOTER_MASTER_CAN_ID = 41;
	public static final int SHOOTER_FOLLOWER_CAN_ID = 42;

	public static final int ACQUIRER_DEPLOY_MOTOR_CAN_ID = 22;
	public static final int ACQUIRER_SPIN_MOTOR_CAN_ID = 21;

	public static final int STORAGE_CONVEYOR_MOTOR_CAN_ID = 23;
	public static final int SHOOTER_FEED_MOTOR_CAN_ID = 24;

	public static final int CLIMBER_WINCH_CAN_ID = 45;
	public static final int CLIMBER_RIGHT_ACTUATOR_CAN_ID = 47; 
	public static final int CLIMBER_LEFT_ACTUATOR_CAN_ID = 46; 

	public static final int PIGEON_CAN_ID = 61;

	// Motor config constants

    public static final int SHOOTER_CURRENT_LIMIT = 40;
	public static final float SHOOTER_CURRENT_DURATION = 1.5f;
	public static final int SHOOTER_CONTINUOUS_CURRENT_LIMIT = 35;
	public final static int kTimeoutMs = 30;

	// Shooting alignment constants

	public static final int ACCEPTABLE_ALIGNMENT_OFFSET = 2;
	public static final int COARSE_ALIGNMENT_THRESHOLD = 50; // original was 50
	public static final int MAX_DETECTED_ALIGNMENT_RANGE = 320; // half of the 640 resolution width
	public static final double FINE_ALIGNMENT_TURN_SPEED = .4; // original was .085
	public static final double COARSE_ALIGNMENT_TURN_SPEED = .6; // original was .12

	public static final float SHOOT_FEED_TIME = 2.0f; 
	public static final double SHOOTER_IDLE_SPEED = 0;

	public static final double OUTER_TARMAC_DISTANCE = 115.0;
	public static final double INCHES_PER_PIXEL = 0.2;

	// Auto constants

	public static final double AUTO_MOVE_SPEED = .2;
	public static final double FINE_AUTO_MOVE_SPEED = .1;
	public static final double COARSE_AUTO_MOVE_SPEED = .45;
	public static final double COARSE_AUTO_MOVE_THRESHOLD_PERCENTAGE = .25; // Distance less than this percentage will be at fine speed
	public static final int TO_BALL_DISTANCE_POS12 = 114; //cm
	public static final int TO_BALL_DISTANCE_POS3 = 93;
	public static final int TO_HUB_FROM_BALL_DISTANCE = 210; //cm

    // Driving constants

	public static final double OPEN_RAMP_SECONDS_TO_FULL = 0.5;
	public static final double JOYSTICK_DEADBAND_PERCENTAGE = 0.1;

	// Motor speeds
	
	public static final double STORAGE_CONVEYOR_SPEED = .4;
	public static final double SHOOTER_FEED_SPEED = .3;
	public static final double ACQUIRER_SPINNER_SPEED = 0.5;
	public static final double CLIMBER_WINCH_MOTOR_SPEED = 1;
	public static final double CLIMBER_SWING_ARM_MOTOR_SPEED = 0.25;
	public static final double HOOK_SPEED = 0.25;

	public static final double HIGH_SHOOT_SPEED = .63;
	public static final double LOW_SHOOT_SPEED = .35;
	public static final double SPIT_SHOOT_SPEED = .2;
	public static final double REVERSE_SHOOT_SPEED = .2;

	public final static double ACQUIRER_DEPLOY_SPEED = 0.4;
    public final static double ACQUIRER_RETRACT_SPEED = 0.65;

	public final static double ACTUATOR_SPEED = 0.2;

	public static final double RUMBLE_SPEED = 0.3;

	// Driving modes

	public static final int SHOOTING_DRIVING_MODE = 1;
	public static final int ACQUIRING_DRIVING_MODE = 0;

	// DIOs

	public static final int CLIMBER_LOWER_LIMIT_DIO = 2;
	public static final int CLIMBER_UPPER_LIMIT_DIO = 3;
	public static final int LEFT_ACTUATOR_LIMIT_DIO = 8;
	public static final int RIGHT_ACTUATOR_LIMIT_DIO = 7;
	public static final int AQUIRER_LOWER_LIMIT_DIO = 1;
	public static final int AQUIRER_UPPER_LIMIT_DIO = 9;
	public static final int CAMERA_SWITCH_DIO = 0;

	// Driver Assistance 

	public static final double ASSISTANCE_SA_RANGE = 600;
	public static final int ASSISTANCE_RANGE = 40;

	public static final double ASSISTANCE_TURN_SPEED = 0.085;
	public static final double ASSISTANCE_STRAFE_SPEED = 0.2;
	public static final double ASSISTANCE_FORWARD_FAST_SPEED = 0.25;
	public static final double ASSISTANCE_FORWARD_SLOW_SPEED = 0.15;

	public static final int ASSISTANCE_LOST_BALL_TIME = 2;

	public static final int STRAFE_RANGE = 10;

	// Auto Angle Turn 
	public static final double ACCEPTABLE_AUTO_TURN_OFFSET = 0.5; // degrees
	public static final double MAX_ANGLE_TO_TURN = 180.0;
	public static final double COARSE_ANGLE_THRESHOLD = 5.0;
	public static final double FINE_AUTO_TURN_SPEED = .15; 
	public static final double COURSE_AUTO_TURN_SPEED = .3;

	// PWMs

	public static final int HOOK_CONTROL_PWM = 0;

	// Miscellaneous

	public static final int BALL_PRESENT_PROX_THRESHOLD = 900;
	
}
