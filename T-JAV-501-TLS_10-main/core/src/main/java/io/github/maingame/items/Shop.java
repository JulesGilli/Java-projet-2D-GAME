package io.github.maingame.items;

import com.badlogic.gdx.Game;
import io.github.maingame.entities.Player;
import io.github.maingame.core.GameStat;

import java.util.ArrayList;
import java.util.List;


public class Shop {
    private final List<Item> items = new ArrayList<>();
    private final Player player;
    private GameStat gameStat;

    public Shop(GameStat gameStat, Player player) {
        this.player = player;
        this.gameStat = gameStat;

        for (int i = 1; i < 6; i++) {
            items.add(new Weapon(i));
        }
        for (int i = 1; i < 4; i++) {
            items.add(new Armor(i));
        }
        items.add(new SpeedPotion(gameStat));
        items.add(new HealPotion(gameStat));
        items.add(new StrengthPotion(gameStat));
        items.add(new ArmorPotion(gameStat));
        this.gameStat = gameStat;
    }

    public boolean isAvailable(Item item) {
        // Débogage : Vérification de l'état de l'inventaire
        if (player.getInventory().getItems().isEmpty()) {
            System.out.println("Debug: L'inventaire du joueur est vide.");
        } else {
            System.out.println("Debug: L'inventaire contient des objets :");
            for (Item inventoryItem : player.getInventory().getItems()) {
                System.out.println("- " + inventoryItem.getClass().getSimpleName() + " (gold: " + inventoryItem.getGold() + ")");
            }
        }

        // Vérification classique de la disponibilité
        if (gameStat.getGolds() < item.getGold()) {
            System.out.println("Debug: Pas assez de gold pour acheter " + item.getClass().getSimpleName());
            return false;
        }
        if (item instanceof Weapon) {
            boolean hasWeapon = player.getInventory().containWeapon();
            System.out.println("Debug: Le joueur " + (hasWeapon ? "possède déjà" : "ne possède pas") + " une arme.");
            return !hasWeapon;
        }
        if (item instanceof Armor) {
            boolean hasArmor = player.getInventory().containArmor();
            System.out.println("Debug: Le joueur " + (hasArmor ? "possède déjà" : "ne possède pas") + " une armure.");
            return !hasArmor;
        }
        if (item instanceof Consumable) {
            boolean hasConsumable = player.getInventory().containConsumable();
            System.out.println("Debug: Le joueur " + (hasConsumable ? "possède déjà" : "ne possède pas") + " un consommable.");
            return !hasConsumable;
        }

        // Retourne vrai si aucune condition spécifique ne bloque l'achat
        return true;
    }


    public boolean buyItem(GameStat stat, Item item) {
        if (player == null) {
            throw new IllegalStateException("Le joueur n'a pas été initialisé correctement.");
        }

        if (item.isUnlocked(stat) && isAvailable(item)) {
            stat.setGolds(stat.getGolds() - item.gold);
            player.getInventory().addItem(item);
            System.out.println("item acheté");
            return true;
        } else {
            System.out.println("achat impossible");
            return false;
        }
    }

    public String unlockCondition(GameStat stat, Item item){
        return "in " + (item.unlockFloor - stat.getMaxFloors()) + " floors";
    }

    public List<Item> getItems() {
        return items;
    }
}
