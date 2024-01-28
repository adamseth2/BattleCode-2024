package moveBot1;

import battlecode.common.*;

public class Attacker extends RunnableBot {

  public void init() {
    this.role = Role.ATTACKER;
  }

  public void runSetUp(RobotController rc) throws GameActionException {
    collectCrumbs(rc);
  }

  public void runMainPhrase(RobotController rc) throws GameActionException {
    if (rc.hasFlag()) {
      goToBase(rc);
    }
    Skirmish.basicOffense(rc);
    heal(rc);
    goToEnemyFlag(rc);
    PathFind.exploreRandomDirection(rc);
  }
}

