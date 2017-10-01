package model;

import java.awt.geom.Point2D;
import java.util.List;

import javafx.scene.paint.Color;

/**
 * Created by arthurbacon on 9/30/17.
 */
public class Baddie {

  private double positionX;
  private double positionY;
  private double speed;
  private double size;

  public Wall wallHoriz;
  public Wall wallVert;

  // This is the vector's needed to get to the next point
  private List<Point2D> patrolPositions;
  private int currentPatrolPosition;
  private double traveled;
  private double distToTravel;
  private Point2D unitVector;

   public Baddie(double x, double y, double size) {
     positionX = x;
     positionY = y;
     this.size = size;

     wallHoriz = new Wall(0, 0, 0, size);
     wallVert = new Wall(0, 0, Math.PI * 3 / 2, size);
     moveWallsToBaddiePos();

     wallHoriz.color1 = Color.WHITE;
     wallHoriz.color2 = Color.WHITE;
     wallVert.color1 = Color.WHITE;
     wallVert.color2 = Color.WHITE;
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


  private void moveWallsToBaddiePos() {
    wallHoriz.x = positionX - (size / 2);
    wallHoriz.y = positionY;

    wallVert.x = positionX;
    wallVert.y = positionY + (size / 2);
  }





  public void setPatrol(List<Point2D> points, double speed) {
    this.patrolPositions = points;
    this.currentPatrolPosition = points.size() - 1; // reset by nextPatrolIndex()
    this.speed = speed;
    this.traveled = 0;
    this.distToTravel = 0;
  }

  private int nextPatrolIndex() {
    if (currentPatrolPosition == patrolPositions.size() - 1) {
      return 0;
    } else {
      return currentPatrolPosition + 1;
    }
  }

    public void updateBaddie(double deltaTime) {
        //Modify the x,y by the standardized vector
        // Check to see if we are >= target distance
        //   if yes:
        //      Update standardized vector
        //      Reset distance traveled to 0
        //        (reset time to 0)
        //   if no:
        //      Keep on the same direction
//        System.out.println("traveled" + traveled + " >= " + "distacnce" + distToTravel);

        if (traveled >= distToTravel) {
            // change move direction (unitVector)
            //System.out.println("this= "+ this);
            currentPatrolPosition = nextPatrolIndex();
            //System.out.println("Want to travel to x=" + patrolPositions.get(currentPatrolPosition).getX() + ", y=" +
            //        patrolPositions.get(currentPatrolPosition).getY());

            double deltaX = patrolPositions.get(currentPatrolPosition).getX() - positionX ;
            double deltaY = patrolPositions.get(currentPatrolPosition).getY() - positionY;

            this.distToTravel = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
            this.traveled = 0;
            //System.out.println("DIST TO TRAVEL = " + distToTravel);
            // Vector of length 1 pointing towards the patrol point to move to.
            this.unitVector = new Point2D.Double(deltaX / distToTravel, deltaY / distToTravel);
            //System.out.println("Travel by x=" + unitVector.getX() + ", y=" + unitVector.getY());

        } else {
            this.positionX += deltaTime * speed * unitVector.getX();
            this.positionY += deltaTime * speed * unitVector.getY();
            moveWallsToBaddiePos();

//            System.out.println("T=" + traveled);
            //if (Math.random() > 0.99)
                //System.out.println("x="+this.positionX +"\nt="+traveled+"\n\n");
            traveled += speed * deltaTime;
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

  public String toString() {
    String val = "";
    val += "Baddie [x=" + positionX + ", y=" + positionY + "]";
    return val;
  }
}
