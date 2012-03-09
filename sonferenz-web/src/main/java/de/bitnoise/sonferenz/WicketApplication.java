package de.bitnoise.sonferenz;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import com.jquery.JQueryResourceReference;
import com.visural.wicket.aturl.AtAnnotation;

import de.bitnoise.sonferenz.web.pages.error.UnauthorisedAccess;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see de.bitnoise.sonferenz.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
  /**
   * @see org.apache.wicket.Application#getHomePage()
   */
  @Override
  public Class<de.bitnoise.sonferenz.web.pages.statics.ConferencePage> getHomePage()
  {
    return de.bitnoise.sonferenz.web.pages.statics.ConferencePage.class;
  }

  /**
   * @see org.apache.wicket.Application#init()
   */
  @Override
  public void init()
  {
    super.init();
    activateSpring();

    // for Selenium debugging
    getDebugSettings().setOutputComponentPath(true);

//    // Activate lesscss
//    getResourceSettings().setResourceStreamLocator(
//        new LessCSSResourceStreamLocator(getResourceFinder()));

    // Visural JS stuff
    addRenderHeadListener(JavascriptPackageResource
        .getHeaderContribution(new JQueryResourceReference()));

    getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
    getRequestCycleSettings().setResponseRequestEncoding("UTF-8");

    // Error pages
    getApplicationSettings().setAccessDeniedPage(UnauthorisedAccess.class);
    getApplicationSettings().setInternalErrorPage(
        de.bitnoise.sonferenz.web.pages.error.InternalError.class);
    getApplicationSettings().setPageExpiredErrorPage(UnauthorisedAccess.class);

    getResourceSettings().addStringResourceLoader(0, new DatabaseResourceLoader());
    // Remap URLSs
    try
    {
      AtAnnotation.mount(this, "de.bitnoise.sonferenz.web.pages");
    }
    catch (ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }
    
  }
  
  public void activateSpring()
  {
    // Activate Spring
    addComponentInstantiationListener(new SpringComponentInjector(this));
  }

  @Override
  public Session newSession(Request request, Response response)
  {
    return new KonferenzSession(request);
  }
}
