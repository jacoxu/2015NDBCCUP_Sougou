
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Step2_1_b_7_genDict_nonSyn {
	public static void main(String[] args) throws Exception {
		//读取训练数据文件
		String dataPathStr="./../";
		//处理所有数据
		BufferedReader srcTrainFile = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(dataPathStr+
						"Data/Step2_1_a_7_labeledData.txt")), "UTF-8"));
		BufferedReader srcTestFile = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(dataPathStr+
						"Data/Step2_1_a_7_unlabeledData.txt")), "UTF-8"));
		BufferedWriter wordMapFileW = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(dataPathStr+
						"feaSet/Step2_1_b_7_wordDictWithIdx_nonSyn.dic")), "UTF-8"));
		
		String tempLine;
		//先读入词典
		int wordReadNum = 0;
		int wordWriteNum = 0;
		int lineNum = 0;
		HashSet<String> wordSet = new HashSet<String>();
		System.out.println("Start to read wordSet ...");
		while ((tempLine = srcTrainFile.readLine()) != null) {
			String[] wordArraysStr = tempLine.trim().split("\\s+");
			//最后一个词 为 Label
			for (int i = 0; i < wordArraysStr.length-1; i++) {
				String tmpWord = wordArraysStr[i].trim();
				if (wordSet.contains(tmpWord)) continue;
				else {
					wordSet.add(tmpWord);
					wordReadNum++;
				}
			}
			lineNum++;
		}
		while ((tempLine = srcTestFile.readLine()) != null) {
			String[] wordArraysStr = tempLine.trim().split("\\s+");
			//最后一个词 为 Label
			for (int i = 0; i < wordArraysStr.length-1; i++) {
				String tmpWord = wordArraysStr[i].trim();
				if (wordSet.contains(tmpWord)) continue;
				else {
					wordSet.add(tmpWord);
					wordReadNum++;
				}
			}
			lineNum++;
		}
		
		System.out.println("Total lineNum:"+lineNum+", and start to output wordSet ...");
		
		Iterator<String> iterator=wordSet.iterator();
		while(iterator.hasNext()){
			wordWriteNum++;
			wordMapFileW.write(iterator.next()+"\t"+wordWriteNum+"\n");
		}
		System.out.println("wordReadNum:" +wordReadNum);
		System.out.println("wordWriteNum:" +wordWriteNum);
		if (wordWriteNum!=wordReadNum) {
			System.out.println("Error! wordWriteNum is diffent with wordReadNum");
		}	
		srcTrainFile.close();
		srcTestFile.close();
		wordMapFileW.close();
	}
}