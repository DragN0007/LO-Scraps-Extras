package com.dragn0007.dragnloextras.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ScrapsExtrasClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue RENDER_DIRT;
    public static final ForgeConfigSpec.BooleanValue RENDER_HALTER;
    public static final ForgeConfigSpec.BooleanValue RENDER_SPIKES;

    static {
        BUILDER.push("Client");
        BUILDER.pop();
        RENDER_DIRT = BUILDER.define("Render Animal Dirt Layer", true);
        RENDER_HALTER = BUILDER.define("Render Halter Layer", true);
        RENDER_SPIKES = BUILDER.define("Render Collar Spikes Layer", true);
        SPEC = BUILDER.build();
    }
}
