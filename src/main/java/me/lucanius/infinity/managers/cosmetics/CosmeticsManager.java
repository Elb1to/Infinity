package me.lucanius.infinity.managers.cosmetics;

import lombok.Getter;
import me.lucanius.infinity.Infinity;
import me.lucanius.infinity.managers.cosmetics.type.Difficulty;
import me.lucanius.infinity.managers.cosmetics.type.PlayerBlock;
import me.lucanius.infinity.managers.cosmetics.type.PlayerMelee;
import me.lucanius.infinity.utils.ConfigFile;
import me.lucanius.infinity.utils.ItemBuilder;
import me.lucanius.infinity.utils.ItemUtil;
import me.lucanius.infinity.utils.ManagerUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 22/01/2022 at 16:42
 */
@Getter
public class CosmeticsManager extends ManagerUtil {

    private final ConfigFile config = this.plugin.getCosmeticsConfig();

    private final List<PlayerBlock> blocks = new ArrayList<>();
    private final List<PlayerMelee> melees = new ArrayList<>();
    private final List<Difficulty> difficulties = new ArrayList<>();

    public CosmeticsManager(Infinity plugin) {
        super(plugin);
        this.loadAll();
    }

    public PlayerBlock getBlockByName(String name) {
        return this.blocks.stream().filter(difficulty -> difficulty.getName().equalsIgnoreCase(name)).findFirst().orElse(this.blocks.get(0));
    }

    public Difficulty getDifficultyByName(String name) {
        return this.difficulties.stream().filter(difficulty -> difficulty.getName().equalsIgnoreCase(name)).findFirst().orElse(this.difficulties.get(0));
    }

    public PlayerMelee getMeleeByName(String name) {
        return this.melees.stream().filter(melee -> melee.getName().equalsIgnoreCase(name)).findFirst().orElse(this.melees.get(0));
    }

    private void loadAll() {
        this.loadBlocks();
        this.loadMelees();
        this.loadDifficulties();
    }

    private void loadBlocks() {
        this.config.getConfigurationSection("BLOCKS").getKeys(false).forEach(string -> {
            String root = "BLOCKS." + string + ".";

            PlayerBlock block = new PlayerBlock(
                    string,
                    this.config.getString(root + "DISPLAY-NAME"),
                    new ItemBuilder(
                            Material.valueOf(this.config.getString(root + "USEABLE-ITEM.MATERIAL")))
                            .durability(this.config.getInt(root + "USEABLE-ITEM.DATA")).amount(64).build(),
                    ItemUtil.createItem(
                            Material.valueOf(this.config.getString(root + "DISPLAY-ITEM.MATERIAL")),
                            this.config.getInt(root + "DISPLAY-ITEM.DATA"),
                            this.config.getString(root + "DISPLAY-ITEM.NAME"),
                            this.config.getStringList(root + "DISPLAY-ITEM.LORE")),
                    this.config.getString(root + "PERMISSION")
            );

            this.blocks.add(block);
        });
    }

    private void loadMelees() {
        this.config.getConfigurationSection("MELEES").getKeys(false).forEach(string -> {
            String root = "MELEES." + string + ".";

            PlayerMelee melee = new PlayerMelee(
                    string,
                    this.config.getString(root + "DISPLAY-NAME"),
                    new ItemBuilder(
                            Material.valueOf(this.config.getString(root + "USEABLE-ITEM.MATERIAL")))
                            .durability(this.config.getInt(root + "USEABLE-ITEM.DATA")).amount(1).build(),
                    ItemUtil.createItem(
                            Material.valueOf(this.config.getString(root + "DISPLAY-ITEM.MATERIAL")),
                            this.config.getInt(root + "DISPLAY-ITEM.DATA"),
                            this.config.getString(root + "DISPLAY-ITEM.NAME"),
                            this.config.getStringList(root + "DISPLAY-ITEM.LORE")),
                    this.config.getString(root + "PERMISSION")
            );

            this.melees.add(melee);
        });
    }

    private void loadDifficulties() {
        this.config.getConfigurationSection("DIFFICULTIES").getKeys(false).forEach(string -> {
            String root = "DIFFICULTIES." + string + ".";

            Difficulty difficulty = new Difficulty(
                    string,
                    this.config.getString(root + "DISPLAY-NAME"),
                    ItemUtil.createItem(
                            Material.valueOf(this.config.getString(root + "DISPLAY-ITEM.MATERIAL")),
                            this.config.getInt(root + "DISPLAY-ITEM.DATA"),
                            this.config.getString(root + "DISPLAY-ITEM.NAME"),
                            this.config.getStringList(root + "DISPLAY-ITEM.LORE")),
                    this.config.getInt(root + "MENU-SLOT"),
                    this.config.getDouble(root + "HORIZONTAL"),
                    this.config.getDouble(root + "VERTICAL"),
                    this.config.getString("PERMISSION")
            );

            this.difficulties.add(difficulty);
        });
    }
}
