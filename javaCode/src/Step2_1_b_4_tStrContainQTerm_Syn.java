
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

import CBrain.tools.DivideWord;
import CBrain.tools.SplitNGram;
import TypeTrans.*;

public class Step2_1_b_4_tStrContainQTerm_Syn {
	/**
	 * @param args
	 * @author jacoxu-2015/04/26
	 * @param tarFeaPath 
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
				
				double tmpTermNum = 0.0;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					linenu++;
//					if (linenu==199) {
//						System.out.println("Here is a pause ...");
//					}
					String[] Segs_11 = lineTXT.split("\t");///此处分为3段，Query、title、lable
					tmpQueryStr = Segs_11[0];
					tmpTitleStr = Segs_11[1];
					tmpScoreStr = Segs_11[2];
					
					//开始判断特征
					//先对Query进行分词，预处理阶段a.7以通过ansj分词完毕，因而只需要空格分割即可
					String[] tmpTermList = tmpQueryStr.split("\\s+");
					for (int i = 0; i < tmpTermList.length; i++) {
						if (tmpTitleStr.contains(tmpTermList[i])) {
							tmpTermNum=tmpTermNum+1;
						}
					}
					//进行归一
					tmpTermNum = tmpTermNum/(double)tmpTermList.length;
				    
					//把处理好的文本写入到新的文本文件中
					Result2Txt(tarFilePath,String.valueOf(tmpTermNum));
					tmpTermNum = 0.0;
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
		String dataPathStr="./../";
		//处理标注数据
	    String scrData=dataPathStr+"Data/Step2_1_a_8_labeledData.txt";
	    String tarFilePath=dataPathStr+"feaSet/Step2_1_b_4_labeledDataFea_Syn.txt";	
		
	    //处理未标注数据
//	    String scrData=dataPathStr+"Data/Step2_1_a_8_unlabeledData.txt";
//	    String tarFilePath=dataPathStr+"feaSet/Step2_1_b_4_unlabeledDataFea_Syn.txt";	
	    
	    Step2_1_b_4_tStrContainQTerm_Syn fr=new Step2_1_b_4_tStrContainQTerm_Syn();
        long readstart=System.currentTimeMillis();
        fr.Read_Write(scrData, tarFilePath);
        long readend=System.currentTimeMillis();
        System.out.println((readend-readstart)/1000.0+"s had been consumed to filter IT suffix");
	}
}
