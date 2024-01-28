package moveBot1;

import battlecode.common.*;

public class Builder extends RunnableBot {

  boolean isOnOffense = false;

  public void init() {
    this.role = Role.BUILDER;
    isOnOffense = RobotPlayer.rng.nextInt(10) >= 5;
  }

  public void runSetUp(RobotController rc) throws GameActionException {
    boolean isInSetUpPhrase = rc.getRoundNum() < GameConstants.SETUP_ROUNDS - 100;
    if (isInSetUpPhrase) {
      buildAroundBase(rc);
    }
  }

  public void runMainPhrase(RobotController rc) throws GameActionException {
    if (isOnOffense) {
      goToEnemyFlag(rc);
      offenseTrap(rc);
      return;
    } else {
      buildAroundBase(rc);
    }
  }


  public static void offenseTrap(RobotController rc) throws GameActionException {
    RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
    if (nearbyEnemies == null || nearbyEnemies.length == 0) {
      return;
    }
    if (rc.canBuild(TrapType.STUN, rc.getLocation())) {
      rc.build(TrapType.STUN, rc.getLocation());
    }
  }

  public static void buildAroundBase(RobotController rc) throws GameActionException {

    MapLocation closestBase = Util.closestLocation(rc, rc.getLocation(), RobotPlayer.centers);
    if (rc.getLocation().distanceSquaredTo(closestBase) <= 64) {
      if (RobotPlayer.rng.nextInt(10) <= 5 || rc.getCrumbs() <= 500) {
        PathFind.exploreRandom(rc);
        return;
      }

      if (rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation())) {
        rc.build(TrapType.EXPLOSIVE, rc.getLocation());
      }
      for (Direction dir : Direction.cardinalDirections())
        if (rc.canBuild(TrapType.EXPLOSIVE, rc.getLocation().add(dir))) {
          rc.build(TrapType.EXPLOSIVE, rc.getLocation().add(dir));
        }
      PathFind.exploreRandom(rc);

    } else {
      PathFind.moveTowardsTarget(rc, closestBase, true, "bugNavOne");
    }
  }
}
