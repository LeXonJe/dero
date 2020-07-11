package net.lexonje.dero.game.shutdown;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import org.bukkit.Bukkit;

public class ShutdownManager {

    private ConfigManager configManager;
    private ShutdownScheduler shutdownScheduler;
    private int scheduleId;
    private Plugin plugin;
    private boolean isRunning;

    public ShutdownManager (ConfigManager configManager, Plugin plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
        if (configManager.getPluginConfig().get("dero.close_time.hours") != null) {
            this.shutdownScheduler = new ShutdownScheduler(configManager.getPluginConfig().getInt("dero.close_time.hours"),
                    configManager.getPluginConfig().getInt("dero.close_time.minutes"), configManager.getPluginConfig().getInt("dero.close_time.seconds"));
            scheduleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, shutdownScheduler, 0, 20);
            isRunning = true;
        } else
            isRunning = false;
    }

    public void setShutdownTime(int hours, int minutes, int seconds) {
        configManager.getPluginConfig().set("dero.close_time.hours", hours);
        configManager.getPluginConfig().set("dero.close_time.minutes", minutes);
        configManager.getPluginConfig().set("dero.close_time.seconds", seconds);
        configManager.getPluginConfig().save();
        if (isRunning) {
            shutdownScheduler.setShutdownTime(hours, minutes, seconds);
        } else {
            this.shutdownScheduler = new ShutdownScheduler(hours, minutes, seconds);
            scheduleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, shutdownScheduler, 0, 20);
            isRunning = true;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void abort() {
        if (shutdownScheduler != null) {
            Bukkit.getScheduler().cancelTask(scheduleId);
            configManager.getPluginConfig().remove("dero.close_time");
            configManager.getPluginConfig().save();
            isRunning = false;
            Bukkit.broadcastMessage(Plugin.prefix + " §7| §cDas automatische Herunterfahren wurde angehalten.");
        }
    }

    public ShutdownScheduler getShutdownScheduler() {
        return shutdownScheduler;
    }
}
