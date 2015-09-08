
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import CBrain.config.SmsBase;
import CBrain.tools.SplitNGram;
import CBrain.tools.StringAnalyzer;
import TypeTrans.*;

public class Step2_3_a_matchNumAndEnglish {
	/**
	 * @param args
	 * @author jacoxu-2015/05/01
	 */

	public HashSet<String> comSylSet  = new HashSet<String>();
	public  void Read_Write(String rawData,String tarFilePath) {
		try {
			String encoding = "UTF-8";
			File file = new File(rawData);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);
//				addComSylSet();
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				String tmpQueryStr = null;
				String tmpTitleStr = null;
				String tmpScoreStr = null;
				int linenu=0;
				StringBuffer tmpContentBuffer = new StringBuffer();
				
				String tmpQueryNum = null;
				String tmpTitleNum = null;
				String tmpQueryEng = null;
				String tmpTitleEng = null;
				int isMatch = 1;
				ArrayList<String> queryNumEngList = new ArrayList<String>();
//				HashSet<String> titleNumEngSet = new HashSet<String>();
				while ((lineTXT = bufferedReader.readLine()) != null) {
					linenu++;
//					if (linenu ==159) {
//						System.out.print("here is a pause!");
//					}
					//首先进行类型转换
					String[] Segs_11 = lineTXT.split("\t");///此处分为3段，Query、title、lable
					tmpQueryStr = Segs_11[0];
					tmpTitleStr = Segs_11[1];
					tmpScoreStr = Segs_11[2];
					
//					tmpQueryEng = StringAnalyzer.extractEnglishCharacter(tmpQueryStr);
					tmpQueryNum = StringAnalyzer.extractNumberCharacter(tmpQueryStr);
				
//					tmpTitleEng = StringAnalyzer.extractEnglishCharacter(tmpTitleStr);
					tmpTitleNum = StringAnalyzer.extractNumberCharacter(tmpTitleStr);
					
//					if ((tmpQueryEng==null)&&(tmpQueryNum==null)) {
					if (tmpQueryNum==null) {
						isMatch = 1;
					}else {
//						if (tmpQueryEng!=null) {
//							String[] tmpQueryEngArray = tmpQueryEng.split("\\s+");
//							for (int i = 0; i < tmpQueryEngArray.length; i++) {
//								queryNumEngList.add(tmpQueryEngArray[i]);
//							}
//						}
						if (tmpQueryNum!=null) {
							String[] tmpQueryNumArray = tmpQueryNum.split("\\s+");
							for (int i = 0; i < tmpQueryNumArray.length; i++) {
								queryNumEngList.add(tmpQueryNumArray[i]);
							}
						}
						
//						if (tmpTitleEng!=null) {
//							String[] tmpTitleEngArray = tmpTitleEng.split("\\s+");
//							for (int i = 0; i < tmpTitleEngArray.length; i++) {
//								titleNumEngSet.add(tmpTitleEngArray[i]);
//							}
//						}
						if (tmpTitleNum!=null) {
//							String[] tmpTitleNumArray = tmpTitleNum.split("\\s+");
//							for (int i = 0; i < tmpTitleNumArray.length; i++) {
//								titleNumEngSet.add(tmpTitleNumArray[i]);
//							}
							tmpTitleNum = tmpTitleNum.replaceAll("\\s+", "");
						}
						
						for (int i = 0; i < queryNumEngList.size(); i++) {
//							if ((comSylSet.contains(queryNumEngList.get(i)))||(queryNumEngList.get(i).length()<1)) {
							if (queryNumEngList.get(i).length()<1) {
								continue;
							}
//							if (!titleNumEngSet.contains(queryNumEngList.get(i))) {
							if (tmpTitleNum==null) {
								isMatch = 0;
								break;
							}else if ((!tmpTitleNum.contains(queryNumEngList.get(i)))) {
								isMatch = 0;
								break;
							}
						}
					}
					//把处理好的文本写入到新的文本文件中
					Result2Txt(tarFilePath,String.valueOf(isMatch));
					queryNumEngList.clear();
//					titleNumEngSet.clear();
					isMatch = 1;
					if (linenu%1000 ==0) {
						System.out.println("hasProcessed text numbers:" + linenu);
					}
				}
				read.close();
				System.out.println("hasProcessed text numbers:" + linenu);
			} else {
				System.out.println("can't find the file");
			}
		} catch (Exception e) {
			System.out.println("something error when reading the content of the file");
			e.printStackTrace();
		}
		return;
	}

	private void addComSylSet() {
		comSylSet.add("de"); comSylSet.add("ba");
		comSylSet.add("ma"); comSylSet.add("cai");
		comSylSet.add("hou"); comSylSet.add("shi");
		comSylSet.add("gao"); comSylSet.add("kao");
		comSylSet.add("cao"); comSylSet.add("zuo");
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
		//处理标注数据
	    String scrData=dataPathStr+"Data/Step2_1_a_7_labeledData.txt";
	    String tarFilePath=dataPathStr+"feaSet/Step2_3_a_labeledData.txt";
	    //处理未标注数据
//	    String scrData=dataPathStr+"Data/Step2_1_a_7_unlabeledData.txt";
//	    String tarFilePath=dataPathStr+"feaSet/Step2_3_a_unlabeledData.txt";
	    
	    Step2_3_a_matchNumAndEnglish fr=new Step2_3_a_matchNumAndEnglish();
        long readstart=System.currentTimeMillis();
        fr.Read_Write(scrData, tarFilePath);
        long readend=System.currentTimeMillis();
        System.out.println((readend-readstart)/1000.0+"s had been consumed to filter IT suffix");
	}
}
