package com.gb6.duels.managers;

import com.gb6.duels.commands.ArenaCommand;
import com.gb6.duels.commands.HelpCommand;
import com.gb6.duels.commands.KitsCommand;
import com.gb6.duels.objects.Command;
import com.gb6.duels.objects.Message;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.gb6.duels.enums.Requirement.*;
import static com.gb6.duels.utils.Constants.COMMAND_LIST;


public class CommandManager implements CommandExecutor {

    public CommandManager() {
        COMMAND_LIST.add(new HelpCommand());
        COMMAND_LIST.add(new ArenaCommand());
        COMMAND_LIST.add(new KitsCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        for (Command cmd : COMMAND_LIST) {
            List<String> output = new LinkedList<>(Arrays.asList(args));

            if (output.size() > 0) {
                output.remove(0);
            }
            if (args.length == 0 && cmd.getClass() == HelpCommand.class) {
                cmd.execute(sender, output);
                return true;
            }

            if (!args[0].equalsIgnoreCase(cmd.getSyntax())) {
                continue;
            }

            Boolean isPlayer = sender instanceof Player;
            Boolean isConsole = sender instanceof ConsoleCommandSender;

            if (!isConsole && cmd.getRequirement() == CONSOLE) {
                new Message("console-only").send(sender);
                return true;
            }

            if (!isPlayer && cmd.getRequirement() == PLAYER) {
                new Message("player-only").send(sender);
                return true;
            }

            if (!sender.isOp() && !sender.hasPermission(cmd.getPermission())) {
                new Message("no-permission").send(sender);
                return true;
            }
            cmd.execute(sender, output);
            return true;
        }
        new Message("unknown-command").send(sender);
        return true;
    }
}

