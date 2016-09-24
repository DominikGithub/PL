package top;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

import model.Formula;
import top.misc.ICnfConvertable;
import top.valuation.ValuationSet;

/**
 * Main class.</br>
 * Expects a PL sentence (one per line) in input.txt.</br>
 * 
 * </br> An optional parameter can redirect the ouput
 * to the specified file: "java -jar Ex3 output.txt"</br> 
 * If none was given results will be printed to the console.</br>
 * 
 * </br> input.txt - Lines starting with:</br> 
 * // header line, will not be evaluated but printed to results</br> 
 * # comment, will be totally ignored</br>  
 *  
 * @author Dominik
 */
public class Ex3 {

	private static final String IN_COMMENT = "#";
	private static final String IN_SECTION_HEADER = "//";
	private static final String IN_SRC_FILE = "src/input.txt";
	private static final int colWidth = 13;

	public static void main(String[] args) {
		ArrayList<String> tokens, lines;
		Formula<?> model, evaluated;
		String sModel;
		boolean satisfiable, unsatisfiable, validity;
		int maxSentenceLen;
		StringBuilder builder = new StringBuilder();
		lines = readInputFile(IN_SRC_FILE);
		{
			maxSentenceLen = lines.stream()
					.max(Comparator.comparing(i -> i.toString().length()))
					.get().length()+2;
			builder.append(String.format("%-"+maxSentenceLen+"s %-"+
					colWidth+"s %-"+colWidth+"s %-"+colWidth+"s %-"+colWidth+"s\n", 
					"Model", "Eval/Entails", "Satisfiable", "Unsatisfiable", "Tautology"));
		}
		try {
			for(String stmt : lines){
				if(stmt.length() > 0) {
					if(stmt.startsWith(IN_SECTION_HEADER)) 
						builder.append(stmt+"\n");
					else {
						tokens = new Lexer().lex(stmt);
//						System.out.println("Tokens: "+tokens);
						model = new Parser(tokens).parse();
						sModel = model.toString();
						evaluated = model.convert();
						satisfiable = ValuationSet.satisfiable(model);
						unsatisfiable = ValuationSet.unsatisfiable(model);
						validity = ValuationSet.tautology(model);
						builder.append(String.format("%-"+maxSentenceLen+"s %-"+
								colWidth+"s %-"+colWidth+"s %-"+colWidth+"s %-"+colWidth+"s\n", 
								sModel, evaluated, satisfiable, unsatisfiable, validity));
					}
				}
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		if(args.length > 0)	writeOutResult(args[0], builder.toString());
		else				System.out.println(builder.toString());
	}

	/**
	 * write out results to file
	 * @param fileName
	 * @param content
	 */
	private static void writeOutResult(String fileName, String content) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName);
			writer.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(writer != null)
				writer.close();
		}
	}

	/**
	 * read input from file
	 * @param fileName
	 * @return input
	 */
	private static ArrayList<String> readInputFile(String fileName){
		FileReader fr = null;
		BufferedReader br = null;
		String line;
		ArrayList<String> input = new ArrayList<String>();
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			while((line=br.readLine()) != null)
				if(!line.startsWith(IN_COMMENT))
					input.add(line);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
				if(fr != null) fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return input;
	}
	
}
