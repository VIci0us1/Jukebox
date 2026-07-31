package com.example.jukeboxui;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jukebox UI — right-click a jukebox (empty-handed) to open a menu of every music
 * disc and click one to play it, without needing the disc in your inventory.
 * Entirely server-side; vanilla clients need nothing installed.
 */
public class JukeboxUiMod implements ModInitializer {

    public static final String MOD_ID = "jukeboxui";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register(JukeboxUiMod::onUseBlock);

        // Clear a virtually-inserted disc before the jukebox is broken so it doesn't
        // pop out as a real item (the disc was never actually in anyone's inventory).
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (world.isClientSide()) {
                return true;
            }
            if (blockEntity instanceof JukeboxBlockEntity jukebox && !jukebox.getTheItem().isEmpty()) {
                jukebox.setTheItem(ItemStack.EMPTY);
            }
            return true;
        });

        LOGGER.info("Jukebox UI loaded!");
    }

    private static InteractionResult onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (!(player instanceof ServerPlayer serverPlayer) || !(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }

        BlockPos pos = hit.getBlockPos();
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof JukeboxBlockEntity jukebox)) {
            return InteractionResult.PASS;
        }

        // If the player is holding a disc, let vanilla handle inserting it normally.
        ItemStack held = player.getItemInHand(hand);
        if (held.has(DataComponents.JUKEBOX_PLAYABLE)) {
            return InteractionResult.PASS;
        }

        // Empty hand on a playing jukebox: stop it.
        if (!jukebox.getTheItem().isEmpty() && held.isEmpty()) {
            jukebox.setTheItem(ItemStack.EMPTY);
            serverPlayer.sendSystemMessage(Component.literal("\u00a77Stopped the jukebox."));
            return InteractionResult.SUCCESS;
        }

        // Otherwise open the disc picker.
        DiscMenu.open(serverPlayer, serverLevel, pos);
        return InteractionResult.SUCCESS;
    }
}
