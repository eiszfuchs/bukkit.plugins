package de.eiszfuchs.bukkit.utils;

import de.eiszfuchs.bukkit.MazeGenerator;
import de.eiszfuchs.bukkit.objects.mazegenMaze;
import org.bukkit.entity.Player;

/**
 *
 * @author eiszfuchs
 */
public class mazegenPlayerUtil {
    
    public static mazegenMaze get_inside(Player player) { return get_inside(player, 0); }
    public static mazegenMaze get_inside(Player player, int margin) {
        for(mazegenMaze maze: MazeGenerator.mazes) {
            if(maze.isInside(player, margin)) {
                return maze;
            }
        }
        return null;
    }
}
