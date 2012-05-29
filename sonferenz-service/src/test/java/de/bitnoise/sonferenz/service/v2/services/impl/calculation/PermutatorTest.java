package de.bitnoise.sonferenz.service.v2.services.impl.calculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.fest.assertions.Assertions.*;

import org.junit.Ignore;
import org.junit.Test;

public class PermutatorTest
{
  List<List<String>> list = new ArrayList<List<String>>();

  long count;

  Permutator<String> sut = new Permutator<String>()
  {

    @Override
    public void onPermutation(Object[] permutation)
    {
      List asList = new ArrayList ();
      asList.addAll(Arrays.asList(permutation));
      list.add( asList);
    }
  };

  Permutator<String> sut2 = new Permutator<String>()
  {

    @Override
    public void onPermutation(Object[] permutation)
    {
      count++;
    }
  };

  @Test
  public void test_1x1_item()
  {
    sut.permutateFor(1, Arrays.asList("s1"));

    assertThat(list).hasSize(1);
    assertThat(list.get(0)).containsOnly("s1");
  }

  @Test
  @Ignore // 2 fix
  public void test_2x1_item()
  {
    sut.permutateFor(2, Arrays.asList("s1"));

    assertThat(list).hasSize(2);
    assertThat(list.get(0)).containsOnly("s1");
    assertThat(list.get(1)).containsOnly("s1");
  }

  @Test
  public void test_1x2_item()
  {
    sut.permutateFor(1, Arrays.asList("s1", "s2"));

    assertThat(list).hasSize(2);
    assertThat(list.get(0)).containsOnly("s1");
    assertThat(list.get(1)).containsOnly("s2");
  }

  @Test
  public void test_2x2_item()
  {
    sut.permutateFor(2, Arrays.asList("s1", "s2"));

    assertThat(list).hasSize(4);
    assertThat(list.get(0)).containsOnly("s1", "s1");
    assertThat(list.get(1)).containsOnly("s2", "s1");
    assertThat(list.get(2)).containsOnly("s1", "s2");
    assertThat(list.get(3)).containsOnly("s2", "s2");
  }

  @Test
  public void test_4x3_item()
  {
    sut.permutateFor(4, Arrays.asList("s1", "s2", "s3"));
    assertThat(list).hasSize(81);
  }

  @Test
  public void test_2x12_item()
  {
    sut.permutateFor(2, Arrays.asList("s1", "s2", "s3", "s4", "s5", "s6", "s7",
        "s8", "s9", "s10", "s11", "s12"));
    assertThat(list).hasSize(144);
  }

  @Test
  @Ignore // inifite run
  public void test_12x12_item()
  {
    sut2.permutateFor(12, Arrays.asList("s1", "s2", "s3", "s4", "s5", "s6", "s7",
            "s8", "s9", "s10", "s11", "s12"));
    assertThat(count).isEqualTo(8916100448256L);
  }
}
