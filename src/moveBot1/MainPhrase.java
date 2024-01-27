package moveBot1;

import battlecode.common.FlagInfo;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;


public class MainPhrase {

  private static MapLocation flagTarget = null;

  public static void runMainPhrase(RobotController rc) throws GameActionException {
    Skirmish.basicOffense(rc);
//    PathFind.exploreRandomly(rc);
  }

  public static void goToEnemyFlag(RobotController rc) throws GameActionException {
    FlagInfo[] visibleFlagArr = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
    if (visibleFlagArr.length != 0) {
      //TODO CLOSEST FLAG LOCATION
      flagTarget = visibleFlagArr[0].getLocation();
      PathFind.moveTowardsTarget(rc, flagTarget, true);
      return;
    }
    MapLocation[] flagLocation = rc.senseBroadcastFlagLocations();
    flagTarget = Util.closestLocation(rc, rc.getLocation(), flagLocation);
    PathFind.moveTowardsTarget(rc, flagTarget, true);

  }
  //have flag to back to spawn
}
