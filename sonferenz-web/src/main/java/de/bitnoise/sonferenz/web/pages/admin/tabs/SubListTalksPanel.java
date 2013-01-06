package de.bitnoise.sonferenz.web.pages.admin.tabs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.data.domain.PageRequest;

import de.bitnoise.sonferenz.model.TalkModel;
import de.bitnoise.sonferenz.model.UserModel;
import de.bitnoise.sonferenz.model.VoteModel;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcTalk;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalcUser;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CalculationConfiguration;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.CollisionResult;
import de.bitnoise.sonferenz.service.v2.services.CalculateTimetableService.SlotOrdern;
import de.bitnoise.sonferenz.service.v2.services.TalkService;
import de.bitnoise.sonferenz.service.v2.services.UserService;
import de.bitnoise.sonferenz.service.v2.services.VoteService;
import de.bitnoise.sonferenz.service.v2.services.impl.calculation.SlotReference;

public class SubListTalksPanel extends Panel
{
  @SpringBean
  CalculateTimetableService calcService;

  @SpringBean
  TalkService talkService;

  @SpringBean
  UserService userService;

  @SpringBean
  VoteService voteService;

  public SubListTalksPanel(String id)
  {
    super(id);
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    AjaxFallbackLink<String> button = new AjaxFallbackLink<String>(
        "calulateTalks")
    {
      @Override
      public void onClick(AjaxRequestTarget target)
      {
        int quality = Integer.MAX_VALUE;
        String best = "";
        int loops = 10;
        while (quality > 10 && loops > 0)
        {
          loops--;
          StringWriter sw = new StringWriter();
          PrintWriter output = new PrintWriter(sw);
          SlotOrdern result = runOneRound(output);
          for (SlotReference slot : result.getResult())
          {
            output.println(slot.getTalks());
          }
          output.println("Calculation order ... DONE");
          if (result.getQuality() < quality)
          {
            best = sw.toString();
            quality=result.getQuality();
            System.out.println("Quality : " + quality);
          }
        }
        System.out.println("-----------------");
        System.out.println(best);
      }
    };
    add(button);

   
  }

  public SlotOrdern runOneRound(PrintWriter output)
  {
    Map<Integer, CalcTalk> _talkMap = new HashMap<Integer, CalcTalk>();
    Map<Integer, CalcUser> _userMap = new HashMap<Integer, CalcUser>();
    List<TalkModel> talks2 = talkService.getTalks(new PageRequest(0, 999))
        .getContent();
    List<UserModel> allUsers = userService.getAllUsers();
    CalculationConfiguration config = calcService.createConfig();

    config.createSlot("slot Fr1", 2);
    config.createSlot("slot Fr2", 2);
    config.createSlot("slot Sa1", 2);
    config.createSlot("slot Sa2", 2);
    config.createSlot("slot Sa3", 2);
    config.createSlot("slot Sa4", 2);
    output.println("Creating talks ...");
    ArrayList<TalkModel> allTalks = new ArrayList<TalkModel>();
    allTalks.addAll(talks2);
    Collections.shuffle(allTalks);
    for (TalkModel talk : allTalks)
    {
      CalcTalk to = config.addTalk(talk.getProposalId());
      _talkMap.put(talk.getProposalId(), to);
      output.println(" db=" + talk.getProposalId() + " = " + talk.getTitle()
          + " : to " + to.getId() + " tid=" + talk.getId());
    }
    output.println("Creating users ...");
    for (UserModel user : allUsers)
    {
      CalcUser uo = config.addUser(user.getId());
      _userMap.put(user.getId(), uo);
      output.println("add user " + user.getId() + ":" + user.getName());

      // add votes
      List<VoteModel> votes = voteService.getVotesFor(user);
      for (VoteModel vote : votes)
      {
        Integer talkId = vote.getTalk().getId();
        Integer rate = vote.getRateing();
        CalcTalk talk = _talkMap.get(talkId);
        if (talk != null && rate > 0)
        {
          talk.addVisit(rate, uo);
          output
              .println("add visit : " + user.getName() + " -> r=" + rate
                  + " for "
                  + vote.getTalk().getId() + "='" + vote.getTalk().getTitle()
                  + "'");
        }
      }
    }
    output.println("Creating Speakers ...");
    // add speaker
    for (TalkModel talk : allTalks)
    {
      CalcUser speaker = _userMap.get(talk.getOwner().getId());
      CalcTalk to = _talkMap.get(talk.getProposalId());
      to.addSpeaker(speaker);
      output.println("add speaker " + talk.getOwner().getId() + ":"
          + talk.getOwner().getName() + " at " + to);
    }

    output.println("Calculation collisions ...");
    CollisionResult collisions = calcService.calculateCollisions(config);
    output.println(collisions);
    output.println("Calculation order ...");
    SlotOrdern result = calcService.calculateTalkorder(config, collisions);
    return result;
  }
}
