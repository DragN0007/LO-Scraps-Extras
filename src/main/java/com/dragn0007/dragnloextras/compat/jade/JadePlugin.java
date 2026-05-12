package com.dragn0007.dragnloextras.compat.jade;

import com.dragn0007.dragnlivestock.compat.jade.gender.MountGenderTooltip;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnloextras.entity.butchering.Corpse;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(new CorpseDespawnTooltip(), Corpse.class);
    }
}
