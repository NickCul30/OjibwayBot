package TwitBot;
import java.util.*;
import java.io.*;

public class PDFInit{
	public static void main(String[]args) throws IOException{
		Scanner agenda = new Scanner(new BufferedReader(new FileReader("agenda.txt")));
		String link = "http://www.citywindsor.ca/cityhall/City-Council-Meetings/Meetings-This-Week/Documents/public agenda feb 6 2017 with items page numbers.pdf";
		int page = 1;

		ArrayList<Line> lines = new ArrayList<Line>();

		while(agenda.hasNextLine()){
			String line = agenda.nextLine();
			if(line.indexOf('') != -1) page ++;
			lines.add(new Line(line, page));
		}

		ArrayList<String> ojib = findWord("ojibway", lines, link);

		for(String l : ojib){
			System.out.println(l + '\n');
		}
	}

	public static ArrayList<String> findWord(String word, ArrayList<Line> dict, String link){
		ArrayList<String> messages = new ArrayList<String>();

		for(Line l : dict){
			String tmp = l.getContent();
			if(tmp.toLowerCase().contains(word.toLowerCase())){
				messages.add("'" + tmp + "' on Page " + l.getPage() + "\n" + link + "#page=" + l.getPage());
			}
		}
		return messages;
	}
}