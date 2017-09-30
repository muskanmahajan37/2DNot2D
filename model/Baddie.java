package model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

/**
 * Created by arthurbacon on 9/30/17.
 */
public class Baddie {

  public double positionX;
  public double positionY;

  public Wall wallHoriz;
  public Wall wallVert;

  // This is the vector's needed to get to the next point
  List<Point2D> patrolDeltas;
  Point2D currentStandardizedVector;
  double speed;
  boolean patroling;
  int currentPatrolDeltaIndex;
  double initialDistance;
  long timesCalled;


  double size;
   public Baddie(double x, double y, double size) {
     positionX = x;
     positionY = y;
     this.size = size;

     double wallLength = 1 * size;
     wallHoriz = new Wall(x - (wallLength / 2), y, 0, wallLength);
     wallVert = new Wall(x, y + (wallLength / 2), Math.PI * 3 / 2, wallLength);

     patroling = false;
     timesCalled = 0;

     wallHoriz.color1 = Color.WHITE;
     wallVert.color1 = Color.WHITE;
   }

  public void scaleBaddie(double scaleBy) {
    positionY = positionY * scaleBy;
    positionX = positionX * scaleBy;

    wallHoriz = new Wall(wallHoriz.x * scaleBy,
                         wallHoriz.y * scaleBy,
                         wallHoriz.theta,
                         wallHoriz.length * scaleBy);
    wallVert = new Wall(wallVert.x * scaleBy,
                        wallVert.y * scaleBy,
                        wallVert.theta,
                        wallVert.length * scaleBy);
  }


  void updateWalls() {
    double wallLength = 1 * size;
    wallVert.x = positionX - (wallLength / 2);
    wallVert.y = positionY;

    wallHoriz.x = positionX;
    wallHoriz.y = positionY + (wallLength / 2);
  }

  public void setPatrol(List<Point2D> points, double speed) {
    //this.patrolPts = points;
    List<Point2D> deltas = new ArrayList<Point2D>(points.size());
    for (int i = 0; i < points.size(); i++) {
      if (i == points.size() - 1) {
        double xDelta = points.get(i).getX() - points.get(0).getX();
        double yDelta = points.get(i).getY() - points.get(0).getY();
        deltas.add(i, new Point2D.Double(xDelta, yDelta));
      } else {
        double xDelta = points.get(i).getX() - points.get(i + 1).getX();
        double yDelta = points.get(i).getY() - points.get(i + 1).getY();
        deltas.add(i, new Point.Double(xDelta, yDelta));
      }
    }


    currentPatrolDeltaIndex = 0;
    timesCalled = 0;
    this.patrolDeltas = deltas;
    nextInitialDistance(); // set the initialDistance
    updateStandardVector(); // set the standardVector
    this.speed = speed/60;
    this.patroling = true;
  }

  void nextInitialDistance() {
    if (currentPatrolDeltaIndex == patrolDeltas.size() - 1) {
      initialDistance = Math.sqrt(Math.pow(patrolDeltas.get(0).getX(), 2) +
                                  Math.pow(patrolDeltas.get(0).getY(), 2));
    } else {
      initialDistance = Math.sqrt(Math.pow(patrolDeltas.get(currentPatrolDeltaIndex + 1).getX(), 2) +
                                  Math.pow(patrolDeltas.get(currentPatrolDeltaIndex + 1).getY(), 2));
    }
  }

  private int nextPatrolDeltaIndex() {
    if (currentPatrolDeltaIndex == patrolDeltas.size() - 1) {
      currentPatrolDeltaIndex = 0;
      return 0;
    } else {
      currentPatrolDeltaIndex += 1;
      return currentPatrolDeltaIndex;
    }
  }


  private double getScalerForStandardization() {
    double x = patrolDeltas.get(currentPatrolDeltaIndex).getX();
    double y = patrolDeltas.get(currentPatrolDeltaIndex).getY();
    double a = 1.0;
    double b = 2*(x + y);
    double c = -1 * ((speed - (Math.pow(x, 2) + Math.pow(y, 2))) / 2);

    return (((-1)*b) + Math.sqrt(Math.pow(b, 2) - (4*a*c)))/(2*a);
  }

  private void updateStandardVector() {
    // Will update standardVector to whatever the index is currently point to.
    double scaler = getScalerForStandardization();
    currentStandardizedVector =
            new Point2D.Double(patrolDeltas.get(currentPatrolDeltaIndex).getX() * scaler,
                               patrolDeltas.get(currentPatrolDeltaIndex).getY() * scaler);
  }

  private void modifyXYByStandard() {
    // modify the current x,y by the currentStandardizedVector
    this.positionX += currentStandardizedVector.getX();
    this.positionY += currentStandardizedVector.getY();
    updateWalls();
  }

  public void updateBaddie() {
    if (patroling) {

      //Modify the x,y by the standardized vector
      // Check to see if we are >= target distance
      //   if yes:
      //      Update standardized vector
      //      Reset distance traveled to 0
      //        (reset time to 0)
      //   if no:
      //      Keep on the same direction


      modifyXYByStandard();

      if (speed * timesCalled >= initialDistance) {
        // Update standardized vector
        currentPatrolDeltaIndex = nextPatrolDeltaIndex();
        nextInitialDistance();
        updateStandardVector();
        // Reset timesCalled to 0;
        timesCalled = 0;
      } else {
        // Do nothing, keep going @ same vector
        timesCalled += 1;
      }
    }
  }


  /*

  x = deltaX
  y = deltaY
  S = speed/ standardized distance to travel per tick

  a = 1
  b = (2 * (x + y))
  c = -((S - (Math.pow(x, 2) + Math.pow(y, 2)))/2)
  dist = Math.abs( ((-b) + Math.sqrt(Math.pow(b, 2) - (4*a*c))) / (2 * a) )

  This standardizes the distance traveled every update.


  dist = standardized distance
  Once (dist * number of updates >= initial distance required to travel) happens:
    start moving in the next direction

  To start moving in the next direction:
    change the deltaX and deltaY vars, recalculate:
       initial distance
       standardized distance vector
       RESET: dist = 0;
              number of Updates = 0;

   */
}
