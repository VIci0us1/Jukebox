package com.example.jukeboxui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;

/**
 * Puts a chosen disc into the jukebox (which starts it playing) and tells the player.
 */
public final class JukeboxController {

    private JukeboxController() {
    }

    public static void playDisc(ServerLevel level, BlockPos pos, ItemStack disc, ServerPlayer player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof JukeboxBlockEntity jukebox)) {
            player.sendSystemMessage(Component.literal("\u00a7cThat jukebox is no longer there."));
            return;
        }

        // Swap out whatever was playing, then insert the new disc.
        if (!jukebox.getTheItem().isEmpty()) {
            jukebox.setTheItem(ItemStack.EMPTY);
        }
        jukebox.setTheItem(disc);

        String discName = disc.getHoverName().getString();
        player.sendSystemMessage(Component.literal("\u00a7aNow playing: \u00a7f" + discName));
        JukeboxUiMod.LOGGER.info("Player '{}' played '{}' in jukebox at {}",
                player.getName().getString(), discName, pos.toShortString());
    }
}
