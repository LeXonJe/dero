package net.lexonje.dero.game.commands;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.game.GameSystem;
import net.lexonje.dero.game.spectator.SpectatorManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LastPositionCommand implements CommandExecutor {

    private ConfigManager configManager;
    private GameSystem gameSystem;
    private SpectatorManager spectatorManager;

    public LastPositionCommand(ConfigManager configManager, GameSystem gameSystem, SpectatorManager spectatorManager) {
        this.configManager = configManager;
        this.gameSystem = gameSystem;
        this.spectatorManager = spectatorManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("dero.cmd.lastposition")) {
                if (gameSystem.isInGame(player)) {
                    if (args.length == 1) {
                        if (Bukkit.getPlayer(args[0]) != null) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (!spectatorManager.isSpectator(Bukkit.getPlayer(args[0]))) {
                                player.teleport(target);
                                player.sendMessage(Plugin.prefix + " §7| §fDu wurdest zu §6§l" + args[0] + " §fteleportiert.");
                            }  else {
                                Location lastLoc = configManager.getPlayerConfig().getLastLocation(target);
                                if (lastLoc != null) {
                                    player.teleport(lastLoc);
                                    player.sendMessage(Plugin.prefix + " §7| §fDu wurdest erfolgreich zu der letzten Position von §6§l" + args[0] + " §fteleportiert.");
                                } else
                                    sender.sendMessage(Plugin.prefix + " §7| §cDie letzte Position von §l" + args[0]+ " §ckonnte nicht gefunden werden.");
                            }
                        } else {
                            OfflinePlayer target = null;
                            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                                if (offlinePlayer.getName().equals(args[0]))
                                    target = offlinePlayer;
                            }
                            if (target != null) {
                                Location lastLoc = configManager.getPlayerConfig().getLastLocation(target);
                                if (lastLoc != null) {
                                    player.teleport(lastLoc);
                                    player.sendMessage(Plugin.prefix + " §7| §fDu wurdest erfolgreich zu der letzten Position von §6§l" + args[0] + " §fteleportiert.");
                                } else
                                    sender.sendMessage(Plugin.prefix + " §7| §cDie letzte Position von §l" + args[0]+ " §ckonnte nicht gefunden werden.");
                            } else
                                sender.sendMessage(Plugin.prefix + " §7| §cDer Spieler §l" + args[0] + " §ckonnte nicht gefunden werden.");
                        }
                    } else
                        sender.sendMessage(Plugin.prefix + " §7| §6§lSyntax: §f/lastposition <Player>");
                } else
                    sender.sendMessage(Plugin.prefix + " §7| §cDu bist nicht im Spiel!");
            } else
                sender.sendMessage(Plugin.prefix + " §7| §cDu hast nicht die Berechtigung (§ldero.teleport§c) dazu!");
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDiesen Command kann nur ein Spieler ausführen!");
        return true;
    }
}
