package net.lexonje.dero.game.commands;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.ConfigUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {

    private ConfigManager configManager;

    public SetupCommand(ConfigManager manager) {
        this.configManager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("dero.setup")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length > 0) {
                    switch (args[0].toLowerCase()) {
                        case "lobbyspawn": {
                            ConfigUtilities.setLocationToConfig(player.getLocation(), configManager.getPluginConfig(),
                                    "dero.location.lobbyspawn");
                            player.sendMessage(Plugin.prefix + " §7| §fDer §aLobbyspawn §fwurde §2erfolgreich §f" +
                                    "gesetzt!");
                            break;
                        }
                        case "worldspawn": {
                            ConfigUtilities.setLocationToConfig(player.getLocation(), configManager.getPluginConfig(),
                                    "dero.location.worldspawn");
                            player.sendMessage(Plugin.prefix + " §7| §fDer §aWorldspawn §fwurde §2erfolgreich §f" +
                                    "gesetzt!");
                            break;
                        }
                    }
                } else
                    sender.sendMessage(Plugin.prefix + " §7| §6§lSyntax: §f/setup <lobbyspawn|worldspawn>");
            } else
                sender.sendMessage(Plugin.prefix + " §7| §cDiesen Kommand kann nur ein Spieler ausführen!");
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDu hast nicht die Berechtigung (§ldero.setup§c) dazu!");
        return true;
    }
}
