package de.bitnoise.sonferenz.web.pages.feedback;

import javax.inject.Inject;

import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.http.WebRequest;

import com.visural.wicket.aturl.At;

import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.web.pages.KonferenzPage;

@At(url = "/feedback")
public class FeedbackPage extends KonferenzPage
{
  @Inject
  ConfigurationService cfgService;

  public FeedbackPage()
  {
    super();
    String redirectUrl = cfgService.getStringValue("feedback.redirect.url");
//    String agent = findAgent();
    throw new RedirectToUrlException(redirectUrl);
  }

//  private String findAgent()
//  {
//    try {
//      String agent = ((WebRequest) getRequest()).getHeader("User-Agent");
//      return agent;
//    }catch(Throwable t) {
//      return t.toString();
//    }
//  }
}
