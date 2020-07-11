package net.lexonje.dero.game.actions;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.game.GameSystem;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.game.Transporter;
import net.lexonje.dero.game.spectator.SpectatorManager;
import net.lexonje.dero.items.ItemAction;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class JoinAction extends ItemAction {

    private Transporter transporter;
    private GameSystem gameSystem;
    private ConfigManager configManager;
    private SpectatorManager spectatorManager;

    public JoinAction(Transporter transporter, GameSystem gameSystem, ConfigManager manager, SpectatorManager spectatorManager) {
        super(GameItems.joinItem);
        this.transporter = transporter;
        this.gameSystem = gameSystem;
        this.configManager = manager;
        this.spectatorManager = spectatorManager;
    }

    @Override
    public void action(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (player.hasPermission("dero.join")) {
                if (!gameSystem.isInGame(player)) {
                    if (configManager.getPlayerConfig().getBoolean(player.getUniqueId() + ".data.newuser")) {
                        transporter.createNewPlayer(player);
                    } else {
                        transporter.transportPlayerToGame(player);
                    }
                } else
                    player.sendMessage(Plugin.prefix + " §7| §cDu bist bereits auf Dero!");
            }
        }
    }
}
