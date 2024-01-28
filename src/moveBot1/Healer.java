package moveBot1;

import battlecode.common.*;

public class Healer extends RunnableBot {

  public void init() {
    this.role = Role.HEALER;
  }

  public void runSetUp(RobotController rc) throws GameActionException {
    collectCrumbs(rc);
  }

  public void runMainPhrase(RobotController rc) throws GameActionException {
    MapLocation homeLoc = RobotPlayer.centers[RobotPlayer.guardianID];
    rc.setIndicatorDot(rc.getLocation(), 0, 255, 0);
    if (!rc.getLocation().equals(homeLoc)) {
      PathFind.moveTowardsTarget(rc, homeLoc, true, "bugNavOne");
    }
//  public static void runAttacker(RobotController rc) throws GameActionException {
//    }
  }

}
