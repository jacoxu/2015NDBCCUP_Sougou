
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.Buffer;
import java.util.HashSet;
import java.util.Iterator;

import CBrain.config.SmsBase;
import CBrain.tools.SplitNGram;
import CBrain.tools.StringAnalyzer;
import TypeTrans.*;

public class Step2_1_a_5_filterSpecialSymbol {
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
				
				while ((lineTXT = bufferedReader.readLine()) != null) {
					linenu++;
					//首先进行类型转换
					String[] Segs_11 = lineTXT.split("\t");///此处分为3段，Query、title、lable
					tmpQueryStr = Segs_11[0];
					tmpTitleStr = Segs_11[1];
					tmpScoreStr = Segs_11[2];
					
					tmpQueryStr = StringAnalyzer.extractGoodCharacter(tmpQueryStr);
					tmpTitleStr = StringAnalyzer.extractGoodCharacter(tmpTitleStr);
					
					tmpContentBuffer.append(tmpQueryStr+"\t"+tmpTitleStr+"\t"+tmpScoreStr);
					//把处理好的文本写入到新的文本文件中
					Result2Txt(tarFilePath,tmpContentBuffer.toString());
					tmpContentBuffer.delete(0, tmpContentBuffer.length());
					
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
//	    String scrData=dataPathStr+"Data/Step2_1_a_4_labeledData.txt";
//	    String tarFilePath=dataPathStr+"Data/Step2_1_a_5_labeledData.txt";
	    //处理未标注数据
	    String scrData=dataPathStr+"Data/Step2_1_a_4_unlabeledData.txt";
	    String tarFilePath=dataPathStr+"Data/Step2_1_a_5_unlabeledData.txt";
	    
	    Step2_1_a_5_filterSpecialSymbol fr=new Step2_1_a_5_filterSpecialSymbol();
        long readstart=System.currentTimeMillis();
        fr.Read_Write(scrData, tarFilePath);
        long readend=System.currentTimeMillis();
        System.out.println((readend-readstart)/1000.0+"s had been consumed to filter IT suffix");
	}
}
