package com.dragn0007.dragnloextras.items.custom;

import com.google.common.collect.Maps;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.Map;

public class HalterItem extends Item {

   private static final Map<DyeColor, HalterItem> ITEM_BY_COLOR = Maps.newEnumMap(DyeColor.class);
   private final DyeColor dyeColor;

   public HalterItem(DyeColor p_41080_, Item.Properties p_41081_) {
      super(p_41081_);
      this.dyeColor = p_41080_;
      ITEM_BY_COLOR.put(p_41080_, this);
   }

   public DyeColor getDyeColor() {
      return this.dyeColor;
   }

   public static HalterItem byColor(DyeColor p_41083_) {
      return ITEM_BY_COLOR.get(p_41083_);
   }
}