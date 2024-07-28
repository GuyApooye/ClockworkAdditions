package com.github.guyapooye.clockworkaddons;


import com.github.guyapooye.clockworkaddons.registries.BlockRegistry;
import com.simibubi.create.AllBlocks;
import io.github.fabricators_of_create.porting_lib.util.ItemGroupUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.clockwork.content.curiosities.WanderliteItem;
import org.valkyrienskies.clockwork.content.curiosities.tools.wanderwand.WanderWandItem;
import org.valkyrienskies.mod.common.block.TestChairBlock;
import org.valkyrienskies.clockwork.ClockworkItems;

public class CWACreativeModeTab extends CreativeModeTab{

    public static CWACreativeModeTab CWACreativeModeTab = new CWACreativeModeTab();
    public CWACreativeModeTab() {
        super(ItemGroupUtil.expandArrayAndGetId(),"Clockwork Addons");
    }


    public static void init(){}

    @Override
    public @NotNull ItemStack makeIcon() {
        return BlockRegistry.KINETIC_FLAP_BEARING.asStack();
    }
}
