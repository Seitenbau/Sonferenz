package de.bitnoise.sonferenz.web.pages.voting;

import org.junit.Before;
import org.junit.Test;
import static org.fest.assertions.Assertions.*;

public class VoteListTest
{
  VoteList foos = new VoteList();

  VoteItem item1;

  VoteItem item3;

  VoteItem item2;

  @Before
  public void before()
  {
    item1 = new VoteItem(1,null);
    item3 = new VoteItem(3,null);
    item2 = new VoteItem(2,null);
    foos.add(item1);
    foos.add(item3);
    foos.add(item2);
  }

  @Test
  public void setupCheck()
  {
    assertThat(foos).hasSize(3)
        .startsWith(item1, item3, item2);

    // step 1
    foos.addBefore(item2, item1);
    assertThat(foos).startsWith(item2, item1, item3);

    // step 2
    foos.addBefore(item2, item1);
    assertThat(foos).startsWith(item2, item1, item3);
    
    // step 3
    foos.movedown(item1);
    assertThat(foos).startsWith(item2, item3 , item1);
    
    // step 4
    foos.moveup(item1);
    assertThat(foos).startsWith(item2, item1 , item3);
    
    // step 5
    foos.movedown(item3);
    assertThat(foos).startsWith(item2, item1 , item3);
    
    // step 6
    foos.moveup(item2);
    assertThat(foos).startsWith(item2, item1 , item3);
  }

}
