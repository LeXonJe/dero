package net.lexonje.dero.game.commands;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.PlayerConfig;
import net.lexonje.dero.game.GameSystem;
import net.lexonje.dero.game.Transporter;
import net.lexonje.dero.game.spectator.SpectatorManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectatorCommand implements CommandExecutor {

    private SpectatorManager spectatorManager;
    private ConfigManager configManager;
    private Transporter transporter;
    private GameSystem system;

    public SpectatorCommand(SpectatorManager spectatorManager, ConfigManager configManager, Transporter transporter, GameSystem system) {
        this.spectatorManager = spectatorManager;
        this.configManager = configManager;
        this.transporter = transporter;
        this.system = system;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("dero.spectate.admin")) {
                PlayerConfig config = configManager.getPlayerConfig();
                if (!system.isInGame(player)) {
                    if (config.getSpectatorLocation(player) != null) {
                        player.teleport(config.getSpectatorLocation(player));
                    } else if (config.getLastLocation(player) != null) { //RARE, if player didnt die once
                        player.teleport(config.getLastLocation(player));
                    } else { //Very rare. Initialize player and then setting to spectator
                        transporter.createNewPlayer(player);
                        onCommand(sender, cmd, label, args);
                        return true;
                    }
                    system.addPlayerToGame(player);
                } else if (spectatorManager.isSpectator(player)) {
                    spectatorManager.unsetSpectator(player);
                } else { //Player is playing
                    config.saveLogout(player);
                }
                if (!spectatorManager.isAdminSpectator(player)) {
                    spectatorManager.setAdminSpectator(player, true);
                    player.sendMessage(Plugin.prefix + " §7| §fDu bist nun ein Spectator.");
                } else {
                    spectatorManager.unsetAdminSpectator(player);
                    transporter.transportPlayerToHub(player);
                    player.sendMessage(Plugin.prefix + " §7| §fDu bist nun kein Spectator mehr.");
                }
            } else
                sender.sendMessage(Plugin.prefix + " §7| §cDu hast nicht die Berechtigung (§ldero.spectate.admin§c) dazu!");
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDiesen Command kann nur ein Spieler ausführen!");
        return true;
    }
}
