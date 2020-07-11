package net.lexonje.dero.game.listeners;

import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.TeamConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TeamPVPListener implements Listener {

    private ConfigManager configManager;

    public TeamPVPListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player hit = (Player) event.getEntity();
            Player damage = (Player) event.getDamager();
            TeamConfig config = configManager.getTeamConfig();
            TeamConfig.Team hitTeam = config.getTeamByMember(hit.getUniqueId());
            TeamConfig.Team damageTeam = config.getTeamByMember(damage.getUniqueId());
            if (!(hitTeam == null || damageTeam == null))
                if (hitTeam.equals(damageTeam)) {
                    if (!hitTeam.isFriendlyFire())
                        event.setCancelled(true);
                }
        }
    }
}
