package net.lexonje.dero.commands;

import net.lexonje.dero.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("\n§f§lMinecraft §4§lDERO §fv" + Plugin.version + "\n§7❖ §fEin Projekt von §6Devastation\n ");
        return true;
    }
}
