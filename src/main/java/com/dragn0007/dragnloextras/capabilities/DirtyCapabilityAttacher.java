package com.dragn0007.dragnloextras.capabilities;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class DirtyCapabilityAttacher {

    public static class DirtyCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(ScrapsExtras.MODID, "dirty_cap");

        private final DirtyCapabilityInterface backend = new DirtyCapabilityImplementation();
        private final LazyOptional<DirtyCapabilityInterface> optionalData = LazyOptional.of(() -> backend);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return DirtyCapability.DIRTY_CAPABILITY.orEmpty(cap, this.optionalData);
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
        final DirtyCapabilityProvider provider = new DirtyCapabilityProvider();
        event.addCapability(DirtyCapabilityProvider.IDENTIFIER, provider);
    }

    private DirtyCapabilityAttacher() {
    }
}