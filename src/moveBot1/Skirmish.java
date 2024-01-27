package moveBot1;

import battlecode.common.*;

public class Skirmish {

  static int timesAttack = 0;

  public static void attackAndMoveBack(RobotController rc) throws GameActionException {

  }

  public static void basicOffense(RobotController rc) throws GameActionException {
    RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
    if (nearbyEnemies.length == 0) {
      PathFind.exploreRandomly(rc);
      return;
    }
    MapLocation enemyLocation = nearbyEnemies[0].location;
    if (rc.canAttack(enemyLocation)) {
      rc.attack(enemyLocation);
      PathFind.moveAwayFromTarget(rc, enemyLocation);
      return;
    }
    //move close and try to attack
    PathFind.bugNavOne(rc, enemyLocation);
    if (rc.canAttack(enemyLocation)) {
      rc.attack(enemyLocation);
      rc.setIndicatorString(Integer.toString(++timesAttack));
      return;
    }
  }

  public static void basicOffense2(RobotController rc) throws GameActionException {
    RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
//    System.out.println(Integer.toString(rc.getActionCooldownTurns()));
    rc.setIndicatorString(Integer.toString(rc.getActionCooldownTurns()));
    if (nearbyEnemies.length == 0) {
      rc.setIndicatorString("Exploring Randomly");
      PathFind.exploreRandomly(rc);
      return;
    }
    MapLocation enemyLocation = nearbyEnemies[0].location;
    if (rc.canAttack(enemyLocation)) {
      rc.attack(enemyLocation);
      PathFind.moveAwayFromTargetAttackRange(rc, enemyLocation);
      return;
    }
//    if (Util.isInAttackRange(rc.getLocation(), enemyLocation)) {
//      PathFind.moveAwayFromTargetAttackRange(rc, enemyLocation);
//      return;
//    }
//    waits until can attack again
    if (rc.getActionCooldownTurns() != 0) {
      System.out.println("Waiting");
      return;
    }
    //move close and try to attack
    PathFind.bugNavOne(rc, enemyLocation);
//    System.out.println("I moved and I can attack: " + Integer.toString(rc.getActionCooldownTurns()));
    if (rc.canAttack(enemyLocation)) {
      rc.attack(enemyLocation);
      return;
    }
  }
}
