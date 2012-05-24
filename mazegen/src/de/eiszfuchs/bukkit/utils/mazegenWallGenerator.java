package de.eiszfuchs.bukkit.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author eiszfuchs
 */
public class mazegenWallGenerator {

    public static HashMap generate(int width, int height, Point start, String type) {
        Random random = new Random();
        random.setSeed((new Date()).getTime());
        
        HashMap<String, Object> myreturn = new HashMap<String, Object>();

        int _x;
        int _y;
        boolean[][] walls = new boolean[height * 2 - 1][width * 2 - 1];
        boolean was_dead;
        ArrayList<Point> cells;
        ArrayList<Point> dead_ends;

        for (_y = 0; _y < (height * 2 - 1); _y++) {
            for (_x = 0; _x < (width * 2 - 1); _x++) {
                walls[_y][_x] = true;
            }
        }

        was_dead = false;
        cells = new ArrayList<Point>();
        dead_ends = new ArrayList<Point>();

        cells.add(start);

        int index = 0;
        int runner = 0;
        Point cell = null;
        ArrayList<Point> dirs = null;
        Point dir = null;
        while(cells.size() > 0) {
            if(type.equals("first")) {
                index = 0;
            } else if(type.equals("random")) {
                index = random.nextInt(cells.size());
            } else if(type.equals("50random")) {
                if(runner%2 == 0) {
                    index = random.nextInt(cells.size());
                } else {
                    index = cells.size() - 1;
                }
            } else if(type.equals("25random")) {
                if(runner%4 == 0) {
                    index = random.nextInt(cells.size());
                } else {
                    index = cells.size() - 1;
                }
            } else {
                index = cells.size() - 1;
            }

            cell = (Point) cells.get(index);
            walls[cell.y * 2][cell.x * 2] = false;

            dirs = new ArrayList<Point>();
            if(cell.x < width - 1)  { if (walls[(cell.y) * 2][(cell.x + 1) * 2]) { dirs.add(new Point(cell.x + 1, cell.y)); } }
            if(cell.x > 0)          { if (walls[(cell.y) * 2][(cell.x - 1) * 2]) { dirs.add(new Point(cell.x - 1, cell.y)); } }
            if(cell.y < height - 1) { if (walls[(cell.y + 1) * 2][(cell.x) * 2]) { dirs.add(new Point(cell.x, cell.y + 1)); } }
            if(cell.y > 0)          { if (walls[(cell.y - 1) * 2][(cell.x) * 2]) { dirs.add(new Point(cell.x, cell.y - 1)); } }

            if(dirs.size() > 0) {
                dir = (Point) dirs.get(random.nextInt(dirs.size()));
                walls[dir.y * 2][dir.x * 2] = false;
                cells.add(dir);
                dir = new Point(dir.x - cell.x, dir.y - cell.y);
                walls[cell.y * 2 + dir.y][cell.x * 2 + dir.x] = false;
                was_dead = false;
            } else {
                cells.remove(index);
                if(!was_dead) {
                    dead_ends.add(cell);
                    was_dead = true;
                }
            }
            runner++;
        }

        myreturn.put("walls", walls);
        myreturn.put("dead", dead_ends);

        return myreturn;
    }
}
