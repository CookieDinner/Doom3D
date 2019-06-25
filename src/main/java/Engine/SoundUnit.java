package Engine;

import Entities.Sound;

public class SoundUnit {
    private static Sound shotSound;
    private static Sound deathPlayerSound;
    private static Sound deathEnemySound;
    private static Sound backgroundMusic;

    public SoundUnit() {
        this.shotSound = new Sound("gun_shot.wav",15,1000);
        this.deathPlayerSound = new Sound("player_death1.wav",0,0);
        this.deathEnemySound = new Sound("enemy_death1.wav",0,0);
        this.backgroundMusic = new Sound("back_short.wav",22,1000);
    }

    public static Sound getShotSound() {
        return shotSound;
    }

    public static Sound getDeathPlayerSound() {
        return deathPlayerSound;
    }

    public static Sound getDeathEnemySound() {
        return deathEnemySound;
    }

    public static Sound getBackgroundMusic() {
        return backgroundMusic;
    }
}
