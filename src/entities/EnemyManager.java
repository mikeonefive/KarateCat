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

    private BufferedImage[][] ghostArray;
    private ArrayList<Ghost> ghosts = new ArrayList<>();

    public EnemyManager(PlayGame playGame) {
        this.playGame = playGame;
        loadEnemyImages();

    }

    public void loadEnemies(Level level) {
        monsters = level.getMonstaz();
        ghosts = level.getGhostz();
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

        for (Ghost ghost : ghosts) {
            if (ghost.isAlive()) {
                ghost.update(levelData, player);
                // if we don't set the isAnyEnemyActive then ghosts can't be killed
                // isAnyEnemyActive = true;
            }
        }

        if (!isAnyEnemyActive)
            playGame.setLevelComplete(true);

    }

    public void draw(Graphics g, int xLevelOffset) {
        drawMonsters(g, xLevelOffset);
        drawGhosts(g, xLevelOffset);
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

    private void drawGhosts(Graphics g, int xLevelOffset) {

        for (Ghost ghost : ghosts) {
            if (ghost.isAlive()) {

                // ghost.drawHitbox(g, xLevelOffset);
                g.drawImage(ghostArray[0][ghost.getAnimationIndex()],
                        (int) ghost.getHitbox().x - xLevelOffset - GHOST_DRAWOFFSET_X + ghost.flipX(),
                        (int) ghost.getHitbox().y - GHOST_DRAWOFFSET_Y,
                        GHOST_WIDTH * ghost.flipWidth(), GHOST_HEIGHT, null);

                // ghost.drawAttackBox(g, xLevelOffset);
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

        // Monsters
        monsterArray = new BufferedImage[4][7];
        BufferedImage tempMonster = LoadSave.getSpriteAtlas(LoadSave.MONSTER_SPRITES);

        for (int y = 0; y < monsterArray.length; y++) {
            for (int x = 0; x < monsterArray[y].length; x++) {
                monsterArray[y][x] = tempMonster.getSubimage(x * MONSTER_DEFAULT_WIDTH, y * MONSTER_DEFAULT_HEIGHT,
                                                        MONSTER_DEFAULT_WIDTH, MONSTER_DEFAULT_HEIGHT);
            }

        }

        // Ghosts
        ghostArray = new BufferedImage[1][6];
        BufferedImage tempGhost = LoadSave.getSpriteAtlas(LoadSave.GHOST_SPRITES);
        for (int y = 0; y < ghostArray.length; y++) {
            for (int x = 0; x < ghostArray[y].length; x++) {
                ghostArray[y][x] = tempGhost.getSubimage(x * GHOST_DEFAULT_WIDTH, y * GHOST_DEFAULT_HEIGHT,
                        GHOST_DEFAULT_WIDTH, GHOST_DEFAULT_HEIGHT);
            }

        }
    }

    public void resetAllEnemies() {
        for (Monster monster : monsters) {
            monster.resetEnemy();
        }

        for (Ghost ghost : ghosts) {
            ghost.resetEnemy();
        }

    }

}
