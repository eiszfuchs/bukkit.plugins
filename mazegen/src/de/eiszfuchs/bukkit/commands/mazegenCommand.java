package de.eiszfuchs.bukkit.commands;

import de.eiszfuchs.bukkit.MazeGenerator;
import de.eiszfuchs.bukkit.listeners.mazegenPlayer;
import de.eiszfuchs.bukkit.objects.mazegenMaze;
import de.eiszfuchs.bukkit.utils.mazegenPlayerUtil;
import java.awt.Point;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eiszfuchs
 */
public class mazegenCommand implements CommandExecutor {
    
    public mazegenCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if((arguments.length > 0) && (sender instanceof Player)) {
            String subcommand = arguments[0];
            Player player = (Player) sender;
            
            if(subcommand.equalsIgnoreCase("add")) {
                if(player.hasPermission("mazegen.add")) {
                    mazegenPlayer.mazes_add.put(player, new HashSet<Location>());
                    return true;
                } else {
                    // TODO: player has not the required permission
                }
            } else if(subcommand.equalsIgnoreCase("remove")) {
                if(player.hasPermission("mazegen.remove")) {
                    mazegenMaze maze = mazegenPlayerUtil.get_inside(player);
                    if(maze != null) {
                        MazeGenerator.mazes.remove(maze);
                    } else {
                        // TODO: player is outside a maze
                    }
                    
                    return true;
                } else {
                    // TODO: player has not the required permission
                }
            } else if(subcommand.equalsIgnoreCase("toggle")) {
                if(player.hasPermission("mazegen.toggle")) {
                    mazegenMaze maze = mazegenPlayerUtil.get_inside(player);
                    if(maze != null) {
                        maze.setActive(!maze.getActive());
                    } else {
                        // TODO: player is outside a maze
                    }
                    
                    return true;
                } else {
                    // TODO: player has not the required permission
                }
            } else if(subcommand.equalsIgnoreCase("mode")) {
                if(player.hasPermission("mazegen.mode")) {
                    mazegenMaze maze = mazegenPlayerUtil.get_inside(player);
                    if(maze != null) {
                        maze.setCakemode(!maze.getCakemode());
                    } else {
                        // TODO: player is outside a maze
                    }
                    
                    return true;
                } else {
                    // TODO: player has not the required permission
                }
            } else if(subcommand.equalsIgnoreCase("lock")) {
                mazegenMaze maze = mazegenPlayerUtil.get_inside(player);
                if(maze != null) {
                    maze.lock();
                }
            } else if(subcommand.equalsIgnoreCase("unlock")) {
                mazegenMaze maze = mazegenPlayerUtil.get_inside(player);
                if(maze != null) {
                    maze.unlock();
                }
            } else if(subcommand.equalsIgnoreCase("generate")) {
                mazegenMaze maze = mazegenPlayerUtil.get_inside(player);
                if(maze != null) {
                    maze.generate(new Point(0, 0));
                }
            } else if(subcommand.equalsIgnoreCase("clear")) {
                mazegenMaze maze = mazegenPlayerUtil.get_inside(player);
                if(maze != null) {
                    maze.clear();
                }
            }
        }
        return false;
    }
}
