package moveBot0;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

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
    PathFind.exploreRandomly(rc);
  }

}
