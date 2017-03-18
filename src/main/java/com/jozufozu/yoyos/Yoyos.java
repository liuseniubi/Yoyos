package com.jozufozu.yoyos;

import com.jozufozu.yoyos.common.CommonProxy;
import com.jozufozu.yoyos.common.ItemYoyo;
import com.jozufozu.yoyos.tinkers.TinkersYoyos;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import slimeknights.mantle.pulsar.config.ForgeCFG;
import slimeknights.mantle.pulsar.control.PulseManager;

@Mod(name = Yoyos.NAME, modid = Yoyos.MODID, version = Yoyos.VERSION, dependencies = "after:tconstruct")
public class Yoyos {

    @Mod.Instance(value = Yoyos.MODID)
    public static Yoyos INSTANCE;

    public static final String MODID = "yoyos";
    public static final String NAME = "Tinkers' Yoyos";
    public static final String VERSION = "@VERSION@";

    public static ForgeCFG pulseConfig = new ForgeCFG("TinkerYoyos", "Yoyos");
    public static PulseManager pulsar = new PulseManager(pulseConfig);

    @SidedProxy(clientSide = "com.jozufozu.yoyos.client.ClientProxy", serverSide = "com.jozufozu.yoyos.common.CommonProxy$ServerProxy")
    public static CommonProxy proxy;

    public static Item WOODEN_YOYO;
    public static Item STONE_YOYO;
    public static Item IRON_YOYO;
    public static Item DIAMOND_YOYO;
    public static Item GOLD_YOYO;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (Loader.isModLoaded("tconstruct")) {
            pulsar.registerPulse(new TinkersYoyos());
        }

        WOODEN_YOYO = register(new ItemYoyo("wooden_yoyo", Item.ToolMaterial.WOOD));
        STONE_YOYO = register(new ItemYoyo("stone_yoyo", Item.ToolMaterial.STONE));
        IRON_YOYO = register(new ItemYoyo("iron_yoyo", Item.ToolMaterial.IRON));
        DIAMOND_YOYO = register(new ItemYoyo("diamond_yoyo", Item.ToolMaterial.DIAMOND));
        GOLD_YOYO = register(new ItemYoyo("gold_yoyo", Item.ToolMaterial.GOLD));

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        addYoyoCrafting(WOODEN_YOYO, "plankWood");
        addYoyoCrafting(STONE_YOYO, "cobblestone");
        addYoyoCrafting(IRON_YOYO, "ingotIron");
        addYoyoCrafting(DIAMOND_YOYO, "gemDiamond");
        addYoyoCrafting(GOLD_YOYO, "ingotGold");

        proxy.init(event);
    }

    private static void addYoyoCrafting(Item item, String oreDict) {
        GameRegistry.addRecipe(new ShapedOreRecipe(item,
                " XX",
                " SX",
                "S  ",
                'S', "string",
                'X', oreDict));
    }

    private static Item register(Item item) {
        GameRegistry.register(item);
        return item;
    }
}
