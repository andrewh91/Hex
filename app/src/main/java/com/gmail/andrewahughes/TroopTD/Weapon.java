package com.gmail.andrewahughes.TroopTD;

/**
 * Created by andy on 23/05/2016.
 */
public class Weapon {

    int ammoType;
    int damage;
    int penetration;
    int splash;
    int cone;
    int delayBetweenShots;
    int ammo;
    int reload;
    int autoReload;
    int range;
    public Weapon(int ammoType,int damage,int penetration,int splash,int cone,int delayBetweenShots,int ammo, int reload,int autoReload,int range)
    {
        this.ammoType=ammoType;
        this.damage=damage;
        this.penetration=penetration;
        this.splash=splash;
        this.cone=cone;
        this.delayBetweenShots=delayBetweenShots;
        this.ammo=ammo;
        this.reload=reload;
        this.autoReload=autoReload;
        this.range=range;
    }

}
