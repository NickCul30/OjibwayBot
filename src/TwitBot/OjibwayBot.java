package TwitBot; /**
 * Created by Matto on 2017-01-27.
 */

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import twitter4j.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class OjibwayBot {

    public static Twitter twitter;


    private static ArrayList<Line> lines = new ArrayList<Line>();
    private static ArrayList<String[]> docs = new ArrayList<String[]>();

    private static String link= "http://www.citywindsor.ca/cityhall/City-Council-Meetings/Meetings-This-Week/Documents/public%20agenda%20feb%206%202017%20with%20items%20page%20numbers.pdf";

    private static final String HELP="Current Commands\n" +
            "'!ping' Returns Pong\n"+
            "'!help' Returns the commands\n" +
            "'!find Line of text' Lists all occurrences of the given phrase in the agenda along with a link to the page it was found on\n" +
            "'!bestpage Word' Returns the page where the word was used the most\n" +
            "'!setAvatar password imgURL' changes the bot avatar if the correct password is given";

    public static void main(String[]args)throws TwitterException, IOException, InterruptedException{
        //access the twitter API using your twitter4j.properties file
        Scanner kb = new Scanner(System.in);
        twitter = TwitterFactory.getSingleton();


        //PROBABLY BROKEN SHIT
        ArrayList<String>pages = pdf("C:\\Users\\Matto\\Desktop\\OjibwayBot\\src\\TwitBot\\agenda.pdf");

        for(int i=0;i<pages.size();i++){
            String[] temp2 = pages.get(i).split("\n");
            for(String s2 : temp2){
                lines.add(new Line(s2,i));
            }
        }



        for(String s : pages){
            String[]words = s.split(" ");
            docs.add(words);
        }
        for(String[] s : docs){
            System.out.println(Arrays.toString(s));
        }



        // Reading msgs and deleting them
        Paging paging;
        List<DirectMessage>messages;
        paging = new Paging(1);
        messages=twitter.getDirectMessages(paging);
        for(DirectMessage msg : messages){
            //readCommand(twitter,msg);
            twitter.destroyDirectMessage(msg.getId());
        }
        while (true){
            paging = new Paging(1);
            messages=twitter.getDirectMessages(paging);
            for(DirectMessage msg : messages){
                    readCommand(twitter,msg);
                twitter.destroyDirectMessage(msg.getId());
            }
            try {
                Thread.sleep(5000);
            }catch (InterruptedException ie){
                System.out.println("interrupted");
            }

        }

        /*if(twitter.getDirectMessages()!=null){
            messages=twitter.getDirectMessages();
            for(DirectMessage msg : messages){
                System.out.println(msg.getText());
                twitter.destroyDirectMessage(msg.getId());
            }
        }*/



    }

    public static void readCommand(Twitter twitter,DirectMessage msg)throws TwitterException, InterruptedException, IOException{
        String text=msg.getText().toLowerCase();
        if(msg.getText().startsWith("!")){
            if(text.startsWith("!ping")){
                ping(msg);
            }
            else if(text.startsWith("!find")){
                String line=text.replace("!find ","");
                ArrayList<String> context = findWord(line, lines, link);
                String message="";
                for(String l : context){
                    message+=l+"\n\n";
                }
                if(!message.equals("")){
                    twitter.sendDirectMessage(msg.getSenderId(),message);
                }
                if(context.size()==0){
                    twitter.sendDirectMessage(msg.getSenderId(),"404 Not found");
                }

            }
            else if(text.startsWith("!help")){
                twitter.sendDirectMessage(msg.getSenderId(),HELP);
            }
            else if(text.startsWith("!bestpage")){
                String term=text.replace("!bestpage ","");
                int i=1, spot = 1, max = 0;

                for(String[]doc : docs){ //perform formula & print
                    int count = 0;
                    for(String s : doc){
                        if(s.toLowerCase().equals(term.toLowerCase())) count ++;
                    }

                    if(count > max){
                        max = count;
                        spot = i;
                    }
                    i++;
                }
                twitter.sendDirectMessage(msg.getSenderId(),"Most Relevant Page: " + spot + ", where " + term + " was found " + max + " times.\n" + link + "#page=" + spot);
            }
            else if(text.startsWith("!setavatar ")){
                String[]terms=text.replace("!setavatar ","").split(" ");
                if(terms.length!=2){
                    twitter.sendDirectMessage(msg.getSenderId(),"Invalid format");
                }
                else if(!terms[0].equals("rasputin")){
                    twitter.sendDirectMessage(msg.getSenderId(),"Invalid password");
                }
                else{
                    //File
                    Image image = null;
                    File pic=new File("C:\\Users\\Matto\\Desktop\\OjibwayBot\\src\\TwitBot\\icon.png");
                    String link = msg.getText().substring(19);
                    System.out.println(link);
                    try {
                        URL url = new URL(link);
                        image = ImageIO.read(url);
                    } catch (IOException e) {}
                    BufferedImage bufferedImage = toBufferedImage(image);
                    ImageIO.write(bufferedImage,"png",pic);

                    twitter.updateProfileImage(pic);

                }


            }
            else if(text.startsWith("!setheader")){
                String[]terms=text.replace("!setheader ","").split(" ");
                if(terms.length!=2){
                    twitter.sendDirectMessage(msg.getSenderId(),"Invalid format");
                }
                else if(!terms[0].equals("rasputin")){
                    twitter.sendDirectMessage(msg.getSenderId(),"Invalid password");
                }
                else {
                    //File
                    Image image = null;
                    File pic = new File("C:\\Users\\Matto\\Desktop\\OjibwayBot\\src\\TwitBot\\header.jpg");
                    String link = msg.getText().substring(20);
                    System.out.println(link);
                    try {
                        URL url = new URL(link);
                        image = ImageIO.read(url);
                    } catch (IOException e) {
                    }
                    BufferedImage bufferedImage = toBufferedImage(image);
                    ImageIO.write(bufferedImage, "jpg", pic);

                    twitter.updateProfileBanner(pic);
                }
            }
            else{
                twitter.sendDirectMessage(msg.getSenderId(),"Invalid command, use !help for command list");
            }
        }
        else{
            twitter.sendDirectMessage(msg.getSenderId(),"Please start all commands with a '!'\nEnter '!help' for the command list");

        }
    }
    public static void ping(DirectMessage msg) throws TwitterException{

        twitter.sendDirectMessage(msg.getSenderId(),"Pong");
    }
    public static ArrayList<String> findWord(String word, ArrayList<Line> dict, String link){
        ArrayList<String> messages = new ArrayList<String>();

        for(Line l : dict){
            String tmp = l.getContent();
            if(messages.size()>20){
                return messages;
            }
            if(tmp.toLowerCase().contains(word.toLowerCase())){
                messages.add('"' + tmp + '"' + " on Page " + l.getPage() + "\n" + link + "#page=" + l.getPage());
            }
        }
        return messages;
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    //Abdullah Code
    public static ArrayList<String> pdf(String fileName) {
        PDFParser parser = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        PDFTextStripper pdfStripper;
        ArrayList<String> pagesList = new ArrayList<String>();

        File file = new File(fileName);
        try {
            parser = new PDFParser(new RandomAccessBufferedFileInputStream(new FileInputStream(file)));
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);


            for (int i = 1; i < pdDoc.getNumberOfPages() + 1; i++)  // loop through pages.
            {
                pdfStripper.setStartPage(i);
                pdfStripper.setEndPage(i);
                pagesList.add(pdfStripper.getText(pdDoc));
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (cosDoc != null)
                    cosDoc.close();
                if (pdDoc != null)
                    pdDoc.close();
            } catch (Exception e1) {
                e.printStackTrace();
            }

        }
        return pagesList;
    }








}
