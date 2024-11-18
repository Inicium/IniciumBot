package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractPlayReply;

public class PlayReply extends AbstractPlayReply {

    @Override
    protected boolean skipBeforePlay() {
        return false;
    }
}
