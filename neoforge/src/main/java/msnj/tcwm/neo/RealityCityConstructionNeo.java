package msnj.tcwm.neo;

import msnj.tcwm.RealityCityConstruction;
import msnj.tcwm.util.Logger;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(RealityCityConstruction.MOD_ID)
public class RealityCityConstructionNeo {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, RealityCityConstruction.MOD_ID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, RealityCityConstruction.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RealityCityConstruction.MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, RealityCityConstruction.MOD_ID);
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, RealityCityConstruction.MOD_ID);

    public static final Logger LOGGER = new Logger(System.out);

    public RealityCityConstructionNeo() {
        LOGGER.info("RCC mod NeoForge Private System is loading...");
    }
}
