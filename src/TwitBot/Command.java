package TwitBot;

import twitter4j.DirectMessage;

/**
 * Created by Matto on 2017-01-28.
 */
public interface Command {
    public boolean called(String[]args, DirectMessage msg);
    public void action(String[] args, DirectMessage msg);
    public String help();
    public void executed(boolean success, DirectMessage msg);
}
