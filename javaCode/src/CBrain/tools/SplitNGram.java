package CBrain.tools;

public class SplitNGram {
    /**
     * @param args  
     * @author jacoxu  
     * @date 2014/04/07 15:29  
     */  
    public static void main(String[] args) {   
        String testStr = "今天是2014年4月7日15pm";   
        System.out.println(split2NGram(testStr,3));   
        return; 
    }
    public static String split2NGram(String testStr, int nGram) {   
        if (nGram > testStr.length()) return testStr;   
        StringBuffer str2NgramBuf = new StringBuffer();   
        char[] strChar = testStr.toCharArray();   
        for (int i=0; i<strChar.length-(nGram-1); i++){   
            for (int j = 0; j < nGram; j++) {   
                str2NgramBuf.append(strChar[i+j]);   
            }
            if (i==(strChar.length - nGram)) break;   
            str2NgramBuf.append(" ");   
        }   
        return str2NgramBuf.toString();   
    }   
}  
