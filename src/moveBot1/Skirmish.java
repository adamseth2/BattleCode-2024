package moveBot1;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class Skirmish {


  public static void attackAndMoveBack(RobotController rc) throws GameActionException {

  }

  public static void basicOffense(RobotController rc) throws GameActionException {
    RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
    if (nearbyEnemies.length == 0) {
      return;
    }
    MapLocation enemyLocation = nearbyEnemies[0].location;
    if (rc.canAttack(enemyLocation)) {
      rc.attack(enemyLocation);
    }

  }
}
