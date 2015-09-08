
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

public class Step2_2_a_extractTags {
	public static void main(String[] args) throws Exception {
		//读取训练数据文件
		String dataPathStr="./../";
		//处理所有数据
		BufferedReader srcTrainFile = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(dataPathStr+
						"Data/Step2_1_a_7_labeledData.txt")), "UTF-8"));

		BufferedWriter trainLabelFileW = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(dataPathStr+
						"feaSet/Step2_2_a_tags.txt")), "UTF-8"));
		
		String tempLine;

		int lineNum = 0;
		System.out.println("Start to read tags of labeld data ...");
		while ((tempLine = srcTrainFile.readLine()) != null) {
			String[] Segs_11 = tempLine.split("\t");///此处分为3段，Query、title、lable
			
			trainLabelFileW.write(Segs_11[2].trim()+"\n");
			lineNum++;
			if (lineNum%1000 ==0) {
				System.out.println("hasProcessed text numbers:" + lineNum);
			}
		}

		System.out.println("Total lineNum:"+lineNum+" !");
		
		srcTrainFile.close();
		trainLabelFileW.close();
	}
}