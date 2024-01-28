package moveBot1;

import battlecode.common.*;

public enum Role {
  ATTACKER,
  HEALER,
  BUILDER,
  GUARDIAN;

  public static RunnableBot getRobotRole(int id) {
    RunnableBot role = new Attacker();
    if (id % 7 == 0) {
      role = new Builder();
    } else if (id % 3 == 0) {
      role = new Healer();
    }
    return role;
  }
}
