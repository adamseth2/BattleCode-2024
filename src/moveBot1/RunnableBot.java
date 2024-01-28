package moveBot1;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.Arrays;

public abstract class RunnableBot {
  public Role role;

  //crumb stuff
  private static MapLocation[] crumbLocationArr = null;
  private static MapLocation targetCrumb = null;
  private static int turnsTryToGoToCrumb = 0;

  public abstract void init();

  public abstract void runSetUp(RobotController rc) throws GameActionException;

  public abstract void runMainPhrase(RobotController rc) throws GameActionException;

  public static void collectCrumbs(RobotController rc) throws GameActionException {
    if (turnsTryToGoToCrumb >= 50 || rc.getLocation().equals(targetCrumb)) {
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
    turnsTryToGoToCrumb++;
    rc.setIndicatorString("My currtargetCrum: " + rc.toString());
    PathFind.moveTowardsTarget(rc, targetCrumb, true, "bugNavOne");
  }

  private static void resetCrumbVars() {
    crumbLocationArr = null;
    targetCrumb = null;
    turnsTryToGoToCrumb = 0;
  }
}
