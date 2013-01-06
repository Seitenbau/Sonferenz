package de.bitnoise.sonferenz.web.pages.calculate;

import java.util.Iterator;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.bitnoise.sonferenz.facade.UiFacade;
import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.service.v2.extend.ports.TimeTable;
import de.bitnoise.sonferenz.service.v2.extend.ports.TimeTable.TalkDetails;
import de.bitnoise.sonferenz.service.v2.services.CalcualtionService;

public class CalculationResultPanel extends Panel
{
  @SpringBean
  CalcualtionService calc;

  @SpringBean
  UiFacade facade;

  public CalculationResultPanel(String id)
  {
    super(id);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    String data = "-";

    ConferenceModel conference = facade.getActiveConference();
    if (conference != null)
    {
      TimeTable liste = calc.getCurrentList(conference);
      StringBuilder sb = new StringBuilder();
      sb.append("<h3>Overview</h3>");
      apppendTracks(sb, liste);
      sb.append("<h3>Pre calculation</h3>");
      apppendTrackList(sb, liste);
      sb.append("<h3>Details</h3>");
      apppendDetails(sb, liste);
      apppendInfo(sb);
      data = sb.toString();
    }

    Label content = new Label("content", Model.of(data));
    content.setEscapeModelStrings(false);
    add(content);
  }

  private void apppendInfo(StringBuilder sb)
  {
    sb.append("<ul>");
    Runtime rt = Runtime.getRuntime();
    long free = rt.freeMemory();
    sb.append("<li>Free Memory:" + mb(free));
    long total = rt.totalMemory();
    sb.append("<li>Total Memory:" + mb(total));
    long max = rt.maxMemory();
    sb.append("<li>Max Memory:" + mb(max));
    sb.append("</ul>");
  }

  private String mb(long total)
  {
    return (total/1024/1024) + "MB (" + total + "bytes )";
  }

  private void apppendTracks(StringBuilder sb, TimeTable table)
  {
    sb.append("<table class=\"tracklist\">");
    sb.append("<tr><th>id</th><th>Talk</th><th>Votes</th></tr>");
    for (TalkDetails track : table.getRawList())
    {
      sb.append("<tr>");
      TD(sb, track.getID());
      TD(sb, track.getTitle());
      TD(sb, track.getVotes());
      sb.append("</tr>");
    }
    sb.append("</table>");
  }

  private void apppendDetails(StringBuilder sb, TimeTable table)
  {
    sb.append("<table class=\"tracklist\">");
    sb.append("<tr><th>id</th><th>Talk</th><th>Vote By</th></tr>");
    for (TalkDetails track : table.getRawList())
    {
      sb.append("<tr>");
      TD(sb, track.getID());
      TD(sb, kurz(track.getTitle()));
      sb.append("<td>");
      for (String by : track.getVotedBy())
      {
        sb.append(by);
        sb.append(", ");
      }
      sb.append("</td>");
      sb.append("</tr>");
    }
    sb.append("</table>");
  }

  private void apppendTrackList(StringBuilder sb, TimeTable liste)
  {
    sb.append("<table class=\"tracks\">");
    Iterator<ProposalModel> t0 = liste.getTrack(0).iterator();
    Iterator<ProposalModel> t1 = liste.getTrack(1).iterator();
    while (t0.hasNext())
    {
      sb.append("<tr>");
      ProposalModel talk0 = t0.next();
      buildTD(sb, talk0);
      ProposalModel talk1 = t1.next();
      buildTD(sb, talk1);
      sb.append("</tr>");
    }
    sb.append("</table>");
  }

  private void buildTD(StringBuilder sb, ProposalModel talk)
  {
    if (talk == null)
    {
      sb.append("<td/>");
      return;
    }
    sb.append("<td>");
    sb.append(talk.getId());
    sb.append(" : ");
    sb.append(kurz(talk.getTitle()));
    sb.append(" [");
    sb.append(talk.getOwner().getName());
    sb.append("] ");
    sb.append("</td>");
  }

  private String kurz(String title)
  {
    if (title == null)
    {
      return "";
    }
    if (title.length() > 30)
    {
      return title.substring(0, 30);
    }
    return title;
  }

  private void TD(StringBuilder sb, Object... values)
  {
    if (values == null)
    {
      sb.append("<td/>");
      return;
    }
    sb.append("<td>");
    for (Object value : values)
    {
      sb.append(value);
    }
    sb.append("</td>");
  }
}
