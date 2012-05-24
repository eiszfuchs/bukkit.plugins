package de.eiszfuchs.bukkit;

import de.eiszfuchs.bukkit.generators.*;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class GeneratorPlugin extends JavaPlugin {

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        if(id.equalsIgnoreCase("void")) {
            return new VoidGenerator();
        } else if(id.equalsIgnoreCase("skygrid")) {
            return new SkyGridGenerator();
        }
        
        return super.getDefaultWorldGenerator(worldName, id);
    }
    
}
