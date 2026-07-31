package com.example.jukeboxui;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds and opens a chest-style menu listing every music disc; clicking one plays
 * it in the target jukebox.
 */
public final class DiscMenu {

    private DiscMenu() {
    }

    public static void open(ServerPlayer player, ServerLevel level, BlockPos jukeboxPos) {
        List<ItemStack> discs = allDiscs();
        int rows = Math.min(6, Math.max(1, (discs.size() + 8) / 9));
        int size = rows * 9;

        SimpleContainer container = new SimpleContainer(size);
        for (int i = 0; i < discs.size() && i < size; i++) {
            container.setItem(i, discs.get(i));
        }

        player.openMenu(new SimpleMenuProvider(
                (syncId, inv, p) -> new DiscSelectMenu(syncId, inv, container, rows, level, jukeboxPos),
                Component.literal("Select a Music Disc")));
    }

    /** Every registered item that carries the jukebox-playable component (i.e. all discs). */
    private static List<ItemStack> allDiscs() {
        List<ItemStack> result = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack stack = new ItemStack(item);
            if (stack.has(DataComponents.JUKEBOX_PLAYABLE)) {
                result.add(stack);
            }
        }
        return result;
    }

    /**
     * A read-only chest menu: clicking a disc plays it instead of picking it up.
     */
    public static class DiscSelectMenu extends ChestMenu {

        private final ServerLevel level;
        private final BlockPos jukeboxPos;
        private final Container discContainer;

        public DiscSelectMenu(int syncId, Inventory inv, Container container, int rows,
                              ServerLevel level, BlockPos jukeboxPos) {
            super(menuTypeForRows(rows), syncId, inv, container, rows);
            this.level = level;
            this.jukeboxPos = jukeboxPos;
            this.discContainer = container;
        }

        private static MenuType<ChestMenu> menuTypeForRows(int rows) {
            return switch (rows) {
                case 1 -> MenuType.GENERIC_9x1;
                case 2 -> MenuType.GENERIC_9x2;
                case 3 -> MenuType.GENERIC_9x3;
                case 4 -> MenuType.GENERIC_9x4;
                case 5 -> MenuType.GENERIC_9x5;
                default -> MenuType.GENERIC_9x6;
            };
        }

        @Override
        public void clicked(int slotId, int button, ContainerInput input, Player player) {
            if (slotId >= 0 && slotId < discContainer.getContainerSize() && player instanceof ServerPlayer sp) {
                ItemStack clicked = discContainer.getItem(slotId);
                if (!clicked.isEmpty() && clicked.has(DataComponents.JUKEBOX_PLAYABLE)) {
                    JukeboxController.playDisc(level, jukeboxPos, clicked.copyWithCount(1), sp);
                    sp.closeContainer();
                }
            }
            // Deliberately swallow clicks: this menu is a picker, not a real inventory.
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }
    }
}
