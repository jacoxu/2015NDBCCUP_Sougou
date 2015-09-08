
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import TypeTrans.*;

public class Step2_1_a_1_transFull2Semi {
	/**
	 * @param args
	 * @author jacoxu-2015/04/26
	 */

	public void Read_Write(String rawData,String tarFilePath) {
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
					//首先进行类型转换
					String[] Segs_11 = lineTXT.split("\t");///此处分为3段，Query、title、lable
					tmpQueryStr = Segs_11[0];
					tmpTitleStr = Segs_11[1];
					tmpScoreStr = Segs_11[2];
					
					tmpQueryStr=Full2Half.ToDBC(tmpQueryStr);//2.1.a.1-全角转半角
					tmpTitleStr=Full2Half.ToDBC(tmpTitleStr);//2.1.a.1-全角转半角
									
					tmpQueryStr=tmpQueryStr.toLowerCase();//2.1.a.2-字母全部小写
					tmpTitleStr=tmpTitleStr.toLowerCase();//2.1.a.2-字母全部小写
					
					tmpQueryStr=tmpQueryStr.replaceAll("\\s+", " ");//2.1.a.3-多个空格缩成单个空格
					tmpTitleStr=tmpTitleStr.replaceAll("\\s+", " ");//2.1.a.3-多个空格缩成单个空格

					lineTXT=tmpQueryStr+"\t"+tmpTitleStr+"\t"+tmpScoreStr;

					Result2Txt(tarFilePath,lineTXT);
					if (linenu%1000 ==0) {
						System.out.println("hasProcessed text numbers:" + linenu);
					}
				};
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
		String dataPathStr="./../Data/";
		//处理标注数据
//	    String rawData=dataPathStr+"2015短文本语义相关度比赛标注数据集.txt";
//	    String tarFilePath=dataPathStr+"Step2_1_a_1-2-3_labeledData.txt";
	    //处理未标注数据
		String rawData=dataPathStr+"2015短文本语义相关度比赛评测数据集.txt";
	    String tarFilePath=dataPathStr+"Step2_1_a_1-2-3_unlabeledData.txt";
	    Step2_1_a_1_transFull2Semi fr=new Step2_1_a_1_transFull2Semi();
        long readstart=System.currentTimeMillis();
        fr.Read_Write(rawData, tarFilePath);
        long readend=System.currentTimeMillis();
        System.out.println((readend-readstart)/1000.0+"s had been consumed to process the raw data");
	}
}
