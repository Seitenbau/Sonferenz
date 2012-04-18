package de.bitnoise.sonferenz;

import org.junit.Test;
import static org.fest.assertions.Assertions.*;

public class KonferenzDefinesTest implements KonferenzDefines
{
  @Test
  public void testRegexUsername_False()
  {
    assertThat("".matches(REGEX_USERNAME)).isFalse();
    assertThat("12".matches(REGEX_USERNAME)).isFalse();
    assertThat("12345678901234567890123A".matches(REGEX_USERNAME)).isFalse();
  }

  @Test
  public void testRegexUsernameDisplay_False()
  {
    assertThat("".matches(REGEX_USER_DISPLAY)).isFalse();
    assertThat("12".matches(REGEX_USER_DISPLAY)).isFalse();
    assertThat("12345678901234567890123A".matches(REGEX_USER_DISPLAY)).isFalse();
  }

  @Test
  public void testRegexPassword_False()
  {
    assertThat("".matches(PASSWORD_REGEX)).isFalse();
    assertThat("1234567".matches(PASSWORD_REGEX)).isFalse();
    assertThat("12345678901234567890123A".matches(PASSWORD_REGEX)).isFalse();
  }
  
  @Test
  public void testRegexUsername_True()
  {
    assertThat("123".matches(REGEX_USERNAME)).isTrue();
    assertThat("12345678901234567890123".matches(REGEX_USERNAME)).isTrue();
  }

  @Test
  public void testRegexUsernameDisplay_True()
  {
    assertThat("123".matches(REGEX_USER_DISPLAY)).isTrue();
    assertThat("12345678901234567890123".matches(REGEX_USER_DISPLAY)).isTrue();
  }

  @Test
  public void testRegexPassword_True()
  {
    assertThat("12345678".matches(PASSWORD_REGEX)).isTrue();
    assertThat("12345678901234567890123".matches(PASSWORD_REGEX)).isTrue();
  }
}
