
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import CBrain.tools.DivideWord;
import TypeTrans.*;

public class Step2_1_a_4_1_findITsuffixHighFrq_bak {
	/**
	 * @param args
	 * @author jacoxu-2015/04/26
	 */

	public  void Read_Write(String rawData,String tarFilePath) {
		try {
			String encoding = "UTF-8";
			File file = new File(rawData);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				int linenu=0;
				HashSet<String> itSuffixSet = new HashSet<String>();
				boolean hasContent = false;
				StringBuffer tmpContentBuffer = new StringBuffer();
				HashMap<String, Integer> wordFrqMap = new HashMap<String, Integer>();
				while ((lineTXT = bufferedReader.readLine()) != null) {
					linenu++;
					//先进行词拆分，然后按照频次进行排序
					ArrayList<String> tmpTextArray = DivideWord.splitWord(lineTXT);
					for (int i = 0; i < tmpTextArray.size(); i++) {
						String tmpWord = tmpTextArray.get(i);
						if (wordFrqMap.containsKey(tmpWord)){
							wordFrqMap.put(tmpWord, wordFrqMap.get(tmpWord)+1);
						}else {
							wordFrqMap.put(tmpWord, 1);
						}
					}
					if (linenu%1000 ==0) {
						System.out.println("hasProcessed text numbers:" + linenu);
					}
				}
				read.close();
				System.out.println("hasProcessed text numbers:" + linenu);
				//开始对高频词进行排序
	            List<Map.Entry<String, Integer>> wordFrqList =
	            	    new ArrayList<Map.Entry<String, Integer>>(wordFrqMap.entrySet());
	            Collections.sort(wordFrqList, new Comparator<Map.Entry<String, Integer>>() {   
	                @Override
					public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
	                    return (o2.getValue() - o1.getValue()); 
	                    //return (o1.getKey()).toString().compareTo(o2.getKey());
	                }
	            }); 
	            for (int i = 0; i < wordFrqList.size(); i++) {
	                String spamWordStr = wordFrqList.get(i).getKey();
	                int spamWordFrq = wordFrqList.get(i).getValue();
	                String tmpSpamInfoStr = spamWordFrq+"<->"+spamWordStr;
	                Result2Txt(tarFilePath,tmpSpamInfoStr);             
	            }
				
			} else {
				System.out.println("can't find the file");
			}
		} catch (Exception e) {
			System.out.println("something error when reading the content of the file");
			e.printStackTrace();
		}
		return;
	}

	public void Result2Txt(String file, String txt) {
		  try {
		   BufferedWriter os = new BufferedWriter(new OutputStreamWriter(   
	                new FileOutputStream(new File(file),true), "UTF-8")); 
		   os.write(txt + "\n");
		   os.close();
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
	 }
	
	public static void main(String[] args) {
		String dataPathStr="./../";
	    String scrData=dataPathStr+"library/itSuffixSpamInfo.dic";
	    String tarFilePath=dataPathStr+"library/itSuffixSpamHighFrqKey.dic";
	    Step2_1_a_4_1_findITsuffixHighFrq_bak fr=new Step2_1_a_4_1_findITsuffixHighFrq_bak();
        long readstart=System.currentTimeMillis();
        fr.Read_Write(scrData, tarFilePath);
        long readend=System.currentTimeMillis();
        System.out.println((readend-readstart)/1000.0+"s had been consumed to find high frequency IT suffix keyword");
	}
}
