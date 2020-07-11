package net.lexonje.dero.prevent;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class PreventionSystem {

    private HashMap<World, boolean[]> worldProtection;
    private ArrayList<Player> allowedPlayers;
    private PreventionListener listener;

    public PreventionSystem(JavaPlugin plugin) {
        this.worldProtection = new HashMap<>();
        this.allowedPlayers = new ArrayList<>();
        this.listener = new PreventionListener(this);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        plugin.getCommand("build").setExecutor(new BuildCommand(this));
    }

    public void setWorldProtection(World world,
                                   boolean preventFarmDestroying, boolean preventItemDrop,
                                   boolean preventInventorySort,
                                   boolean preventSwapping, boolean foodSaturation, boolean preventInteract) {
        this.worldProtection.put(world, new boolean[] {
                preventFarmDestroying, preventItemDrop, preventInventorySort,
                preventSwapping, foodSaturation, preventInteract});
    }

    public boolean[] getWorldProtectionSettings(World world) {
        if (worldProtection.containsKey(world)) {
            return worldProtection.get(world);
        } else {
            return new boolean[] {false, false, false, false, false, false};
        }
    }

    protected void addBuilder(Player player) {
        this.allowedPlayers.add(player);
    }

    protected void removeBuilder(Player player) {
        this.allowedPlayers.remove(player);
    }

    protected boolean containsBuilder(Player player) {
        return allowedPlayers.contains(player);
    }
}
