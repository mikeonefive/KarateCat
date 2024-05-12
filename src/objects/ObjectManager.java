package objects;

import entities.Player;
import gamestates.PlayGame;
import levels.Level;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.Projectiles.*;
import static utilz.HelpMethods.canCannonSeePlayer;
import static utilz.HelpMethods.isProjectileHittingLevelBorder;

public class ObjectManager {

    private PlayGame playing;
    private BufferedImage[][] potionImages, containerImages;
    private BufferedImage spikeImage, arrowImage;
    private BufferedImage[] cannonImages;

    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>();


    public ObjectManager(PlayGame playing) {
        this.playing = playing;
        loadImages();

    }

    public void checkIfSpikesTouched(Player player) {
        for (Spike spike : spikes) {
            if (spike.getHitbox().intersects(player.getHitbox())) {
                player.kill();
            }
        }
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
            if (container.isActive() && !container.doAnimation) {
                if (container.getHitbox().intersects(attackBox)) {
                    container.setAnimation(true);

                    // if we destroy a container, we find a potion inside
                    int type = 0;
                    if (container.getObjectType() == BARREL)
                        type = 1;

                    potions.add(new Potion((int)(container.getHitbox().x + container.getHitbox().width / 2),
                            (int)(container.getHitbox().y - container.getHitbox().height / 2),
                            type));
                    return;
                }
            }
        }
    }

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions()); // new lists because otherwise we would keep all the other elements that were created before
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
        projectiles.clear(); // every time we start a new level clear list of projectiles
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

        spikeImage = LoadSave.getSpriteAtlas(LoadSave.SPIKES_ATLAS);

        cannonImages = new BufferedImage[5];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.CANNON_ATLAS);

        for (int i = 0; i < cannonImages.length; i++) {
            cannonImages[i] = temp.getSubimage(i * 32, 0, 32, 32);
        }

        arrowImage = LoadSave.getSpriteAtlas(LoadSave.CANNON_ARROW);
    }

    public void update(int[][] levelData, Player player) {

        for(Potion potion : potions) {
           if (potion.isActive)
               potion.update();
        }

        for (GameContainer container : containers) {
            if (container.isActive)
                container.update();
        }

        updateCannons(levelData, player);
        updateProjectiles(levelData, player);
    }

    private void updateProjectiles(int[][] levelData, Player player) {

        for (Projectile p: projectiles) {
            if (p.getIsActive()) {
                p.updatePosition();
            }

            // is projectile the hitting the player?
            if (p.getHitbox().intersects(player.getHitbox())) {
                player.updateHealth(-10);
                p.setIsActive(false);
            }
            // is projectile hitting the border of the level?
            else if (isProjectileHittingLevelBorder(p, levelData)) {
                p.setIsActive(false);
            }
        }
    }


    private boolean isPlayerInRange(Cannon cannon, Player player) {
        int absDistance = (int) Math.abs(player.getHitbox().x - cannon.getHitbox().x); // returns the absolute distance between player and enemy
        return absDistance <= Game.TILES_SIZE * 5; // if player 5 tiles in range
    }

    private boolean isPlayerInFrontOfCannon(Cannon cannon, Player player) {
        if (cannon.getObjectType() == CANNON_LEFT) {
            if (cannon.getHitbox().x > player.getHitbox().x)    // player is to the left of the cannon's hitbox
                return true;
        } else if (cannon.getHitbox().x < player.getHitbox().x)
            return true;

        return false;
    }

    private void updateCannons(int[][] levelData, Player player) { // levelData is need for cannons to spot player

        for (Cannon currentCannon : cannons) {

            if (!currentCannon.doAnimation)
                if (currentCannon.getTileY() == player.getTileY())
                    if (isPlayerInRange(currentCannon, player))
                        if (isPlayerInFrontOfCannon(currentCannon, player))
                            if (canCannonSeePlayer(levelData, player.getHitbox(), currentCannon.getHitbox(), currentCannon.getTileY())) {
                                currentCannon.setAnimation(true);
                            }

            currentCannon.update();
            // we do this so the cannon don't shoot right away but at index 4
            if (currentCannon.getAnimationIndex() == 3 && currentCannon.getAnimationTick() == 0) {
                shootCannon(currentCannon);
            }
        }
    }

    private void shootCannon(Cannon currentCannon) {

        int direction = 1;
        if (currentCannon.getObjectType() == CANNON_LEFT)
            direction = -1;
        projectiles.add(new Projectile((int)(currentCannon.getHitbox().x),
                (int)(currentCannon.getHitbox().y),
                direction));

    }


    public void draw(Graphics g, int xLevelOffset) {
        drawPotions(g, xLevelOffset);
        drawContainers(g, xLevelOffset);
        drawSpikes(g, xLevelOffset);
        drawCannons(g, xLevelOffset);
        drawProjectiles(g, xLevelOffset);
    }

    private void drawProjectiles(Graphics g, int xLevelOffset) {
        for (Projectile p : projectiles) {
            if (p.getIsActive()) {

                int x =  (int)(p.getHitbox().x - xLevelOffset);
                int width = CANNON_ARROW_WIDTH;

                // check which direction and draw correct arrow
                if (p.getDirection() == 1) {
                    x += width;
                    width *= -1;
                }
                g.drawImage(arrowImage, x, (int)(p.getHitbox().y),
                        width, CANNON_ARROW_HEIGHT, null);

            }
        }
    }

    private void drawCannons(Graphics g, int xLevelOffset) {
        for (Cannon cannon : cannons) {
            int x =  (int)(cannon.getHitbox().x - xLevelOffset);
            int width = CANNON_WIDTH;

            // flip image if cannon faces to the right
            if (cannon.getObjectType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }

            g.drawImage(cannonImages[cannon.getAnimationIndex()],
                    x, (int)(cannon.getHitbox().y), width, CANNON_HEIGHT, null);
        }
    }

    private void drawSpikes(Graphics g, int xLevelOffset) {
        for (Spike spike : spikes) {
            g.drawImage(spikeImage, (int)(spike.getHitbox().x - xLevelOffset),
                    (int)(spike.getHitbox().y - spike.getyDrawOffset()),
                    SPIKE_WIDTH,
                    SPIKE_HEIGHT, null);
        }
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

        loadObjects(playing.getLevelManager().getCurrentLevel());

        for (Potion potion : potions) {
            potion.reset();
        }

        for (GameContainer container : containers) {
            container.reset();
        }

        for (Cannon cannon : cannons) {
            cannon.reset();
        }

    }


}
