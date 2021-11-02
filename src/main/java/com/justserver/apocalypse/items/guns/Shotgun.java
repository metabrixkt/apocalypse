package com.justserver.apocalypse.items.guns;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Gun;
import com.justserver.apocalypse.items.ItemRarity;

public class Shotgun extends Gun{
    public Shotgun(Apocalypse plugin) {
        super(plugin, true, 3, 10, 10, 5);
    }

    @Override
    public int getRechargeTime() {
        return 2;
    }

    @Override
    public String customName() {
        return "Дробовик";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public double getLeftDamage() {
        return 0;
    }

    @Override
    public int getSlowdown() {
        return 20;
    }

    @Override
    protected void init() {

    }

    @Override
    public boolean isTriple() {
        return true;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    // Фанни начни с пистолета пж пж
}
