package net.lexonje.dero.prevent;

import net.lexonje.dero.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

    private PreventionSystem system;

    public BuildCommand(PreventionSystem system) {
        this.system = system;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("net.lexonje.dero.build")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (system.containsBuilder((Player) sender)){
                        system.removeBuilder((Player) sender);
                        ((Player) sender).setGameMode(GameMode.SURVIVAL);
                        sender.sendMessage(Plugin.prefix + " §7| §fDu bist nun nicht mehr im §6Buildmodus§f!");
                    } else {
                        system.addBuilder((Player) sender);
                        ((Player) sender).setGameMode(GameMode.CREATIVE);
                        sender.sendMessage(Plugin.prefix + " §7| §fDu bist nun im §6Buildmodus§f!");
                    }
                } else
                    sender.sendMessage(Plugin.prefix + " §7| §6§lSyntax: §f/build <Name>");
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(Plugin.prefix + " §7| §cDer Spieler §l" + args[0] + " §ckonnte nicht " +
                            "gefunden werden.");
                    return true;
                }
                if (system.containsBuilder(target)){
                    system.removeBuilder(target);
                    target.setGameMode(GameMode.SURVIVAL);
                    sender.sendMessage(Plugin.prefix + " §7| §fDer Spieler §a" + args[0] + " §f ist nun nicht mehr im " +
                            "§6Buildmodus§f!");
                    target.sendMessage(Plugin.prefix + " §7| §fDu bist nun im §6Buildmodus§f!");
                } else {
                    system.addBuilder(target);
                    target.setGameMode(GameMode.CREATIVE);
                    sender.sendMessage(Plugin.prefix + " §7| §fDer Spieler §a" + args[0] + " §f wurde in den " +
                            "§6Buildmodus §fgesetz!");
                    target.sendMessage(Plugin.prefix + " §7| §fDu bist nun nicht mehr im §6Buildmodus§f!");
                }
            } else
                sender.sendMessage(Plugin.prefix + " §7| §6§lSyntax: §f/build <Name>");
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDu hast nicht die Berechtigung (§ldero.build§c) dazu!");
        return true;
    }
}
