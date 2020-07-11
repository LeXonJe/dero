package net.lexonje.dero.game.invuis.profile.team;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.TeamConfig;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.items.ActionSystem;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MemberOptionUI extends InventoryUI {

    private OfflinePlayer offlinePlayer;
    private ListTeamMembersUI listTeamMembersUI;

    private ActionSystem actionSystem;
    private ConfigManager configManager;

    public MemberOptionUI(OfflinePlayer player, ListTeamMembersUI listTeamMembersUI, ActionSystem actionSystem, ConfigManager configManager) {
        super("§6§lMitglied §7» §r" + player.getName(), 9*3);
        this.offlinePlayer = player;
        this.listTeamMembersUI = listTeamMembersUI;
        this.actionSystem = actionSystem;
        this.configManager = configManager;
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, getRows(), getName());
        ItemStack deleteMemberItem = new ItemBuilder(Material.POPPY)
                .setName("§c§lVom Team entfernen")
                .setLore("§fEntfernt §3" + offlinePlayer.getName() + " §fvom Team.")
                .build();
        if (configManager.getTeamConfig().getTeamByOwner(offlinePlayer.getUniqueId()) == null)
            inv.setItem(13, deleteMemberItem);
        inv.setItem(18, GameItems.backItem);
        return inv;
    }

    @Override
    public void action(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() != null) {
            Player player = (Player) event.getWhoClicked();
            switch (event.getCurrentItem().getType()) {
                case EMERALD: {
                    player.openInventory(listTeamMembersUI.getInv(player, 0));
                    actionSystem.removeInventoryAction(this);
                    break;
                }
                case POPPY: {
                    TeamConfig.Team team = configManager.getTeamConfig().getTeamByOwner(player.getUniqueId());
                    team.removeMember(offlinePlayer.getUniqueId());
                    player.openInventory(listTeamMembersUI.getInv(player, 0));
                    player.sendMessage(Plugin.prefix + " §7| §fDer Spieler §3" + offlinePlayer.getName() + " §fwurde vom Team §centfernt§f.");
                    if (Bukkit.getPlayer(offlinePlayer.getUniqueId()) != null)
                        Bukkit.getPlayer(offlinePlayer.getUniqueId()).sendMessage(Plugin.prefix + " §7| §fDu wurdest vom Team §3" + team.getName() +
                                " §centfernt§f.");
                    actionSystem.removeInventoryAction(this);
                    break;
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {
        actionSystem.removeInventoryAction(this);
    }
}
