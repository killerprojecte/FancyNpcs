package de.oliver.commands;

import de.oliver.Npc;
import de.oliver.NpcPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class NpcCMD implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 1){
            return Arrays.asList("create", "delete", "skin", "movehere");
        } else if(args.length == 2 && !args[0].equalsIgnoreCase("create")){
            return NpcPlugin.getInstance().getNpcManager().getAllNpcs().stream().map(Npc::getName).toList();
        }

        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player p)){
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Only players can execute this command</red>"));
            return false;
        }

        /*
            /npc create <name>
            /npc delete <name>
            /npc skin <name> <skin>
            /npc movehere <name>

         */

        if (args.length < 2){
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
            return false;
        }

        String subcommand = args[0];
        String name = args[1];

        switch (subcommand.toLowerCase()){
            case "create" -> {
                Npc npc = new Npc(name, p.getLocation());
                npc.spawnForAll();
                NpcPlugin.getInstance().getNpcManager().registerNpc(npc);
            }

            default -> {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                return false;
            }
        }

        return false;
    }
}
