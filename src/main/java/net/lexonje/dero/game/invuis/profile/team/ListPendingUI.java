package net.lexonje.dero.game.invuis.profile.team;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.TeamConfig;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.items.ActionSystem;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.items.ItemBuilder;
import net.lexonje.dero.items.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class ListPendingUI extends InventoryUI {

    private SettingsTeamUI settingsTeamUi;
    private ConfigManager configManager;
    private ActionSystem actionSystem;
    private HashMap<Player, Integer> pages;
    private HashMap<Player, HashMap<String, UUID>> pendingList;

    public ListPendingUI(SettingsTeamUI settingsTeamUi, ActionSystem actionSystem, ConfigManager configManager) {
        super("§6§lAlle Anfragen", 9*3);
        this.pages = new HashMap<>();
        this.settingsTeamUi = settingsTeamUi;
        this.configManager = configManager;
        this.actionSystem = actionSystem;
        this.pendingList = new HashMap<>();
    }

    @Override
    public Inventory getInv(Player player) {
        return getInv(player, 0);
    }

    public Inventory getInv(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, getRows(), getName());
        TeamConfig.Team team = configManager.getTeamConfig().getTeamByOwner(player.getUniqueId());
        inv.setItem(18, GameItems.backItem);
        pages.put(player, page);
        HashMap<String, UUID> ids = new HashMap<>();
        if (team.getPendingList().size() < 1) {
            inv.setItem(13, GameItems.noPendingItem);
        } else if ((team.getPendingList().size() / 18) < 1) {
            for (int i = 0; i < team.getPendingList().size(); i++) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(team.getPendingList().get(i));
                ItemStack teamInfo = new SkullBuilder(offlinePlayer)
                        .setName("§e§l" + offlinePlayer.getName())
                        .setLore("§fLinksklick um §aAnzunehmen §foder §cAbzulehnen§f.")
                        .build();
                inv.setItem(i, teamInfo);
                ids.put(offlinePlayer.getName(), offlinePlayer.getUniqueId());
            }
        } else {
            int slot = 0;
            for (int i = 18 * page; i < (team.getPendingList().size() < (18 * page + 18) ? team.getPendingList().size() : (18 * page + 18)); i++) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(team.getPendingList().get(i));
                ItemStack teamInfo = new SkullBuilder(offlinePlayer)
                        .setName("§e§l" + offlinePlayer.getName())
                        .setLore("§fLinksklick um §aAnzunehmen §foder §cAbzulehnen§f.")
                        .build();
                inv.setItem(slot, teamInfo);
                ids.put(offlinePlayer.getName(), offlinePlayer.getUniqueId());
                slot++;
            }
            if (page != 0) {
                ItemStack navBackItem = new ItemBuilder(Material.ARROW)
                        .setName("§e§lvorherige Seite")
                        .setLore("§fSeite " + page)
                        .build();
                inv.setItem(25, navBackItem);
            } else {
                inv.setItem(25, GameItems.redItem);
            }
            if (page < (team.getPendingList().size() / 18)) {
                ItemStack navNextItem = new ItemBuilder(Material.ARROW)
                        .setName("§e§lnächste Seite")
                        .setLore("§fSeite " + (page + 2))
                        .build();
                inv.setItem(26, navNextItem);
            } else {
                inv.setItem(26, GameItems.redItem);
            }
        }
        pendingList.put(player, ids);
        return inv;
    }

    @Override
    public void action(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() != null) {
            Player player = (Player) event.getWhoClicked();
            switch (event.getCurrentItem().getType()) {
                case EMERALD: {
                    player.openInventory(settingsTeamUi.getInv(player));
                    break;
                }
                case ARROW: {
                    switch (event.getSlot()) {
                        case 25: {
                            pages.put(player, pages.get(player) - 1);
                            player.openInventory(getInv(player, pages.get(player)));
                            break;
                        }
                        case 26: {
                            pages.put(player, pages.get(player) + 1);
                            player.openInventory(getInv(player, pages.get(player)));
                            break;
                        }
                    }
                    break;
                }
                case PLAYER_HEAD: {
                    HashMap<String, UUID> ids = pendingList.get(player);
                    UUID id = ids.get(event.getCurrentItem().getItemMeta().getDisplayName()
                            .replaceFirst("§e§l", ""));
                    if (Bukkit.getOfflinePlayer(id) != null) {
                        ConfirmPendingUI ui = new ConfirmPendingUI(id, configManager.getTeamConfig().getTeamByOwner(player.getUniqueId()),
                                this, configManager, actionSystem);
                        actionSystem.registerInventoryAction(ui);
                        player.openInventory(ui.getInv(player));
                    } else
                        player.sendMessage(Plugin.prefix + " §7| §cEtwas lief schief beim Auswählen einer Spieleranfrage.");
                    break;
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {
    }
}
