package net.lexonje.dero.game.listeners;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.*;
import net.lexonje.dero.game.Ranks;
import net.lexonje.dero.game.Transporter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private PlayerConfig playerConfig;
    private PluginConfig pluginConfig;
    private TeamConfig teamConfig;
    private Transporter transporter;

    public JoinListener(ConfigManager configManager, Transporter transporter) {
        this.playerConfig = configManager.getPlayerConfig();
        this.pluginConfig = configManager.getPluginConfig();
        this.teamConfig = configManager.getTeamConfig();
        this.transporter = transporter;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerConfig.controlPlayer(player);
        player.setPlayerListName(Ranks.getRank(player).getTitle() + "§7 | §f" + player.getName());
        player.setPlayerListHeaderFooter("\n§c♦ §f§lMinecraft §4§lDERO §c♦\n ",
                "\n§f   ➤ Ein Projekt von §6§lDevastation§f   \n ");
        if (pluginConfig.getString("dero.location.lobbyspawn.world") != null) {
            player.teleport(ConfigUtilities.getLocationFromConfig(pluginConfig,
                    "dero.location.lobbyspawn"));
        }
        boolean isNotVisitor = player.hasPermission("dero.join");
        if (isNotVisitor) {
            event.setJoinMessage("§e❖ " + Ranks.getRank(player).getTitle() + " §f§l" + event.getPlayer().getName() + " §f" +
                    "ist beigetreten.");
        } else {
            event.setJoinMessage(null);
        }
        player.setCollidable(false);
        transporter.initHubUI(player, !isNotVisitor);
        if (teamConfig.getTeamByOwner(player.getUniqueId()) != null) {
            TeamConfig.Team team = teamConfig.getTeamByOwner(player.getUniqueId());
            if (!team.getPendingList().isEmpty())
                player.sendMessage(Plugin.prefix + " §7| §fEs stehen noch Anfragen offen, deinem Team §3" + team.getName()
                    + " §fbeizutreten. Schaue in die Teameinstellungen für mehr Informationen!");
        }
    }
}
