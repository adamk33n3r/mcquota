package com.adam_keenan.mcmods.mcquota;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

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
        return "mcquota <player> <time>";
    }

    @Override
    public List getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        if (iCommandSender instanceof EntityPlayer) {
            System.out.println("Player sent command");
        }
        iCommandSender.addChatMessage(new ChatComponentText("hello world"));
        if (strings.length == 0) {
            System.out.println("Invalid command!");
            return;
        }
        FMLLog.info("Command received! Args: " + StringUtils.join(strings, ", "));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        // Make sure OP
        return iCommandSender.canCommandSenderUseCommand(2, "");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender iCommandSender, String[] strings) {
        FMLLog.info("%s", StringUtils.join(strings, ", "));
        switch (strings.length) {
            case 1:
                return CommandBase.getListOfStringsMatchingLastWord(strings, this.subcommands);
            case 2:
                if (strings[0].equals("player")) {
                    FMLLog.info("player sub");
                    return CommandBase.getListOfStringsMatchingLastWord(strings, MinecraftServer.getServer().getAllUsernames());
                } else {
                    FMLLog.info("not player");
                    FMLLog.info(":%s:", strings[0]);
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
