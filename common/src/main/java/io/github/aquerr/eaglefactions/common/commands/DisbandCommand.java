package io.github.aquerr.eaglefactions.common.commands;

import io.github.aquerr.eaglefactions.api.EagleFactions;
import io.github.aquerr.eaglefactions.api.entities.Faction;
import io.github.aquerr.eaglefactions.common.EagleFactionsPlugin;
import io.github.aquerr.eaglefactions.common.PluginInfo;
import io.github.aquerr.eaglefactions.common.messaging.MessageLoader;
import io.github.aquerr.eaglefactions.common.messaging.Messages;
import io.github.aquerr.eaglefactions.common.messaging.Placeholders;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class DisbandCommand extends AbstractCommand
{
    public DisbandCommand(EagleFactions plugin)
    {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException
    {
        if(!(source instanceof Player))
            throw new CommandException(Text.of(PluginInfo.ERROR_PREFIX, TextColors.RED, Messages.ONLY_IN_GAME_PLAYERS_CAN_USE_THIS_COMMAND));

        final Player player = (Player) source;

        // There's a bit of code duplicating, but...
        Optional<Faction> faction = context.getOne("faction");
        if (faction.isPresent()) {
            if (!super.getPlugin().getPlayerManager().hasAdminMode(player))
                throw new CommandException(Text.of(PluginInfo.ERROR_PREFIX, TextColors.RED, Messages.YOU_NEED_TO_TOGGLE_FACTION_ADMIN_MODE_TO_DO_THIS));

            boolean didSucceed = super.getPlugin().getFactionLogic().disbandFaction(faction.get().getName());
            if (didSucceed) {
                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, Messages.FACTION_HAS_BEEN_DISBANDED));
            } else {
                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.RED, Messages.SOMETHING_WENT_WRONG));
            }
            return CommandResult.success();
        }

        Optional<Faction> optionalPlayerFaction = super.getPlugin().getFactionLogic().getFactionByPlayerUUID(player.getUniqueId());
        if(!optionalPlayerFaction.isPresent())
            throw new CommandException(Text.of(PluginInfo.ERROR_PREFIX, TextColors.RED, Messages.YOU_MUST_BE_IN_FACTION_IN_ORDER_TO_USE_THIS_COMMAND));

        Faction playerFaction = optionalPlayerFaction.get();

        if(playerFaction.isSafeZone() || playerFaction.isWarZone())
            throw new CommandException(Text.of(PluginInfo.ERROR_PREFIX, TextColors.RED, "This faction cannot be disbanded!"));

        //If player has adminmode
        if(super.getPlugin().getPlayerManager().hasAdminMode(player))
        {
            boolean didSucceed = super.getPlugin().getFactionLogic().disbandFaction(playerFaction.getName());
            if(didSucceed)
            {
                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, Messages.FACTION_HAS_BEEN_DISBANDED));
                EagleFactionsPlugin.AUTO_CLAIM_LIST.remove(player.getUniqueId());
                EagleFactionsPlugin.CHAT_LIST.remove(player.getUniqueId());
            }
            else
            {
                player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.RED, Messages.SOMETHING_WENT_WRONG));
            }
            return CommandResult.success();
        }

        //If player is leader
        if(!playerFaction.getLeader().equals(player.getUniqueId()))
            throw new CommandException(Text.of(PluginInfo.ERROR_PREFIX, Messages.YOU_MUST_BE_THE_FACTIONS_LEADER_TO_DO_THIS));

        boolean didSucceed = super.getPlugin().getFactionLogic().disbandFaction(playerFaction.getName());
        if(didSucceed)
        {
            player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, Messages.FACTION_HAS_BEEN_DISBANDED));
            EagleFactionsPlugin.AUTO_CLAIM_LIST.remove(player.getUniqueId());
            EagleFactionsPlugin.CHAT_LIST.remove(player.getUniqueId());
        }
        else
        {
            player.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.RED, Messages.SOMETHING_WENT_WRONG));
        }

        return CommandResult.success();
    }
}
