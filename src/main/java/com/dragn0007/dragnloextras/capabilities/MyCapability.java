package com.dragn0007.dragnloextras.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public interface MyCapability {

    Capability<MyCapabilityInterface> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    static void register(RegisterCapabilitiesEvent event) {
        event.register(MyCapabilityInterface.class);
    }
}