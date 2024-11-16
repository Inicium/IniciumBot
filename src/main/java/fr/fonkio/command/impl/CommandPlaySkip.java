package fr.fonkio.command.impl;

import fr.fonkio.command.AbstractPlayCommand;


public class CommandPlaySkip extends AbstractPlayCommand {

    @Override
    protected boolean skipBeforePlay() {
        return true;
    }
}
