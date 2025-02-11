
/**************************************
 * Author       : Vinayak Salavi
 * Arguments    : two command line argments are required to pass while executing the code.
 *                  1. Path to input files
 *                  2. Destination folder path where output will be stored.
 * ***/
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class project_phase4 {

	public static TreeMap<String, Integer> testMap = new TreeMap<>();
	public static String outputRepository, inputRepository;
	public static TreeMap<String, List<TreeMap<Integer, Integer>>> indexMap = new TreeMap<>();
	public static TreeMap<String, TreeMap<Integer, Double>> weightIndexMap = new TreeMap<>();
	public static TreeMap<Integer, Double> numeratorDocumentWeightsMap = new TreeMap<>();
	public static TreeMap<Integer, Double> denominatorDocumentWeightsMap = new TreeMap<>();
	public static TreeMap<Integer, Double> similarityMap = new TreeMap<>();
	public static TreeMap<Integer, Integer> tempStoreMap = null;
	public static List<TreeMap<Integer, Integer>> tempStoreList = new ArrayList<>();
	public static int[] wordCountPerDocArr = new int[503];
	public static List<String> keyOfOneOccurance = new ArrayList<String>();
	public static List<Integer> interval = new ArrayList<>(Arrays.asList(10, 20, 40, 80, 100, 200, 300, 400, 503));
	public static float averageDoc;
	public static int totalDoc = 503;
	public static double k = 1;
	public static TreeMap<Integer, TreeSet<Double>> docWeightsMap = new TreeMap<>();

	public static List<String> getStopWords() {
		String stopWrds = "a#about#above#according#across#actually#adj#after#afterwards#again#against#all#almost#alone#along#already#also#although#always#among#amongst#an#and#another#any#anybody#anyhow#anyone#anything#anywhere#are#area#areas#aren't#around#as#ask#asked#asking#asks#at#away#b#back#backed#backing#backs#be#became#because#become#becomes#becoming#been#before#beforehand#began#begin#beginning#behind#being#beings#below#beside#besides#best#better#between#beyond#big#billion#both#but#by#c#came#can#can't#cannot#caption#case#cases#certain#certainly#clear#clearly#co#come#could#couldn't#d#did#didn't#differ#different#differently#do#does#doesn't#don't#done#down#downed#downing#downs#during#e#each#early#eg#eight#eighty#either#else#elsewhere#end#ended#ending#ends#enough#etc#even#evenly#ever#every#everybody#everyone#everything#everywhere#except#f#face#faces#fact#facts#far#felt#few#fifty#find#finds#first#five#for#former#formerly#forty#found#four#from#further#furthered#furthering#furthers#g#gave#general#generally#get#gets#give#given#gives#go#going#good#goods#got#great#greater#greatest#group#grouped#grouping#groups#h#had#has#hasn't#have#haven't#having#he#he'd#he'll#he's#hence#her#here#here's#hereafter#hereby#herein#hereupon#hers#herself#high#higher#highest#him#himself#his#how#however#hundred#i#i'd#i'll#i'm#i've#ie#if#important#in#inc#indeed#instead#interest#interested#interesting#interests#into#is#isn't#it#it's#its#itself#j#just#k#l#large#largely#last#later#latest#latter#latterly#least#less#let#let's#lets#like#likely#long#longer#longest#ltd#m#made#make#makes#making#man#many#may#maybe#me#meantime#meanwhile#member#members#men#might#million#miss#more#moreover#most#mostly#mr#mrs#much#must#my#myself#n#namely#necessary#need#needed#needing#needs#neither#never#nevertheless#new#newer#newest#next#nine#ninety#no#nobody#non#none#nonetheless#noone#nor#not#nothing#now#nowhere#number#numbers#o#of#off#often#old#older#oldest#on#once#one#one's#only#onto#open#opened#opens#or#order#ordered#ordering#orders#other#others#otherwise#our#ours#ourselves#out#over#overall#own#p#part#parted#parting#parts#per#perhaps#place#places#point#pointed#pointing#points#possible#present#presented#presenting#presents#problem#problems#put#puts#q#quite#r#rather#really#recent#recently#right#room#rooms#s#said#same#saw#say#says#second#seconds#see#seem#seemed#seeming#seems#seven#seventy#several#she#she'd#she'll#she's#should#shouldn't#show#showed#showing#shows#sides#since#six#sixty#small#smaller#smallest#so#some#somebody#somehow#someone#something#sometime#sometimes#somewhere#state#states#still#stop#such#sure#t#take#taken#taking#ten#than#that#that'll#that's#that've#the#their#them#themselves#then#thence#there#there'd#there'll#there're#there's#there've#thereafter#thereby#therefore#therein#thereupon#these#they#they'd#they'll#they're#they've#thing#things#think#thinks#thirty#this#those#though#thought#thoughts#thousand#three#through#throughout#thru#thus#to#today#together#too#took#toward#towards#trillion#turn#turned#turning#turns#twenty#two#u#under#unless#unlike#unlikely#until#up#upon#us#use#used#uses#using#v#very#via#w#want#wanted#wanting#wants#was#wasn't#way#ways#we#we'd#we'll#we're#we've#well#wells#were#weren't#what#what'll#what's#what've#whatever#when#whence#whenever#where#where's#whereafter#whereas#whereby#wherein#whereupon#wherever#whether#which#while#whither#who#who'd#who'll#who's#whoever#whole#whom#whomever#whose#why#will#with#within#without#won't#work#worked#working#works#would#wouldn't#x#y#year#years#yes#yet#you#you'd#you'll#you're#you've#young#younger#youngest#your#yours#yourself#yourselves#z";
		stopWrds = stopWrds.replaceAll("'", "");
		return Arrays.asList(stopWrds.split("#"));
	}

	public static void html_reader() {

		int fileCount = 0;
		Instant startTime, endTime;
		// Store the list of files available in input repository
		File dir = new File(inputRepository);
		File[] listOfFiles = dir.listFiles();
		// iterate over each file
		if (listOfFiles != null) {
//			startTime = Instant.now();
			for (File file : listOfFiles) {

				fileCount++;

				Document htmlFile = null;
				try {
					// using Jsoup parser to parse the text from html file
					htmlFile = Jsoup.parse(file, null);

				} catch (IOException e) {
					e.printStackTrace();
				}

				// Remove all of the non-alphabetic characters from the parsed text and change
				// it to lowercase letters
				String text = htmlFile.text().replaceAll("[^A-Za-z ]", "").toLowerCase();

				// split the text stored by space
				String[] strArr = text.split(" ");

				// stored values in list to remove blanks, null and additional whitespace chars
				List<String> list = new ArrayList<String>(Arrays.asList(strArr));

				list.removeAll(Arrays.asList("", null, " "));

				// remove all stop words
				list.removeAll(getStopWords());
				for (String str : list) {
					if (str.length() == 1) {
						list.remove(str);
					}
				}

				// get the name of file (same name is used for output file)
				String name = file.getName().split("\\.")[0];

				// commented as calulating time for varying input instead while processing
//				if (interval.contains(fileCount)) {
//					endTime = Instant.now();
//					System.out.println("Preprocessed File count = " + fileCount + "\t Time required = "
//							+ Duration.between(startTime, endTime).toMillis());
//					startTime = endTime;
//				}
				// storing words and count in Treemap, which stores the keys in sorted order
				for (String str : list) {
					// skip words of length 1
					if (str.length() < 2) {
						continue;
					}
					if (testMap.containsKey(str.trim())) {
						int count = testMap.get(str) + 1;
						testMap.put(str, count);
					} else {
						testMap.put(str, 1);
					}

				}
				// store total word count of file
				wordCountPerDocArr[fileCount - 1] = list.size();

				testMap.forEach((k, v) -> {
					// tempStoreMap< doc id, count of word>
					tempStoreMap = new TreeMap<>();
					tempStoreList = new ArrayList<>();
					int existingWordCount = 0;
					if (null != indexMap.get(k)) {
						tempStoreList.addAll(indexMap.get(k));
						existingWordCount = getExistingWordCount(tempStoreList);
					}
					if (v - existingWordCount > 0) {
						tempStoreMap.put(Integer.valueOf(name), v - existingWordCount);
						tempStoreList.add(tempStoreMap);
						indexMap.put(k, tempStoreList);
					}

				});

			}

		}

	}

	// remove words occuring only once in entire corpus
	private static void reduceCorpus() {
		// first store the keys of words occuring only once
		indexMap.forEach((k, v) -> {
			tempStoreList = new ArrayList<>();
			tempStoreList = v;
			// first check if word is in only one document
			if (tempStoreList.size() == 1) {
				// if word is only in one document fetch its count
				tempStoreMap = new TreeMap<>();
				tempStoreMap = tempStoreList.get(0);
				int wordCount = tempStoreMap.get(tempStoreMap.firstKey());

				// if wordcount is one, store corresponding key for further removal and update
				// total word count matrix
				if (wordCount == 1) {
					int documentID = tempStoreMap.firstKey();
					// update total word count for respective document
					wordCountPerDocArr[documentID - 1] -= wordCount;
					keyOfOneOccurance.add(k);

				}

			}

		});

		// remove words
		for (String key : keyOfOneOccurance) {
			testMap.remove(key);
			indexMap.remove(key);
		}
	}

	public static float getAverageDoc() {
		float sum = 0;
		for (int i : wordCountPerDocArr) {
			sum += i;
		}

		return sum / (float) totalDoc;
	}

	// function calculate term weights and create dictionary and posting files
	private static void invertedIndexProcessing() {
		double tf, idf, weight;
//		Instant startTime, endTime;
		averageDoc = getAverageDoc();
		int currLineNumber = 1;
		int counter = 0;
		TreeMap<Integer, Double> tempWeightIndexMap;
		try {
			// create files for posting and dictionary
			FileWriter postingFileWriter = new FileWriter(outputRepository + "/output_files/posting.txt", false);
			FileWriter DictionaryFileWriter = new FileWriter(outputRepository + "/output_files/dictionary.txt", false);
//			startTime = Instant.now();
			int totalTerms = indexMap.keySet().size();

			for (String key : indexMap.keySet()) {
				counter++;
				tempStoreList = new ArrayList<>();
				tempStoreList = indexMap.get(key);
				if (key.equals("the"))
					System.out.println("the found");
				int DFj = tempStoreList.size();// Df(j)no of docs containing words
				tempWeightIndexMap = new TreeMap<Integer, Double>();
				for (TreeMap<Integer, Integer> map : tempStoreList) {

					int docID = map.firstKey();
					// added if condition to go doc wise
					int Cij = map.get(docID);// C(i,j)count of word in currenct doc
					// D(i) = wordCountPerDocArr[i] = total words in current doc
					// SOA formula for term frequency
					tf = Cij / (float) (Cij + (k * wordCountPerDocArr[docID - 1]) / averageDoc);
					// D = 503 ,
					idf = Math.log(totalDoc / (double) DFj);
					weight = tf * idf;
					tempWeightIndexMap.put(docID, weight);
					postingFileWriter.write(docID + "," + weight + "\n");

					TreeSet<Double> set = new TreeSet<>();
					if (docWeightsMap.get(docID) != null)
						set.addAll(docWeightsMap.get(docID));

					set.add(weight);
					docWeightsMap.put(docID, set);

				}
				weightIndexMap.put(key, tempWeightIndexMap);
				DictionaryFileWriter.write(key + "\n" + DFj + "\n" + currLineNumber + "\n");
				currLineNumber += DFj;
//				if (counter % 4000 == 0 || counter == totalTerms) {
//					endTime = Instant.now();
//					System.out.println("Processed Terms = " + counter + "\t Time required = "
//							+ Duration.between(startTime, endTime).toMillis());
//					startTime = endTime;
//				}
			}

			postingFileWriter.close();
			DictionaryFileWriter.close();

		} catch (Exception e) {
			System.out.println("Exception occurred :" + e.getMessage());

		}
	}

	private static int getExistingWordCount(List<TreeMap<Integer, Integer>> tempStoreList2) {
		// TODO Auto-generated method stub
		int listSize = tempStoreList2.size();
		int sum = 0;
		for (int i = 0; i < listSize; i++) {
			TreeMap<Integer, Integer> tempMap = tempStoreList2.get(i);
			for (Integer key : tempMap.keySet()) {
				sum += tempMap.get(key);
			}
		}
		return sum;
	}

	public static void main(String[] args) {

		// check in both arguments are provided
		int argcount = args.length;

		if (argcount > 2) {
			Instant start = Instant.now();
			TreeMap<String, Float> queryMap = new TreeMap<>();
			inputRepository = args[0];
			outputRepository = args[1];

			for (int i = 1; i < 504; i++) {
				numeratorDocumentWeightsMap.put(i, 0d);
				denominatorDocumentWeightsMap.put(i, 0d);
			}

			if (args[2].equalsIgnoreCase("wt")) {
				for (int i = 3; i < argcount; i++) {
					// input is of form wt1 word1 wt2 word2
					String key = "";
					float weight = 0;
					if (i % 2 == 0) {
						weight = Float.valueOf(args[i - 1]);
						key = args[i].toLowerCase();
						queryMap.put(key, weight);
					}

				}
			} else {
				for (int i = 3; i < argcount; i++) {
					// input is of form wt1 word1 wt2 word2
					if (null != queryMap.get(args[i]))
						queryMap.put(args[i].toLowerCase(), 1.0f);
					else {
						queryMap.put(args[i].toLowerCase(), queryMap.get(args[i]) + 1.0f);
					}
				}

			}
			// create o/p repo
			File directory = new File(outputRepository + "/output_files");
			directory.mkdir();
			// read data from html files and create inverted index
			html_reader();
			// call function to remove words of one occurance
			reduceCorpus();
			invertedIndexProcessing();
//			int queryWordCount = queryMap.keySet().size();
//			
			for (String key : queryMap.keySet()) {
				double docWeightSquaredSum = 0d;
				TreeMap<Integer, Double> tempMap = weightIndexMap.get(key);
				if (null != tempMap) {
					for (int docID : tempMap.keySet()) {
						double currentWeight = tempMap.get(docID);
						docWeightSquaredSum += Math.pow(currentWeight, 2);
						double product = currentWeight * queryMap.get(key);
//					numerator values
						numeratorDocumentWeightsMap.put(docID, numeratorDocumentWeightsMap.get(docID) + product);
//					 denominator values wt(i,ink)^2 + wt(i, pink)^2
						denominatorDocumentWeightsMap.put(docID,
								denominatorDocumentWeightsMap.get(docID) + docWeightSquaredSum);
					}
				} else {
					System.out.println("word " + key + " not found in corpus\n");
					continue;
				}
			}

			double queryWeightSquaredSum = 0d;

			for (String key : queryMap.keySet()) {
				queryWeightSquaredSum += Math.pow(queryMap.get(key), 2);
			}

			for (int docID : numeratorDocumentWeightsMap.keySet()) {

				double numerator = numeratorDocumentWeightsMap.get(docID);
				double denominator_DocPart = denominatorDocumentWeightsMap.get(docID);
				double similarityScore = calaulateCosineSimilarity(numerator, denominator_DocPart,
						queryWeightSquaredSum);
//				System.out.println(docID+"\t"+similarityScore);
				similarityMap.put(docID, similarityScore);

			}

			// sort the treemap based on values
			LinkedHashMap<Integer, Double> mapSortedByValue = new LinkedHashMap<>();

			similarityMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					.forEachOrdered(x -> mapSortedByValue.put(x.getKey(), x.getValue()));

			System.out.println("DocID\tCosineSimilarity\t\t Top 10 TFIDF");
			System.out.println("####################################################################");
			int count = 0;
			for (int i : mapSortedByValue.keySet()) {
				double score = mapSortedByValue.get(i);
				if (count > 9 || score == 0)
					break;
				System.out.print("\n" + i + "\t" + score);
				getTopTenTFIDF(i);
				count++;
			}

			Instant end = Instant.now();
			Duration timeElapsed = Duration.between(start, end);
			System.out.println("\nTime taken for code to execute: " + timeElapsed.toMillis() + " milliseconds");
		} else {
			System.out.println(
					"Please enter correct number of command line argument : input_repo_path and output_repo_path followed by weight and words");
		}

	}

	private static double calaulateCosineSimilarity(double numerator, double denominator_DocPart,
			double queryWeightSquaredSum) {
		// TODO Auto-generated method stub
		if (numerator == 0 || denominator_DocPart == 0)
			return 0;
		double denom = Math.sqrt(queryWeightSquaredSum) * Math.sqrt(denominator_DocPart);

		return numerator / denom;
	}

	private static void getTopTenTFIDF(int docID) {
		// TODO Auto-generated method stub
		TreeSet<Double> tfIDF = (TreeSet<Double>) docWeightsMap.get(docID).descendingSet();
		if (null != tfIDF) {
			int count = 0;
			for (Double wt : tfIDF) {
				if (count > 9) {
					break;
				}
				System.out.print("\n\t\t\t\t\t " + wt);
				count++;
			}
		}

	}
}

/****************
 * References: https://jsoup.org/
 * https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html
 * 
 */