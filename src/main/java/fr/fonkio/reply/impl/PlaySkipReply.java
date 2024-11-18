package fr.fonkio.reply.impl;

import fr.fonkio.reply.AbstractPlayReply;


public class PlaySkipReply extends AbstractPlayReply {

    @Override
    protected boolean skipBeforePlay() {
        return true;
    }
}
