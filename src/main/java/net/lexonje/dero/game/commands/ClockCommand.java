package net.lexonje.dero.game.commands;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.game.shutdown.ShutdownManager;
import net.lexonje.dero.game.shutdown.ShutdownScheduler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClockCommand implements CommandExecutor {

    private ShutdownManager shutdownManager;

    public ClockCommand(ShutdownManager shutdownManager) {
        this.shutdownManager = shutdownManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (shutdownManager.isRunning()) {
            ShutdownScheduler shutdownScheduler = shutdownManager.getShutdownScheduler();
            int[] leftTime = shutdownScheduler.getLeftTime();
            sender.sendMessage(Plugin.prefix + " §7| §f" + leftTime[0] + " §7: §f" +
                    leftTime[1] + " §7: §f" + leftTime[2] + " verbleibend.");
        } else {
            sender.sendMessage(Plugin.prefix + " §7| §cDas automatische Herunterfahren wurde ausgeschaltet!");
        }
        return true;
    }
}
