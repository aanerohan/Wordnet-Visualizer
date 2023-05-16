package ngordnet.proj2b_testing;

import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.main.HyponymsHandler;
import ngordnet.main.HyponymsParser;
import ngordnet.ngrams.NGramMap;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {
        HyponymsParser hp = new HyponymsParser(synsetFile, hyponymFile);
        NGramMap ngm = new NGramMap(wordFile, countFile);
        return new HyponymsHandler(hp, ngm);
    }
}
