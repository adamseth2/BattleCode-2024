package moveBot0;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Guardian extends RunnableBot {

  public void init() {
    this.role = Role.GUARDIAN;
  }

  public void runSetUp(RobotController rc) throws GameActionException {
    MapLocation homeLoc = RobotPlayer.centers[RobotPlayer.guardianID];
    rc.setIndicatorDot(rc.getLocation(), 0, 255, 0);
    if (!rc.getLocation().equals(homeLoc)) {
      PathFind.moveTowardsTarget(rc, homeLoc, true, "bugNavOne");
    }
  }

  public void runMainPhrase(RobotController rc) throws GameActionException {
    runSetUp(rc);
  }
//    public static void runGuardian(RobotController rc) throws GameActionException {

}
