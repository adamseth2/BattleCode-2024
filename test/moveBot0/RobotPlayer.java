package moveBot0;

import battlecode.common.*;

import java.util.Random;

/**
 * RobotPlayer is the class that describes your main robot strategy.
 * The run() method inside this class is like your main function: this is what we'll call once your robot
 * is created!
 */
public strictfp class RobotPlayer {

  /**
   * We will use this variable to count the number of turns this robot has been alive.
   * You can use static variables like this to save any information you want. Keep in mind that even though
   * these variables are static, in Battlecode they aren't actually shared between your robots.
   */
  static int turnCount = 0;

  /**
   * A random number generator.
   * We will use this RNG to make some random moves. The Random class is provided by the java.util.Random
   * import at the top of this file. Here, we *seed* the RNG with a constant number (6147); this makes sure
   * we get the same sequence of numbers every time this code is run. This is very useful for debugging!
   */
  static Random rng = null;
  public static int personalID = -1;
  public static int guardianID = -1;
  public static RunnableBot bot;
  public static MapLocation[] centers = new MapLocation[3];

  /**
   * Array containing all the possible movement directions.
   */
  static final Direction[] directions = {
          Direction.NORTH,
          Direction.NORTHEAST,
          Direction.EAST,
          Direction.SOUTHEAST,
          Direction.SOUTH,
          Direction.SOUTHWEST,
          Direction.WEST,
          Direction.NORTHWEST,
  };

  /**
   * run() is the method that is called when a robot is instantiated in the Battlecode world.
   * It is like the main function for your robot. If this method returns, the robot dies!
   *
   * @param rc The RobotController object. You use it to perform actions from this robot, and to get
   *           information on its current status. Essentially your portal to interacting with the world.
   **/
  @SuppressWarnings("unused")
  public static void run(RobotController rc) throws GameActionException {
    boolean reachedDest = false;
    MapLocation dest = new MapLocation(4, 8);
    if (rng == null) {
      rng = new Random(rc.getID());
    }
    bot = Role.getRobotRole(rc.getID());
    if (personalID == -1 && rc.canWriteSharedArray(0, 0)) {
      personalID = rc.readSharedArray(63);
      rc.writeSharedArray(63, personalID + 1);

      // Assign first three builders to be in the guardian role
      int guardianCount = rc.readSharedArray(62);
      if (bot.role == Role.BUILDER && guardianCount < 3) {
        bot = new Guardian();
        guardianID = guardianCount;
        rc.writeSharedArray(62, guardianCount + 1);
      }
    }
    rc.setIndicatorString("Role is " + bot);
    getSpawnCenters(rc);

    // Hello world! Standard output is very useful for debugging.
    // Everything you say here will be directly viewable in your terminal when you run a match!
    // You can also use indicators to save debug notes in replays.

    while (true) {
      // This code runs during the entire lifespan of the robot, which is why it is in an infinite
      // loop. If we ever leave this loop and return from run(), the robot dies! At the end of the
      // loop, we call Clock.yield(), signifying that we've done everything we want to do.

      turnCount += 1;  // We have now been alive for one more turn!

      // Try/catch blocks stop unhandled exceptions, which cause your robot to explode.
      try {
        // Make sure you spawn your robot in before you attempt to take any actions!
        // Robots not spawned in do not have vision of any tiles and cannot perform any actions.
//        if (rc.readSharedArray(0) == 0) {
//          rc.writeSharedArray(0, rc.getID());
//        }
//        if (rc.readSharedArray(0) == rc.getID()) {
//        }
        trySpawn(rc);

//                    MapLocation[] crumbArr = rc.senseNearbyCrumbs(-1);
//                    MapLocation nearestCrumb = null;
//                    if(crumbArr.length != 0) {
//                      nearestCrumb = crumbArr[0];
//                    }
//          rc.setIndicatorString(Boolean.toString(reachedDest));
        boolean isInSetUpPhrase = rc.getRoundNum() < GameConstants.SETUP_ROUNDS;
        if (isInSetUpPhrase) {
          bot.runSetUp(rc);
        } else {
          bot.runMainPhrase(rc);
        }
        if (rc.isSpawned()) {
          if (bot.role == Role.BUILDER) {
            rc.setIndicatorDot(rc.getLocation(), 0, 0, 255);
          } else if (bot.role == Role.GUARDIAN) {
            rc.setIndicatorDot(rc.getLocation(), 0, 255, 0);
          } else if (bot.role == Role.HEALER) {
            rc.setIndicatorDot(rc.getLocation(), 255, 255, 0);
          }
//          if (!reachedDest && !rc.getLocation().equals(dest)) {
//            PathFind.bugNavOne(rc, dest, true);
//            continue;
//          } else if (!reachedDest) {
//            System.out.println("INSIDE");
//            PathFind.resetBug();
//            reachedDest = true;
//          }
//          Skirmish.basicOffense2(rc);


//                  if (rc.canMove(Direction.WEST)) {
//                    rc.move(Direction.WEST);
//                  } else {
//                    rc.setIndicatorString("I hit a wall!");
//                  }

        }

      } catch (GameActionException e) {
        // Oh no! It looks like we did something illegal in the Battlecode world. You should
        // handle GameActionExceptions judiciously, in case unexpected events occur in the game
        // world. Remember, uncaught exceptions cause your robot to explode!
        System.out.println("GameActionException");
        e.printStackTrace();

      } catch (Exception e) {
        // Oh no! It looks like our code tried to do something bad. This isn't a
        // GameActionException, so it's more likely to be a bug in our code.
        System.out.println("Exception");
        e.printStackTrace();

      } finally {
        // Signify we've done everything we want to do, thereby ending our turn.
        // This will make our code wait until the next turn, and then perform this loop again.
        Clock.yield();
      }
      // End of loop: go back to the top. Clock.yield() has ended, so it's time for another turn!
    }

    // Your code should never reach here (unless it's intentional)! Self-destruction imminent...
  }

  public static void trySpawn(RobotController rc) throws GameActionException {
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

  public static void getSpawnCenters(RobotController rc) throws GameActionException {
    MapLocation[] spawnLocs = rc.getAllySpawnLocations();
    for (int i = 0; i < spawnLocs.length; i++) {
      int adjCount = 0;
      for (int j = 0; j < spawnLocs.length; j++) {
        if (spawnLocs[j].isAdjacentTo(spawnLocs[i])) {
          adjCount++;
        }
      }
      if (adjCount == 9) {
        Util.addToNextEmptyIndex(centers, spawnLocs[i]);
      }
    }

//    }
  }

  public static void updateEnemyRobots(RobotController rc) throws GameActionException {
    // Sensing methods can be passed in a radius of -1 to automatically
    // use the largest possible value.
    RobotInfo[] enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
    if (enemyRobots.length != 0) {
      rc.setIndicatorString("There are nearby enemy robots! Scary!");
      // Save an array of locations with enemy robots in them for future use.
      MapLocation[] enemyLocations = new MapLocation[enemyRobots.length];
      for (int i = 0; i < enemyRobots.length; i++) {
        enemyLocations[i] = enemyRobots[i].getLocation();
      }
      // Let the rest of our team know how many enemy robots we see!
      if (rc.canWriteSharedArray(0, enemyRobots.length)) {
        rc.writeSharedArray(0, enemyRobots.length);
        int numEnemies = rc.readSharedArray(0);
      }
    }
  }
}
