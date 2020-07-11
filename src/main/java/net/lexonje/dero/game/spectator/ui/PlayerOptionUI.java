package net.lexonje.dero.game.spectator.ui;

import net.lexonje.dero.items.InventoryUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class PlayerOptionUI extends InventoryUI {

    public PlayerOptionUI() {
        super(name, rows);
    }

    @Override
    public Inventory getInv(Player player) {
        return null;
    }

    @Override
    public void action(InventoryClickEvent event) {

    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
