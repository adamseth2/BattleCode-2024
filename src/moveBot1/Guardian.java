package moveBot1;

import battlecode.common.*;

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

  @Override
  public void trySpawn(RobotController rc) throws GameActionException {
    if (rc.isSpawned()) {
      return;
    }
    MapLocation centerSpawn = RobotPlayer.centers[RobotPlayer.guardianID];
    if (rc.canSpawn(centerSpawn)) {
      rc.spawn(centerSpawn);
    }
    for (Direction currDir : Direction.allDirections()) {
      if (rc.canSpawn(centerSpawn.add(currDir))) {
        rc.spawn(centerSpawn.add(currDir));
      }
    }
  }
//    public static void runGuardian(RobotController rc) throws GameActionException {

}
