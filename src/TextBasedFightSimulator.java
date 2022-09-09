package FightSimulator;
/**
 *This Java program allows 2 players to fight, which is turn based, using text commands.
 * Players have the choice of three options when fighting. Once a players' health hits
 * 0, they lose the fight.
 */

public class TextBasedFightSimulator {
    private static final String WELCOME_PLAYER_1 = "\nWelcome Player One!";
    private static final String WELCOME_PLAYER_2 = "\nWelcome Player Two!";

    public static void main(String[] args) {
        System.out.println(WELCOME_PLAYER_1);
        Player player1 = Player.createPlayer();
        player1.printStats();
        Input.waitForInput();
        System.out.println(WELCOME_PLAYER_2);
        Player player2 = Player.createPlayer();
        player2.printStats();
        Input.waitForInput();

        Fight fight = new Fight(player1, player2);
        fight.doFight();
    }
}
