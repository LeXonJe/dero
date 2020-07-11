package net.lexonje.dero.game.listeners;

import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.game.GameSystem;
import net.lexonje.dero.game.Ranks;
import net.lexonje.dero.game.spectator.SpectatorManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private ConfigManager configManager;
    private GameSystem gameSystem;
    private SpectatorManager spectatorManager;

    public QuitListener(GameSystem gameSystem, ConfigManager configManager, SpectatorManager spectatorManager) {
        this.gameSystem = gameSystem;
        this.configManager = configManager;
        this.spectatorManager = spectatorManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (gameSystem.isInGame(player)) {
            if (spectatorManager.isSpectator(player)) {
                configManager.getPlayerConfig().setSpectatorPosition(player);
                spectatorManager.unsetSpectator(player);
            } else if (spectatorManager.isAdminSpectator(player)) {
                configManager.getPlayerConfig().setSpectatorPosition(player);
                spectatorManager.unsetAdminSpectator(player);
            } else {
                configManager.getPlayerConfig().saveLogout(player);
            }
            System.out.println(player.getName() + " was saved!");
        }
        if (player.hasPermission("dero.join")) {
            event.setQuitMessage("§e❖ " + Ranks.getRank(player).getTitle() + " §f§l" + event.getPlayer().getName() + " §f" +
                    "hat den Server verlassen.");
        } else {
            event.setQuitMessage(null);
        }
    }

}
