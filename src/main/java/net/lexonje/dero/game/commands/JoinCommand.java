package net.lexonje.dero.game.commands;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.game.GameSystem;
import net.lexonje.dero.game.Transporter;
import net.lexonje.dero.game.spectator.SpectatorManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {

    private ConfigManager configManager;
    private Transporter transporter;
    private GameSystem gameSystem;
    private SpectatorManager spectatorManager;

    public JoinCommand(Transporter transporter, GameSystem gameSystem, ConfigManager configManager, SpectatorManager spectatorManager) {
        this.transporter = transporter;
        this.gameSystem = gameSystem;
        this.configManager = configManager;
        this.spectatorManager = spectatorManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("dero.join")) {
                if (!gameSystem.isInGame(player)) {
                    if (configManager.getPlayerConfig().getBoolean(player.getUniqueId() + ".data.newuser")) {
                        transporter.createNewPlayer(player);
                    } else {
                        transporter.transportPlayerToGame(player);
                    }
                } else
                    player.sendMessage(Plugin.prefix + " §7| §cDu bist bereits auf Dero!");
            } else
                player.sendMessage(Plugin.prefix + " §7| §cDu hast nicht die Berechtigung (§ldero.join§c) dazu!");
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDiesen Kommand kann nur ein Spieler ausführen!");
        return true;
    }
}
