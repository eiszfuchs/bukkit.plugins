package de.eiszfuchs.bukkit.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class SkyGridGenerator extends ChunkGenerator {

    byte[][] result;
    
    private List<Material> mass = new ArrayList<Material>();
    private List<Material> mass_above_grass = new ArrayList<Material>();
    
    public SkyGridGenerator() {
        // base materials
        addToMass(Material.SAND, 3);
        addToMass(Material.DIRT, 3);
        addToMass(Material.GRAVEL, 4);
        addToMass(Material.GRASS, 15);
        addToMass(Material.SANDSTONE, 2);
        addToMass(Material.STONE, 5);
        addToMass(Material.MYCEL, 2); // well ...
        addToMass(Material.OBSIDIAN, 1);
        addToMass(Material.STATIONARY_WATER, 1);
        addToMass(Material.SNOW_BLOCK, 1);
        
        // plants
        addToMass(Material.LEAVES, 4);
        addToMass(Material.LOG, 8);
        addToMass(Material.PUMPKIN, 2);
        addToMass(Material.MELON_BLOCK, 2);
        
        // ores
        addToMass(Material.CLAY, 2);
        addToMass(Material.COAL_ORE, 5);
        addToMass(Material.IRON_ORE, 4);
        addToMass(Material.GOLD_ORE, 3);
        addToMass(Material.LAPIS_ORE, 2);
        addToMass(Material.REDSTONE_ORE, 2);
        addToMass(Material.DIAMOND_ORE, 1);
        
        // hazards
        addToMass(Material.CACTUS, 2);
        addToMass(Material.STATIONARY_LAVA, 1);
        addToMass(Material.ICE, 3); // it's slippy
        
        
        // on grass
        addToGrass(Material.AIR, 2);
        addToGrass(Material.LONG_GRASS, 15);
        addToGrass(Material.SNOW, 2);
        addToGrass(Material.BROWN_MUSHROOM, 5);
        addToGrass(Material.RED_MUSHROOM, 3);
        addToGrass(Material.RED_ROSE, 1);
        addToGrass(Material.YELLOW_FLOWER, 1);
        addToGrass(Material.SUGAR_CANE_BLOCK, 2);
    }
    
    void setBlock(int x, int y, int z, byte block_id) {
        if(result[y >> 4] == null) {
            result[y >> 4] = new byte[16 * 16 * 16];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = block_id;
    }
    
    private void addToMass(Material material) { addToMass(material, 1); }
    private void addToMass(Material material, int amount) {
        for(int i = 0; i < amount; i++)
            this.mass.add(material);
    }
    
    private void addToGrass(Material material) { addToGrass(material, 1); }
    private void addToGrass(Material material, int amount) {
        for(int i = 0; i < amount; i++)
            this.mass_above_grass.add(material);
    }
    
    private Material getFromMass(Random random) {
        return this.mass.get(random.nextInt(this.mass.size()));
    }
    private Material getFromGrass(Random random) {
        return this.mass_above_grass.get(random.nextInt(this.mass_above_grass.size()));
    }
    
    @Override
    public byte[][] generateBlockSections(World world, Random random, int cx, int cz, BiomeGrid biomes) {
        
        int height = world.getMaxHeight();
        result = new byte[height / 16][];
        
        for(int section = 0; section < height / 16; section++) {
            for(int s = 0; s < 16; s += 4) {
                for(int x = 0; x < 16; x += 4) {
                    for(int z = 0; z < 16; z += 4) {
                        int y = (section * 16 + s);
                        
                        Material material = this.getFromMass(random);
                        setBlock(x, y, z, (byte) material.getId());
                        
                        if(material.equals(Material.GRASS))
                            setBlock(x, y + 1, z, (byte) getFromGrass(random).getId());
                    }
                }
            }
        }
        
        return result;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        int x = (random.nextInt(128) - 64) * 4;
        int y = (world.getSeaLevel() + 2) + (random.nextInt(4) * 4);
        int z = (random.nextInt(128) - 64) * 4;
        
        return new Location(world, x, y, z);
    }
    
}
