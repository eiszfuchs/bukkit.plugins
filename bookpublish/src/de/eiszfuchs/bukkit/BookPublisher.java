package de.eiszfuchs.bukkit;

import de.eiszfuchs.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author eiszfuchs
 */
public class BookPublisher extends JavaPlugin {
    
    @Override
    public void onEnable() {
        if (!this.getDataFolder().exists()) {
            this.getConfig().set("publish_path", this.getDataFolder().getAbsolutePath());
            this.saveConfig();
        }
        this.getCommand("publish").setExecutor(new BookPublisherCommand(this));
    }
    
    @Override
    public void onDisable() {
    }
}
