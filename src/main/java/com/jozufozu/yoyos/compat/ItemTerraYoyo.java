/*
 * Copyright (c) 2018 Jozsef Augusztiny
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.jozufozu.yoyos.compat;

import com.jozufozu.yoyos.Yoyos;
import com.jozufozu.yoyos.common.EntityYoyo;
import com.jozufozu.yoyos.common.ModConfig;
import com.jozufozu.yoyos.network.MessageAcquireTarget;
import com.jozufozu.yoyos.network.YoyoNetwork;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTerraYoyo extends ItemManaYoyo
{
    public ItemTerraYoyo(String name, ToolMaterial material, int manaPerDamage)
    {
        super(name, material, manaPerDamage);

        addPropertyOverride(new ResourceLocation(Yoyos.MODID, "lined"), (itemStack, world, entityLivingBase) -> isLined(itemStack) ? 1.0F : 0.0f);

        if (Yoyos.proxy.runningOnClient())
            MinecraftForge.EVENT_BUS.register(this);
    }

    public static void setLined(ItemStack yoyo)
    {
        NBTTagCompound compound = yoyo.getTagCompound();

        if (compound == null)
        {
            compound = new NBTTagCompound();
            yoyo.setTagCompound(compound);
        }

        compound.setBoolean("lined", true);
    }

    public static boolean isLined(ItemStack yoyo)
    {
        NBTTagCompound compound = yoyo.getTagCompound();

        if (compound == null) return false;

        return compound.getBoolean("lined");
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void leftClick(PlayerInteractEvent.LeftClickEmpty evt)
    {
        if (!evt.getItemStack().isEmpty() && evt.getItemStack().getItem() == this)
        {
            YoyoNetwork.INSTANCE.sendToServer(new MessageAcquireTarget());
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        ItemStack itemStack = playerIn.getHeldItem(hand);
        if (!worldIn.isRemote && !EntityYoyo.CASTERS.containsKey(playerIn))
        {
            if (itemStack.getItemDamage() <= itemStack.getMaxDamage() || this == Yoyos.CREATIVE_YOYO)
            {
                EntityYoyo yoyo = new EntityChaserYoyo(worldIn, playerIn);
                worldIn.spawnEntity(yoyo);

                worldIn.playSound(null, yoyo.posX, yoyo.posY, yoyo.posZ, Yoyos.YOYO_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

                playerIn.addExhaustion(0.05F);
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment != Yoyos.COLLECTING && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public int collecting(ItemStack yoyo)
    {
        int level = super.collecting(yoyo);
        if (isLined(yoyo)) level += 1;
        return level;
    }

    @Override
    public float getAttackDamage(ItemStack yoyo)
    {
        return ModConfig.vanillaYoyos.diamondYoyo.damage;
    }

    @Override
    public float getWeight(ItemStack yoyo)
    {
        return ModConfig.vanillaYoyos.diamondYoyo.weight;
    }

    @Override
    public float getLength(ItemStack yoyo)
    {
        return ModConfig.vanillaYoyos.diamondYoyo.length;
    }

    @Override
    public int getDuration(ItemStack yoyo)
    {
        return ModConfig.vanillaYoyos.diamondYoyo.duration;
    }
}