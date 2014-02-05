package de.bitnoise.sonferenz.web.app;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.settings.IRequestLoggerSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.visural.wicket.aturl.AtAnnotation;

import de.bitnoise.sonferenz.web.pages.error.UnauthorisedAccess;

/**
 * Application object for your web application. If you want to run
 * this application without deploying, run the Start class.
 * 
 * @see de.bitnoise.sonferenz.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
  private SpringComponentInjector injector;

  static Logger logger = LoggerFactory.getLogger(WicketApplication.class);

  /**
   * @see org.apache.wicket.Application#getHomePage()
   */
  @Override
  public Class getHomePage()
  {
    if (KonferenzSession.noUserLoggedIn())
    {
      return de.bitnoise.sonferenz.web.pages.statics.ConferencePage.class;
    }
    else
    {
      return de.bitnoise.sonferenz.web.pages.statics.InfoPage.class;
    }

  }

  /**
   * @see org.apache.wicket.Application#init()
   */
  @Override
  public void init()
  {
    super.init();

    try
    {
      logger.debug("Start preparing SSL");
      prepareSSL();
      logger.debug("Start preparing SSL ... done");
    }
    catch (Exception e1)
    {
      e1.printStackTrace();
      logger.error("There was an error while preparing SSL", e1);
    }

    activateSpring();

    // // for Selenium debugging
    // getDebugSettings().setOutputComponentPath(true);

    // // Activate lesscss
    // getResourceSettings().setResourceStreamLocator(
    // new LessCSSResourceStreamLocator(getResourceFinder()));

    // // Visural JS stuff
    // addRenderHeadListener(JavascriptPackageResource
    // .getHeaderContribution(new JQueryResourceReference()));

    getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
    getRequestCycleSettings().setResponseRequestEncoding("UTF-8");

    // Error pages
    getApplicationSettings().setAccessDeniedPage(UnauthorisedAccess.class);
    getApplicationSettings().setInternalErrorPage(
        de.bitnoise.sonferenz.web.pages.error.InternalError.class);
    getApplicationSettings().setPageExpiredErrorPage(
        UnauthorisedAccess.class);

    getResourceSettings().getStringResourceLoaders().add(
        new DatabaseResourceLoader());
    // Remap URLSs
    try
    {
      AtAnnotation.mount(this, "de.bitnoise.sonferenz.web.pages");
    }
    catch (ClassNotFoundException e)
    {
      throw new RuntimeException(e);
    }

    SecurePackageResourceGuard guard = new SecurePackageResourceGuard();
    guard.addPattern("+*.png");
    guard.addPattern("+*.gif");
    guard.addPattern("+*.js");
    guard.addPattern("+*.css");
    guard.addPattern("+*.cur");
    getResourceSettings().setPackageResourceGuard(guard);

    IRequestLoggerSettings reqLogger = Application.get().getRequestLoggerSettings();
    // Enable the logger
    reqLogger.setRequestLoggerEnabled(true);

    /**
     * Set the window of all the requests that is kept in memory for
     * viewing. Default is 2000, You can set this to 0 then only
     * Sessions data is recorded (number of request, total time,
     * latest size)
     */
    reqLogger.setRequestsWindowSize(0);
  }

  protected void prepareSSL() throws Exception
  {
    InputStream stream = null;
    File fn = new File("src/main/webapp/WEB-INF/config/truststore.jks");
    if (fn.exists())
    {
      stream = new FileInputStream(fn);
    }
    if (stream == null)
    {
      fn = new File("WEB-INF/config/truststore.jks");
      if (fn.exists())
      {
        stream = new FileInputStream(fn);
      }
    }
    if (stream == null)
    {
      stream = getServletContext().getResourceAsStream("/WEB-INF/config/truststore.jks");
    }
    if (stream != null)
    {
      logger.info("Loading keystore " + fn);
      TrustManagerFactory tmFactory = TrustManagerFactory
          .getInstance("PKIX");
      tmFactory.init((KeyStore) null);

      KeyStore ks = KeyStore.getInstance("JKS");

      ks.load(stream, "changeit".toCharArray());
      // Set up key manager factory to use our key store
      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, "changeit".toCharArray());

      KeyManager[] km = kmf.getKeyManagers();
      tmFactory.init(ks);
      TrustManager[] tm = tmFactory.getTrustManagers();

      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(km, tm, null);
      SSLEngine engine = sslContext.createSSLEngine();
      SSLContext.setDefault(sslContext);
      stream.close();
    }
  }

  public void activateSpring()
  {
    // Activate Spring
    injector = new SpringComponentInjector(this);
    // addComponentInstantiationListener(injector);
    getComponentInstantiationListeners().add(injector);
  }

  @Override
  public Session newSession(Request request, Response response)
  {
    // TODO Auto-generated method stub
    return new KonferenzSession(request);
  }

  @SuppressWarnings("unused")
  // only used by tests via reflection
  private void setInternalMockContext(ApplicationContext applicationContext)
      throws BeansException
  {
    injector = new SpringComponentInjector(this, applicationContext, true);
    // addComponentInstantiationListener(injector);
    getComponentInstantiationListeners().add(injector);
  }

}
