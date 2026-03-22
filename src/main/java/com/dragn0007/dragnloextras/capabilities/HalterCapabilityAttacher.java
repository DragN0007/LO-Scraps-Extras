package com.dragn0007.dragnloextras.capabilities;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class HalterCapabilityAttacher {

    public static class HalterCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(ScrapsExtras.MODID, "halter_cap");

        private final HalterCapabilityInterface backend = new HalterCapabilityImplementation();
        private final LazyOptional<HalterCapabilityInterface> optionalData = LazyOptional.of(() -> backend);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return SECapabilities.HALTER_CAPABILITY.orEmpty(cap, this.optionalData);
        }

        void invalidate() {
            this.optionalData.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        final HalterCapabilityProvider provider = new HalterCapabilityProvider();
        event.addCapability(HalterCapabilityProvider.IDENTIFIER, provider);
    }

    private HalterCapabilityAttacher() {
    }
}