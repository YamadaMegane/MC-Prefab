package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * @author WuestMan
 */
public class ItemPalletOfBricks extends Item {
    public ItemPalletOfBricks() {
        super(new Item.Properties().tab(ModRegistry.PREFAB_GROUP));

    }
}