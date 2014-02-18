package Test;

import robocode.*;

/**
 * WallAvoidExample - A simple example using factored wall avoidance.
 */
public class WallAvoidExample extends AdvancedRobot {

    /**
     * Two times PI.
     */
    private static final double DOUBLE_PI = (Math.PI * 2);

    /**
     * PI divided by two.
     */
    private static final double HALF_PI = (Math.PI / 2);

    // Adjust the following constants to set how wary of the walls our
    // bot should be

    /**
     * Distance intervals at which to increase wall avoidance priority.
     */
    private static final double WALL_AVOID_INTERVAL = 10;

    /**
     * The number of wall avoidance intervals.
     */
    private static final double WALL_AVOID_FACTORS = 20;

    /**
     * Distance at which to begin avoiding walls.
     */
    private static final double WALL_AVOID_DISTANCE =
            (WALL_AVOID_INTERVAL * WALL_AVOID_FACTORS);

	/**
	 * run: WallAvoidExample's default behavior
	 */
	public void run() {
		while(true) {
            // Always attempt to move straight ahead - the
            // adjustHeadingForWalls method will handle wall avoidance
			setTurnRightRadiansOptimal(adjustHeadingForWalls(0));
			setAhead(100);
			execute();
		}
	}

    // Helper Methods

    /**
     * Avoids walls by altering the desired heading to turn towards
     * the center of the battlefield.<br>
     * @param desiredHeading A double containing the desired heading
     * @return A double containing the heading modified to avoid the walls
     */
    private double adjustHeadingForWalls(double heading) {

        // Get battlefield dimensions and calculate battlefield center.
        // NOTE: This is example code - we could gain a little speed by storing
        // this data in instance variables in the class constructor instead
        // of recalculating it every time.
        double fieldHeight = getBattleFieldHeight();
        double fieldWidth = getBattleFieldWidth();
        double centerX = (fieldWidth / 2);
        double centerY = (fieldHeight / 2);

        // Get information about our navigation state
// Uncomment the following line if using the commented out desiredX and
// desiredY calculations below
//        double absoluteHeading = getHeadingRadians();
        double currentHeading = getRelativeHeadingRadians();
        double x = getX();
        double y = getY();

// Uncomment the following line if using the commented out desiredX and
// desiredY calculations below
//        double velocity = getVelocity();

        // Assume we're not close to a wall
        boolean nearWall = false;

        // Allocate variables for determining where we want to go if we are
        // too close to a wall
        double desiredX;
        double desiredY;

        // If we are too close to a wall, calculate a course towards the center
        // of the battlefield.
        if ((y < WALL_AVOID_DISTANCE) ||
                ((fieldHeight - y) < WALL_AVOID_DISTANCE)) {
            desiredY = centerY;
            nearWall = true;
        } else {
            desiredY = y;
// Alternatively, we could calculate our next y position - this would give us
// a heading closer to the desired heading but would tend to cause more wall
// collisions
//            desiredY = (y + (velocity * Math.cos(absoluteHeading)));
        }
        if ((x < WALL_AVOID_DISTANCE) ||
                ((fieldWidth - x) < WALL_AVOID_DISTANCE)) {
            desiredX = centerX;
            nearWall = true;
        } else {
            desiredX = x;
// Alternatively, we could calculate our next x position - this would give us
// a heading closer to the desired heading but would tend to cause more wall
// collisions
//            desiredX = (x + (velocity * Math.sin(absoluteHeading)));
        }
        if (nearWall) {
            double desiredBearing = calculateBearingToXYRadians(x,
                                                                y,
                                                                currentHeading,
                                                                desiredX,
                                                                desiredY);
            double distanceToWall = Math.min(
                    Math.min(x, (fieldWidth - x)),
                    Math.min(y, (fieldHeight - y)));
            int wallFactor =
                    (int)Math.min((distanceToWall / WALL_AVOID_INTERVAL),
                                  WALL_AVOID_FACTORS);
            return ((((WALL_AVOID_FACTORS - wallFactor) * desiredBearing) +
                     (wallFactor * heading)) / WALL_AVOID_FACTORS);
        } else {
            return heading;
        }
    }

    /**
     * Returns the direction the robot is facing, in radians. The value
     * returned will be between 0 and 2 * PI.
     * @return A double specifying the relative heading (based on movement
     *         direction) of the robot in radians.
     */
    public double getRelativeHeadingRadians() {
        double relativeHeading = getHeadingRadians();
	        if (direction < 1) {
            relativeHeading =
                    normalizeAbsoluteAngleRadians(relativeHeading + Math.PI);
        }
        return relativeHeading;
    }

    /**
     * Reverses the current movement direction.
	 * NOTE: If the bot is moving, there will be up to 4 frames where the
     *       bot will be moving the opposite direction it thinks it is -
     *       this is too small of a margin to generally get too concerned
     *       about, but could be an issue if attempting to do very precise
     *       movement.
     */
    public void reverseDirection() {
        double distance = (getDistanceRemaining() * direction);
        direction *= -1;
        setAhead(distance);
    }

    /**
     * Moves the droid ahead the specified distance relative to the current
     * direction. The bot's direction will be reversed if distance parameter
     * is negative.
     * @param distance A double specifying the distance to move
     */
    public void setAhead(double distance) {
        double relativeDistance = (distance * direction);
        super.setAhead(relativeDistance);
        // If distance is negative, reverse our direction
        if (distance < 0) {
            direction *= -1;
        }
    }

    /**
     * Moves the droid back the specified distance relative to the current
     * direction.  The bot's direction will be reversed if distance parameter
     * is positive.
     * @param distance A double specifying the distance to move
     */
    public void setBack(double distance) {
        double relativeDistance = (distance * direction);
        super.setBack(relativeDistance);
        // If distance is positive, reverse our direction
        if (distance > 0) {
            direction *= -1;
        }
    }

    /**
     * Turns left the specified angle in radians, reversing course if it's
     * more efficient.
     * @param angle A double specifying the angle to turn in radians
     */
    public void setTurnLeftRadiansOptimal(double angle) {
        double turn = normalizeRelativeAngleRadians(angle);
        if (Math.abs(turn) > HALF_PI) {
            reverseDirection();
            if (turn < 0) {
                turn = (HALF_PI + (turn % HALF_PI));
            } else if (turn > 0) {
                turn = -(HALF_PI - (turn % HALF_PI));
            }
        }
        setTurnLeftRadians(turn);
    }

    /**
     * Turns right to the specified direction, reversing course if it's
     * more efficient.
     * @param angle A double specifying the angle to turn in radians
     */
    public void setTurnRightRadiansOptimal(double angle) {
        double turn = normalizeRelativeAngleRadians(angle);
        if (Math.abs(turn) > HALF_PI) {
            reverseDirection();
            if (turn < 0) {
                turn = (HALF_PI + (turn % HALF_PI));
            } else if (turn > 0) {
                turn = -(HALF_PI - (turn % HALF_PI));
            }
        }
            setTurnRightRadians(turn);
    }

    /**
     * Calculates the bearing from the specified source point to the specified
     * target point in radians.
     * @param sourceX A double specifying the source X coordinate
     * @param sourceY A double specifying the source Y coordinate
     * @param sourceHeading A double specifying the heading of the source point
     *                      in radians
     * @param targetX A double specifying the target X coordinate
     * @param targetY A double specifying the target Y coordinate
     * @return A double containing the relative bearing from sourceX,sourceY to
     *         targetX,targetY based on sourceHeading
     */
    public double calculateBearingToXYRadians(double sourceX, double sourceY,
                                              double sourceHeading,
                                              double targetX, double targetY) {
        return normalizeRelativeAngleRadians(
                       Math.atan2((targetX - sourceX), (targetY - sourceY)) -
                       sourceHeading);
    }

    /**
     * Returns a normalized absolute angle in radians.
     * @param angle A double containing the angle to normalize
     * @return A double containing the normalized absolute angle
     */
    public double normalizeAbsoluteAngleRadians(double angle) {
        if (angle < 0) {
            return (DOUBLE_PI + (angle % DOUBLE_PI));
        } else {
            return (angle % DOUBLE_PI);
        }
    }

    /**
     * Returns a normalized relative angle in radians.
     * @param angle A double containing the angle to normalize
     * @return A double containing the normalized relative angle
     */
    public static double normalizeRelativeAngleRadians(double angle) {
        double trimmedAngle = (angle % DOUBLE_PI);
        if (trimmedAngle > Math.PI) {
            return -(Math.PI - (trimmedAngle % Math.PI));
        } else if (trimmedAngle < -Math.PI) {
            return (Math.PI + (trimmedAngle % Math.PI));
        } else {
            return trimmedAngle;
        }
    }

    /**
     * The current direction of the bot.
     */
    private int direction = 1;

}
