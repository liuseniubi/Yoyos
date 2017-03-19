package com.jozufozu.yoyos.client;

import com.jozufozu.yoyos.Yoyos;
import com.jozufozu.yoyos.common.CommonProxy;
import com.jozufozu.yoyos.common.EntityYoyo;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        RenderingRegistry.registerEntityRenderingHandler(EntityYoyo.class, RenderYoYo::new);

        registerModel(Yoyos.WOODEN_YOYO);
        registerModel(Yoyos.STONE_YOYO);
        registerModel(Yoyos.IRON_YOYO);
        registerModel(Yoyos.DIAMOND_YOYO);
        registerModel(Yoyos.GOLD_YOYO);
        registerModel(Yoyos.SHEAR_YOYO);
        registerModel(Yoyos.CORD);
    }

    public static void registerModel(Item item) {
        if (item.getHasSubtypes()) {
            List<ItemStack> list = new ArrayList<>();

            item.getSubItems(item, null, list);

            for (ItemStack i : list) {
                ModelBakery.registerItemVariants(i.getItem(), i.getItem().getRegistryName());
                ModelLoader.setCustomModelResourceLocation(item, i.getItemDamage(), new ModelResourceLocation(Yoyos.MODID + ":" + i.getUnlocalizedName().substring(5)));
            }
        }
        else {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Yoyos.MODID + ":" + item.getUnlocalizedName().substring(5)));
        }
    }
}
