package com.justserver.apocalypse.dungeons.puzzles;


import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.gui.Gui;
import com.justserver.apocalypse.utils.ItemBuilder;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;
import java.util.ArrayList;

public class WhoLiesPuzzle extends Gui implements Puzzle {
    private ArrayList<ArrayList<Pair<String, Boolean>>> answers = new ArrayList<>();
    private final SecureRandom random = new SecureRandom();
    public WhoLiesPuzzle(Apocalypse plugin){

        int size = plugin.getConfig().getConfigurationSection("wholies").getKeys(false).size();
        for(int i = 0; i < size; i++){
            ArrayList<Pair<String, Boolean>> pairs = new ArrayList<>();
            String path = "wholies." + (i + 1);
            for(String question : plugin.getConfig().getConfigurationSection(path).getKeys(false)){
                pairs.add(new Pair<>(question, plugin.getConfig().getBoolean(path + "." + question)));
            }
            answers.add(pairs);
        }
        inventory = Bukkit.createInventory(null, 54, getName());
        ItemBuilder itemBuilder = new ItemBuilder(Material.PAPER).setName(ChatColor.GREEN + "Что нужно?");
        for(String loreLine : getLoreDescription()){
            itemBuilder.addLoreLine(loreLine);
        }
        inventory.addItem(itemBuilder.toItemStack());
    }

    @Override
    public String getName() {
        return "Головоломка \"" + getPuzzleName() + "\"";
    }

    @Override
    public Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) throws NoSuchFieldException, IllegalAccessException {

        return null;
    }

    @Override
    public Gui handleInventoryClick(InventoryClickEvent event, Player player, ItemStack itemStack, ClickType clickType) {
        return null;
    }

    @Override
    public String getPuzzleName() {
        return "Кто врет?";
    }

    @Override
    public String[] getLoreDescription() {
        return new String[] {
                ChatColor.GRAY + "У вас есть несколько показаний нескольких людей",
                ChatColor.GRAY + "но на вид все они говорят неправду",
                ChatColor.GRAY + "От вас требуется, чтобы вы нашли того, кто говорит",
                ChatColor.GREEN + "ПРАВДУ"
        };
    }
}
