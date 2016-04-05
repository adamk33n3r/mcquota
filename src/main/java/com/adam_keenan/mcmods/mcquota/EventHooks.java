package com.adam_keenan.mcmods.mcquota;

import com.adam_keenan.mcmods.mcquota.utils.Log;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EventHooks {
    @SubscribeEvent
    public void entityJoined(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayer) {
            Log.info("Player connected: " + ((EntityPlayer) event.entity).getDisplayName());
        }
    }

    @SubscribeEvent
    public void entityDamaged(net.minecraftforge.event.entity.living.LivingDeathEvent event) {
        Log.info("Something died");
        Log.info(event.entityLiving.getClass().getCanonicalName());
        Entity e = event.source.getEntity();
        Entity e2 = event.source.getSourceOfDamage();
        if (e2 != null) {
            Log.info(e2.getClass().getCanonicalName());
        } else {
            Log.info("e2 is null");
        }
        if (e != null) {
            Log.info(e.getClass().getCanonicalName());
            if (e instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)e;
                Log.info("Killed by: " + player.getDisplayName());
                ChunkCoordinates cco = player.getPlayerCoordinates();
                try {
                    Log.info("coordinates");
                    if (cco != null) {
                        Log.info(cco);
                    } else {
                        Log.info("null");
                    }
                } catch (Exception ex) {
                    Log.info("boo");
                }
                //player.setPositionAndUpdate(cco.posX, cco.posY+10, cco.posZ);
                Block b = Blocks.wool;
                player.getEntityWorld().setBlock(cco.posX, cco.posY -1, cco.posZ, b, 1, 2);
                player.getEntityWorld().setBlock(cco.posX, cco.posY + 2, cco.posZ, b, 2, 2);
                player.getEntityWorld().setBlock(cco.posX - 1, cco.posY, cco.posZ, b, 3, 2);
                player.getEntityWorld().setBlock(cco.posX + 1, cco.posY, cco.posZ, b, 4, 2);
                player.getEntityWorld().setBlock(cco.posX - 1, cco.posY + 1, cco.posZ, b, 5, 2);
                player.getEntityWorld().setBlock(cco.posX + 1, cco.posY + 1, cco.posZ, b, 6, 2);
                player.getEntityWorld().setBlock(cco.posX, cco.posY, cco.posZ - 1, b, 7, 2);
                player.getEntityWorld().setBlock(cco.posX, cco.posY, cco.posZ + 1, b, 8, 2);
                player.getEntityWorld().setBlock(cco.posX, cco.posY + 1, cco.posZ - 1, b, 9, 2);
                player.getEntityWorld().setBlock(cco.posX, cco.posY + 1, cco.posZ + 1, b, 10, 2);
            }
        } else {
            Log.info("e is null");
        }
//        if (event.entityLiving.getLastAttacker() instanceof EntityPlayer) {
//            EntityPlayer player = (EntityPlayer)event.entityLiving.getLastAttacker();
//            Log.info("Was killed by player");
//            Log.info(player.getDisplayName());
//            player.setJumping(true);
//        }
    }
}
