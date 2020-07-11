package net.lexonje.dero.commands;

import net.lexonje.dero.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AlertCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("net.lexonje.dero.alert")) {
            if (args.length > 0) {
                String s = "";
                for (String string : args) {
                    s += " " + string;
                }
                Bukkit.broadcastMessage("\n" + Plugin.prefix + " §7| §c§lAlert §7»§f" + s);
                Bukkit.broadcastMessage("");
            } else
                sender.sendMessage(Plugin.prefix + " §7| §6§lSyntax: §f/alert <Nachricht>");
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDu hast nicht die Berechtigung (§ldero.alert§c) dazu!");
        return true;
    }
}
