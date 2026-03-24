package com.dragn0007.dragnloextras.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public interface SECapabilities {

    Capability<DirtyCapabilityInterface> DIRTY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    Capability<HalterCapabilityInterface> HALTER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    Capability<TraitCapabilityInterface> TRAIT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    Capability<HalterColorCapabilityInterface> HALTER_COLOR_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    Capability<ImmunityCapabilityInterface> IMMUNITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    Capability<SleepingCapabilityInterface> SLEEPING_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    static void register(RegisterCapabilitiesEvent event) {
        event.register(DirtyCapabilityInterface.class);
        event.register(HalterCapabilityInterface.class);
        event.register(TraitCapabilityInterface.class);
        event.register(HalterColorCapabilityInterface.class);
        event.register(ImmunityCapabilityInterface.class);
        event.register(SleepingCapabilityInterface.class);
    }
}