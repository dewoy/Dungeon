package graphic.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import controller.ScreenController;
import tools.Constants;

/**
 * Creates a dialogue object, formats and passes the dialogue to the `ScreenController` so that the
 * dialogue can be displayed on the screen.
 */
public class ResponsiveDialogue<T extends Actor> extends ScreenController<T> {
    /**
     * Creates a new ResponsiveDialogue with a new Spritebatch
     *
     * @param skin Resources that can be used by UI widgets
     * @param msgColor colour of the text
     * @param arrayOfMessages Content displayed in the dialogue
     */
    public ResponsiveDialogue(Skin skin, Color msgColor, String... arrayOfMessages) {
        this(new SpriteBatch(), skin, msgColor, arrayOfMessages);
    }

    /**
     * Creates a new ResponsiveDialogue with a given Spritebatch
     *
     * @param batch to display the textures
     * @param skin Resources that can be used by UI widgets
     * @param msgColor colour of the text
     * @param arrayOfMessages Content displayed in the dialogue
     */
    public ResponsiveDialogue(
            SpriteBatch batch, Skin skin, Color msgColor, String... arrayOfMessages) {
        super(batch);
        TextDialog dialog = createDialog(skin, arrayOfMessages);
        add((T) dialog);
        formatDependingOnGameScreen(dialog, msgColor);
    }

    /**
     * Creates Dialog with a label for text-output and Button
     *
     * @param skin Resources that can be used by UI widgets
     * @param arrayOfMessages Content displayed in the dialogue
     */
    private TextDialog createDialog(Skin skin, String... arrayOfMessages) {
        String[] formatIdentifier = new String[3];
        setupMessagesForIdentifier(formatIdentifier, arrayOfMessages);
        return new TextDialog(skin, formatIdentifier[0], formatIdentifier[1], formatIdentifier[2]);
    }

    /**
     * All values for thne variable Identifier are correctly read from the parameter arrayOfMessages
     * and assigned to it.
     *
     * @param formatIdentifier Parameters that are passed to the dialogue
     * @param arrayOfMessages Parameters defined by the user
     */
    private void setupMessagesForIdentifier(String[] formatIdentifier, String... arrayOfMessages) {
        final int inputArraySize = arrayOfMessages.length;
        final int outputArraySize = formatIdentifier.length;
        final String[] defaultCaptions = {
            Constants.DEFAULT_MESSAGE, Constants.DEFAULT_BUTTON_MESSAGE, Constants.DEFAULT_HEADING
        };

        for (int counter = 0; counter < outputArraySize; counter++) {
            if (inputArraySize > counter
                    && arrayOfMessages[counter] != null
                    && arrayOfMessages[counter].length() > 0)
                formatIdentifier[counter] = arrayOfMessages[counter];
            else formatIdentifier[counter] = defaultCaptions[counter];
        }
    }

    /**
     * formats dialogue depending on the game screen and positions it in the screen centre
     *
     * @param dialog with info field and button to cancel play pause
     * @param msgColor Text colour
     */
    private void formatDependingOnGameScreen(TextDialog dialog, Color msgColor) {
        dialog.setColor(msgColor);
        dialog.setWidth(Constants.WINDOW_WIDTH - Constants.DIALG_DIFFERENCE_MEASURE);
        dialog.setHeight(Constants.WINDOW_HEIGHT - Constants.DIALG_DIFFERENCE_MEASURE);
        dialog.setPosition(
                (Constants.WINDOW_WIDTH) / 2f,
                (Constants.WINDOW_HEIGHT) / 2f,
                Align.center | Align.top / 2);
    }
}
