package de.eiszfuchs.bukkit.listeners;

import de.eiszfuchs.bukkit.MazeGenerator;
import de.eiszfuchs.bukkit.objects.mazegenMaze;
import de.eiszfuchs.bukkit.utils.mazegenPlayerUtil;
import java.awt.Point;
import java.util.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eiszfuchs
 */
public class mazegenPlayer implements Listener {
    
    public static HashMap<Player, HashSet<Location>> mazes_add = new HashMap<Player, HashSet<Location>>();
    
    private HashMap<Player, mazegenMaze> mazes_inside = new HashMap<Player, mazegenMaze>();
    private HashMap<Player, Location> mazes_outside = new HashMap<Player, Location>();
    private HashMap<Player, ItemStack[]> mazes_inventories = new HashMap<Player, ItemStack[]>();
    private HashMap<Player, Date> mazes_dates = new HashMap<Player, Date>();
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        mazegenMaze maze_was, maze_is;
        Player player = event.getPlayer();
        
        maze_was = this.mazes_inside.get(player);
        if(maze_was != null) {
            maze_is = mazegenPlayerUtil.get_inside(player, 1);
        } else {
            maze_is = mazegenPlayerUtil.get_inside(player);
        }
        
        if(maze_was == maze_is && maze_is != null) {
            // player moved inside the same maze
        } else {
            if(maze_was == null) {
                // player has NOT been inside a maze
                if(maze_is != null) {
                    // player entered a maze
                    if(maze_is.getActive() && this.mazes_outside.containsKey(player)) {
                        this.mazes_inventories.put(player, player.getInventory().getContents());
                        player.getInventory().clear();
                    
                        Point start = new Point(
                            (player.getLocation().getBlockZ() - maze_is.getMin().getBlockZ()) / 2,
                            (player.getLocation().getBlockX() - maze_is.getMin().getBlockX()) / 2
                        );
                        maze_is.generate(start);
                    
                        this.mazes_inside.put(player, maze_is);
                        this.mazes_dates.put(player, new Date());
                    }   
                } else {
                    // player moved outside of all mazes
                    this.mazes_outside.put(player, player.getLocation());
                }
            } else {
                // player HAS been inside a maze
                if(maze_is == null) {
                    // player left a maze
                    Date now = new Date();
                    Double time = (now.getTime() - this.mazes_dates.get(player).getTime()) / 1000.0;
                    time = time/60.0;
                    time = Math.round(time * 10) / 10.0;
                    
                    player.sendMessage(time + " minutes wasted.");
                
                    maze_was.clear();
            
                    ItemStack[] inventory = this.mazes_inventories.get(player);
                    if(inventory != null) {
                        player.getInventory().setContents(inventory);
                    }
                    
                    this.mazes_inside.remove(player);
                    this.mazes_dates.remove(player);
                } else {
                    // player from a maze into another
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if(mazegenPlayer.mazes_add.containsKey(player)) {
            Location loc = event.getClickedBlock().getLocation();
            HashSet<Location> edges = mazegenPlayer.mazes_add.get(player);
            
            edges.add(loc);
            mazegenPlayer.mazes_add.put(player, edges);
            
            if(edges.size() < 2) {
                player.sendMessage(ChatColor.GOLD + "Noch eine Ecke, bitte!");
            } else {
                Object[] edge_array = edges.toArray();
                
                Location edge1 = ((Location) edge_array[0]);
                Location edge2 = ((Location) edge_array[1]);

                if(mazegenMaze.checkLocations(edge1, edge2)) {
                    player.sendMessage(ChatColor.GREEN + "Ecken wurden akzeptiert!");
                    
                    mazegenMaze maze = new mazegenMaze(edge1, edge2);
                    
                    List<Integer> id = new ArrayList<Integer>();
                    List<Byte> data = new ArrayList<Byte>();
                    
                    id.add(5);
                    id.add(5);
                    id.add(5);
                    
                    data.add((byte) 0);
                    data.add((byte) 0);
                    data.add((byte) 0);
                    
                    maze.setWallID(id);
                    maze.setWallData(data);
                    
                    MazeGenerator.mazes.add(maze);
                } else {
                    player.sendMessage(ChatColor.RED + "Ecken ungültig!");
                }

                mazegenPlayer.mazes_add.remove(player);
            }
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        mazegenMaze maze_is;
        Player player = event.getPlayer();
        
        maze_is = mazegenPlayerUtil.get_inside(player);
        if(maze_is != null) {
            // TODO: ALTHOUGH THIS SHOULD NEVER HAPPEN DO SOMETHING ABOUT IT!
        } else {
            this.mazes_outside.put(player, player.getLocation());
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        mazegenMaze maze_was;
        Player player = event.getPlayer();
        
        maze_was = this.mazes_inside.get(player);
        if(maze_was != null) {
            // player quit while being inside a maze
            maze_was.clear();
            
            ItemStack[] inventory = this.mazes_inventories.get(player);
            if(inventory != null) {
                player.getInventory().setContents(inventory);
            }
            
            player.teleport(this.mazes_outside.get(player));
            this.mazes_inside.remove(player);
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        // ouch!
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            Boolean inside = false;
            for(mazegenMaze maze: MazeGenerator.mazes) {
                if(maze.isInside(player)) {
                    inside = true;
                }
            }
            
            if(inside) {
                // player is inside a maze and should not be hurt!
                event.setCancelled(true);
            }
        }
    }
}
