package net.lexonje.dero.game.invuis.profile.team;

import com.google.common.collect.Lists;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.TeamConfig;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.items.ActionSystem;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.items.ItemBuilder;
import net.lexonje.dero.items.SignGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.UUID;

public class TeamsUI extends InventoryUI {

    private InventoryUI profileUI;
    private SettingsTeamUI settingsTeamUi;
    private SignGUI signGUI;
    private Plugin plugin;

    private ActionSystem actionSystem;
    private ConfigManager configManager;
    private ListTeamUI listTeamUI;

    public TeamsUI(InventoryUI profileUI, ActionSystem actionSystem, ConfigManager configManager, Plugin plugin) {
        super("§6§lTeamverwaltung", 9*3);
        this.profileUI = profileUI;
        this.signGUI = actionSystem.getSignGUI();
        this.plugin = plugin;
        this.actionSystem = actionSystem;
        this.configManager = configManager;
        this.listTeamUI = new ListTeamUI(this, configManager);
        this.settingsTeamUi = new SettingsTeamUI(this, actionSystem, configManager);
        actionSystem.registerInventoryAction(listTeamUI);
        actionSystem.registerInventoryAction(settingsTeamUi);
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, getRows(), getName());
        TeamConfig config = configManager.getTeamConfig();
        if (config.getTeamByOwner(player.getUniqueId()) != null) {
            TeamConfig.Team team = config.getTeamByOwner(player.getUniqueId());
            ArrayList<String> labels = new ArrayList<>();
            labels.add("§6" + team.getName() + " §7{§e" + team.getPrefix() + "§7}");
            labels.add("§ferstellt von §3" + Bukkit.getOfflinePlayer(team.getOwnerId()).getName());
            labels.add("");
            labels.add("§dMitglieder:");
            for (UUID id : team.getMembers())
                labels.add(" §7- §f" + Bukkit.getOfflinePlayer(id).getName());
            ItemStack infoItem = new ItemBuilder(Material.PAPER)
                    .setName("§a§lDein Team")
                    .setLore(labels.toArray(new String[labels.size()]))
                    .build();
            inv.setItem(10, infoItem);
            inv.setItem(12, GameItems.listTeamItem);
            inv.setItem(14, GameItems.settingsTeamItem);
            inv.setItem(16, GameItems.deleteTeamItem);
        } else if (config.getTeamByPendingMember(player.getUniqueId()) != null) {
            TeamConfig.Team team = config.getTeamByPendingMember(player.getUniqueId());
            ArrayList<String> labels = new ArrayList<>();
            labels.add("§6" + team.getName() + " §7{§e" + team.getPrefix() + "§7}");
            labels.add("§ferstellt von §3" + Bukkit.getOfflinePlayer(team.getOwnerId()).getName());
            labels.add("");
            labels.add("§dMitglieder:");
            for (UUID id : team.getMembers())
                labels.add(" §7- §f" + Bukkit.getOfflinePlayer(id).getName());
            ItemStack pendingItem = new ItemBuilder(Material.WRITABLE_BOOK)
                    .setName("§e§lDeine Anfrage steht noch aus.")
                    .setLore(labels.toArray(new String[labels.size()]))
                    .build();
            inv.setItem(12, pendingItem);
            inv.setItem(14, GameItems.leavePendingItem);
        } else if (config.getTeamByMember(player.getUniqueId()) == null) {
            inv.setItem(12, GameItems.joinTeamItem);
            inv.setItem(14, GameItems.createTeamItem);
        } else {
            TeamConfig.Team team = config.getTeamByMember(player.getUniqueId());
            ArrayList<String> labels = new ArrayList<>();
            labels.add("§6" + team.getName() + " §7{§e" + team.getPrefix() + "§7}");
            labels.add("§ferstellt von §3" + Bukkit.getOfflinePlayer(team.getOwnerId()).getName());
            labels.add("");
            labels.add("§dMitglieder:");
            for (UUID id : team.getMembers())
                labels.add(" §7- §f" + Bukkit.getOfflinePlayer(id).getName());
            ItemStack infoItem = new ItemBuilder(Material.PAPER)
                    .setName("§a§lDein Team")
                    .setLore(labels.toArray(new String[labels.size()]))
                    .build();
            inv.setItem(11, infoItem);
            inv.setItem(13, GameItems.listTeamItem);
            inv.setItem(15, GameItems.quitTeamItem);
        }
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
                    player.openInventory(profileUI.getInv(player));
                    break;
                }
                case DIAMOND: {
                    signGUI.newMenu(player, Lists.newArrayList("",
                            "§e§l↑ §6Teamname §e§l↑", "-*-", "§3§lTeamerstellung"))
                            .reopenIfFail()
                            .response((user, stringsName) -> {
                                if (!stringsName[0].isEmpty()) {
                                    signGUI.newMenu(player, Lists.newArrayList("",
                                            "§e§l↑ §6Teamprefix §e§l↑", "-*-", "§3§lTeamerstellung"))
                                            .reopenIfFail()
                                            .response((user1, stringsPrefix) -> {
                                                if (!stringsPrefix[0].isEmpty()) {
                                                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (configManager.getTeamConfig().getTeamByName(stringsName[0]) == null) {
                                                                ConfirmTeamUI confirmTeamUI = new ConfirmTeamUI(stringsName[0],
                                                                        stringsPrefix[0], player, actionSystem, configManager);
                                                                actionSystem.registerInventoryAction(confirmTeamUI);
                                                                player.openInventory(confirmTeamUI.getInv(player));
                                                            } else {
                                                                player.sendMessage(net.lexonje.dero.Plugin.prefix +
                                                                        " §7| §cDieses Team existiert bereits! Du kannst dem Team " +
                                                                        "aber eine Anfrage zum Beitreten schicken.");
                                                            }
                                                        }
                                                    });
                                                    return true;
                                                }
                                                return false;
                                            })
                                            .open();
                                    return true;
                                }
                                return false;
                            })
                            .open();
                    break;
                }
                case KNOWLEDGE_BOOK:
                case ENDER_EYE: {
                    player.openInventory(listTeamUI.getInventory(player, 0));
                    break;
                }
                case FIRE_CORAL: {
                    if (configManager.getTeamConfig().getTeamByOwner(player.getUniqueId()) != null) {
                        TeamConfig.Team team = configManager.getTeamConfig().getTeamByOwner(player.getUniqueId());
                        configManager.getTeamConfig().removeTeam(team.getName());
                        player.closeInventory();
                        player.sendMessage(net.lexonje.dero.Plugin.prefix + " §7| §fDas Team §3" + team.getName() + " §fwurde " +
                                "erfolgreich §cgelöscht§f.");
                    } else if (configManager.getTeamConfig().getTeamByMember(player.getUniqueId()) != null){
                        TeamConfig.Team team = configManager.getTeamConfig().getTeamByMember(player.getUniqueId());
                        team.removeMember(player.getUniqueId());
                        player.closeInventory();
                        player.sendMessage(net.lexonje.dero.Plugin.prefix + " §7| §fDu hast das Team §3" + team.getName() +
                                " §ferfolgreich §cverlassen§f.");
                    }
                    break;
                }
                case CLOCK: {
                    player.openInventory(settingsTeamUi.getInv(player));
                    break;
                }
                case RED_DYE: {
                    if (configManager.getTeamConfig().getTeamByPendingMember(player.getUniqueId()) != null) {
                        TeamConfig.Team team = configManager.getTeamConfig().getTeamByPendingMember(player.getUniqueId());
                        team.removePending(player.getUniqueId());
                        player.closeInventory();
                        player.sendMessage(net.lexonje.dero.Plugin.prefix + " §7| §fDeine Anfrage wurde erfolgreich §cgelöscht§f.");
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
