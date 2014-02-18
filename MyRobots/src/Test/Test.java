package Test;

import robocode.util.Utils;
import robocode.*;

import java.awt.Color;
import java.util.Random;

public class Test extends AdvancedRobot {
	private int radar = 20;
	private int radarFlag = 0;
	private int fireFlag = 0;
	private int moving = 1;
	private int avoidWallsFlag = 0;
	private Random R = new Random();

	public void run() {
		// set colors
		setBodyColor(Color.gray);
		setGunColor(Color.red);
		setRadarColor(Color.orange);
		setBulletColor(Color.white);
		setScanColor(Color.white);

		setAdjustGunForRobotTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		while (true) {
			if (getRadarTurnRemaining() == 0.0) {
				setTurnRadarRight(360);
			}
			if (getTurnRemaining() == 0.0) {
				if (R.nextDouble() > 0.5) {
					setTurnRight(testAvoidWalls(180));
				} else {
					setTurnRight(testAvoidWalls(-180));
				}
			}
			setAhead(moving * 500);
			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		if (fireFlag == 1) {
			setFire(setFirePower(e));
			fireFlag = 0;
		} else {
			if (Utils.normalRelativeAngleDegrees(getHeading()
					- getRadarHeading() + e.getBearing()) < 10
					&& getOthers() >= 2) {
				if (radarFlag == 0) {
					setTurnRadarRight(radar);
					radarFlag = 1;
				} else {
					setTurnRadarRight(-2 * radar);
					radarFlag = 0;
				}
			} else {
				setTurnRadarRight(Utils.normalRelativeAngleDegrees(getHeading()
						- getRadarHeading() + e.getBearing()));
			}

			if (Math.abs(Utils.normalRelativeAngleDegrees(getHeading()
					- e.getBearing() - 90)) > 45
					&& avoidWallsFlag != 1) {
				setTurnRight(testAvoidWalls(Utils
						.normalRelativeAngleDegrees(90 + e.getBearing())));
			}

			setTurnGunRight(Utils.normalRelativeAngleDegrees(getHeading()
					- getGunHeading() + e.getBearing()));

			if (getGunHeat() == 0.0 && Math.abs(getGunTurnRemaining()) < 10) {
				if (e.getDistance() < 500) {
					setFire(setFirePower(e));
				} else {
					fireFlag = 1;
					setTurnGunRight(predictEnemy(e, setFirePower(e)));
				}
			}
		}
		
		if (e.getEnergy() >= 0.0) {
			if (R.nextDouble() > 0.5 && avoidWallsFlag != 1) {
				moving *= -1;
			}
			setAhead(moving * 30000 / e.getDistance());
			scan();
		}
	}

	private double setFirePower(ScannedRobotEvent e) {
		double firePower;
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

	private double predictEnemy(ScannedRobotEvent e, double firePower) {
		double time = e.getDistance() / (20 - 3 * firePower) + 1;

		double oldX = getX()
				+ Math.sin(getHeadingRadians() + e.getBearingRadians())
				* e.getDistance();
		double oldY = getY()
				+ Math.cos(getHeadingRadians() + e.getBearingRadians())
				* e.getDistance();

		double newX = oldX + Math.cos(e.getHeadingRadians()) * e.getVelocity()
				* time;
		double newY = oldY + Math.sin(e.getHeadingRadians()) * e.getVelocity()
				* time;

		double gunTurning = 90 - Math.atan2(newY - getY(), newX - getX()) * 360
				/ (2 * Math.PI);
		return Utils.normalRelativeAngleDegrees(gunTurning - getGunHeading());
	}

	public void onHitRobot(HitRobotEvent e) {
		moving *= -1;
		if (Math.abs(Utils.normalRelativeAngleDegrees(e.getBearing())) < 10) {
			setFire(3);
			scan();
		}
	}

	public void onHitWall(HitWallEvent e) {
		moving *= -1;
	}

	public void onHitByBullet(HitByBulletEvent e) {
		if (R.nextDouble() > 0.5) {
			moving *= -1;
		}
	}

	private double testAvoidWalls(double turning) {
		double distance = 150;
		double fixedTurning = 120;

		if (getX() <= distance) {
			avoidWallsFlag = 1;
			if (Utils.normalRelativeAngleDegrees(getHeading()) < -90) {
				return -fixedTurning;
			} else {
				return fixedTurning;
			}
		} else if (getX() >= (getBattleFieldWidth() - distance)) {
			avoidWallsFlag = 1;
			if (Utils.normalRelativeAngleDegrees(getHeading()) < 90) {
				return fixedTurning;
			} else {
				return -fixedTurning;
			}
		}

		if (getY() <= distance) {
			avoidWallsFlag = 1;
			if (Utils.normalRelativeAngleDegrees(getHeading()) < 0) {
				return fixedTurning;
			} else {
				return -fixedTurning;
			}
		} else if (getY() >= (getBattleFieldHeight() - distance)) {
			avoidWallsFlag = 1;
			if (Utils.normalRelativeAngleDegrees(getHeading()) < 0) {
				return -fixedTurning;
			} else {
				return fixedTurning;
			}
		}
		avoidWallsFlag = 0;
		return turning;
	}
}