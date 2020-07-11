package net.lexonje.dero.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("\n  §8-§7*§8-               §c♦ §f§lMinecraft §4§lDERO §c♦               §8-§7*§8-\n" +
                "                           §7{§eAlle Befehle§7}\n \n" +
                " §6§l/help §7- §fGibt eine Übersicht mit allen Commands.\n" +
                " §6§l/join §7- §fTeleportiert dich zu §lMinecraft §4§lDERO§f.\n" +
                " §6§l/hub §7- §fTeleportiert dich zur Lobby.\n" +
                " §6§l/msg §7<§eSpieler§7> §7<§eNachricht§7> - §fSendet eine private Nachricht an einen Spieler.\n" +
                " §6§l/clock §7- §fGibt die verbleibende Zeit an, bevor der Server herunterfährt.");
        if (sender.hasPermission("net.lexonje.dero.team.commands")) {
            sender.sendMessage(" §6§l/setup §7<§elobbyspawn | ...§7> §7- §fKonfiguriert das Spiel.\n" +
                    " §6§l/alert §7<§eNachricht§7> §7- §fBroadcastet eine Nachricht.\n" +
                    " §6§l/shutdown <Stunde> <Minute> | <abort> §7- §fKonfiguriert das automatische Herunterfahren.");
        }
        sender.sendMessage("");
        return true;
    }
}
