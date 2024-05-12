package entities;

import gamestates.PlayGame;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {

    private PlayGame playGame;

    private BufferedImage[][] monsterArray;
    private ArrayList<Monster> monsters = new ArrayList<>();

    public EnemyManager(PlayGame playGame) {
        this.playGame = playGame;
        loadEnemyImages();

    }

    public void loadEnemies(Level level) {
        monsters = level.getMonstaz();
        // System.out.println("that many monsters in game: " + monsters.size());

        // crabbies = level.getCrabs();
        // System.out.println("that many crabs in game: " + crabbies.size());
    }

    public void update(int[][] levelData, Player player) {

        boolean isAnyEnemyActive = false;

        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                monster.update(levelData, player);
                isAnyEnemyActive = true;
            }
        }

        if (!isAnyEnemyActive)
            playGame.setLevelComplete(true);

    }

    public void draw(Graphics g, int xLevelOffset) {
        drawMonsters(g, xLevelOffset);
    }

    private void drawMonsters(Graphics g, int xLevelOffset) {

        for (Monster monster : monsters) {
            if (monster.isAlive()) {

                // monster.drawHitbox(g, xLevelOffset);
                g.drawImage(monsterArray[monster.getState()][monster.getAnimationIndex()],
                        (int) monster.getHitbox().x - xLevelOffset - MONSTER_DRAWOFFSET_X + monster.flipX(),
                        (int) monster.getHitbox().y - MONSTER_DRAWOFFSET_Y,
                        MONSTER_DEFAULT_WIDTH * monster.flipWidth(), MONSTER_DEFAULT_HEIGHT, null);

                // monster.drawAttackBox(g, xLevelOffset);
            }
        }
    }


    public void checkIfEnemyWasHit(Rectangle2D.Float attackBox) {
        for (Monster monster : monsters) {
            if (monster.isAlive()) {

                if (attackBox.intersects(monster.getHitbox())) {
                    monster.receiveDamage(10);
                    return;
                }
            }
        }
    }

    private void loadEnemyImages() {

        monsterArray = new BufferedImage[4][7];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.MONSTER_SPRITES);

        for (int y = 0; y < monsterArray.length; y++) {
            for (int x = 0; x < monsterArray[y].length; x++) {
                monsterArray[y][x] = temp.getSubimage(x * MONSTER_DEFAULT_WIDTH, y * MONSTER_DEFAULT_HEIGHT,
                                                        MONSTER_DEFAULT_WIDTH, MONSTER_DEFAULT_HEIGHT);
            }

        }
    }

    public void resetAllEnemies() {
        for (Monster monster : monsters) {
            monster.resetEnemy();
        }

    }

}
