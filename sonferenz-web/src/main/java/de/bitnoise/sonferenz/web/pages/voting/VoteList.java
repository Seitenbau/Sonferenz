package de.bitnoise.sonferenz.web.pages.voting;

import java.util.ArrayList;
import java.util.Collections;


public class VoteList extends ArrayList<VoteItem> {
  public void addBefore(VoteItem drag, VoteItem drop)
	{
    int from= indexOf(drag);
    int to = indexOf(drop);
    remove(from);
    if(from<to) {
      to--;
    }
    add(to,drag);
	}
	
	public void addAfter(VoteItem drag, VoteItem drop)
	{
    int from= indexOf(drag);
    int to = indexOf(drop);
    remove(from);
    if(from<to) {
      to--;
    }
    add(to+1,drag);
	}
}