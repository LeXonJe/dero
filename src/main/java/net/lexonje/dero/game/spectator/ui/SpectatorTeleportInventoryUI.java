package net.lexonje.dero.game.spectator.ui;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.TeamConfig;
import net.lexonje.dero.game.GameSystem;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.game.spectator.SpectatorManager;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.items.ItemBuilder;
import net.lexonje.dero.items.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SpectatorTeleportInventoryUI extends InventoryUI {

    private ConfigManager configManager;
    private SpectatorManager spectatorManager;
    private GameSystem gameSystem;
    private HashMap<Player, Integer> pages;

    public SpectatorTeleportInventoryUI(ConfigManager configManager, SpectatorManager spectatorManager, GameSystem gameSystem) {
        super("Spielerteleporter", 9*3);
        this.pages = new HashMap<>();
        this.configManager = configManager;
        this.gameSystem = gameSystem;
        this.spectatorManager = spectatorManager;
    }

    @Override
    public Inventory getInv(Player player) {
        return getInv(player, 0);
    }

    public Inventory getInv(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, getRows(), getName());
        TeamConfig.Team team = configManager.getTeamConfig().getTeamByMember(player.getUniqueId());
        if (team != null) {
            pages.put(player, page);
            if ((team.getMembers().size() / 18) < 1) {
                int slot = 0;
                for (int i = 0; i < team.getMembers().size(); i++) {
                    Player otherPlayer = Bukkit.getPlayer(team.getMembers().get(i));
                    if (otherPlayer != null) {
                        if (!otherPlayer.equals(player) && gameSystem.isInGame(otherPlayer)) {
                            ItemStack teamInfo = new SkullBuilder(otherPlayer)
                                    .setName("§e§l" + otherPlayer.getName())
                                    .setLore("§fLinksklick, um zu teleportieren.")
                                    .build();
                            inv.setItem(slot, teamInfo);
                            slot++;
                        }
                    }
                }
                if (slot == 0) {
                    inv.setItem(13, GameItems.noOthersTeamItem);
                }
            } else {
                int slot = 0;
                for (int i = 18 * page; i < (team.getMembers().size() < (18 * page + 18) ? team.getMembers().size() : (18 * page + 18)); i++) {
                    Player otherPlayer = Bukkit.getPlayer(team.getMembers().get(i));
                    if (otherPlayer != null) {
                        if (!otherPlayer.equals(player) && gameSystem.isInGame(otherPlayer)) {
                            ItemStack teamInfo = new SkullBuilder(otherPlayer)
                                    .setName("§e§l" + otherPlayer.getName())
                                    .setLore("§fLinksklick, um zu teleportieren.")
                                    .build();
                            inv.setItem(slot, teamInfo);
                        }
                    }
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
        } else {
            inv.setItem(13, GameItems.noTeamItem);
        }
        return inv;
    }

    @Override
    public void action(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (spectatorManager.isSpectator(player)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                switch (event.getCurrentItem().getType()) {
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
                        String name = event.getCurrentItem().getItemMeta().getDisplayName().replaceFirst("§e§l", "");
                        if (Bukkit.getPlayer(name) != null) {
                            Player otherPlayer = Bukkit.getPlayer(name);
                            if (gameSystem.isInGame(otherPlayer)) {
                                player.teleport(otherPlayer.getLocation());
                                player.sendMessage(Plugin.prefix + " §7| §fDu wurdest zu §6§l" + name + " §fteleportiert.");
                            } else
                                player.sendMessage(Plugin.prefix + " §7| §cDer Spieler ist nicht mehr im Spiel.");
                        } else
                            player.sendMessage(Plugin.prefix + " §7| §cDer Spieler ist nicht mehr online.");
                        player.closeInventory();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {
    }
}
