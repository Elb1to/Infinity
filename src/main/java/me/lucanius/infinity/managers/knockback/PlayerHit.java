package me.lucanius.infinity.managers.knockback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by Lucanius
 * Project: Infinity
 * Created on 20/01/2022 at 22:58
 */
@Getter
@Setter
@AllArgsConstructor
public class PlayerHit {

    private Vector direction;
    private boolean sprinting;
    private int knockbackLevel;
    private Player attacker;
}
