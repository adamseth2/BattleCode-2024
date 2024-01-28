package moveBot0;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.Arrays;
import java.util.HashSet;

import static moveBot0.RobotPlayer.directions;

public class PathFind {

  private static int bugState = 0; //0 head to target, 1 circling
  private static MapLocation closestObstacle = null;
  private static int closestObstacleDist = Integer.MAX_VALUE;
  private static Direction bugDir = null;

  private static boolean blockedByFriendlyDuck = false;

  private static int lastRoundCount = 1;
  //NAV2 variables
  private static MapLocation prevDest = null;
  private static HashSet<MapLocation> line = null;
  private static int obstacleStartDist = 0;

  //explore variables
  private static Direction exploreDirection = null;

  public static void resetBug() {
    bugState = 0; //0 head to target, 1 circling
    closestObstacle = null;
    closestObstacleDist = Integer.MAX_VALUE;
    bugDir = null;
    blockedByFriendlyDuck = false;


    //Nav2 stuff
    prevDest = null;
    line = null;
    obstacleStartDist = 0;

  }

  public static void resetBugIfReachedDest(RobotController rc, MapLocation dest) throws GameActionException {
    if (rc.getLocation().equals(dest)) {
      resetBug();
    }
  }

  public static void bugNavOne(RobotController rc, MapLocation dest, boolean isAllowedToFill) throws GameActionException {
//    if (closestObstacle != null) {
//      System.out.println(rc.getLocation().toString());
//      System.out.println(dest.toString());
////      System.out.println(closestObstacle.toString());
//
//    }

//    if (dest.equals(new MapLocation(27, 29))) {
//      System.out.println("BugState: " + bugState + " bugDir: " + bugDir + "isAtLocation: " + Boolean.toString(rc.getLocation().equals(dest)) + " dest = " + dest.toString());
//      System.out.println("closestObstalce: " + closestObstacle);
//    }
    if (dest == null) {
      System.out.println("DEST IS NULL in bugNavONE");
    }
    if (++lastRoundCount != rc.getRoundNum()) {
      resetBug();
    }
    lastRoundCount = rc.getRoundNum();
//    rc.setIndicatorString("BugState: " + bugState + " bugDir: " + bugDir + "isAtLocation: " + Boolean.toString(rc.getLocation().equals(dest)) + " dest = " + dest.toString());
    if (!rc.isMovementReady() || rc.getLocation().equals(dest)) {
      rc.setIndicatorString("I am at Destination or I cannot Move: " + dest.toString());
      return;
    }
//    if(rc.sensePassability(dest)) {
//      rc.setIndicatorString("Path is not passable");
//    }


    if (bugState == 0) {
      bugDir = rc.getLocation().directionTo(dest);
//      System.out.println("bugDir: " + bugDir.toString());
      MapLocation fillLoc = rc.getLocation().add(bugDir);
      if (rc.canMove(bugDir)) {
        rc.move(bugDir);
        resetBugIfReachedDest(rc, dest);
        return;
      }
      if (rc.canSenseRobotAtLocation(rc.getLocation().add(bugDir))) {

        blockedByFriendlyDuck = true;
        //TODO REMOVE IF NOT HELPFUL
        if (rc.canMove(bugDir.rotateLeft())) {
          rc.move(bugDir.rotateLeft());
          return;
        }
        if (rc.canMove(bugDir.rotateRight())) {
          rc.move(bugDir.rotateRight());
          return;
        }
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
    Direction[] fillableDir = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    for (Direction curr : fillableDir) {
      MapLocation fillLoc = rc.getLocation().add(curr);
      if (canFill(rc, fillLoc, isAllowedToFill) && closestObstacle.equals(fillLoc)) {
        resetBug();
      }

    }

    for (int i = 0; i < 9; i++) {
      if (rc.canMove(bugDir)) {
        rc.move(bugDir);
        resetBugIfReachedDest(rc, dest);
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

  public static void bugNavTwo(RobotController rc, MapLocation dest, boolean isAllowedToFill) throws GameActionException {
    rc.setIndicatorString("BugState: " + bugState + " closestObstacle: " + closestObstacle + "isAtLocation: " + Boolean.toString(rc.getLocation().equals(dest)) + " dest = " + dest.toString());
    if (!rc.isMovementReady() || rc.getLocation().equals(dest)) {
      rc.setIndicatorString("I am at Destination or I cannot Move");
      resetBug();
      return;
    }
    if (!dest.equals(prevDest)) {
      prevDest = dest;
      line = Util.createLine(rc.getLocation(), dest);
    }
    for (MapLocation loc : line) {
      rc.setIndicatorDot(loc, 255, 0, 0);
    }
    if (bugState == 0) {
      bugDir = rc.getLocation().directionTo(dest);
//      System.out.println("bugDir: " + bugDir.toString());
      if (rc.canMove(bugDir)) {
        rc.move(bugDir);
        resetBugIfReachedDest(rc, dest);
        return;
      }
      bugState = 1;
      obstacleStartDist = rc.getLocation().distanceSquaredTo(dest);
      bugDir = rc.getLocation().directionTo(dest);
      return;
    }
    //bugState =1

    if (line.contains(rc.getLocation()) && rc.getLocation().distanceSquaredTo(dest) < obstacleStartDist) {
      bugState = 0;
    }
    Direction[] fillableDir = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    for (Direction curr : fillableDir) {
      MapLocation fillLoc = rc.getLocation().add(curr);
      if (canFill(rc, fillLoc, isAllowedToFill) && closestObstacle.equals(fillLoc)) {
        resetBug();
      }
    }
    for (int i = 0; i < 9; i++) {
      if (rc.canMove(bugDir)) {
        rc.move(bugDir);
        resetBugIfReachedDest(rc, dest);
        bugDir = bugDir.rotateRight();
        bugDir = bugDir.rotateRight();
        break;
      } else {
        bugDir = bugDir.rotateLeft();
      }
    }
  }

  public static void moveTowardsTarget(RobotController rc, MapLocation target, boolean isAllowedToFill, String pathFindingAlgo) throws GameActionException {
    HashSet<String> pathFindingAlgoOptions = new HashSet<>(Arrays.asList("bugNavOne", "bugNavTwo"));

    if (!pathFindingAlgoOptions.contains(pathFindingAlgo)) {
      System.out.println("ERROR: MoveTowardTarget pathfinding algo is invalid:" + pathFindingAlgo);
      return;
    }
    if (rc.getLocation().equals(target)) {
      System.out.println("IM AT TARGET");
//      resetBug();
      return;
    }
    switch (pathFindingAlgo) {
      case "bugNavOne": {
        bugNavOne(rc, target, isAllowedToFill);
        break;
      }
      case "bugNavTwo": {
        bugNavTwo(rc, target, isAllowedToFill);
        break;
      }
    }

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
    Direction[] priorityMovementOrder;
    Direction rotatedLeft = oppositeDirection.rotateLeft();
    Direction rotatedLeftLeft = rotatedLeft.rotateLeft();
    Direction rotatedRight = oppositeDirection.rotateRight();
    Direction rotatedRightRight = rotatedRight.rotateRight();
    if (Util.isSamePlane(rc.getLocation(), target)) {
      priorityMovementOrder = new Direction[]{rotatedLeft, rotatedRight, oppositeDirection, rotatedLeftLeft, rotatedRightRight};
    } else {
      priorityMovementOrder = new Direction[]{oppositeDirection, rotatedLeft, rotatedRight, rotatedLeftLeft, rotatedRightRight};
    }
    for (Direction currDir : priorityMovementOrder) {
      if (rc.canMove(currDir)) {
        rc.move(currDir);
        return;
      }
    }
//    if (rc.canMove(oppositeDirection)) {
//      rc.move(oppositeDirection);
//      return;
//    }
//    //if cannot move in exact opposite, will go away in an angle
//    if (rc.canMove(oppositeDirection.rotateLeft())) {
//      rc.move(oppositeDirection.rotateLeft());
//      return;
//    }
//    if (rc.canMove(oppositeDirection.rotateRight())) {
//      rc.move(oppositeDirection.rotateRight());
//      return;
//    }
//    //moves perpendicular to target
//    if (rc.canMove(oppositeDirection.rotateLeft().rotateLeft())) {
//      rc.move(oppositeDirection.rotateRight().rotateLeft());
//      return;
//    }
//    if (rc.canMove(oppositeDirection.rotateRight().rotateRight())) {
//      rc.move(oppositeDirection.rotateRight().rotateRight());
//      return;
//    }
  }

  //Not very helpful as doesn't really go anywhere
  public static void exploreRandomly(RobotController rc) throws GameActionException {
    if (!rc.isMovementReady()) {
      return;
    }
    //TODO if can fill fill
    if (exploreDirection != null && rc.canMove(exploreDirection)) {
      rc.move(exploreDirection);
    } else {
      exploreDirection = directions[RobotPlayer.rng.nextInt(directions.length)];
      if (rc.canMove(exploreDirection)) {
        rc.move(exploreDirection);
      }
    }
  }

  public static boolean canFill(RobotController rc, MapLocation target, boolean isAllowedToFill) throws GameActionException {
    if (!isAllowedToFill || !rc.canFill(target)) {
      return false;
    }
    rc.fill(target);
    return true;
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

  // maybe useful idk
//  public static void exploreRandomly(RobotController rc) throws GameActionException {
//    if (!rc.isMovementReady()) {
//      return;
//    }
//    if(exploreDirection == null)
//      Direction dir = directions[RobotPlayer.rng.nextInt(directions.length)];
////    MapLocation nextLoc = rc.getLocation().add(dir);
//    //does 3 times
//    if (!rc.canMove(dir)) {
//      dir = directions[RobotPlayer.rng.nextInt(directions.length)];
//      if (!rc.canMove(dir)) {
//        dir = directions[RobotPlayer.rng.nextInt(directions.length)];
//        if (!rc.canMove(dir)) {
//          return;
//        }
//      }
//    }
//    rc.move(dir);
//  }
}