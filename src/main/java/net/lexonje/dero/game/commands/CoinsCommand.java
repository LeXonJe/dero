package net.lexonje.dero.game.commands;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CoinsCommand implements CommandExecutor {

    private ConfigManager configManager;

    public CoinsCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("dero.cmd.coins")) {
            if (args.length == 3) {

            } else
                sender.sendMessage(Plugin.prefix + " §7| §6§lSyntax: §f/coins <Spieler> <add|remove|set> <Coins>");
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDu hast nicht die Berechtigung (§ldero.coins.change§c) dazu!");
        return true;
    }
}
