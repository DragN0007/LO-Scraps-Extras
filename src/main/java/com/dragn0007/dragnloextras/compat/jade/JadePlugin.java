package com.dragn0007.dragnloextras.compat.jade;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnloextras.blocks.custom.FeedBowlBlock;
import com.dragn0007.dragnloextras.blocks.custom.FeedTroughBlock;
import com.dragn0007.dragnloextras.compat.jade.pregnancy.HorsePregnancyTooltip;
import com.dragn0007.dragnloextras.entity.butchering.Corpse;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(new CorpseDespawnTooltip(), Corpse.class);
        registration.registerEntityComponent(new HorsePregnancyTooltip(), OHorse.class);

        registration.registerBlockComponent(new TroughTooltipProvider(), FeedTroughBlock.class);
        registration.registerBlockComponent(new BowlTooltipProvider(), FeedBowlBlock.class);
    }
}
