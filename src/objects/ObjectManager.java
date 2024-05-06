package objects;

import gamestates.PlayGame;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {

    private PlayGame playing;
    private BufferedImage[][] potionImages, containerImages;

    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;


    public ObjectManager(PlayGame playing) {
        this.playing = playing;
        loadImages();

    }

    public void checkIfObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion potion : potions) {
            if (potion.isActive()) {
                if (hitbox.intersects(potion.getHitbox())) {
                    potion.setActive(false);
                    applyEffectToPlayer(potion);
                }
            }
        }
    }

    public void applyEffectToPlayer(Potion potion) {

        if (potion.getObjectType() == RED_POTION) {
            playing.getPlayer().updateHealth(RED_POTION_VALUE);
        } else {
            playing.getPlayer().updatePower(BLUE_POTION_VALUE);
        }

    }

    public void checkIfObjectWasHit(Rectangle2D.Float attackBox) {
        for (GameContainer container : containers) {
            if (container.isActive()) {
                if (container.getHitbox().intersects(attackBox)) {
                    container.setAnimation(true);

                    // if we destroy a container, we find a potion inside
                    int type = 0;
                    if (container.getObjectType() == BARREL)
                        type = 1;

                    potions.add(new Potion((int)(container.getHitbox().x + container.getHitbox().width / 2),
                            (int)(container.getHitbox().y + container.getHitbox().getHeight() / 4),
                            type));
                    return;
                }
            }
        }
    }

    public void loadObjects(Level newLevel) {
        potions = newLevel.getPotions();
        containers = newLevel.getContainers();
    }

    private void loadImages() {
        BufferedImage potionSprite = LoadSave.getSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImages = new BufferedImage[2][7];

        for (int y = 0; y < potionImages.length; y++) {
            for (int x = 0; x < potionImages[y].length; x++) {
                potionImages[y][x] = potionSprite.getSubimage(12 * x, 16 * y, 12, 16);
            }
        }


        BufferedImage containerSprite = LoadSave.getSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImages = new BufferedImage[2][8];

        for (int y = 0; y < containerImages.length; y++) {
            for (int x = 0; x < containerImages[y].length; x++) {
                containerImages[y][x] = containerSprite.getSubimage(40 * x, 30 * y, 40, 30);
            }
        }
    }

    public void update() {

        for(Potion potion : potions) {
           if (potion.isActive)
               potion.update();
        }

        for (GameContainer container : containers) {
            if (container.isActive)
                container.update();
        }

    }

    public void draw(Graphics g, int xLevelOffset) {
        drawPotions(g, xLevelOffset);
        drawContainers(g, xLevelOffset);
    }

    private void drawContainers(Graphics g, int xLevelOffset) {

        for (GameContainer container : containers) {
            if (container.isActive) {
                int type = 0;
                if (container.getObjectType() == BARREL) {
                    type = 1;
                }
                // based on which type it is it draws a barrel or a box
                g.drawImage(containerImages[type][container.getAnimationIndex()],
                        (int)(container.getHitbox().x - container.getxDrawOffset() - xLevelOffset),
                        (int)(container.getHitbox().y - container.getyDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);
            }
        }

    }

    private void drawPotions(Graphics g, int xLevelOffset) {

        for(Potion potion : potions) {
            if (potion.isActive) {
                int type = 0;

                if (potion.getObjectType() == RED_POTION)
                    type = 1;

                g.drawImage(potionImages[type][potion.getAnimationIndex()],
                        (int)(potion.getHitbox().x - potion.getxDrawOffset() - xLevelOffset),
                        (int)(potion.getHitbox().y - potion.getyDrawOffset()),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);
            }
        }

    }

    public void resetAllObjects() {
        for (Potion potion : potions) {
            potion.reset();
        }

        for (GameContainer container : containers) {
            container.reset();
        }
    }


}
