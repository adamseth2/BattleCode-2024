package moveBot1;


import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.Arrays;

public class Setup {

  private static MapLocation[] crumbLocationArr = null;

  private static MapLocation targetCrumb = null;

  public static void runSetUp(RobotController rc) throws GameActionException {
    collectCrumbs(rc);
  }

  private static void resetCrumbVars() {
    crumbLocationArr = null;
    targetCrumb = null;
  }

  public static void collectCrumbs(RobotController rc) throws GameActionException {
    if (rc.getLocation().equals(targetCrumb)) {
      resetCrumbVars();
    }
    System.out.println(Arrays.toString(crumbLocationArr));
    if (crumbLocationArr == null || crumbLocationArr.length == 0) {
      crumbLocationArr = rc.senseNearbyCrumbs(-1);
      rc.setIndicatorString("There is no Crumbs in my vision");
      PathFind.exploreRandomly(rc);
      return;
    }
    if (targetCrumb == null) {
      targetCrumb = Util.closestLocation(rc, rc.getLocation(), crumbLocationArr);
    }
    rc.setIndicatorString("My currtargetCrum: " + rc.toString());

    PathFind.moveTowardsTarget(rc, targetCrumb, true);
  }

}
