package moveBot1;

import battlecode.common.*;

public enum Role {
  ATTACKER,
  HEALER,
  BUILDER,
  GUARDIAN;

  public static RunnableBot getRobotRole(int id) {
    RunnableBot bot = new Attacker();
    if (id % 7 == 0) {
      bot = new Builder();
    } else if (id % 3 == 0) {
      bot = new Healer();
    }
    bot.init();
    System.out.println("My role is: " + bot.role);
    return bot;
  }
}
