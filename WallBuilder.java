import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Scanner;
import model.Wall;

/**
 * Created by arthurbacon on 9/30/17.
 */
public class WallBuilder {


  /**
   * A simple constructor for a wall builder
   */
  public WallBuilder() {


  }


  int getLargestRowIndex(List<List<Boolean> > askiiInBools) {
    int result = 0;
    int largestSize = 0;
    for (int i = 0; i < askiiInBools.size(); i++) {
      if (askiiInBools.get(i).size() > largestSize) {
        largestSize = askiiInBools.get(i).size();
        result = i;
      }
    }
    return result;
  }


  List<Wall> generateVertWalls(List<List<Boolean>> askiiInBools) {

    List<Wall> result = new ArrayList<>();

    Point2D.Double startPoint = new Point2D.Double(0, 0);

    int largestRowIndex = getLargestRowIndex(askiiInBools);
    for (int columnNumber = 0; columnNumber < askiiInBools.get(largestRowIndex).size(); columnNumber++) {
      boolean workingOnWall = false;
      for (int rowNumber = 0; rowNumber < askiiInBools.size(); rowNumber++) {
        ///////////////
        // Error handeling
        if (askiiInBools.get(rowNumber).size() == 0 || columnNumber >= askiiInBools.get(rowNumber).size()) {

          // If we are parsing past the end of the row
          //   IE we could have an empty row
          //   or we could have a short row
          if (workingOnWall) {
            // If we are working on a wall
            // End and add it
            double dist = Math.abs(startPoint.y - rowNumber);
            result.add(new Wall(startPoint.x, startPoint.y, Math.PI * 1 / 2, dist - 1));
            workingOnWall = false;
          } else {
            // If we are NOT working on a wall
          }

          continue;
        }

        /////////////////
        // Main logic
        if (askiiInBools.get(rowNumber).get(columnNumber)) {
          // If the current spot has a wall
          if (workingOnWall) {
            // if we're working on a wall
            // Ignore it we're already building
          } else {
            // If we're not working on a wall
            if (rowNumber >= (askiiInBools.size() - 1)) {
              // If we're at the real edge of the askiiInBoolsTable
            } else {
              // we are NOT at the real edge of the askiiInBoolsTable

              try {
                askiiInBools.get(rowNumber + 1).get(columnNumber);
                // Next line is NOT out of boudns => we are NOT at the effective edge of askiiTable

                if (askiiInBools.get(rowNumber + 1).get(columnNumber)) {
                  // There is a wall below us
                  // Start building a wall
                  startPoint = new Point2D.Double(columnNumber, rowNumber);
                  workingOnWall = true;
                }
              } catch(IndexOutOfBoundsException exception) {
                // Next line is out of bounds => effective edge of askiiInBoolTable

                // There is NOT a wall below us
                // Current wall is probably horizontal pls ignore.
              }
            }
          }
        } else {
          // If the current spot does NOT have a wall
          if (workingOnWall) {
            // If we are working on a wall
            // finish it!
            double dist = Math.abs(startPoint.y - rowNumber);
            result.add(new Wall(startPoint.x, startPoint.y, Math.PI * 1 / 2, dist - 1));
            workingOnWall = false;
          } else {
            // If we are not working on a wall
            // Ignore it
          }
        } // End mother of all If statements
      } // End column

      if (workingOnWall) {
        // end the wall
        double dist = Math.abs(startPoint.y - askiiInBools.size());
        result.add(new Wall(startPoint.x, startPoint.y, Math.PI * 1 / 2, dist - 1));
        workingOnWall = false;
      }
    } // End row

    return result;
  }


  List<Wall> generateHorizWalls(List<List<Boolean>> askiiInBools) {


    List<Wall> result = new ArrayList<>();

    Point2D.Double startPoint = new Point2D.Double(0, 0);

    for (int rowNumber = 0; rowNumber < askiiInBools.size(); rowNumber++) {
      boolean workingOnWall = false;
      for (int columnNumber = 0; columnNumber < askiiInBools.get(rowNumber).size(); columnNumber++) {

        if (askiiInBools.get(rowNumber).get(columnNumber)) {
          // If the current spot has a wall
          if (workingOnWall) {
            // If we are working on a wall
            // Ignore it, we're already building it.
          } else {
            // If we're not working on a wall
            if (columnNumber >= askiiInBools.get(rowNumber).size()) {
              // If we're at the real edge of the askiiInBools table

              // Ignore it. It's probably a vert wall
            } else {
              // We are NOT at the real end of the askiiInBools table
              // Start building a wall

              try {
                askiiInBools.get(rowNumber).get(columnNumber + 1);
                // Next char is NOT out of boudns => we are NOT at the effective edge of askiiTable

                if (askiiInBools.get(rowNumber).get(columnNumber + 1)) {
                  // There is a wall right of us
                  // Start building a wall
                  startPoint = new Point2D.Double(columnNumber, rowNumber);
                  workingOnWall = true;
                }
              } catch(IndexOutOfBoundsException exception) {
                // Next character is out of bounds => effective edge of askiiInBoolTable

                // There is NOT a wall right of us
                // Current wall is probably horizontal pls ignore.
              }

            }
          }
        } else {
          // If the current spot does NOT have a wall
          if (workingOnWall) {
            // We are currently working on a wall
            // Build that wall!
            double dist = Math.abs(startPoint.x - columnNumber);
            result.add(new Wall(startPoint.x, startPoint.y, 0, dist - 1));
            workingOnWall = false;
          } else {
            // We are not currently working on a wall
            // Ignore spot
          }
        }

      } // End for loop (column number)

      if (workingOnWall) {
        // If we ended the row and we're still working on a wall
        // build it
        double dist = Math.abs(startPoint.x - askiiInBools.get(rowNumber).size());
        result.add(new Wall(startPoint.x, startPoint.y, 0, dist - 1));
        workingOnWall = false;
      }
    } // End for loop (row number


    return result;
  }




  public List<Wall> wallsFromFile(URL targetFileName) {

    Scanner scan = new Scanner(System.in);
    try {
      scan = new Scanner(targetFileName.openStream());
    } catch (Exception e) {
      System.out.println("ERROR");
    }
    List<Wall> result = new ArrayList<>();

    List<List<Boolean>> askiiInBools = new ArrayList<List<Boolean>>();

    // create a 100 X 100 sized box for use to play with
//    for(int i = 0; i < 100; i++) {
//      askiiInBools.add(new ArrayList<>(Collections.nCopies(100, false)));
//    }

    askiiInBools.add(new ArrayList<>());

    int rowNumber = 0;

    while (scan.hasNextLine()) {
      String line = scan.nextLine();
      for (int columnNumber = 0; columnNumber < line.length(); columnNumber++) {
        if (line.charAt(columnNumber) == ' ') {
          askiiInBools.get(rowNumber).add(false);
        } else {
          askiiInBools.get(rowNumber).add(true);
        }
      } // End for loop

      // We finished the row, move to the next
      rowNumber += 1;
      askiiInBools.add(new ArrayList<>());
    } // End while loop

    //////////////////////////////

    // Loop through the table and generate walls from that


    // Generate the horiz walls first

   List<Wall> hroizWalls = generateHorizWalls(askiiInBools);
    ///////////////////////////
    // Generate vert walls
    List<Wall> vertWalls = generateVertWalls(askiiInBools);

    result.addAll(hroizWalls);
    result.addAll(vertWalls);
    return result;

  }



}
