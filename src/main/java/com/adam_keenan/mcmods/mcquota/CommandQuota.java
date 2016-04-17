package com.adam_keenan.mcmods.mcquota;

import com.adam_keenan.mcmods.mcquota.quota.QuotaDBManager;
import com.adam_keenan.mcmods.mcquota.quota.QuotaManager;
import com.adam_keenan.mcmods.mcquota.utils.Config;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import com.adam_keenan.mcmods.mcquota.utils.Utils;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.UsernameCache;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommandQuota extends CommandBase implements ICommand {

    protected List<String> aliases;
    protected String[] subcommands;

    public CommandQuota() {
        this.aliases = Arrays.asList("quota", "q");
        this.subcommands = new String[] {
            "help",
            "global",
            "player"
        };
    }

    @Override
    public String getCommandName() {
        return "mcquota";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/mcquota <player> <time>";
    }

    @Override
    public List getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        if (iCommandSender instanceof EntityPlayer) {
            Log.info("Player sent command");
        } else {
            Log.info("not player");
        }
        Log.info("Command received! Args: " + StringUtils.join(strings, ", "));
        if (strings.length == 0) {
            iCommandSender.addChatMessage(new ChatComponentText("Usage: /mcquota <global | player <player>> [quota]"));
        } else {
            if (strings[0].equals("player")) {
                Log.info("player sub");
                if (strings.length == 2) {
                    EntityPlayerMP player;
                    String uuid = null;
                    try {
                        uuid = Utils.playerToUUID(CommandBase.getPlayer(iCommandSender, strings[1]));
                    } catch (PlayerNotFoundException e) {
                        Log.info("Player not found. Looking in cache");
                        Map<UUID, String> userMap = UsernameCache.getMap();
                        for (Map.Entry<UUID, String> entry : userMap.entrySet()) {
                            if (entry.getValue().equals(strings[1])) {
                                uuid = entry.getKey().toString();
                                Log.info("Found in cache");
                                break;
                            }
                        }
                        if (uuid == null) {
                            throw e;
                        }
                    }
                    int quota = QuotaDBManager.getInstance().getPlayerQuota(uuid);
                    int timePlayed = QuotaDBManager.getInstance().getTimeSpent(uuid);
                    iCommandSender.addChatMessage(new ChatComponentText(String.format("Quota is set to %d", quota)));
                    iCommandSender.addChatMessage(new ChatComponentText(String.format("Time played today is %d", timePlayed)));
                } else if (strings.length == 3) {
                    EntityPlayerMP player = CommandBase.getPlayer(iCommandSender, strings[1]);
                    if (strings[2].equals("off")) {
                        iCommandSender.addChatMessage(new ChatComponentText("Quota turned off"));
                        QuotaDBManager.getInstance().setPlayerQuota(Utils.playerToUUID(player), -1);
                    } else {
                        int quota = Integer.parseInt(strings[2]);
                        QuotaDBManager.getInstance().setPlayerQuota(Utils.playerToUUID(player), quota);
                        iCommandSender.addChatMessage(new ChatComponentText(String.format("Quota set to %d", quota)));
                    }
                }
            } else if (strings[0].equals("global")) {
                if (strings.length >= 2) {
                    Config.quotaLength = Integer.parseInt(strings[1]);
                    iCommandSender.addChatMessage(new ChatComponentText(String.format("Global quota set to " + Config.quotaLength)));
                    Config.save();
                } else {
                    iCommandSender.addChatMessage(new ChatComponentText(String.format("Global quota is set to " + Config.quotaLength)));
                }
            } else {
                iCommandSender.addChatMessage(new ChatComponentText("Unknown command"));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        // Make sure OP
        return iCommandSender.canCommandSenderUseCommand(2, "");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender iCommandSender, String[] strings) {
        Log.info((Object[])strings);
        switch (strings.length) {
            case 1:
                return CommandBase.getListOfStringsMatchingLastWord(strings, this.subcommands);
            case 2:
                if (strings[0].equals("player")) {
                    Log.info("player sub");
                    return CommandBase.getListOfStringsMatchingLastWord(strings, MinecraftServer.getServer().getAllUsernames());
                } else {
                    Log.info("not player");
                    Log.info(":%s:", strings[0]);
                }
                break;
        }
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        FMLLog.info("%s|%s|%s", StringUtils.join(strings, ", "), strings[i], i);
        return strings[0].equals("player") && i == 1;
    }
}
