package fr.fonkio.utils;

import java.util.Timer;

public class IniciumTimer extends Timer {
    private boolean isCancelled = false;

    public IniciumTimer(String timerName) {
        super(timerName);
    }

    @Override
    public void cancel() {
        super.cancel();
        isCancelled = true;
    }

    public boolean isCancelled() {
        return isCancelled;
    }
}
