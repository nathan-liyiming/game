/**
 * This robot moves and search the enemy, then turn the gun to fire in the direction.
 * Two ways to lock:
 * <ol>
 * <li>When there are more than one enemies, it will lock this enemy and waver radar. 
 * It can search any other enemies because maybe others will attack it and it needs to return fire.
 * If this robot always locks one enemy, others may bring it to be dead.</li>
 * <li> When there is only one enemy, it will lock it.</li>
 * </ol>
 * At the same time, when the distance is large than 500, it will predict the direction and turn gun, then fire.
 * 
 * @author <a href="mailto:Y.Li38@student.liverpool.ac.uk">Li Yiming(200838891)</a>
 * @version 1.0
 * 
 */
package comp222;

import robocode.util.Utils;
import robocode.*;

import java.awt.Color;
import java.util.Random;

/*
 * Extends AdvancedRobot, want to run more actions at the same time.
 */
public class YimingLi extends AdvancedRobot {

	/*
	 * The value of radar, the robot needs to waver.
	 */
	private int radar = 20;

	/*
	 * The flag is set to turn the radar in turn. "0"----Turn Right "1"----Turn
	 * Left
	 */
	private int radarFlag = 0;

	/*
	 * The flag is set to fire by next time, this will be used when the
	 * distance>=500. "0"----distance<500 "1"----distance>=500 and need to fire
	 * next time
	 */
	private int fireFlag = 0;

	/*
	 * The direction of robot. We always keep the direction and it is convenient
	 * to use setAhead() instead of using setBack() at the same time.
	 */
	private int direction = 1;

	/*
	 * The flag is set to judge if avoid walls when it is close to walls.
	 * "0"----not need to avoid walls "1"----need to avoid walls(too close)
	 */
	private int avoidWallsFlag = 0;

	/*
	 * The random object to get the random number.
	 */
	private Random R = new Random();

	/**
	 * Run: YimingLi's main run function.
	 */
	public void run() {
		// set colors
		setBodyColor(Color.gray);
		setGunColor(Color.red);
		setRadarColor(Color.orange);
		setBulletColor(Color.white);
		setScanColor(Color.white);

		// make turning gun, radar and robot independent
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		// Loop forever
		while (true) {
			// if the is no remaining radar, turn a circle
			if (getRadarTurnRemaining() == 0.0) {
				setTurnRadarRight(360);
			}
			// if the is no remaining turn, turn half a circle
			// random turning, make the enemy can't follow our track
			if (getTurnRemaining() == 0.0) {
				if (R.nextDouble() > 0.5) {
					setTurnRight(testAvoidWalls(180));
				} else {
					setTurnRight(testAvoidWalls(-180));
				}
			}
			// move ahead 500 or -500 depending on "direction"
			// the direction will change when hitting a robot,
			// hitting walls, hitting bullet or changing on scanning to avoid
			// bullet
			setAhead(direction * 500);
			// it is like to block and pause
			// if there is no execute(), only set methods, at the beginning, the
			// robot is "Disabled".
			execute();
		}
	}

	/**
	 * Turn the Gun, robot and waver radar(enemies>=2), then lock and fire.
	 * 
	 * @param e
	 *            the scanning enemy at this time
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// it needs to fire and fire to predicting direction
		if (fireFlag == 1) {
			setFire(setFirePower(e));
			// has setFired
			fireFlag = 0;
		} else {
			// if the direction of radar is close to that of enemy and there
			// are
			// more than one enemies,
			// then waver radars and expand the range of searching for any other
			// enemies
			// otherwise, turn the radar to lock. For only one enemy, we only
			// lock
			// it and do not worry about other enemies.(i.e.there are no other
			// enemies.)
			if (Utils.normalRelativeAngleDegrees(getHeading()
					- getRadarHeading() + e.getBearing()) < 10
					&& getOthers() >= 2) {
				// waver radars around the direction of enemy
				// to find any other enemies
				if (radarFlag == 0) {
					setTurnRadarRight(radar);
					radarFlag = 1;
				} else {
					setTurnRadarRight(-2 * radar);
					radarFlag = 0;
				}
			} else {
				// normalRelativeAngleDegrees to set range -180~180 degrees
				setTurnRadarRight(Utils.normalRelativeAngleDegrees(getHeading()
						- getRadarHeading() + e.getBearing()));
			}

			// the gun to lock the direction of enemy
			setTurnGunRight(Utils.normalRelativeAngleDegrees(getHeading()
					- getGunHeading() + e.getBearing()));

			// do not make the direction of robot close to that of enemy
			// because we need to ahead or back(by changing direction) to avoid
			// the
			// bullet coming from this enemy
			// when the robot is avoiding walls and turning, we can't set it
			// again.
			if (Math.abs(Utils.normalRelativeAngleDegrees(getHeading()
					- e.getBearing() - 90)) > 45
					&& avoidWallsFlag != 1) {
				// set to perpendicular to the heading of enemy
				setTurnRight(testAvoidWalls(Utils
						.normalRelativeAngleDegrees(90 + e.getBearing())));
			}

			// if the gun cools down and gun has turned close direction of
			// enemy, at the same time, distance<500,
			// then fire depending on distance and its energy.
			// Otherwise, it will turn gun right firstly and fire next time
			if (getGunHeat() == 0.0 && Math.abs(getGunTurnRemaining()) < 10) {
				if (e.getDistance() < 500) {
					// use private method to return firePower
					setFire(setFirePower(e));
				} else {
					fireFlag = 1;
					// use private method to return firePower
					// use private method to predict the location of enemy
					setTurnGunRight(predictEnemy(e, setFirePower(e)));
				}
			}
		}
		// the enemy is live and we should change the direction
		// to avoid its bullet. Using random number to make the enemy confused
		// about the track.
		if (e.getEnergy() >= 0.0) {
			// if the robot needs to avoid walls, it can't change the direction
			if (R.nextDouble() > 0.5 && avoidWallsFlag != 1) {
				direction *= -1;
			}
			// avoid bullet from this enemy
			// at the same time, more close, more movings to be far away from
			// the enemy
			setAhead(direction * 30000 / e.getDistance());
			// start again from the top
			// scan, lock and fire
			scan();
		}
	}

	/**
	 * Depending on the distance to set the power.
	 * 
	 * @param e
	 *            the scanning enemy at this time
	 * @return the fire power
	 */
	private double setFirePower(ScannedRobotEvent e) {
		double firePower;
		// if energy is large than 10, it has power to fire
		// otherwise fire small one(0.5)
		if (getEnergy() > 10) {
			if (e.getDistance() < 300) {
				firePower = 3;
			} else if (e.getDistance() < 600) {
				firePower = 2;
			} else {
				firePower = 1;
			}
		} else {
			firePower = 0.5;
		}
		return firePower;
	}

	/**
	 * Predict enemy and get the degrees to turn.
	 * 
	 * @param e
	 *            the scanning enemy at this time
	 * @param firePower
	 *            the fire power
	 * @return the turning degrees for the next firing
	 */
	private double predictEnemy(ScannedRobotEvent e, double firePower) {
		// (20 - 3 * firePower) is the speed of bullet
		// plus "1" because the turning this degrees needs one turn and then
		// fire
		// next turn
		double time = e.getDistance() / (20 - 3 * firePower) + 1;

		// by the location of robot and compute the location of enemy
		double oldX = getX()
				+ Math.sin(getHeadingRadians() + e.getBearingRadians())
				* e.getDistance();
		double oldY = getY()
				+ Math.cos(getHeadingRadians() + e.getBearingRadians())
				* e.getDistance();

		// by the time and direction of enemy and compute the new location
		double newX = oldX + Math.cos(e.getHeadingRadians()) * e.getVelocity()
				* time;
		double newY = oldY + Math.sin(e.getHeadingRadians()) * e.getVelocity()
				* time;

		// for the robot, it has turning degree from the north(top of field)
		double gunTurning = 90 - Math.atan2(newY - getY(), newX - getX()) * 360
				/ (2 * Math.PI);

		// now, how many degrees the gun needs to turn to the new location
		return Utils.normalRelativeAngleDegrees(gunTurning - getGunHeading());
	}

	/**
	 * Hit the enemy and change the direction. If head to it, then fire and
	 * scan.
	 * 
	 * @param e
	 *            the hitting enemy at this time
	 */
	public void onHitRobot(HitRobotEvent e) {
		direction *= -1;
		// too close and fire
		if (getGunHeat() == 0.0&&Math.abs(Utils.normalRelativeAngleDegrees(getHeading()
				- getGunHeading() + e.getBearing())) < 10) {
			setFire(3);
			scan();
		}
	}

	/**
	 * Hit the wall and change the direction.
	 * 
	 * @param e
	 *            the hitting wall at this time
	 */
	public void onHitWall(HitWallEvent e) {
		direction *= -1;
	}

	/**
	 * Hit the bullet and change the direction by random.
	 * 
	 * @param e
	 *            the hitting bullet at this time
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		if (R.nextDouble() > 0.5) {
			direction *= -1;
		}
	}

	/**
	 * Test whether it is close to the walls when turning. If it is close to
	 * walls, then return a fixedTurning and leave the walls. Otherwise, return
	 * original turning value.
	 * 
	 * @param turning
	 *            the turning degrees to test
	 * @return the turning degrees after testing
	 */
	private double testAvoidWalls(double turning) {
		// distance to be close
		double distance = 150;
		// fixedTurning to avoid to be close to walls
		double fixedTurning = 120;

		// avoidWallsFlag = 1 means the robot needs time to turn and avoid to
		// hit wall

		// X VS distance and getBattleFieldWidth() - distance
		if (getX() <= distance) {
			avoidWallsFlag = 1;
			// depend on the heading
			if (Utils.normalRelativeAngleDegrees(getHeading()) < -90) {
				return -fixedTurning;
			} else {
				return fixedTurning;
			}
		} else if (getX() >= (getBattleFieldWidth() - distance)) {
			avoidWallsFlag = 1;
			// depend on the heading
			if (Utils.normalRelativeAngleDegrees(getHeading()) < 90) {
				return fixedTurning;
			} else {
				return -fixedTurning;
			}
		}

		// Y VS distance and getBattleFieldHeight() - distance
		if (getY() <= distance) {
			avoidWallsFlag = 1;
			// depend on the heading
			if (Utils.normalRelativeAngleDegrees(getHeading()) < 0) {
				return fixedTurning;
			} else {
				return -fixedTurning;
			}
		} else if (getY() >= (getBattleFieldHeight() - distance)) {
			avoidWallsFlag = 1;
			// depend on the heading
			if (Utils.normalRelativeAngleDegrees(getHeading()) < 0) {
				return -fixedTurning;
			} else {
				return fixedTurning;
			}
		}
		// otherwise, nothing happen
		avoidWallsFlag = 0;
		return turning;
	}
}
