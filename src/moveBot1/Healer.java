package moveBot1;

import battlecode.common.*;

public class Healer extends RunnableBot {

  public void init() {
    this.role = Role.HEALER;
  }

  public void runSetUp(RobotController rc) throws GameActionException {
    collectCrumbs(rc);
  }

  public void runMainPhrase(RobotController rc) throws GameActionException {
    if (rc.hasFlag()) {
      goToBase(rc);
    }
    heal(rc);
    //TODO MOVE AWAY
    Skirmish.basicOffense(rc);
    goToEnemyFlag(rc);
    PathFind.exploreRandomDirection(rc);
  }

}
