package fr.fonkio.reply;

import fr.fonkio.message.StringsConst;
import net.dv8tion.jda.api.components.buttons.Button;

public abstract class AbstractStringSelectMenuReply extends AbstractReply {

    protected Button getButtonSaved() {
        return Button.success("saved", StringsConst.BUTTON_SAVED).asDisabled();
    }
}
