package de.bitnoise.sonferenz.web.pages.voting;

import java.util.ArrayList;


public class VoteList extends ArrayList<VoteItem> {
  public void moveup(VoteItem object)
  {
    int from= indexOf(object);
    int to = from-1;
    if(to<0) {
      return;
    }
    remove(from);
    if(from<to) {
      to--;
    }
    add(to,object);
  }

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
    if(from<=to) {
      to--;
    }
    add(to+1,drag);
	}

  public void movedown(VoteItem object)
  {
    int from= indexOf(object);
    int to = from+1;
    if(to>size()-1) {
      return;
    }
    remove(from);
    if(from<to) {
      to--;
    }
    add(to+1,object);
    
  }

}