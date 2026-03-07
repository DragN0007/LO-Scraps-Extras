package com.dragn0007.dragnloextras.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ScrapsExtrasClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Client");
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
