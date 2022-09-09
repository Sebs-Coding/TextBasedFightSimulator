package FightSimulator;

public class Fight {
    private static final String INVALID_SELECTION_MESSAGE = "\nINVALID SELECTION! Please try again.\n";

    private final Player player1;
    private final Player player2;

    private boolean concluded;
    private Player winner = null;

    private Player attackingPlayer;
    private Player defendingPlayer;

    public Fight(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        attackingPlayer = this.player1;
        defendingPlayer = this.player2;
    }

    public boolean hasWinner() {
        return winner != null;
    }

    public Player getWinner() {
        return winner;
    }

    public void doFight() {
        printPrefightInfo();
        Input.waitForInput();
        printFightIntro();
        while (!concluded) {
            doTurn();
            updatePlayerStatus();
            if (concluded)
                break;
            showFightInfo();
            swapPlayerRoles();
            Input.waitForInput();
        }
    }

    private void doTurn() {
        System.out.println("\n\t\t===== " + attackingPlayer.getName().toUpperCase() + "'S TURN =====\n");
        Choice validChoice = getValidAttackingPlayerChoice();
        if (validChoice == Choice.EAT)
            doAttackingPlayerSingleEat();
        else if (validChoice == Choice.COMBO_EAT)
            doAttackingPlayerComboEat();
        else
            doAttackingPlayerHit();
    }

    private Choice getValidAttackingPlayerChoice() {
        Choice validChoice = null;
        while (validChoice == null) {
            presentValidChoicesForAttacker();
            String choice = Input.readLine();
            if (choice.equalsIgnoreCase("eat") && attackingPlayer.hasSingleFood())
                validChoice = Choice.EAT;
            else if (choice.equalsIgnoreCase("combo eat") && attackingPlayer.hasComboFood())
                validChoice = Choice.COMBO_EAT;
            else if (choice.equalsIgnoreCase("hit"))
                validChoice = Choice.HIT;
            else
                System.out.println(INVALID_SELECTION_MESSAGE);
        }
        return validChoice;
    }

    private void presentValidChoicesForAttacker() {
        System.out.println("You currently have " + attackingPlayer.getCurrentHp() + " HP.");
        System.out.print("You have " + attackingPlayer.getSingleEatFoodAmount() + " sharks remaining. ");
        if (attackingPlayer.hasSingleFood())
            System.out.println("You may [EAT] on this turn.");
        else
            System.out.println("You have ran out of sharks");

        System.out.print("You have " + attackingPlayer.getComboEatFoodAmount() + " combo food remaining. ");
        if (attackingPlayer.hasComboFood())
            System.out.println("You may [COMBO EAT] on this turn.");
        else
            System.out.println("You have ran out of combo food");

        System.out.println("You may attempt to [HIT] " + defendingPlayer.getName() + " on this turn.");
        System.out.print("\nWhat would you like to do?: ");
    }

    private void doAttackingPlayerSingleEat() {
        attackingPlayer.heal(Player.SINGLE_EAT_HEAL);
        attackingPlayer.minusSingleFood();
        System.out.println(attackingPlayer.getName() + " ate a shark. They now have " + attackingPlayer.getCurrentHp() + " hitpoints.\n");
    }

    private void doAttackingPlayerComboEat() {
        attackingPlayer.heal(Player.COMBO_EAT_HEAL);
        attackingPlayer.minusComboFood();
        System.out.println(attackingPlayer.getName() + " combo ate. They now have " + attackingPlayer.getCurrentHp() + " hitpoints.\n");
    }

    private void doAttackingPlayerHit() {
        System.out.println(attackingPlayer.getName() + " attempts to hit " + defendingPlayer.getName() + "!");
        if (!isAttackerHitSuccessful()) {
            System.out.println(attackingPlayer.getName() + " has missed!\n");
            return;
        }
        int damage = calculateHitDamage();
        System.out.println(attackingPlayer.getName() + " hits " + defendingPlayer.getName() + " for " + damage + " damage!\n");
        defendingPlayer.takeDamage(damage);
    }

    private boolean isAttackerHitSuccessful() {
        double attackRoll = Math.random() * (attackingPlayer.getAttack() * Player.PLAYER_ATTACK_MODIFIER);
        double defenceRoll = Math.random() * (defendingPlayer.getDefence());
        if (defenceRoll >= attackRoll)
            return false;
        else
            return true;
    }

    private int calculateHitDamage() {
        double damageRoll = Math.floor(Math.random() * attackingPlayer.getStrength());
        if (damageRoll >= Player.PLAYER_MAX_HIT)
            damageRoll = Player.PLAYER_MAX_HIT;
        int damage = (int) damageRoll;
        if (damage < 0)
            damage = 0;
        return damage;
    }

    private void updatePlayerStatus() {
        if (defendingPlayer.isDead()) {
            System.out.println("\n===============================");
            System.out.println(defendingPlayer.getName() + " has died! Game over!");
            System.out.println(attackingPlayer.getName() + " is the winner of the fight!");
            System.out.println("===============================");
            winner = attackingPlayer;
            concluded = true;
        }
    }

    private void showFightInfo() {
        if (!concluded) {
            System.out.println(player1.getName() + " currently has " + player1.getCurrentHp() + " hitpoints remaining");
            System.out.println(player2.getName() + " currently has " + player2.getCurrentHp() + " hitpoints remaining\n");
        }
    }

    private void swapPlayerRoles() {
        Player temp = attackingPlayer;
        attackingPlayer = defendingPlayer;
        defendingPlayer = temp;
    }

    private void printFightIntro() {
        System.out.println("\n " + attackingPlayer.getName() + " goes first, good luck!");
    }

    private static void printPrefightInfo() {
        System.out.println("\t\tYou start with 10 sharks and 4 combo food\n\t\tEach turn you can either [EAT], [COMBO EAT] or [HIT]\n");
    }
}
