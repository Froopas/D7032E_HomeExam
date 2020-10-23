package Language;

import java.util.ArrayList;

public class LanguageInfo {

    private ArrayList<String> dictionaries;
    private ArrayList<String> dimensions;

    public LanguageInfo(ArrayList<String> dictionaries, ArrayList<String> dimensions) {
        this.dictionaries = dictionaries;
        this.dimensions = dimensions;
    }

    public ArrayList<String> getDimensions() {
        return dimensions;
    }

    public ArrayList<String> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(ArrayList<String> dictionaries) {
        this.dictionaries = dictionaries;
    }

    public void setDimensions(ArrayList<String> dimensions) {
        this.dimensions = dimensions;
    }
}
