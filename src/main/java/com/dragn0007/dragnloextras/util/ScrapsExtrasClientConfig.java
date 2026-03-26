package com.dragn0007.dragnloextras.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ScrapsExtrasClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue RENDER_DIRT;

    static {
        BUILDER.push("Client");
        BUILDER.pop();
        RENDER_DIRT = BUILDER.define("Render Animal Dirt Layer", true);
        SPEC = BUILDER.build();
    }
}
