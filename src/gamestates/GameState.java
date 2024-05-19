package gamestates;

public enum GameState { // special "class" represents a group of constants (unchangeable variables, like final variables)
                        // but there's more functionality to enums as well

    PLAYGAME, MENU, OPTIONS, QUIT;         // constants for game states

    public static GameState state = MENU;
}
