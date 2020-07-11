package net.lexonje.dero.game.invuis.profile.team;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.TeamConfig;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ListTeamUI extends InventoryUI {

    private TeamsUI teamsUI;
    private ConfigManager configManager;
    private HashMap<Player, Integer> pages;

    public ListTeamUI(TeamsUI teamsUI, ConfigManager configManager) {
        super("§6§lAlle Teams", 9*3);
        this.pages = new HashMap<>();
        this.teamsUI = teamsUI;
        this.configManager = configManager;
    }

    @Override
    public Inventory getInv(Player player) {
        return getInventory(player, 0);
    }

    protected Inventory initInv(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, getRows(), getName());
        TeamConfig config = configManager.getTeamConfig();
        inv.setItem(18, GameItems.backItem);
        pages.put(player, page);
        if (config.getTeams().size() < 1) {
            inv.setItem(13, GameItems.noTeamsItem);
        } else if ((config.getTeams().size() / 18) < 1) {
            for (int i = 0; i < config.getTeams().size(); i++) {
                ArrayList<String> labels = new ArrayList<>();
                TeamConfig.Team team = config.getTeams().get(i);
                labels.add("§ferstellt von §3" + Bukkit.getOfflinePlayer(team.getOwnerId()).getName());
                labels.add("");
                labels.add("§dMitglieder:");
                for (UUID id : team.getMembers()) {
                    labels.add(" §7- §f" + Bukkit.getOfflinePlayer(id).getName());
                }
                ItemStack teamInfo = new ItemBuilder(Material.PAPER)
                        .setName("§6§l" + team.getName() + " §7{§e" + team.getPrefix() + "§7}")
                        .setLore(labels.toArray(new String[labels.size()]))
                        .build();
                inv.setItem(i, teamInfo);
            }
        } else {
            int slot = 0;
            for (int i = 18 * page; i < (config.getTeams().size() < (18 * page + 18) ? config.getTeams().size() : (18 * page + 18)); i++) {
                ArrayList<String> labels = new ArrayList<>();
                TeamConfig.Team team = config.getTeams().get(i);
                labels.add("§ferstellt von §3" + Bukkit.getOfflinePlayer(team.getOwnerId()).getName());
                labels.add("");
                labels.add("§dMitglieder:");
                for (UUID id : team.getMembers()) {
                    labels.add(" §7- §f" + Bukkit.getOfflinePlayer(id).getName());
                }
                ItemStack teamInfo = new ItemBuilder(Material.PAPER)
                        .setName("§6§l" + team.getName() + " §7{§e" + team.getPrefix() + "§7}")
                        .setLore(labels.toArray(new String[labels.size()]))
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
            if (page < (config.getTeams().size() / 18)) {
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

    public Inventory getInventory(Player player, int page) {
        return initInv(player, page);
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
                case ARROW: {
                    switch (event.getSlot()) {
                        case 25: {
                            pages.put(player, pages.get(player) - 1);
                            player.openInventory(getInventory(player, pages.get(player)));
                            break;
                        }
                        case 26: {
                            pages.put(player, pages.get(player) + 1);
                            player.openInventory(getInventory(player, pages.get(player)));
                            break;
                        }
                    }
                    break;
                }
                case PAPER: {
                    TeamConfig config = configManager.getTeamConfig();
                    if (config.getTeamByMember(player.getUniqueId()) == null) {
                        String name = event.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0]
                                .replaceFirst("§6§l", "");
                        player.closeInventory();
                        if (config.getTeamByName(name) != null) {
                            TeamConfig.Team team = config.getTeamByName(name);
                            team.addPending(player.getUniqueId());
                            if (Bukkit.getPlayer(team.getOwnerId()) != null) {
                                Player owner = Bukkit.getPlayer(team.getOwnerId());
                                owner.sendMessage(Plugin.prefix + " §7| §e" + player.getName() + " §fmöchte deinen Team §3" + team.getName()
                                        + " §fbeitreten. Schau in die Teameinstellungen für mehr Informationen.");
                            }
                            player.sendMessage(Plugin.prefix + " §7| §fDeine Anfrage zum Beitreten des Teams §3" + name
                                    + " §fwurde §2erfolgreich §fgesendet.");
                        } else {
                            player.sendMessage(Plugin.prefix + " §7| §cEin Fehler ist aufgetreten bei der Teamauswahl.");
                        }
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
