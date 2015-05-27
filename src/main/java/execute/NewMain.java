package execute;

import utils.PropertyRetriever;

import java.io.*;
import java.util.*;

/**
 * Created by Szymon on 2015-05-23.
 */
public class NewMain {

	String inputFileName;
	String inputAbsolute;
	String output;
	PropertyRetriever pr;
	Map<String, Long> classifiedWords;
	List<String> allWords;
	TreeMap<String,Long> sortedClassifiedWords;
	ValueComparator comparator;
	long maxOccurances = 0;

	private final String FILE_INPUT_PROPERTY = "file.input";
	private final String FILE_OUTPUT_PROPERTY = "file.output";
	private final String OUTPUT_PATH = "OUTPUT/";
	private final String INPUT_PATH = "INPUT/";
	private final String OUT_PREFIX = "OUT ";



	private final String UTF8 = "UTF-8";

	private String[] stopWords = {"-", " ", ""};

	public NewMain() {
		pr = new PropertyRetriever();
	}

	public boolean start() {
		setFileNamesFromProperties();
		setPathForOutput();
		setPathForInput();

		System.out.println("Fetching words from: " + inputAbsolute);
		allWords = fetchWordsFromFile();
		classifiedWords = countWords();

		comparator =  new ValueComparator(classifiedWords);
		sortedClassifiedWords = new TreeMap<>(comparator);
		sortedClassifiedWords.putAll(classifiedWords);

		System.out.println("Writing words to: " + output);
		writeOutWords();
		return true;
	}

	private void setPathForOutput() {
		output = OUTPUT_PATH + OUT_PREFIX + inputFileName;
	}

	private void setPathForInput() {
		inputAbsolute = INPUT_PATH + inputFileName;
	}
	private void writeOutWords() {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), UTF8));
			writeLine(writer, "NUMBER OF WORDS: " + sortedClassifiedWords.entrySet().size());
			writer.newLine();
			int i =0;
			for(Map.Entry<String, Long> entry : sortedClassifiedWords.entrySet()) {
				i++;
				writeLine(writer, entry.getKey() + " - " + entry.getValue() + " | " + maxOccurances + "/" + i + " = " + maxOccurances/i);
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("Something went wrong during writing words to file");
		}
	}

	private void writeLine(BufferedWriter writer, String text) throws Exception {
		writer.write(text);
		writer.newLine();
	}

	private HashMap<String, Long> countWords() {
		HashMap<String, Long> countedWords = new HashMap<>();
		for(int i = 0; i< allWords.size() ; i ++) {
			if(!countedWords.containsKey(allWords.get(i))) {
				countedWords.put(allWords.get(i), new Long(1));
			}
			else {
				long numberOfOccurances = countedWords.get(allWords.get(i)) + 1;
				countedWords.replace(allWords.get(i), numberOfOccurances);
				if(numberOfOccurances > maxOccurances) {
					maxOccurances = numberOfOccurances;
				}
			}
		}
		return countedWords;
	}

	private ArrayList<String> fetchWordsFromFile() {
		ArrayList<String> allWordsList = new ArrayList<>();
		try {
			FileInputStream fstream = new FileInputStream(inputAbsolute);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			while ((strLine = br.readLine()) != null) {
				for(String word : strLine.split(" ")) {
					if(Arrays.asList(stopWords).contains(word)) {
						break;
					}
					allWordsList.add(formatWord(word));
				}
			}
			in.close();
		} catch (Exception e) {
			System.out.println("Something went wrong during fetching words from file");
		}
		return allWordsList;
	}



	private String formatWord(String word) {
		return word.replace(".", "").replace(",", "").replace("!", "").replace("?", "").replace(";", "").replace("\n", "").replace("\r", "").toLowerCase();
	}


	class ValueComparator implements Comparator<String> {

		Map<String, Long> base;
		public ValueComparator(Map<String, Long> base) {
			this.base = base;
		}

		public int compare(String a, String b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	private void setFileNamesFromProperties(){
		inputFileName = pr.getProperty(FILE_INPUT_PROPERTY);
	}
}
