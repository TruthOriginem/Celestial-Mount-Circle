package data.utils.cmc;

import com.fs.starfarer.api.Global;

/**
 * Useful for externalize the text in strings.json for translation.
 */
public class I18n {

    private String category;

    public I18n(String category) {
        this.category = category;
    }

    public String get(String id) {
        return Global.getSettings().getString(category, id);
    }

    public String format(String id, Object... args) {
        return String.format(get(id), args);
    }
}
