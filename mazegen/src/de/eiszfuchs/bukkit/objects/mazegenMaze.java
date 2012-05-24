package de.eiszfuchs.bukkit.objects;

import de.eiszfuchs.bukkit.MazeGenerator;
import de.eiszfuchs.bukkit.utils.mazegenWallGenerator;
import java.awt.Point;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author eiszfuchs
 */
public class mazegenMaze {
    private Location p1;
    private Location p2;
    
    private Player creator;
    
    private Boolean active;
    private Boolean cakemode;
    
    private Random random;
    
    private List<Integer> wallID;
    private List<Byte> wallData;
    
    public mazegenMaze(Location p1, Location p2) {
        int minX = Math.min(p1.getBlockX(), p2.getBlockX());
        int maxX = Math.max(p1.getBlockX(), p2.getBlockX());
        int minY = Math.min(p1.getBlockY(), p2.getBlockY());
        int maxY = Math.max(p1.getBlockY(), p2.getBlockY());
        int minZ = Math.min(p1.getBlockZ(), p2.getBlockZ());
        int maxZ = Math.max(p1.getBlockZ(), p2.getBlockZ());

        this.p1 = new Location(p1.getWorld(), minX, minY, minZ);
        this.p2 = new Location(p2.getWorld(), maxX, maxY, maxZ);
        
        this.active = false;
        this.cakemode = false;
        
        // randomizing everywhere!
        this.random = new Random();
        this.random.setSeed((new Date()).getTime());
    }
    public void setWallID(List<Integer> ID) { this.wallID = ID; }
    public void setWallData(List<Byte> data) { this.wallData = data; }
    public List<Integer> getWallID() { return this.wallID; }
    public List<Byte> getWallData() { return this.wallData; }
    
    public Location getMin() { return this.p1; }
    public Location getMax() { return this.p2; }
    
    public Boolean getCakemode() { return this.cakemode; }
    public Boolean getActive() { return this.active; }

    public void setCakemode(Boolean cakemode) { this.cakemode = cakemode; }
    public void setActive(Boolean active) { this.active = active; }
    
    public Boolean isInside(Player player) { return isInside(player, 0); }
    public Boolean isInside(Player player, int margin) {
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();

        if(
            (x >= this.p1.getBlockX() - margin && x <= this.p2.getBlockX() + margin) &&
            (y >= this.p1.getBlockY() - margin && y <= this.p2.getBlockY() + margin) &&
            (z >= this.p1.getBlockZ() - margin && z <= this.p2.getBlockZ() + margin)
        ) {
            return true;
        }
        return false;
    }
    
    public static Boolean checkLocations(Location p1, Location p2) {
        if(!p1.getWorld().equals(p2.getWorld())) return false;
        
        int minX = Math.min(p1.getBlockX(), p2.getBlockX());
        int maxX = Math.max(p1.getBlockX(), p2.getBlockX());
        int minY = Math.min(p1.getBlockY(), p2.getBlockY());
        int maxY = Math.max(p1.getBlockY(), p2.getBlockY());
        int minZ = Math.min(p1.getBlockZ(), p2.getBlockZ());
        int maxZ = Math.max(p1.getBlockZ(), p2.getBlockZ());

        int depth  = maxX - minX + 1;
        int height = maxY - minY + 1;
        int width  = maxZ - minZ + 1;

        if(width%2 != 0 && depth%2 != 0 && (height + 1) % 4 == 0 && width > 3 && depth > 3)
            return true;
        
        return false;
    }
    
    public void clear() {
        if(!this.active) return;
        
        int x, y, z;
        
        int minX = this.p1.getBlockX();
        int minY = this.p1.getBlockY();
        int minZ = this.p1.getBlockZ();
        int maxX = this.p2.getBlockX();
        int maxY = this.p2.getBlockY();
        int maxZ = this.p2.getBlockZ();
        
        int depth  = maxX - minX + 1;
        int height = maxY - minY + 1;
        int width  = maxZ - minZ + 1;
        
        World world = this.p1.getWorld();
        
        // first clear all ladders (or else they will be dropped)
        for(z = 0; z < width; z++) {
            for(y = 0; y < height; y++) {
                for(x = 0; x < depth; x++) {
                    Block block = world.getBlockAt(minX + x, minY + y, minZ + z);
                    if(block.getType().equals(Material.LADDER)) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        
        // now the walls
        for(z = 0; z < width; z++) {
            for(y = 0; y < height; y++) {
                for(x = 0; x < depth; x++) {
                    Block block = world.getBlockAt(minX + x, minY + y, minZ + z);
                    block.setType(Material.AIR);
                }
            }
        }
        
        this.unlock();
    }
    public void generate(Point start) {
        if(!this.active) return;
        
        int minX = this.p1.getBlockX();
        int minY = this.p1.getBlockY();
        int minZ = this.p1.getBlockZ();
        int maxX = this.p2.getBlockX();
        int maxY = this.p2.getBlockY();
        int maxZ = this.p2.getBlockZ();
        
        int depth  = maxX - minX + 1;
        int height = maxY - minY + 1;
        int width  = maxZ - minZ + 1;
        
        int x;
        int y;
        int step;
        int floor;

        int lx = 0;
        int ly = 0;
        Byte face = 0;

        for(floor = 0; floor < (height + 1) / 4; floor++) {
            HashMap mymaze = mazegenWallGenerator.generate(((width + 1) / 2), ((depth + 1) / 2), start, "25random");
            boolean[][] walls = (boolean[][]) mymaze.get("walls");

            // floor
            if(floor > 0) {
                for(y=0; y<walls.length; y++) {
                    for(x=0; x<walls[y].length; x++) {
                        if(y%2 > 0 && x%2 > 0) {
                            this.p1.getWorld().getBlockAt(minX + y, minY + floor*4 - 1, minZ + x).setType(Material.GLOWSTONE);
                        } else {
                            this.p1.getWorld().getBlockAt(minX + y, minY + floor*4 - 1, minZ + x).setType(Material.WOOL);
                            if((x+y)%2 == 0) {
                                this.p1.getWorld().getBlockAt(minX + y, minY + floor*4 - 1, minZ + x).setData(Byte.valueOf("0"));
                            } else {
                                this.p1.getWorld().getBlockAt(minX + y, minY + floor*4 - 1, minZ + x).setData(Byte.valueOf("15"));
                            }
                        }
                    }
                }

                // ladder

                int i;
                for(i=0; i<4; i++) {
                    if(i < 3) {
                        // if(wallIDs.get(i) == 20) continue;
                    }
                    this.p1.getWorld().getBlockAt(minX + ly, minY + (floor-1)*4 + i, minZ + lx).setType(Material.AIR);
                    this.p1.getWorld().getBlockAt(minX + ly, minY + (floor-1)*4 + i, minZ + lx).setType(Material.LADDER);
                    this.p1.getWorld().getBlockAt(minX + ly, minY + (floor-1)*4 + i, minZ + lx).setData(face);
                }
            }

            for(step=0; step<3; step++) {
                for(y=0; y<walls.length; y++) {
                    for(x=0; x<walls[y].length; x++) {
                        if(walls[y][x]) {
                            this.p1.getWorld().getBlockAt(minX + y, minY + step + floor*4, minZ + x).setTypeId(wallID.get(step));
                            this.p1.getWorld().getBlockAt(minX + y, minY + step + floor*4, minZ + x).setData(wallData.get(step));
                        } else {
                            this.p1.getWorld().getBlockAt(minX + y, minY + step + floor*4, minZ + x).setType(Material.AIR);
                        }
                    }
                }
            }

            if(floor<(height+1)/4 - 1) {
                // ladder preset
                Boolean found = false;
                while(!found) {
                    ly = this.random.nextInt(((depth + 1) / 2) - 2) + 1;
                    lx = this.random.nextInt(((width + 1) / 2) - 2) + 1;
                    ly *= 2;
                    lx *= 2;
                    if(walls[ly][lx+1] || walls[ly][lx-1] || walls[ly+1][lx] || walls[ly-1][lx]) {
                        found = true;
                    } else {
                        MazeGenerator.log.info(MazeGenerator.log_prefix + "No ladder possible, repeat.");
                    }
                }
                MazeGenerator.log.info(MazeGenerator.log_prefix + "The ladder is placed on x" + lx + ", y" + ly + ".");

                if(walls[ly+1][lx]) {
                    // north
                    face = Byte.valueOf("4");
                }
                if(walls[ly-1][lx]) {
                    // south
                    face = Byte.valueOf("5");
                }
                if(walls[ly][lx+1]) {
                    // east
                    face = Byte.valueOf("2");
                }
                if(walls[ly][lx-1]) {
                    // west
                    face = Byte.valueOf("3");
                }
            }
        }
        
        if(this.cakemode) {
            this.lock();
            this.cake();
        }
    }
    public void lock() {
        int x, y, z;
        
        int minX = this.p1.getBlockX();
        int minY = this.p1.getBlockY();
        int minZ = this.p1.getBlockZ();
        int maxX = this.p2.getBlockX();
        int maxY = this.p2.getBlockY();
        int maxZ = this.p2.getBlockZ();
        
        int depth  = maxX - minX + 1;
        int height = maxY - minY + 1;
        int width  = maxZ - minZ + 1;
        
        World world = this.p1.getWorld();

        for(z = -1; z < (width + 1); z++) {
            for(y = -1; y < (height + 1); y++) {
                for(x = -1; x < (depth + 1); x++) {
                    if(
                        (x < 0 || x >= depth) ||
                        (y < 0 || y >= height) ||
                        (z < 0 || z >= width)
                    ) {
                        if(world.getBlockAt(minX + x, minY + y, minZ + z).getType().equals(Material.AIR)) {
                            world.getBlockAt(minX + x, minY + y, minZ + z).setType(Material.FENCE);
                        }
                    }
                }
            }
        }
    }
    
    public void unlock() {
        int x, y, z;
        
        int minX = this.p1.getBlockX();
        int minY = this.p1.getBlockY();
        int minZ = this.p1.getBlockZ();
        int maxX = this.p2.getBlockX();
        int maxY = this.p2.getBlockY();
        int maxZ = this.p2.getBlockZ();
        
        int depth  = maxX - minX + 1;
        int height = maxY - minY + 1;
        int width  = maxZ - minZ + 1;
        
        World world = this.p1.getWorld();

        for(z = -1; z < (width + 1); z++) {
            for(y = -1; y < (height + 1); y++) {
                for(x = -1; x < (depth + 1); x++) {
                    if(
                        (x < 0 || x >= depth) ||
                        (y < 0 || y >= height) ||
                        (z < 0 || z >= width)
                    ) {
                        if(world.getBlockAt(minX + x, minY + y, minZ + z).getType().equals(Material.FENCE)) {
                            world.getBlockAt(minX + x, minY + y, minZ + z).setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
    
    private void cake() {
        int minX = this.p1.getBlockX();
        int minY = this.p1.getBlockY();
        int minZ = this.p1.getBlockZ();
        int maxX = this.p2.getBlockX();
        int maxY = this.p2.getBlockY();
        int maxZ = this.p2.getBlockZ();
        
        int depth  = maxX - minX + 1;
        int height = maxY - minY + 1;
        int width  = maxZ - minZ + 1;

        int y     = this.random.nextInt((depth  + 1) / 2);
        int x     = this.random.nextInt((width  + 1) / 2);
        int floor = this.random.nextInt((height + 1) / 4);

        MazeGenerator.log.info(MazeGenerator.log_prefix + "The cake is placed on x" + x + ", y" + y + " (floor " + (floor+1) + " of "+ ((height+1)/4) + ").");
        
        this.p1.getWorld().getBlockAt(minX + y*2, minY + floor*4, minZ + x*2).setType(Material.CAKE_BLOCK);
    }
}
