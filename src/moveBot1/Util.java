package moveBot1;

import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;

public class Util {
  public static boolean isInAttackRange(MapLocation loc1, MapLocation loc2) throws GameActionException {
    return loc1.isWithinDistanceSquared(loc2, GameConstants.ATTACK_RADIUS_SQUARED);
  }
}
