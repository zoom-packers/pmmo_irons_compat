package net.silvertide.pmmo_spellbooks_compat.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.silvertide.pmmo_spellbooks_compat.config.writers.PackGenerator;

public class CmdPmmoSpellBooksRoot {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("pmmo_irons_compat")
            .then(Commands.literal("genSpellData")
                .requires(ctx -> ctx.hasPermission(2))
                .executes(ctx -> PackGenerator.generatePack(ctx.getSource().getServer())))
            );
    }
}
