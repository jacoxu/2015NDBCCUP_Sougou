
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
import CBrain.tools.DivideWord;
import CBrain.tools.SplitNGram;
import CBrain.tools.StringAnalyzer;
import TypeTrans.*;

public class Step2_1_a_7_divideWords {
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
				String tmpQueryStr = null;
				String tmpTitleStr = null;
				String tmpScoreStr = null;
				int linenu=0;
				StringBuffer tmpContentBuffer = new StringBuffer();
				ArrayList<String> tmpQueryTermList = new ArrayList<String>();
				ArrayList<String> tmpTitleTermList = new ArrayList<String>();
								
				while ((lineTXT = bufferedReader.readLine()) != null) {
					linenu++;
					//首先进行类型转换
					String[] Segs_11 = lineTXT.split("\t");///此处分为3段，Query、title、lable
					tmpQueryStr = Segs_11[0];
					tmpTitleStr = Segs_11[1];
					tmpScoreStr = Segs_11[2];
					
					tmpQueryTermList = DivideWord.splitWord(tmpQueryStr);
					tmpTitleTermList = DivideWord.splitWord(tmpTitleStr);
					//添加查询Query的分词结果
					for (int i = 0; i < tmpQueryTermList.size(); i++) {
						if (i!=0) {
							tmpContentBuffer.append(" ");
						}
						tmpContentBuffer.append(tmpQueryTermList.get(i));						
					}
					tmpContentBuffer.append("\t");
					//添加查询Title的分词结果
					for (int i = 0; i < tmpTitleTermList.size(); i++) {
						if (i!=0) {
							tmpContentBuffer.append(" ");
						}
						tmpContentBuffer.append(tmpTitleTermList.get(i));						
					}
					tmpContentBuffer.append("\t");
					//添加相关度得分
					tmpContentBuffer.append(tmpScoreStr);
					//把处理好的文本写入到新的文本文件中
					Result2Txt(tarFilePath,tmpContentBuffer.toString());
					tmpContentBuffer.delete(0, tmpContentBuffer.length());
					tmpQueryTermList.clear();
					tmpTitleTermList.clear();
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
		try {
			SmsBase.SmsBaseLoadConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String dataPathStr="./../";
		//处理标注数据
	    String scrData=dataPathStr+"Data/Step2_1_a_6_labeledData.txt";
	    String tarFilePath=dataPathStr+"Data/Step2_1_a_7_labeledData.txt";
	    //处理未标注数据
//	    String scrData=dataPathStr+"Data/Step2_1_a_6_unlabeledData.txt";
//	    String tarFilePath=dataPathStr+"Data/Step2_1_a_7_unlabeledData.txt";
	    
	    Step2_1_a_7_divideWords fr=new Step2_1_a_7_divideWords();
        long readstart=System.currentTimeMillis();
        fr.Read_Write(scrData, tarFilePath);
        long readend=System.currentTimeMillis();
        System.out.println((readend-readstart)/1000.0+"s had been consumed to filter IT suffix");
	}
}
