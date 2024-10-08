package me.wiceh.companies.objects;

import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class HelpCommand {

    private final String label;
    private final List<Command> commands;

    public HelpCommand(String label, List<Command> commands) {
        this.label = label;
        this.commands = commands;
    }

    public void send(Player player) {
        player.sendMessage(text(""));
        player.sendMessage(text("§6ⓘ /" + label));
        player.sendMessage(text(""));
        for (Command command : commands) {
            if (command.getPermission() != null && !player.hasPermission(command.getPermission())) continue;
            player.sendMessage(text("§6» /" + label + " §e" + command.getUsage()).hoverEvent(HoverEvent.showText(text("§e§o" + command.getDescription()))).clickEvent(ClickEvent.suggestCommand("/" + label + " " + command.getUsage())));
        }
        player.sendMessage(text(""));
    }

    public String getLabel() {
        return label;
    }

    public List<Command> getCommands() {
        return commands;
    }
}
