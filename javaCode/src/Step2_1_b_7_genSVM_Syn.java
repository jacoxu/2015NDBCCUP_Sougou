
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Step2_1_b_7_genSVM_Syn {
	public static void main(String[] args) throws Exception {
		//Test
//		String tempLine ="宸濄伄娴併倢銇倛銇?28521"; 
//		String[] tokensStr  = tempLine.split("\t");
		//利用纯文本和wordmap构建基于词频的向量空间模型，用于STH预处理
		//all = [test_data;train_data]!
		String dataPathStr="./../";
		String wordMapStr = dataPathStr+"/feaSet/Step2_1_b_7_wordDictWithIdx_Syn.dic";
		String vsmBW_TrainQueryStr = dataPathStr+"/feaSet/Step2_1_b_7_vsmBW_TrainQuery_Syn.txt";
		String vsmBW_TrainTitleStr = dataPathStr+"/feaSet/Step2_1_b_7_vsmBW_TrainTitle_Syn.txt";
		String vsmBW_TestQueryStr = dataPathStr+"/feaSet/Step2_1_b_7_vsmBW_TestQuery_Syn.txt";
		String vsmBW_TestTitleStr = dataPathStr+"/feaSet/Step2_1_b_7_vsmBW_TestTitle_Syn.txt";
		BufferedReader srcTrainFile = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(dataPathStr+
						"Data/Step2_1_a_8_labeledData.txt")), "UTF-8"));
		BufferedReader srcTestFile = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(dataPathStr+
						"Data/Step2_1_a_8_unlabeledData.txt")), "UTF-8"));
		
		BufferedReader wordMapRD = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(wordMapStr)), "UTF-8"));
		
//		BufferedWriter vsmBW_TrainQuery = new BufferedWriter(
//				new OutputStreamWriter(new FileOutputStream(new File(vsmBW_TrainQueryStr)), "UTF-8"));
//		BufferedWriter vsmBW_TrainTitle = new BufferedWriter(
//				new OutputStreamWriter(new FileOutputStream(new File(vsmBW_TrainTitleStr)), "UTF-8"));
//		BufferedWriter vsmBW_TestQuery = new BufferedWriter(
//				new OutputStreamWriter(new FileOutputStream(new File(vsmBW_TestQueryStr)), "UTF-8"));
//		BufferedWriter vsmBW_TestTitle = new BufferedWriter(
//				new OutputStreamWriter(new FileOutputStream(new File(vsmBW_TestTitleStr)), "UTF-8"));
		//构造训练VSM词频向量空间模型
		creatVSMText(srcTrainFile,wordMapRD,vsmBW_TrainQueryStr,vsmBW_TrainTitleStr);
		wordMapRD.close();
		wordMapRD = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(wordMapStr)), "UTF-8"));
		//构造测试VSM词频向量空间模型
		creatVSMText(srcTestFile,wordMapRD,vsmBW_TestQueryStr,vsmBW_TestTitleStr);
		
		srcTrainFile.close();
		srcTestFile.close();
		wordMapRD.close();
//		vsmBW_TrainQuery.close();		
//		vsmBW_TrainTitle.close();
//		vsmBW_TestQuery.close();
//		vsmBW_TestTitle.close();
		System.out.println("It is done, ok!");
	}
	
	public static void creatVSMText(BufferedReader sourceTextRD,
			BufferedReader wordMapRD, String vsmBW_QueryStr, String vsmBW_TitleStr) throws IOException, Exception {
		System.out.println("Start to create VSM ...!");
		String tempLine;
		//先读入词典
		int wordIdxNum = 1;
		HashMap<String, Integer> wordMap = new HashMap<String,Integer>();

		while ((tempLine = wordMapRD.readLine()) != null) {
			//词典中放着词和索引号，索引号
			if (wordMap.containsKey(tempLine.trim())) {
				System.out.println("Test, the word is replicate:"+tempLine.trim());
			}
			if (tempLine.trim().length()==0) continue;
			//wordMap.put(tempLine.trim(), wordIdxNum);
			wordMap.put(tempLine.split("\\s+")[0].trim(), Integer.valueOf(tempLine.split("\\s+")[1]));	
			wordIdxNum =wordIdxNum+1;
		}
		//定义了这个数据集的特征维数，注意具有数据集独立化
		int dimVector = wordIdxNum-1;
		System.out.println("Has read the dictionary, the size is:"+wordMap.size());
		ArrayList<Integer> wordFreqList_Query = new ArrayList<Integer>();
		ArrayList<Integer> wordFreqList_Title = new ArrayList<Integer>();
		int lineNum = 1;
		boolean hasWordFeature = false;
		StringBuffer tmpQueryVSMBuffer = new StringBuffer();
		StringBuffer tmpTitleVSMBuffer = new StringBuffer();
		while ((tempLine = sourceTextRD.readLine()) != null) {
			if (lineNum>22591) {
				System.out.print("need a pause for check...");
			}
			hasWordFeature = false;
			//读入一行，即一个文档；
			wordFreqList_Query.clear();
			for (int i = 0; i < dimVector; i++) {
				wordFreqList_Query.add(0);
			}
			wordFreqList_Title.clear();
			for (int i = 0; i < dimVector; i++) {
				wordFreqList_Title.add(0);
			}

			String[] subStrings  = tempLine.trim().split("\t");
			String[] tokensStr_Query;
			String[] tokensStr_Title;
			boolean isvalid = true;
			if (subStrings.length!=3){
				isvalid = false;
			}else {
				tokensStr_Query = subStrings[0].trim().split("\\s+");
				tokensStr_Title = subStrings[1].trim().split("\\s+");
				if (!(tokensStr_Query.length<1||tokensStr_Title.length<1)) {
					for (int j = 0; j < tokensStr_Query.length; j++) {
						String tempToken = tokensStr_Query[j];
						if (wordMap.containsKey(tempToken.trim())) {
							hasWordFeature = true;
							int index = wordMap.get(tempToken.trim());
							if (index>dimVector) {
								System.out.print("Error, and the word is: "+tempToken.trim());
							}
							wordFreqList_Query.set(index-1, wordFreqList_Query.get(index-1)+1);
						}else {
							System.out.println("error: the map has not contain the word:"
									+tempToken+" in Line:"+lineNum);
						}
					}
					for (int j = 0; j < tokensStr_Title.length; j++) {
						String tempToken = tokensStr_Title[j];
						if (wordMap.containsKey(tempToken.trim())) {
							hasWordFeature = true;
							int index = wordMap.get(tempToken.trim());
							if (index>dimVector) {
								System.out.print("Error, and the word is: "+tempToken.trim());
							}
							wordFreqList_Title.set(index-1, wordFreqList_Title.get(index-1)+1);
						}else {
							System.out.println("error: the map has not contain the word:"
									+tempToken+" in Line:"+lineNum);
						}
					}
				}else {
					isvalid = false;
				}
			}
			
			if (!isvalid) {
				System.out.println("error: the string has lacked contents:"
						+tempLine.trim()+" in Line:"+lineNum);
			}
			for (int tempFreq:wordFreqList_Query) {
				tmpQueryVSMBuffer.append(String.valueOf(tempFreq)+" ");
			}
			for (int tempFreq:wordFreqList_Title) {
				tmpTitleVSMBuffer.append(String.valueOf(tempFreq)+" ");
			}
			//把处理好的文本写入到新的文本文件中
			Result2Txt(vsmBW_QueryStr,tmpQueryVSMBuffer.toString().trim());
			tmpQueryVSMBuffer.delete(0, tmpQueryVSMBuffer.length());
			Result2Txt(vsmBW_TitleStr,tmpTitleVSMBuffer.toString().trim());
			tmpTitleVSMBuffer.delete(0, tmpTitleVSMBuffer.length());
			
			if (!hasWordFeature) {
				System.out.println("++++++++++"+"has no word in Line:"+lineNum+"++++++++++");
			}
			lineNum++;
			if (lineNum%1000 ==0) {
				System.out.println("hasProcessed text numbers:" + lineNum);
			}
		}
	}
	public static void Result2Txt(String file, String txt) {
		  try {
			   BufferedWriter os = new BufferedWriter(new OutputStreamWriter(   
		                new FileOutputStream(new File(file),true), "UTF-8")); 
			   os.write(txt + "\n");
			   os.close();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	 }
}
