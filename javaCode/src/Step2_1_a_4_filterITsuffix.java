
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

public class Step2_1_a_4_filterITsuffix {
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
				boolean hasContent = false;
				StringBuffer tmpContentBuffer = new StringBuffer();
				HashSet<String> query2TermSet = new HashSet<String>();
				
				while ((lineTXT = bufferedReader.readLine()) != null) {
					linenu++;
					String[] Segs_11 = lineTXT.split("\t");///此处分为3段，Query、title、lable
					tmpQueryStr = Segs_11[0];
					tmpTitleStr = Segs_11[1];
					tmpScoreStr = Segs_11[2];
					tranText2Set(tmpQueryStr,query2TermSet);
					tmpContentBuffer.append(tmpQueryStr+"\t");
					String[] tmpTitleArray= tmpTitleStr.split("–|—|-|_");//2.1.a.4-拆分后判断可疑垃圾冗余信息
					for (int i = 0; i < tmpTitleArray.length; i++) {
						String tmpQuerySnippet = tmpTitleArray[i].trim();
						if (tmpQuerySnippet.length()<1) continue;
						if (hasContent) {
							//判断是不是IT后缀垃圾无效信息
							if (isITSuffixSpamInfo(query2TermSet, tmpQuerySnippet)){
								continue;
							}else {
								tmpContentBuffer.append(" "+tmpQuerySnippet);	
							}
						}else {
							//如果还没有内容，则不需要判断是否垃圾，直接写入到Content中
							tmpContentBuffer.append(tmpQuerySnippet);
							hasContent = true;
						}
					}
					tmpContentBuffer.append("\t"+tmpScoreStr);
					//把过滤好的文本写入到新的文本文件中
					Result2Txt(tarFilePath,tmpContentBuffer.toString());
					tmpContentBuffer.delete(0, tmpContentBuffer.length());
					
					if (linenu%1000 ==0) {
						System.out.println("hasProcessed text numbers:" + linenu);
					}
					hasContent = false;
					
					query2TermSet.clear();
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

	private void tranText2Set(String tmpQueryStr, HashSet<String> query2TermSet) {
		String splitTextStr = SplitNGram.split2NGram(tmpQueryStr,2);
		String[] splitArraysStr = splitTextStr.split(" "); 
		for (int i = 0; i < splitArraysStr.length; i++) {
			if (splitArraysStr[i].trim().length()>0) {
				query2TermSet.add(splitArraysStr[i]);
			}
		}
	}

	private boolean isITSuffixSpamInfo(HashSet<String> query2TermSet, String tmpQuerySnippet) {
		String[] tmpQueryTerms = SplitNGram.split2NGram(tmpQuerySnippet, 2).split(" ");
		String queryTermStr;
		for (int i = 0; i < tmpQueryTerms.length; i++) {
			queryTermStr = tmpQueryTerms[i].trim();
			if ((queryTermStr.length()>0)&&query2TermSet.contains(queryTermStr)) 
				return false;
		}
		return true;
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
//	    String scrData=dataPathStr+"Data/Step2_1_a_1-2-3_labeledData.txt";
//	    String tarFilePath=dataPathStr+"Data/Step2_1_a_4_labeledData.txt";
	    //处理未标注数据
	    String scrData=dataPathStr+"Data/Step2_1_a_1-2-3_unlabeledData.txt";
	    String tarFilePath=dataPathStr+"Data/Step2_1_a_4_unlabeledData.txt";
	    
	    Step2_1_a_4_filterITsuffix fr=new Step2_1_a_4_filterITsuffix();
        long readstart=System.currentTimeMillis();
        fr.Read_Write(scrData, tarFilePath);
        long readend=System.currentTimeMillis();
        System.out.println((readend-readstart)/1000.0+"s had been consumed to filter IT suffix");
	}
}
