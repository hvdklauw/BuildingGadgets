package com.direwolf20.buildinggadgets.common.data;

import com.direwolf20.buildinggadgets.BuildingGadgets;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = BuildingGadgets.MOD_ID)
public class DataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

    }
}