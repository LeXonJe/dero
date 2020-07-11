package net.lexonje.dero.game.invuis.profile.team;

import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.TeamConfig;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.items.ActionSystem;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SettingsTeamUI extends InventoryUI {

    private TeamsUI teamsUI;
    private ListPendingUI listPendingUI;
    private ListTeamMembersUI listTeamMembersUI;
    private ConfigManager configManager;

    public SettingsTeamUI(TeamsUI teamsUI, ActionSystem actionSystem, ConfigManager configManager) {
        super("§6§lTeameinstellungen", 9*3);
        this.teamsUI = teamsUI;
        this.configManager = configManager;
        this.listPendingUI = new ListPendingUI(this, actionSystem, configManager);
        this.listTeamMembersUI = new ListTeamMembersUI(this, actionSystem, configManager);
        actionSystem.registerInventoryAction(listPendingUI);
        actionSystem.registerInventoryAction(listTeamMembersUI);
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, getRows(), getName());
        TeamConfig.Team team = configManager.getTeamConfig().getTeamByMember(player.getUniqueId());
        ItemStack pendingList = new ItemBuilder(Material.ENCHANTED_BOOK)
                .setName("§c§lAnfragen §7{§e" + team.getPendingList().size() + "§7}")
                .setLore("§fListet alle Anfrangen, deinem Team beizutreten, auf.")
                .build();
        ItemStack membersList = new ItemBuilder(Material.BOOKSHELF)
                .setName("§3§lMitglieder §7{§e" + team.getMembers().size() + "§7}")
                .setLore("§fVerwalte alle Mitglieder des Teams.")
                .build();
        ItemStack friendlyFire;
        if (team.isFriendlyFire()) {
            friendlyFire = new ItemBuilder(Material.NETHER_STAR)
                    .setName("§6§lFriendly Fire §7{§aAn§7}")
                    .setLore("§fElaubt PVP zwischen Spielern in deinem Team.")
                    .build();
        } else {
            friendlyFire = new ItemBuilder(Material.FIREWORK_STAR)
                    .setName("§6§lFriendly Fire §7{§cAus§7}")
                    .setLore("§fElaubt PVP zwischen Spielern in deinem Team.")
                    .build();
        }
        inv.setItem(11, pendingList);
        inv.setItem(13, membersList);
        inv.setItem(15, friendlyFire);
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
                    player.openInventory(teamsUI.getInv(player));
                    break;
                }
                case ENCHANTED_BOOK: {
                    player.openInventory(listPendingUI.getInv(player, 0));
                    break;
                }
                case BOOKSHELF: {
                    player.openInventory(listTeamMembersUI.getInv(player, 0));
                    break;
                }
                case FIREWORK_STAR: {
                    configManager.getTeamConfig().getTeamByMember(player.getUniqueId()).setFriendlyFire(true);
                    player.openInventory(getInv(player));
                    break;
                }
                case NETHER_STAR: {
                    configManager.getTeamConfig().getTeamByMember(player.getUniqueId()).setFriendlyFire(false);
                    player.openInventory(getInv(player));
                    break;
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
