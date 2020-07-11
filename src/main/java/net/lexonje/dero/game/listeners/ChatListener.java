package net.lexonje.dero.game.listeners;

import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.TeamConfig;
import net.lexonje.dero.game.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private ConfigManager configManager;

    public ChatListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        String msg = "";
        String[] msgSplit = event.getMessage().split(" ");
        for (String split : msgSplit) {
            if (split.startsWith("@"))
                msg += " §3" + split + "§f";
            else
                msg += " " + split;
        }
        TeamConfig config = configManager.getTeamConfig();
        if (config.getTeamByMember(player.getUniqueId()) == null) {
            Bukkit.broadcastMessage(Ranks.getRank(player).getTitle() + " §f" + player.getName() + " §7»§f" + msg);
        } else {
            Bukkit.broadcastMessage(Ranks.getRank(player).getTitle() + " §f" + player.getName() + " §7{§e" +
                    config.getTeamByMember(player.getUniqueId()).getPrefix() + "§7} »§f" + msg);
        }
    }
}
