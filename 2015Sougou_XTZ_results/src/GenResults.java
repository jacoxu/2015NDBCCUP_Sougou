import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GenResults {
	/**
	 * @param args
	 * @author jacoxu-2015/04/26
	 * @param scoreData 
	 */

	public void Read_Write(String rawData, String scoreData, String tarFilePath) {
		try {
			String encoding = "GBK";
			File rawFile = new File(rawData);
			File scoreFile = new File(scoreData);
			if (rawFile.isFile() && rawFile.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(rawFile), encoding);
				BufferedReader rawDataReader = new BufferedReader(read);
				InputStreamReader read2 = new InputStreamReader(
						new FileInputStream(scoreFile), encoding);
				BufferedReader scoreDataReader = new BufferedReader(read2);
				String rawDataText = null;
				String scoreDataText = null;
				String tmpQueryStr = null;
				String tmpTitleStr = null;
				String tmpScoreStr = null;
				int linenu = 0;
				while ((rawDataText = rawDataReader.readLine()) != null) {
					linenu++;
					if (linenu==1) continue;
					scoreDataText = scoreDataReader.readLine();
					rawDataText = rawDataText +"\t" + String.valueOf(Double.valueOf(scoreDataText));
					
					Result2Txt(tarFilePath, rawDataText);
					if (linenu % 1000 == 0) {
						System.out.println("hasProcessed text numbers:"
								+ linenu);
					}
				}
				read.close();
				read2.close();
				System.out.println("hasProcessed text numbers:" + linenu);
			} else {
				System.out.println("can't find the file");
			}
		} catch (Exception e) {
			System.out
					.println("something error when reading the content of the file");
			e.printStackTrace();
		}
		return;
	}

	public void Result2Txt(String file, String txt) {
		try {
			BufferedWriter os = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(file), true), "GBK"));
			os.write(txt + "\n");
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String dataPathStr = "./../";
		// 开始合并结果集合
		String rawData = dataPathStr + "2015短文本语义相关度比赛评测数据集.txt";
		String scoreData = dataPathStr + "predictRandomForest_labelP.txt";
		String tarFilePath = dataPathStr + "results.txt";

		GenResults fr = new GenResults();
		long readstart = System.currentTimeMillis();
		fr.Read_Write(rawData, scoreData, tarFilePath);
		long readend = System.currentTimeMillis();
		System.out.println((readend - readstart) / 1000.0
				+ "s had been consumed to process the raw data");
	}
}
