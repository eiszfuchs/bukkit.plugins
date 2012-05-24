package de.eiszfuchs.bukkit.generators;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator {

    byte[][] result;
    
    void setBlock(int x, int y, int z, byte block_id) {
        if(result[y >> 4] == null) {
            result[y >> 4] = new byte[16 * 16 * 16];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = block_id;
    }
    
    @Override
    public byte[][] generateBlockSections(World world, Random random, int cx, int cz, BiomeGrid biomes) {
        
        int height = world.getMaxHeight();
        result = new byte[height / 16][];
        
        if(cx != 0 || cz != 0) return result;
        
        for(int section = 0; section < height / 16; section++) {
            for(int s = 0; s < 16; s++) {
                for(int x = 0; x < 16; x++) {
                    for(int z = 0; z < 16; z++) {
                        int y = (section * 16 + s);
                        
                        if(x == 0 && z == 0 && y == world.getSeaLevel())
                            setBlock(x, y, z, (byte) Material.BEDROCK.getId());
                    }
                }
            }
        }
        
        return result;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, world.getSeaLevel() + 2, 0);
    }
    
}
