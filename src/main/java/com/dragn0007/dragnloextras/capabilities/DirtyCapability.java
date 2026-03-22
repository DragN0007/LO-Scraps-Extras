package com.dragn0007.dragnloextras.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public interface DirtyCapability {

    Capability<DirtyCapabilityInterface> DIRTY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    static void register(RegisterCapabilitiesEvent event) {
        event.register(DirtyCapabilityInterface.class);
    }
}