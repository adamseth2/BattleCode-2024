package moveBot1;

import battlecode.common.*;

import java.util.Arrays;

import static moveBot1.RobotPlayer.rng;

public abstract class RunnableBot {
  public Role role;

  //go to map stuff
  private static MapLocation flagTarget = null;
  //crumb stuff
  private static MapLocation[] spawnLocation = null;
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
//    System.out.println(Arrays.toString(crumbLocationArr));
    if (crumbLocationArr == null || crumbLocationArr.length == 0) {
      crumbLocationArr = rc.senseNearbyCrumbs(-1);
      rc.setIndicatorString("There is no Crumbs in my vision");
      PathFind.exploreRandomDirection(rc);
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

  public static void goToEnemyFlag(RobotController rc) throws GameActionException {
    FlagInfo[] visibleFlagArr = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
    if (visibleFlagArr.length != 0) {
      //TODO CLOSEST FLAG LOCATION
      flagTarget = visibleFlagArr[0].getLocation();
      PathFind.moveTowardsTarget(rc, flagTarget, true, "bugNavOne");
      if (rc.canPickupFlag(flagTarget)) {
        rc.pickupFlag(flagTarget);
      }
      return;
    }
    //moves towards flag
    MapLocation[] flagLocation2 = rc.senseBroadcastFlagLocations();
    MapLocation[] flagLocation = new MapLocation[flagLocation2.length + 1];
    int z = 0;
    for (MapLocation curr : flagLocation2) {
      flagLocation[z] = curr;
      z++;
    }
    flagLocation[flagLocation2.length] = Communication.intToLocation(rc, rc.readSharedArray(0));
    boolean isAllFlagsTaken = flagLocation.length == 0;
    if (isAllFlagsTaken) {
      return;
    }
    flagTarget = Util.closestLocation(rc, rc.getLocation(), flagLocation);
//    System.out.println("flagLocation: " + Arrays.toString(flagLocation));
    PathFind.moveTowardsTarget(rc, flagTarget, true, "bugNavOne");
  }

  //have flag to back to spawn
  public static void goToBase(RobotController rc) throws GameActionException {
    if (spawnLocation == null) {
      spawnLocation = rc.getAllySpawnLocations();
    }
    if (rc.hasFlag()) {
      rc.writeSharedArray(0, Communication.locationToInt(rc, rc.getLocation()));
    }
    MapLocation closestSpawn = Util.closestLocation(rc, rc.getLocation(), spawnLocation);
    if (closestSpawn == null) {
      System.out.println("ERROR for some reason spawnlocation is null");
      return;
    }
    System.out.println("Closest spawn is: " + closestSpawn.toString());
    PathFind.moveTowardsTarget(rc, closestSpawn, true, "bugNavTwo");
  }

  public static void heal(RobotController rc) throws GameActionException {
    RobotInfo[] nearbyFriends = rc.senseNearbyRobots(-1, rc.getTeam());
    if (nearbyFriends.length == 0) {
      return;
    }
    for (RobotInfo currFriend : nearbyFriends) {
      if (!rc.isActionReady()) {
        return;
      }
      if (rc.canHeal(currFriend.getLocation())) {
        rc.heal(currFriend.getLocation());
      }
    }
  }

  public void trySpawn(RobotController rc) throws GameActionException {
    if (rc.isSpawned()) {
      return;
    }

    // Pick a random spawn location to attempt spawning in.
    //tries 3 times
    MapLocation[] spawnLocs = rc.getAllySpawnLocations();
    MapLocation randomLoc = spawnLocs[rng.nextInt(spawnLocs.length - 1)];
    if (rc.canSpawn(randomLoc)) {
      rc.spawn(randomLoc);
      return;
    }
    randomLoc = spawnLocs[rng.nextInt(spawnLocs.length - 1)];
    if (rc.canSpawn(randomLoc)) {
      rc.spawn(randomLoc);
      return;
    }
    randomLoc = spawnLocs[rng.nextInt(spawnLocs.length - 1)];
    if (rc.canSpawn(randomLoc)) {
      rc.spawn(randomLoc);
      return;
    }
  }
}
