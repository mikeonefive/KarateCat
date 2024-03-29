package entities;

import gamestates.PlayGame;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {

    private PlayGame playGame;

    private BufferedImage[][] monsterArray;
    private ArrayList<Monster> monsters = new ArrayList<>();

    private BufferedImage[][] crabbyArray;
    private ArrayList<Crabby> crabbies = new ArrayList<>();


    public EnemyManager(PlayGame playGame) {
        this.playGame = playGame;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        monsters = LoadSave.getMonsters();
        System.out.println("that many monsters in game: " + monsters.size());

        crabbies = LoadSave.getCrabs();
        System.out.println("that many crabs in game: " + crabbies.size());
    }

    public void update(int[][] levelData, Player player) {
        for (Monster monster : monsters) {
            monster.update(levelData, player);
        }

        for (Crabby crab : crabbies)
            crab.update(levelData, player);
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawMonsters(g, xLevelOffset);
        drawCrabs(g, xLevelOffset);
    }

    private void drawMonsters(Graphics g, int xLevelOffset) {

        for (Monster monster : monsters) {
            // monster.drawHitbox(g, xLevelOffset);
            g.drawImage(monsterArray[monster.getEnemyState()][monster.getAnimationIndex()],
                    (int)monster.getHitbox().x - xLevelOffset - MONSTER_DRAWOFFSET_X + monster.flipX(),
                    (int)monster.getHitbox().y - MONSTER_DRAWOFFSET_Y,
                    MONSTER_DEFAULT_WIDTH * monster.flipWidth(), MONSTER_DEFAULT_HEIGHT, null);

            monster.drawAttackBox(g, xLevelOffset);
        }
    }

    private void drawCrabs(Graphics g, int xLevelOffset) {
        for (Crabby c : crabbies) {
            g.drawImage(crabbyArray[c.getEnemyState()][c.getAnimationIndex()],
                    (int) c.getHitbox().x - xLevelOffset - CRABBY_DRAWOFFSET_X,
                    (int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y,
                    CRABBY_WIDTH, CRABBY_HEIGHT, null);
			// c.drawHitbox(g, xLvlOffset);
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


        crabbyArray = new BufferedImage[5][9];
        BufferedImage tempCrabImg = LoadSave.getSpriteAtlas(LoadSave.CRABBY_SPRITES);
        for (int j = 0; j < crabbyArray.length; j++)
            for (int i = 0; i < crabbyArray[j].length; i++)
                crabbyArray[j][i] = tempCrabImg.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
    }

}
