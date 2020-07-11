package net.lexonje.dero.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(Material material1) {
        this.item = new ItemStack(material1);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder setName(String displayName) {
        this.meta.setDisplayName(displayName);
        return this;
    }

    public ItemBuilder setLore(String... lore){
        this.meta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder setAmount(int amount){
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int value, boolean ignoreLevelDistrictions) {
        this.meta.addEnchant(enchantment, value, ignoreLevelDistrictions);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        this.meta.addItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag... itemFlags) {
        this.meta.removeItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder setMagic(boolean bool) {
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
        this.item.setItemMeta(meta);
        return item;
    }
}
