package CBrain.tools;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class DivideWord {
	
	public static ArrayList<String> splitWord(String shortDoc) throws IOException {
	
		ArrayList<String> termsOnDoc = new ArrayList<String>(); 
		List<Term> all = new ArrayList<Term>();
		Analysis udf = new ToAnalysis(new StringReader(shortDoc));
		Term term = null;
		while ((term = udf.next()) != null) {
			String tempTerm = term.toString().trim();
			if (tempTerm.length()>0){
				termsOnDoc.add(tempTerm);
			}
		}
		return termsOnDoc;
	}
}


