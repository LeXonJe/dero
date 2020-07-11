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

public class ListTeamMembersUI extends InventoryUI {

    private SettingsTeamUI settingsTeamUi;
    private ConfigManager configManager;
    private ActionSystem actionSystem;
    private HashMap<Player, Integer> pages;

    public ListTeamMembersUI(SettingsTeamUI settingsTeamUi, ActionSystem actionSystem, ConfigManager configManager) {
        super("§6§lAlle Mitglieder", 9*3);
        this.pages = new HashMap<>();
        this.settingsTeamUi = settingsTeamUi;
        this.configManager = configManager;
        this.actionSystem = actionSystem;
    }

    @Override
    public Inventory getInv(Player player) {
        return null;
    }

    private Inventory initInv(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, getRows(), getName());
        TeamConfig.Team team = configManager.getTeamConfig().getTeamByMember(player.getUniqueId());
        inv.setItem(18, GameItems.backItem);
        pages.put(player, page);
        if (team.getMembers().size() < 1) {
            inv.setItem(13, GameItems.noPendingItem);
        } else if ((team.getMembers().size() / 18) < 1) {
            for (int i = 0; i < team.getMembers().size(); i++) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(team.getMembers().get(i));
                ItemStack teamInfo = new SkullBuilder(offlinePlayer)
                        .setName("§e§l" + offlinePlayer.getName())
                        .setLore("§fLinksklick für Optionen.")
                        .build();
                inv.setItem(i, teamInfo);
            }
        } else {
            int slot = 0;
            for (int i = 18 * page; i < (team.getMembers().size() < (18 * page + 18) ? team.getMembers().size() : (18 * page + 18)); i++) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(team.getMembers().get(i));
                ItemStack teamInfo = new SkullBuilder(offlinePlayer)
                        .setName("§e§l" + offlinePlayer.getName())
                        .setLore("§fLinksklick für Optionen.")
                        .build();
                inv.setItem(slot, teamInfo);
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
            if (page < (team.getMembers().size() / 18)) {
                ItemStack navNextItem = new ItemBuilder(Material.ARROW)
                        .setName("§e§lnächste Seite")
                        .setLore("§fSeite " + (page + 2))
                        .build();
                inv.setItem(26, navNextItem);
            } else {
                inv.setItem(26, GameItems.redItem);
            }
        }
        return inv;
    }

    public Inventory getInv(Player player, int page) {
        return initInv(player, page);
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
                    TeamConfig.Team team = configManager.getTeamConfig().getTeamByOwner(player.getUniqueId());
                    String name = event.getCurrentItem().getItemMeta().getDisplayName().replaceFirst("§e§l", "");
                    UUID id = null;
                    for (UUID uuid : team.getMembers()) {
                        if (Bukkit.getOfflinePlayer(uuid).getName().equals(name))
                            id = uuid;
                    }
                    if (id != null && Bukkit.getOfflinePlayer(id) != null) {
                        MemberOptionUI memberOptionUI = new MemberOptionUI(Bukkit.getOfflinePlayer(id), this,
                                actionSystem, configManager);
                        actionSystem.registerInventoryAction(memberOptionUI);
                        player.openInventory(memberOptionUI.getInv(player));
                    } else
                        player.sendMessage(Plugin.prefix + " §7| §cEtwas ist bei der Spielerauswahl falsch gelaufen.");
                    break;
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {
    }
}
