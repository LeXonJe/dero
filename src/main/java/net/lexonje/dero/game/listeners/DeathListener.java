package net.lexonje.dero.game.listeners;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.ConfigUtilities;
import net.lexonje.dero.config.PlayerConfig;
import net.lexonje.dero.game.GameSystem;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.game.spectator.SpectatorManager;
import net.lexonje.dero.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private GameSystem gameSystem;
    private ConfigManager configManager;
    private SpectatorManager spectatorManager;
    private Plugin plugin;

    public DeathListener(GameSystem gameSystem, ConfigManager configManager, SpectatorManager spectatorManager, Plugin plugin) {
        this.gameSystem = gameSystem;
        this.configManager = configManager;
        this.spectatorManager = spectatorManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof  Player) {
            Player player = (Player) event.getEntity();
            if (spectatorManager.isSpectator(player) && player.getHealth() - event.getFinalDamage() < 0.5) {
                player.getInventory().setItem(0, new ItemBuilder(Material.AIR).build());
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (gameSystem.isInGame(player)) {
            if (spectatorManager.isSpectator(player)) {
                player.setHealth(20);
                return;
            }
            event.setDeathMessage("§4§l☠ §c" + event.getDeathMessage());
            PlayerConfig config = configManager.getPlayerConfig();
            Location deathLocation = player.getLocation();
            config.set(player.getUniqueId() + ".dead", true);
            ConfigUtilities.setCurrentDateToConfig(config, player.getUniqueId() + ".dead_time");
            ConfigUtilities.setLocationToConfig(deathLocation, config, player.getUniqueId() + ".data.location");
            config.setSpectatorPosition(player);
            config.save();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> { //Automatischer Respawn
                player.spigot().respawn();
                player.teleport(deathLocation);
                if (!spectatorManager.isSpectator(player))
                    spectatorManager.setSpectator(player, true);
                else
                    player.getInventory().setItem(0, GameItems.teamTeleporterItem);
                player.sendMessage(Plugin.prefix + " §7| §fDu bist nun aufgrund deines Todes ein Spectator. Mithilfe des Teleporters kannst du dich " +
                        "zu Teammitgliedern teleportieren.");
            }, 20);
        } else {
            event.setDeathMessage(null);
        }
    }
}
