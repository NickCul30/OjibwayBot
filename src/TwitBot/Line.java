package TwitBot;
public class Line{
	private String content;
	private int page;

	public Line(String l, int p){
		content = l;
		page = p;
	}

	public String getContent(){
		return content;
	}
	public int getPage(){
		return page;
	}

	public String toString(){
		return content + "," + page;
	}
}