package net.lexonje.dero.commands;

import net.lexonje.dero.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MessageCommand implements CommandExecutor {

    private HashMap<CommandSender, CommandSender> lastMsg;

    public MessageCommand() {
        this.lastMsg = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("dero.msg")) {
            if (label.equalsIgnoreCase("r")) {
                if (args.length > 0) {
                    if (lastMsg.containsKey(sender)) {
                        CommandSender target = lastMsg.get(sender);
                        String msg = "";
                        for (int i = 0; i < args.length; i++) {
                            msg += " " + args[i];
                        }
                        target.sendMessage("§a§lMsg §7| §e" + sender.getName() + " §7▶ §e" + target.getName() + " §7»§f" + msg);
                        sender.sendMessage("§a§lMsg §7| §e" + sender.getName() + " §7▶ §e" + target.getName() + " §7»§f" + msg);
                        lastMsg.put(sender, target);
                        lastMsg.put(target, sender);
                    } else
                        sender.sendMessage(Plugin.prefix + " §7| §cDu kannst auf keine Nachricht antworten, " +
                                "weil du mit niemanden geschrieben hast!");
                } else
                    sender.sendMessage(Plugin.prefix + " §7| §6§lSyntax: §f/r <Nachricht>");
            } else {
                if (args.length > 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (!target.equals(sender)) {
                            String msg = "";
                            for (int i = 1; i < args.length; i++) {
                                msg += " " + args[i];
                            }
                            target.sendMessage("§a§lMsg §7| §e" + sender.getName() + " §7▶ §e" + target.getName() + " §7»§f" + msg);
                            sender.sendMessage("§a§lMsg §7| §e" + sender.getName() + " §7▶ §e" + target.getName() + " §7»§f" + msg);
                            lastMsg.put(sender, target);
                            lastMsg.put(target, sender);
                        } else
                            sender.sendMessage("§a§lMsg §7| §cSelbstgespräche kannst du auch so führen! :D");
                    } else
                        sender.sendMessage(Plugin.prefix + " §7| §cDer Spieler §l" + args[0] + " §ckonnte nicht " +
                                "gefunden werden");
                } else
                    sender.sendMessage(Plugin.prefix + " §7| §6§lSyntax: §f/msg <Spieler> <Nachricht>");
            }
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDu hast nicht die Berechtigung (§ldero.msg§c) dazu!");
        return true;
    }
}
