package moveBot0;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Builder extends RunnableBot {

  public void init() {
    this.role = Role.BUILDER;
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
    PathFind.exploreRandomly(rc);
  }

}
