package studio.safe.wordoftheday;

import static studio.safe.wordoftheday.WotdUtils.EMPTY;

/**
 * Class Wotd
 *
 * @author ansidev
 */

class Wotd {
    private String word;
    private String meaning;
    private String url;

    Wotd() {
        this.word = EMPTY;
        this.meaning = EMPTY;
        this.url = EMPTY;
    }

    String getWord() {
        if (word == null) {
            return "";
        }
        return word;
    }

    void setWord(String word) {
        this.word = word;
    }

    String getMeaning() {
        if (meaning == null) {
            return "";
        }
        return meaning;
    }

    void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }
}
