
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.nio.Buffer;
//import java.util.HashSet;
//import java.util.Iterator;
//
//import CBrain.tools.SplitNGram;
//import TypeTrans.*;

public class Step2_1_a_6_tansNum2Char {
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
//				String tmpQueryStr = null;
//				String tmpTitleStr = null;
//				String tmpScoreStr = null;
				int linenu=0;
//				StringBuffer tmpContentBuffer = new StringBuffer();
				
				while ((lineTXT = bufferedReader.readLine()) != null) {
					linenu++;
//					String[] Segs_11 = lineTXT.split("\t");///此处分为3段，Query、title、lable
//					tmpQueryStr = Segs_11[0];
//					tmpTitleStr = Segs_11[1];
//					tmpScoreStr = Segs_11[2];
//					
//					lineTXT=tmpQueryStr+"\t"+tmpTitleStr;
//					//拆分开找数字
//					char[] val = String.valueOf(lineTXT).toCharArray();
//				    for (int i = 0; i < val.length; i++) {
//				    	String inputsString=val[i]+"";
//				    	boolean re=IsNum.isNumeric3(inputsString);
//				    	if (re) {
//				    		tmpContentBuffer.append(Num2Character.N2C(inputsString));
//				    	}else {
//				    		tmpContentBuffer.append(inputsString);
//				    	}
//				    }
//				    tmpContentBuffer.append("\t"+tmpScoreStr);
				    
					
					lineTXT = lineTXT.replaceAll("零", "0");
					lineTXT = lineTXT.replaceAll("壹|一", "1");
					lineTXT = lineTXT.replaceAll("贰|二", "2");
					lineTXT = lineTXT.replaceAll("叁|三", "3");
					lineTXT = lineTXT.replaceAll("肆|四", "4");
					lineTXT = lineTXT.replaceAll("伍|五", "5");
					lineTXT = lineTXT.replaceAll("陆|六", "6");
					lineTXT = lineTXT.replaceAll("柒|七", "7");
					lineTXT = lineTXT.replaceAll("捌|八", "8");
					lineTXT = lineTXT.replaceAll("玖|九", "9");
					
					//把处理好的文本写入到新的文本文件中
					Result2Txt(tarFilePath,lineTXT);
//					tmpContentBuffer.delete(0, tmpContentBuffer.length());
					
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
//	    String scrData=dataPathStr+"Data/Step2_1_a_5_labeledData.txt";
//	    String tarFilePath=dataPathStr+"Data/Step2_1_a_6_labeledData.txt";
	    //处理未标注数据
	    String scrData=dataPathStr+"Data/Step2_1_a_5_unlabeledData.txt";
	    String tarFilePath=dataPathStr+"Data/Step2_1_a_6_unlabeledData.txt";
	    
	    Step2_1_a_6_tansNum2Char fr=new Step2_1_a_6_tansNum2Char();
        long readstart=System.currentTimeMillis();
        fr.Read_Write(scrData, tarFilePath);
        long readend=System.currentTimeMillis();
        System.out.println((readend-readstart)/1000.0+"s had been consumed to filter IT suffix");
	}
}
