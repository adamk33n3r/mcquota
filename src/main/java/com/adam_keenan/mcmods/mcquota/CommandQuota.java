package com.adam_keenan.mcmods.mcquota;

import com.adam_keenan.mcmods.mcquota.quota.QuotaDBManager;
import com.adam_keenan.mcmods.mcquota.utils.Config;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import com.adam_keenan.mcmods.mcquota.utils.Utils;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
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
    public void processCommand(ICommandSender iCommandSender, String[] args) {
        if (iCommandSender instanceof EntityPlayer) {
            Log.info("Player sent command");
        } else {
            Log.info("not player");
        }
        Log.info("Command received! Args: " + StringUtils.join(args, ", "));
        if (args.length == 0) {
            iCommandSender.addChatMessage(new ChatComponentText("Usage: /mcquota <global | player <player>> [quota]"));
        } else {
            try {
                if (args[0].equals("player")) {
                    Log.info("player sub");
                    if (args.length > 1) {
                        String uuid = null;
                        try {
                            uuid = Utils.playerToUUID(CommandBase.getPlayer(iCommandSender, args[1]));
                        } catch (PlayerNotFoundException e) {
                            Log.info("Player not found. Looking in cache");
                            Map<UUID, String> userMap = UsernameCache.getMap();
                            for (Map.Entry<UUID, String> entry : userMap.entrySet()) {
                                if (entry.getValue().equals(args[1])) {
                                    uuid = entry.getKey().toString();
                                    Log.info("Found in cache");
                                    break;
                                }
                            }
                            if (uuid == null) {
                                throw e;
                            }
                        }
                        if (args.length == 2) {
                            int quota = QuotaDBManager.getInstance().getPlayerQuota(uuid);
                            int timePlayed = QuotaDBManager.getInstance().getTimeSpent(uuid);
                            iCommandSender.addChatMessage(new ChatComponentText(String.format("Quota is set to %d", quota)));
                            iCommandSender.addChatMessage(new ChatComponentText(String.format("Time played today is %d", timePlayed)));
                        } else if (args.length == 3) {
                            iCommandSender.addChatMessage(new ChatComponentText("/mcquota player <player> quota|time <number>"));
                        } else if (args.length == 4) {
                            if (args[2].equals("quota")) {
                                if (args[3].equals("off")) {
                                    iCommandSender.addChatMessage(new ChatComponentText("Quota turned off"));
                                    QuotaDBManager.getInstance().setPlayerQuota(uuid, -1);
                                } else {
                                    int quota = Integer.parseInt(args[3]);
                                    QuotaDBManager.getInstance().setPlayerQuota(uuid, quota);
                                    iCommandSender.addChatMessage(new ChatComponentText(String.format("Quota set to %d", quota)));
                                }
                            } else if (args[2].equals("time")) {
                                QuotaDBManager.getInstance().setTimeSpent(uuid, Integer.parseInt(args[3]));
                            }
                        }
                    }
                } else if (args[0].equals("global")) {
                    if (args.length >= 2) {
                        if (args[1].equals("off")) {
                            Config.globalEnabled.set(false);
                            iCommandSender.addChatMessage(new ChatComponentText(String.format("Global check set to " + Config.globalEnabled.getBoolean())));
                        } else if (args[1].equals("on")) {
                            Config.globalEnabled.set(true);
                            iCommandSender.addChatMessage(new ChatComponentText(String.format("Global check set to " + Config.globalEnabled.getBoolean())));
                        } else {
                            Config.quotaLength.set(Integer.parseInt(args[1]));
                            iCommandSender.addChatMessage(new ChatComponentText(String.format("Global quota set to " + Config.quotaLength.getInt())));
                        }
                        Config.save();
                    } else {
                        iCommandSender.addChatMessage(new ChatComponentText(String.format("Global check set to " + Config.globalEnabled.getBoolean())));
                        iCommandSender.addChatMessage(new ChatComponentText(String.format("Global quota is set to " + Config.quotaLength.getInt())));
                    }
                } else {
                    iCommandSender.addChatMessage(new ChatComponentText("Unknown command"));
                }
            } catch (NumberFormatException e) {
                iCommandSender.addChatMessage(new ChatComponentText("Could not parse number."));
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
        Log.info("%s|%s|%s", StringUtils.join(strings, ", "), strings[i], i);
        return strings[0].equals("player") && i == 1;
    }
}
