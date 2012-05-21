package de.bitnoise.sonferenz.testing;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.fest.reflect.core.Reflection.*;
import de.bitnoise.sonferenz.web.app.WicketApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
  {
      "classpath:/config/security.xml",
      "classpath:/config/spring-context.xml",
      "classpath:/config/test-context.xml"
})
/**
 * Create a WicketTester instance and a spring context
 */
public abstract class SpringWicketTest extends TestBase
{
  protected WicketTester tester;

  @Autowired
  ApplicationContext ctx;
  
  @Before
  public void setUp()
  {
    WicketApplication app = new WicketApplication(){
      @Override
      public void activateSpring()
      {
    	method("setInternalMockContext")
    		.withParameterTypes(ApplicationContext.class)
    		.in(this)
    		.invoke(ctx);
      }
    };
    tester = new WicketTester(app);
  }
}
