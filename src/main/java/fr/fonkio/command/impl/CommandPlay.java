package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractPlayCommand;

public class CommandPlay extends AbstractPlayCommand {

    @Override
    protected boolean skipBeforePlay() {
        return false;
    }
}
