package net.lexonje.dero.items;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class SkullBuilder {

    private ItemStack skull;
    private SkullMeta meta;

    public SkullBuilder(OfflinePlayer player) {
        this.skull = new ItemStack(Material.PLAYER_HEAD);
        this.meta = (SkullMeta) skull.getItemMeta();
        this.meta.setOwningPlayer(player);
    }

    public SkullBuilder setName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public SkullBuilder setLore(String... lore){
        this.meta.setLore(Arrays.asList(lore));
        return this;
    }

    public SkullBuilder setAmount(int amount){
        this.skull.setAmount(amount);
        return this;
    }

    public SkullBuilder addEnchantment(Enchantment enchantment, int value, boolean ignoreLevelDistrictions) {
        this.meta.addEnchant(enchantment, value, ignoreLevelDistrictions);
        return this;
    }

    public SkullBuilder removeEnchantment(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    public SkullBuilder addItemFlags(ItemFlag... itemFlags) {
        this.meta.addItemFlags(itemFlags);
        return this;
    }

    public SkullBuilder removeItemFlags(ItemFlag... itemFlags) {
        this.meta.removeItemFlags(itemFlags);
        return this;
    }

    public SkullBuilder setMagic(boolean bool) {
        if (bool) {
            this.meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
            this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            this.meta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS);
            this.meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemStack build() {
        this.skull.setItemMeta(meta);
        return skull;
    }
}
