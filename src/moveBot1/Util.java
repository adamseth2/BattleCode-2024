package moveBot1;

import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.HashSet;

public class Util {
  public static boolean isInAttackRange(MapLocation loc1, MapLocation loc2) throws GameActionException {
    return loc1.isWithinDistanceSquared(loc2, GameConstants.ATTACK_RADIUS_SQUARED);
  }

  public static boolean isSamePlane(MapLocation loc1, MapLocation loc2) throws GameActionException {
    return loc1.x == loc2.x || loc1.y == loc2.y;
  }

  //shortest distance
  public static MapLocation closestLocation(RobotController rc, MapLocation origin, MapLocation[] locArr) {
    if (locArr == null || locArr.length == 0) {
      return null;
    }
    int closestLocDistance = locArr[0].distanceSquaredTo(origin);
    MapLocation closestLoc = locArr[0];
    for (int i = 1; i < locArr.length; i++) {
      int currDistance = locArr[i].distanceSquaredTo(origin);
      if (currDistance < closestLocDistance) {
        closestLoc = locArr[i];
        closestLocDistance = currDistance;
      }
    }
    return closestLoc;
  }

  public static HashSet<MapLocation> createLine(MapLocation a, MapLocation b) {
    HashSet<MapLocation> locs = new HashSet<>();
    int x = a.x, y = a.y;
    int dx = b.x - a.x;
    int dy = b.y - a.y;
    int sx = (int) Math.signum(dx);
    int sy = (int) Math.signum(dy);
    dx = Math.abs(dx);
    dy = Math.abs(dy);
    int d = Math.max(dx, dy);
    int r = d / 2;
    if (dx > dy) {
      for (int i = 0; i < d; i++) {
        locs.add(new MapLocation(x, y));
        x += sx;
        r += dy;
        if (r >= dx) {
          locs.add(new MapLocation(x, y));
          y += sy;
          r -= dx;
        }
      }
    } else {
      for (int i = 0; i < d; i++) {
        locs.add(new MapLocation(x, y));
        y += sy;
        r += dx;
        if (r >= dy) {
          locs.add(new MapLocation(x, y));
          x += sx;
          r -= dy;
        }
      }
    }
    locs.add(new MapLocation(x, y));
    return locs;
  }
}
