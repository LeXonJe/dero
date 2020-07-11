package net.lexonje.dero.items;

import org.bukkit.Bukkit;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionSystem {

    private HashMap<ItemStack, ItemAction> itemActions;
    private HashMap<String, InventoryUI> inventoryActions;
    private SignGUI signGUI;
    private ActionListener listener;

    public ActionSystem(JavaPlugin plugin) {
        this.itemActions = new HashMap<>();
        this.inventoryActions = new HashMap<>();
        this.listener = new ActionListener(this);
        this.signGUI = new SignGUI(plugin);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public ItemAction getItemAction(ItemStack stack) {
        return itemActions.get(stack);
    }

    public InventoryUI getInventoryUI(InventoryView view) {
        return inventoryActions.get(view.getTitle());
    }

    public SignGUI getSignGUI() {
        return signGUI;
    }

    public void registerItemAction(ItemAction action) {
        this.itemActions.put(action.getItem(), action);
    }

    public void registerInventoryAction(InventoryUI inventoryUI) {
        this.inventoryActions.put(inventoryUI.getName(), inventoryUI);
    }

    public void removeItemAction(ItemAction action) {
        this.itemActions.remove(action);
    }

    public void removeInventoryAction(InventoryUI action) {
        this.inventoryActions.remove(action.getName());
    }
}
