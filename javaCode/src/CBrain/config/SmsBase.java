package CBrain.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

public class SmsBase {

	private static HashSet<Integer> symbolSet = new HashSet<Integer>();
	private static HashMap<String, String> synMap = new HashMap<String,String>();
	
	public static void SmsBaseLoadConfig() throws Exception {
		System.out.println("+++{Start load the config}+++");
		File symbolsDicFile = new File("./../library/SMSSymbolsFilter.dic");
		File synWordsFile = new File("./../library/哈工大信息检索研究中心同义词词林扩展版.txt");
		loadSymbolsDictionary(symbolsDicFile);
		loadsynWords(synWordsFile);
	}
	public static HashSet<Integer> getSymbolSet() {return symbolSet;}
	public static HashMap<String, String> getsynWordsMap() {return synMap;}
	
	@SuppressWarnings("resource")
	private static void loadSymbolsDictionary(File symbolsDicFile) throws IOException {
		FileInputStream fis = null;
		BufferedReader reader = null;
		System.out.println("+++[ start to load symbols dictionary ... ]+++");
		try {
			fis = new FileInputStream(symbolsDicFile);
			reader = new BufferedReader(new InputStreamReader(
					fis, "UTF-8"));
			String ss = null;

			while ((ss = reader.readLine()) != null) {
				char[] testChars = ss.toCharArray();
				if (testChars.length!=1) {
					System.out.println("++++++++ Warning:" + testChars.length);
				}
				symbolSet.add((int)testChars[0]);
			}
		} catch (IOException e) {
			reader.close();
			fis.close();
			e.printStackTrace();
			throw(e);
		}
		System.out.println("+++[ loading symbols dictionary done! ]+++");
	}

	@SuppressWarnings("resource")
	private static void loadsynWords(File synWordsFile) throws IOException {
		FileInputStream fis = null;
		BufferedReader reader = null;
		System.out.println("+++[ start to load synonym words ... ]+++");
		try {
			fis = new FileInputStream(synWordsFile);
			reader = new BufferedReader(new InputStreamReader(
					fis, "UTF-8"));
			String lineStr = null;
			String normCodeStr = null;
			String synWordsStr = null;
			while ((lineStr = reader.readLine()) != null) {
				if (lineStr.contains("=")) {
					String[] tmpStrs = lineStr.split("=");
					normCodeStr = tmpStrs[0].trim();
					synWordsStr = tmpStrs[1].trim();
					String[] tmpSynWordsStrs = synWordsStr.split("\\s+");
					for (int i = 0; i < tmpSynWordsStrs.length; i++) {
						//如果已经加载过此词则不再加载
						if (!synMap.containsKey(tmpSynWordsStrs[i].trim())) {
							synMap.put(tmpSynWordsStrs[i].trim(), normCodeStr);
						}
					}
				}
			}
		} catch (IOException e) {
			reader.close();
			fis.close();
			e.printStackTrace();
			throw(e);
		}
		System.out.println("+++[ loading symbols dictionary done! ]+++");
	}
	
	public static void main (String [] args){
		File symbolsDicFile = new File("./symbols.dic");
		try {
			loadSymbolsDictionary(symbolsDicFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ss ="℃＄¤￠￡‰§№☆★○●◎◇◆□■△▲※→←↑↓〓ⅰⅱ我ⅲⅳⅴⅵⅶ北 京ⅷⅸⅹ⒈⒉，你呢？⒊⒋⒌⒍⒎⒏";
		StringBuffer str = new StringBuffer();
		char test1 = 0x25;
		System.out.println(test1);    
		char[] ch = ss.toCharArray();

		System.out.println(ss);
		System.out.println(str.toString());
	}
}
