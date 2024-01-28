package moveBot0;//package moveBot1;
//
//import battlecode.common.*;
//
//
//public class MainPhrase {
//
//  private static MapLocation flagTarget = null;
//
//  public static void runMainPhrase(RobotController rc) throws GameActionException {
//    if (RobotPlayer.bot == Role.GUARDIAN) {
//      Guardian.runGuardian(rc);
//      return;
//    }
//    if (rc.hasFlag()) {
//      goToBase(rc);
//    }
//    Skirmish.basicOffense(rc);
//    heal(rc);
//    goToEnemyFlag(rc);
//    PathFind.exploreRandomly(rc);
//  }
//
//  public static void goToEnemyFlag(RobotController rc) throws GameActionException {
//    FlagInfo[] visibleFlagArr = rc.senseNearbyFlags(-1, rc.getTeam().opponent());
//    if (visibleFlagArr.length != 0) {
//      //TODO CLOSEST FLAG LOCATION
//      flagTarget = visibleFlagArr[0].getLocation();
//      PathFind.moveTowardsTarget(rc, flagTarget, true, "bugNavOne");
//      if (rc.canPickupFlag(flagTarget)) {
//        rc.pickupFlag(flagTarget);
//      }
//      return;
//    }
//    //moves towards flag
//    MapLocation[] flagLocation = rc.senseBroadcastFlagLocations();
//    boolean isAllFlagsTaken = flagLocation.length == 0;
//    if (isAllFlagsTaken) {
//      return;
//    }
//    flagTarget = Util.closestLocation(rc, rc.getLocation(), flagLocation);
////    System.out.println("flagLocation: " + Arrays.toString(flagLocation));
//    PathFind.moveTowardsTarget(rc, flagTarget, true, "bugNavOne");
//  }
//
//  //have flag to back to spawn
//  public static void goToBase(RobotController rc) throws GameActionException {
//    MapLocation[] spawnLocation = rc.getAllySpawnLocations();
//    MapLocation closestSpawn = Util.closestLocation(rc, rc.getLocation(), spawnLocation);
//    if (closestSpawn == null) {
//      System.out.println("ERROR for some reason spawnlocation is null");
//      return;
//    }
//    System.out.println("Closest spawn is: " + closestSpawn.toString());
//    PathFind.moveTowardsTarget(rc, closestSpawn, true, "bugNavTwo");
//  }
//
//  public static void heal(RobotController rc) throws GameActionException {
//    RobotInfo[] nearbyFriends = rc.senseNearbyRobots(-1, rc.getTeam());
//    if (nearbyFriends.length == 0) {
//      return;
//    }
//    for (RobotInfo currFriend : nearbyFriends) {
//      if (!rc.isActionReady()) {
//        return;
//      }
//      if (rc.canHeal(currFriend.getLocation())) {
//        rc.heal(currFriend.getLocation());
//      }
//    }
//  }
//}
