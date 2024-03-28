package entities;

import gamestates.PlayGame;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {

    private PlayGame playGame;
    private BufferedImage[][] monsterArray;
    private ArrayList<Monster> monsters = new ArrayList<>();

    public EnemyManager(PlayGame playGame) {
        this.playGame = playGame;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        monsters = LoadSave.getMonsters();
        System.out.println("how many monsters in game: " + monsters.size());
    }

    public void update() {
        for (Monster monster : monsters) {
            monster.update();
        }
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawMonsters(g, xLevelOffset);
    }

    private void drawMonsters(Graphics g, int xLevelOffset) {
        for (Monster monster : monsters) {
            g.drawImage(monsterArray[monster.getEnemyState()][monster.getAnimationIndex()],
                    (int) monster.getHitbox().x - xLevelOffset, (int) monster.getHitbox().y, MONSTER_WIDTH, MONSTER_HEIGHT, null);
        }
    }

    private void loadEnemyImages() {
        monsterArray = new BufferedImage[4][7];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.MONSTER_SPRITES);

        for (int y = 0; y < monsterArray.length; y++) {
            for (int x = 0; x < monsterArray[y].length; x++) {
                monsterArray[y][x] = temp.getSubimage(x * MONSTER_DEFAULT_WIDTH, y * MONSTER_DEFAULT_HEIGHT, MONSTER_DEFAULT_WIDTH, MONSTER_DEFAULT_HEIGHT);
            }

        }
    }

}
