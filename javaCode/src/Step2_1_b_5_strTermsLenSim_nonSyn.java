
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

import CBrain.tools.SplitNGram;
import TypeTrans.*;

public class Step2_1_b_5_strTermsLenSim_nonSyn {
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
				
				while ((lineTXT = bufferedReader.readLine()) != null) {
					linenu++;
					String[] Segs_11 = lineTXT.split("\t");///此处分为3段，Query、title、lable
					tmpQueryStr = Segs_11[0];
					tmpTitleStr = Segs_11[1];
					tmpScoreStr = Segs_11[2];
					
					//开始判断特征
					int tmpQueryLen = tmpQueryStr.split("\\s+").length;
					int tmpTitleLen = tmpTitleStr.split("\\s+").length;
					
					//长度绝对值
					int tmpAbsLenDiff = Math.abs(tmpQueryLen - tmpTitleLen);
					double strLengthSim = 0;
					//进行长度归一
					if (tmpQueryLen>=tmpTitleLen) {
						strLengthSim = 1-((double)tmpAbsLenDiff/(double)tmpQueryLen);
					}else {
						strLengthSim = 1-((double)tmpAbsLenDiff/(double)tmpTitleLen);
					}
				    
					//把过滤好的文本写入到新的文本文件中
					Result2Txt(tarFilePath,String.valueOf(strLengthSim));
					
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
	    String scrData=dataPathStr+"Data/Step2_1_a_7_labeledData.txt";
	    String tarFilePath=dataPathStr+"feaSet/Step2_1_b_5_labeledDataFea_nonSyn.txt";	
		
	    //处理未标注数据
//	    String scrData=dataPathStr+"Data/Step2_1_a_7_unlabeledData.txt";
//	    String tarFilePath=dataPathStr+"feaSet/Step2_1_b_5_unlabeledDataFea_nonSyn.txt";	
	    
	    Step2_1_b_5_strTermsLenSim_nonSyn fr=new Step2_1_b_5_strTermsLenSim_nonSyn();
        long readstart=System.currentTimeMillis();
        fr.Read_Write(scrData, tarFilePath);
        long readend=System.currentTimeMillis();
        System.out.println((readend-readstart)/1000.0+"s had been consumed to filter IT suffix");
	}
}
