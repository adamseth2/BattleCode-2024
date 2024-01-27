package moveBot1;

import battlecode.common.*;

import static moveBot1.RobotPlayer.directions;

public class PathFind {

  private static int bugState = 0; //0 head to target, 1 circling
  private static MapLocation closestObstacle = null;
  private static int closestObstacleDist = Integer.MAX_VALUE;
  private static Direction bugDir = null;

  private static MapLocation enemyFlagLocation = null;

  public static void resetBug() {
    int bugState = 0; //0 head to target, 1 circling
    MapLocation closestObstacle = null;
    int closestObstacleDist = Integer.MAX_VALUE;
    Direction bugDir = null;
  }

  public static void bugNavOne(RobotController rc, MapLocation dest) throws GameActionException {
    rc.setIndicatorString("BugState: " + bugState + " closestObstacle: " + closestObstacle + "isAtLocation: " + Boolean.toString(rc.getLocation().equals(dest)));
    if (!rc.isMovementReady() || rc.getLocation().equals(dest)) {
      rc.setIndicatorString("I am at Destination or I cannot Move");
      return;
    }
//    if(rc.sensePassability(dest)) {
//      rc.setIndicatorString("Path is not passable");
//    }
    if (bugState == 0) {
      bugDir = rc.getLocation().directionTo(dest);
      if (rc.canMove(bugDir)) {
        rc.move(bugDir);
        return;
      }
      bugState = 1;
      closestObstacle = null;
      closestObstacleDist = Integer.MAX_VALUE;
      return;
    }
    //bugState =1
    if (rc.getLocation().equals(closestObstacle)) {
      bugState = 0;
    }
    if (rc.getLocation().distanceSquaredTo(dest) < closestObstacleDist) {
      closestObstacleDist = rc.getLocation().distanceSquaredTo(dest);
      closestObstacle = rc.getLocation();
    }

    for (int i = 0; i < 9; i++) {
      if (rc.canMove(bugDir)) {
        rc.move(bugDir);
        bugDir = bugDir.rotateRight();
        bugDir = bugDir.rotateRight();
        break;
      } else {
        bugDir = bugDir.rotateLeft();
      }
    }
//    int i = 0;
//    do {
//      bugDir = bugDir.rotateLeft();
//      if (rc.canMove(bugDir)) {
//        rc.move(bugDir);
//      }
//      i++;
//    } while (i < 8);
  }

  public static void moveAwayFromTarget(RobotController rc, MapLocation target) throws GameActionException {
    if (!rc.isMovementReady()) {
      return;
    }
    Direction oppositeDirection = rc.getLocation().directionTo(target).opposite();
    if (rc.canMove(oppositeDirection)) {
      rc.move(oppositeDirection);
      return;
    }
    //if cannot move in exact opposite, will go away in an angle
    if (rc.canMove(oppositeDirection.rotateLeft())) {
      rc.move(oppositeDirection.rotateLeft());
      return;
    }
    if (rc.canMove(oppositeDirection.rotateRight())) {
      rc.move(oppositeDirection.rotateRight());
      return;
    }
  }

  public static void moveAwayFromTargetAttackRange(RobotController rc, MapLocation target) throws GameActionException {
    if (!rc.isMovementReady()) {
      return;
    }
    // not in attack range
    if (!Util.isInAttackRange(rc.getLocation(), target)) {
      return;
    }
    Direction oppositeDirection = rc.getLocation().directionTo(target).opposite();
    if (rc.canMove(oppositeDirection)) {
      rc.move(oppositeDirection);
      return;
    }
    //if cannot move in exact opposite, will go away in an angle
    if (rc.canMove(oppositeDirection.rotateLeft())) {
      rc.move(oppositeDirection.rotateLeft());
      return;
    }
    if (rc.canMove(oppositeDirection.rotateRight())) {
      rc.move(oppositeDirection.rotateRight());
      return;
    }
  }

  //Not very helpful as doesn't really go anywhere
  public static void exploreRandomly(RobotController rc) throws GameActionException {
    if (!rc.isMovementReady()) {
      return;
    }
    Direction dir = directions[RobotPlayer.rng.nextInt(directions.length)];
//    MapLocation nextLoc = rc.getLocation().add(dir);
    if (rc.canMove(dir)) {
      rc.move(dir);
    }
  }

//  public static void goToEnemyFlag(RobotController rc) throws GameActionException {
//    if(enemyFlagLocation == null) {
//      enemyFlagLocation = rc.senseBroadcastFlagLocations()
//    }
//    if (!rc.isMovementReady()) {
//      return;
//    }
//    Direction dir = directions[RobotPlayer.rng.nextInt(directions.length)];
////    MapLocation nextLoc = rc.getLocation().add(dir);
//    if (rc.canMove(dir)) {
//      rc.move(dir);
//    }
//  }

}