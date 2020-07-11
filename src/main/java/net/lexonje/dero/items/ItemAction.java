package net.lexonje.dero.items;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ItemAction {

    private ItemStack stack;

    public ItemAction(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getItem() {
        return stack;
    }

    public abstract void action(PlayerInteractEvent event);
}
