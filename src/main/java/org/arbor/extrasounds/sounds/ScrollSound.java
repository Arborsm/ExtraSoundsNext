package org.arbor.extrasounds.sounds;

import net.minecraft.sounds.SoundSource;
import org.arbor.extrasounds.ESConfig;

public class ScrollSound {

    private int lastPos = 0;

    private long lastTime = 0L;

    public void play(int row) {
        final long now = System.currentTimeMillis();
        final long timeDiff = now - lastTime;
        if (timeDiff > 20 && lastPos != row && !(lastPos != 1 && row == 0)) {
            SoundManager.playSound(
                    Sounds.INVENTORY_SCROLL,
                    (1f - 0.1f + 0.1f * Math.min(1, 50f / timeDiff)),
                    SoundSource.PLAYERS, ESConfig.CONFIG.INVENTORY.get().floatValue());
            lastTime = now;
            lastPos = row;
        }
    }

    public void play() {
        final long now = System.currentTimeMillis();
        final long timeDiff = now - lastTime;
        if (timeDiff > 20) {
            SoundManager.playSound(
                    Sounds.INVENTORY_SCROLL,
                    (1f - 0.1f + 0.1f * Math.min(1, 50f / timeDiff)),
                    SoundSource.PLAYERS, ESConfig.CONFIG.INVENTORY.get().floatValue());
            lastTime = now;
        }
    }
}
