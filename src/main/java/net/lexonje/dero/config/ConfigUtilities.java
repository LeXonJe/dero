package net.lexonje.dero.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;

public class ConfigUtilities {

    public static void setLocationToConfig(Location location, Config config, String path) {
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());
    }

    public static Location getLocationFromConfig(Config config, String path) {
        return new Location(Bukkit.getWorld(config.getString(path + ".world")),
                config.getDouble(path + ".x"), config.getDouble(path + ".y"),
                config.getDouble(path + ".z"), (float) config.getDouble(path + ".yaw"),
                (float) config.getDouble(path + ".pitch"));
    }

    public static void setCurrentDateToConfig(Config config, String path) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
        config.set(path + ".year", calendar.get(Calendar.YEAR));
        config.set(path + ".month", calendar.get(Calendar.MONTH));
        config.set(path + ".day", calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static int[] getDateFromConfig(Config config, String path) {
        return new int[] {config.getInt(path + ".day"), config.getInt(path + ".month"), config.getInt(path + ".year")};
    }

    public static void setInventoryToConfig(PlayerInventory inv, Config config, String path) {
        config.set(path + ".content", invToBase64(inv)[0]);
        config.set(path + ".armor", invToBase64(inv)[1]);
        config.set(path + ".extra", invToBase64(inv)[2]);
    }

    public static void giveInventoryFromConfig(Player player, Config config, String path) {
        ItemStack[][] stacks = base64ToInventory(new String[] {config.getString(path + ".content"),
                config.getString(path + ".armor"), config.getString(path + ".extra")});
        player.getInventory().setContents(stacks[0]);
        player.getInventory().setArmorContents(stacks[1]);
        player.getInventory().setExtraContents(stacks[2]);
    }

    private static String[] invToBase64(PlayerInventory inv) {
        String content = itemsToBase64(inv.getContents());
        String armor = itemsToBase64(inv.getArmorContents());
        String extra = itemsToBase64(inv.getExtraContents());

        return new String[] {content, armor, extra};
    }

    private static ItemStack[][] base64ToInventory(String[] values) {
        ItemStack[] content = itemFromBase64(values[0]);
        ItemStack[] armor = itemFromBase64(values[1]);
        ItemStack[] extra = itemFromBase64(values[2]);

        return new ItemStack[][] {content, armor, extra};
    }

    private static ItemStack[] itemFromBase64(String data) {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        try {
            BukkitObjectInputStream dataIn = new BukkitObjectInputStream(in);
            ItemStack[] items = new ItemStack[dataIn.readInt()];
            for (int i = 0; i < items.length; i++) {
                try {
                    items[i] = (ItemStack) dataIn.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            dataIn.close();
            return items;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String itemsToBase64(ItemStack[] items) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            BukkitObjectOutputStream dataOut = new BukkitObjectOutputStream(out);
            dataOut.writeInt(items.length);
            for (ItemStack item : items) {
                dataOut.writeObject(item);
            }
            dataOut.close();
            return Base64Coder.encodeLines(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setEffectsToConfig(Collection<PotionEffect> effects, Config config, String path) {
        config.set(path, effectsToBase64(effects));
    }

    public static Collection<PotionEffect> getEffectsFromConfig(Config config, String path) {
        return effectsFromBase64(config.getString(path));
    }

    private static String effectsToBase64(Collection<PotionEffect> effects) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            BukkitObjectOutputStream dataOut = new BukkitObjectOutputStream(out);
            dataOut.writeInt(effects.size());
            for (PotionEffect effect : effects) {
                dataOut.writeObject(effect);
            }
            dataOut.close();
            return Base64Coder.encodeLines(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Collection<PotionEffect> effectsFromBase64(String data) {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        try {
            BukkitObjectInputStream dataIn = new BukkitObjectInputStream(in);
            int size = dataIn.readInt();
            Collection<PotionEffect> effects = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                try {
                    effects.add((PotionEffect) dataIn.readObject());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            dataIn.close();
            return effects;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
