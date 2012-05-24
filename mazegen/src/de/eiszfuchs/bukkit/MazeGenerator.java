package de.eiszfuchs.bukkit;

import de.eiszfuchs.bukkit.commands.mazegenCommand;
import de.eiszfuchs.bukkit.listeners.mazegenPlayer;
import de.eiszfuchs.bukkit.objects.mazegenMaze;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author eiszfuchs
 */
public class MazeGenerator extends JavaPlugin {
    
    // we would like to log
    public static final Logger log = Logger.getLogger("Minecraft");
    // we would like to use the same identifier for our messages
    static public String log_prefix = "";
    
    // all the mazes
    public static List<mazegenMaze> mazes = new ArrayList<mazegenMaze>();
    
    @Override
    public void onEnable() {
        // first we build our identifier for the log
        PluginDescriptionFile pdf = this.getDescription();
        log_prefix = "[" + pdf.getName() + " v" + pdf.getVersion() + "] ";
        
        // say hello to the world
        log.info(log_prefix + "Hello! I heard you like random mazes. :)");
        
        // restore mazes
        log.info(log_prefix + "Restoring mazes ...");
        List<HashMap> maze_storage = (List<HashMap>) this.getConfig().getList("mazes");
        int i = 0;
        for(HashMap hash: maze_storage) {
            World world = getServer().getWorld(String.valueOf(hash.get("world")));
            
            int x, y, z;
            
            x = Integer.valueOf(hash.get("min_x").toString());
            y = Integer.valueOf(hash.get("min_y").toString());
            z = Integer.valueOf(hash.get("min_z").toString());
            
            Location p1 = new Location(world, x, y, z);
            
            x = Integer.valueOf(hash.get("max_x").toString());
            y = Integer.valueOf(hash.get("max_y").toString());
            z = Integer.valueOf(hash.get("max_z").toString());
            
            Location p2 = new Location(world, x, y, z);
            
            mazegenMaze maze = new mazegenMaze(p1, p2);
                    
            List<Integer> id = (List<Integer>) hash.get("wall_id");
            List<Integer> data_int = (List<Integer>) hash.get("wall_data");
            List<Byte> data = new ArrayList<Byte>();
            for(int num: data_int) {
                data.add(Byte.valueOf(Integer.toString(num)));
            }

            maze.setWallID(id);
            maze.setWallData(data);
            
            maze.setActive(Boolean.valueOf(hash.get("active").toString()));
            maze.setCakemode(Boolean.valueOf(hash.get("cakemode").toString()));
            
            log.info(log_prefix + "Restored #" + i + " in world \"" + world.getName() + "\".");
            MazeGenerator.mazes.add(maze);
            
            i++;
        }
        
        log.info(log_prefix + "Finished mazes!");
        
        getServer().getPluginManager().registerEvents(new mazegenPlayer(), this);
        getCommand("mazegen").setExecutor(new mazegenCommand());
    }

    @Override
    public void onDisable() {
        // generate maze object for storage
        List<HashMap> maze_storage = new ArrayList<HashMap>();
        for(mazegenMaze maze: MazeGenerator.mazes) {
            HashMap hash = new HashMap();
            
            hash.put("world", maze.getMin().getWorld().getName());
            hash.put("active", maze.getActive());
            hash.put("cakemode", maze.getCakemode());
            
            hash.put("wall_id", maze.getWallID());
            hash.put("wall_data", maze.getWallData());
            
            hash.put("min_x", maze.getMin().getBlockX());
            hash.put("min_y", maze.getMin().getBlockY());
            hash.put("min_z", maze.getMin().getBlockZ());
            
            hash.put("max_x", maze.getMax().getBlockX());
            hash.put("max_y", maze.getMax().getBlockY());
            hash.put("max_z", maze.getMax().getBlockZ());
            
            maze_storage.add(hash);
        }
        
        this.getConfig().set("mazes", maze_storage);
        this.saveConfig();
    }
}
