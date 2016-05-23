package com.gmail.andrewahughes.TroopTD;

/**
 * Created by andy on 23/05/2016.
 */
public class Weapon {

    int damage;
    int penetration;
    int splash;
    int delayBetweenShots;
    int ammo;
    int reload;
    int autoReload;
    public Weapon(int damage,int penetration,int splash,int delayBetweenShots,int ammo, int reload,int autoReload)
    {
        this.damage=damage;
        this.penetration=penetration;
        this.splash=splash;
        this.delayBetweenShots=delayBetweenShots;
        this.ammo=ammo;
        this.reload=reload;
        this.autoReload=autoReload;
    }

}
