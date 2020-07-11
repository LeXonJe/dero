package net.lexonje.dero.items;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public abstract class InventoryUI {

    private String name;
    private int rows;

    public InventoryUI(String name, int rows) {
        this.name = name;
        this.rows = rows;
    }

    public abstract Inventory getInv(Player player);

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public abstract void action(InventoryClickEvent event);

    public abstract void close(InventoryCloseEvent event);
}
