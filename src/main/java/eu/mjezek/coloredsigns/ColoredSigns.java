package eu.mjezek.coloredsigns;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ColoredSigns extends JavaPlugin implements Listener {
    private final Map<Byte, ChatColor> dyeColorChatColorMap = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initializeMap();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock.getType() != Material.SIGN && clickedBlock.getType() != Material.SIGN_POST && clickedBlock.getType() != Material.WALL_SIGN)
            return;

        Player p = event.getPlayer();
        if (!p.hasPermission("coloredsigns.use"))
            return;

        ItemStack itemStack = p.getItemInHand();
        if (itemStack == null || itemStack.getType() != Material.INK_SACK)
            return;

        @SuppressWarnings("deprecation")
        byte itemStackData = itemStack.getData().getData();
        ChatColor chatColor = dyeColorChatColorMap.get(itemStackData);
        if (chatColor == null)
            return;

        if (getConfig().getBoolean("removeDye") && p.getGameMode() != GameMode.CREATIVE) {
            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(itemStack.getAmount() - 1);
            } else {
                p.setItemInHand(null);
            }
        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        for (int line = 0; line <= 3; line++) {
            sign.setLine(line, chatColor + ChatColor.stripColor(sign.getLine(line)));
        }

        sign.update();
    }

    @SuppressWarnings("deprecation")
    private void initializeMap() {
        dyeColorChatColorMap.put(DyeColor.BLACK.getDyeData(), ChatColor.BLACK);
        dyeColorChatColorMap.put(DyeColor.BLUE.getDyeData(), ChatColor.BLUE);
        dyeColorChatColorMap.put(DyeColor.PINK.getDyeData(), ChatColor.RED);
        dyeColorChatColorMap.put(DyeColor.YELLOW.getDyeData(), ChatColor.YELLOW);
        dyeColorChatColorMap.put(DyeColor.ORANGE.getDyeData(), ChatColor.GOLD);
        dyeColorChatColorMap.put(DyeColor.LIGHT_BLUE.getDyeData(), ChatColor.AQUA);
        dyeColorChatColorMap.put(DyeColor.GREEN.getDyeData(), ChatColor.DARK_GREEN);
        dyeColorChatColorMap.put(DyeColor.LIME.getDyeData(), ChatColor.GREEN);
        dyeColorChatColorMap.put(DyeColor.CYAN.getDyeData(), ChatColor.DARK_AQUA);
        dyeColorChatColorMap.put(DyeColor.GRAY.getDyeData(), ChatColor.DARK_GRAY);
        dyeColorChatColorMap.put(DyeColor.SILVER.getDyeData(), ChatColor.GRAY);
        dyeColorChatColorMap.put(DyeColor.WHITE.getDyeData(), ChatColor.WHITE);
        dyeColorChatColorMap.put(DyeColor.PURPLE.getDyeData(), ChatColor.DARK_PURPLE);
        dyeColorChatColorMap.put(DyeColor.MAGENTA.getDyeData(), ChatColor.LIGHT_PURPLE);
        dyeColorChatColorMap.put(DyeColor.RED.getDyeData(), ChatColor.DARK_RED);
    }
}
