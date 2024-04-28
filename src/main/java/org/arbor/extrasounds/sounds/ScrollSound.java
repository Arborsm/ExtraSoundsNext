package org.arbor.extrasounds.sounds;

public class ScrollSound {

    private int lastPos = 0;

    private long lastTime = 0L;

    private static void playScrollSound(long timeDiff) {
        SoundManager.playSound(
                Sounds.INVENTORY_SCROLL,
                (1f - 0.1f + 0.1f * Math.min(1, 50f / timeDiff)),
                Mixers.INVENTORY);
    }

    public void play(int row) {
        final long now = System.currentTimeMillis();
        final long timeDiff = now - lastTime;
        if (timeDiff > 20 && lastPos != row && !(lastPos != 1 && row == 0)) {
            playScrollSound(timeDiff);
            lastTime = now;
            lastPos = row;
        }
    }

    public void play() {
        final long now = System.currentTimeMillis();
        final long timeDiff = now - lastTime;
        if (timeDiff > 20) {
            playScrollSound(timeDiff);
            lastTime = now;
        }
    }
}
