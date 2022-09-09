package FightSimulator;

import java.util.InputMismatchException;

class Player {
    public static final int STAT_MAX = 99;
    public static final int STAT_MIN = 1;
    public static final int SINGLE_EAT_HEAL = 20;
    public static final int COMBO_EAT_HEAL = 53;
    public static final double PLAYER_ATTACK_MODIFIER = 1.5;
    public static final int PLAYER_MAX_HIT = 60;
    public static final int DEFAULT_SINGLE_FOOD_AMOUNT = 10;
    public static final int DEFAULT_COMBO_FOOD_AMOUNT = 4;
    private static final int HP_STAT_DIVISION = 3;
    private static final int MAX_NAME_LENGTH = 12;
    private static final String GET_STAT_PART1 = "Please enter your ";
    private static final String GET_STAT_PART2 = " level (" + STAT_MIN + "-" + STAT_MAX + "): ";
    private static final String GET_NAME_REQUEST = "Please enter your name: ";
    private static final String INVALID_NAME_TOO_LONG = "Your name cannot be greater than " + MAX_NAME_LENGTH + " characters long, please try again.\n";
    private static final String INVALID_NAME_BLANK = "Name cannot be blank, please try again.\n";
    private static final String INVALID_STAT = "Invalid input. Enter a whole number between " + STAT_MIN + "-" + STAT_MAX + ". Please try again.\n";

    private String name;
    private int attack;
    private int strength;
    private int defence;
    private int maxHp;
    private int singleEatFoodAmount;
    private int comboEatFoodAmount;

    private int currentHp;
    private boolean dead = false;

    public Player(String name, int attack, int strength, int defence, int maxHp, int singleEatFoodAmount, int comboEatFoodAmount) {
        this.name = name;
        this.attack = attack;
        this.strength = strength;
        this.defence = defence;
        this.maxHp = maxHp;
        this.singleEatFoodAmount = singleEatFoodAmount;
        this.comboEatFoodAmount = comboEatFoodAmount;
        this.currentHp = this.maxHp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be blank");
        this.name = name;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) throws IllegalArgumentException {
        if (OutsideStatRange(attack))
            throw new IllegalArgumentException("Provided attack value outside of the allowed stat range");
        this.attack = attack;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) throws IllegalArgumentException {
        if (OutsideStatRange(strength))
            throw new IllegalArgumentException("Provided strength value outside of the allowed stat range");
        this.strength = strength;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) throws IllegalArgumentException {
        if (OutsideStatRange(defence))
            throw new IllegalArgumentException("Provided defence value outside of the allowed stat range");
        this.defence = defence;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        if (OutsideStatRange(maxHp))
            throw new IllegalArgumentException("Provided HP value outside of the allowed stat range");
        this.maxHp = maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) throws IllegalArgumentException {
        if (currentHp < 0)
            throw new IllegalArgumentException("Current HP cannot be negative");
        this.currentHp = currentHp;
    }

    public int getSingleEatFoodAmount() {
        return singleEatFoodAmount;
    }

    public void setSingleEatFoodAmount(int singleEatFoodAmount) {
        this.singleEatFoodAmount = singleEatFoodAmount;
    }

    public int getComboEatFoodAmount() {
        return comboEatFoodAmount;
    }

    public void setComboEatFoodAmount(int comboEatFoodAmount) {
        this.comboEatFoodAmount = comboEatFoodAmount;
    }

    public boolean hasSingleFood() {
        return singleEatFoodAmount > 0;
    }

    public void minusSingleFood() {
        if (hasSingleFood())
            singleEatFoodAmount--;
    }

    public boolean hasComboFood() {
        return comboEatFoodAmount > 0;
    }

    public void minusComboFood() {
        if (hasComboFood())
            comboEatFoodAmount--;
    }

    public boolean isFullHp() {
        return currentHp == maxHp;
    }

    public boolean isDead() {
        return dead;
    }

    public void killPlayer() {
        setCurrentHp(0);
        dead = true;
    }

    public void takeDamage(int damageAmount) {
        int remainingHP = currentHp - damageAmount;
        if (remainingHP <= 0)
            killPlayer();
        else
            currentHp = remainingHP;
    }

    public void heal(int healAmount) {
        int hp = currentHp + healAmount;
        if (hp > maxHp)
            hp = maxHp;
        setCurrentHp(hp);
    }

    public void printStats() {
        System.out.println("STATS FOR " + name.toUpperCase() + ": \n" +
                                attack + " ATTACK \n" +
                                strength + " STRENGTH \n" +
                                defence + " DEFENCE \n" +
                                maxHp + " HITPOINTS \n" );
    }

    private static boolean OutsideStatRange(int stat) {
        return (stat < STAT_MIN || stat > STAT_MAX);
    }

    public static Player createPlayer() {
        String name = getPlayerName();
        int attackLevel = getPlayerStat("Attack");
        int strengthLevel = getPlayerStat("Strength");
        int defenceLevel = getPlayerStat("Defence");
        int hitpointsLevel = calculateHitpoints(attackLevel, strengthLevel, defenceLevel);
        return new Player(name, attackLevel, strengthLevel, defenceLevel, hitpointsLevel,DEFAULT_SINGLE_FOOD_AMOUNT,DEFAULT_COMBO_FOOD_AMOUNT);
    }

    private static String getPlayerName() {
        String name;
        while (true) {
            System.out.print(GET_NAME_REQUEST);
            name = Input.readLine();
            if (name == null || name.isBlank())
                System.out.println(INVALID_NAME_BLANK);
            else if (name.length() > MAX_NAME_LENGTH)
                System.out.println(INVALID_NAME_TOO_LONG);
            else
                return name;
        }
    }

    private static int getPlayerStat(String statName) {
        int stat;
        while (true) {
            System.out.print(GET_STAT_PART1 + statName + GET_STAT_PART2);
            try {
                stat = Input.readInt();
            } catch (InputMismatchException e) {
                System.out.println(INVALID_STAT);
                continue;
            }
            if (OutsideStatRange(stat))
                System.out.println(INVALID_STAT);
            else
                return stat;
        }
    }

    private static int calculateHitpoints(int attackLevel, int strengthLevel, int defenceLevel) {
        return (attackLevel + strengthLevel + defenceLevel) / HP_STAT_DIVISION;
    }
}



