package yaama;

import java.util.ArrayList;
import java.util.List;

import jep.Jep;
import jep.SharedInterpreter;
import jep.SubInterpreter;

public class KeywordExtractor {
	private Jep py = new SharedInterpreter();
	public List<String> extract(String sentence) {
		
		py.eval("from keybert import KeyBERT");
		py.eval("doc = '" + sentence + "'");
		py.eval("kw_model = KeyBERT()");
		py.eval("keywords = [kword[0] for kword in kw_model.extract_keywords(doc,keyphrase_ngram_range=(1,1), top_n=5)]");
		List<String> keywords = py.getValue("keywords", ArrayList.class);
		return keywords;
	}
}