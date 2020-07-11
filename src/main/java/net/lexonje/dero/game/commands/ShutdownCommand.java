package net.lexonje.dero.game.commands;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.game.shutdown.ShutdownManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShutdownCommand implements CommandExecutor {

    private ShutdownManager shutdownManager;

    public ShutdownCommand(ShutdownManager shutdownManager) {
        this.shutdownManager = shutdownManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("net.lexonje.dero.shutdown")) {
            if (args.length == 3) {
                int hour;
                int minute;
                int seconds;
                try {
                    hour = Integer.parseInt(args[0]);
                    minute = Integer.parseInt(args[1]);
                    seconds = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Plugin.prefix + " §7| §cBitte gebe eine gültige Zahl ein!");
                    return true;
                }
                if (hour < 24) {
                    if (minute < 60) {
                        if (seconds < 60) {
                            shutdownManager.setShutdownTime(hour, minute, seconds);
                        } else
                            sender.sendMessage(Plugin.prefix + " §7| §cEine Minute hat nur 60 Sekunden!");
                    } else
                        sender.sendMessage(Plugin.prefix + " §7| §cEine Stunde hat nur 60 Minuten!");
                } else
                    sender.sendMessage(Plugin.prefix + " §7| §cEin Tag hat nur 24 Stunden!");
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("abort")) {
                    if (shutdownManager.isRunning()) {
                        shutdownManager.abort();
                    } else
                        sender.sendMessage(Plugin.prefix + " §7| §cDas automatische Herunterfahren ist bereits angehalten!");
                } else
                    sender.sendMessage(Plugin.prefix + " §7| §6§lSyntax: §f/shutdown <Stunde> <Minute> <Sekunde> §7oder §f/shutdown abort");
            } else
                sender.sendMessage(Plugin.prefix + " §7| §6§lSyntax: §f/shutdown <Stunde> <Minute> <Sekunde> §7oder §f/shutdown abort");
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDu hast nicht die Berechtigung (§ldero.join§c) dazu!");
        return true;
    }
}
