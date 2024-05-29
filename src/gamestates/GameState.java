package gamestates;

public enum GameState { // special "class" represents a group of constants (unchangeable variables, like final variables)
                        // but there's more functionality to enums as well

    PLAYGAME, MENU, OPTIONS, QUIT;         // constants for game states

    // first game state that's called is MENU
    public static GameState state = MENU;
}
