package de.bitnoise.sonferenz.service.v2.services.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import de.bitnoise.sonferenz.model.ConferenceModel;
import de.bitnoise.sonferenz.model.ProposalModel;
import de.bitnoise.sonferenz.model.VoteModel;
import de.bitnoise.sonferenz.repo.ProposalRepository;
import de.bitnoise.sonferenz.repo.VoteRepository;
import de.bitnoise.sonferenz.service.v2.exceptions.GeneralConferenceException;
import de.bitnoise.sonferenz.service.v2.extend.CalculateTalkOrder;
import de.bitnoise.sonferenz.service.v2.extend.ports.TimeTable;
import de.bitnoise.sonferenz.service.v2.services.CalcualtionService;

@Service
public class CalcualtionServiceImpl implements CalcualtionService
{
  @Autowired
  ProposalRepository talkRepo;

  @Autowired
  VoteRepository voteRepo;
  
  // TODO: Reimplement this
  @Autowired(required=false)
  CalculateTalkOrder logic;
  
  @Override
  public synchronized TimeTable getCurrentList(ConferenceModel conference)
      throws GeneralConferenceException
  {
    try
    {
      return logic.calculate(conference);
    }
    catch (Exception e)
    {
      throw new GeneralConferenceException("error", e);
    }
  }

 
//  private TimeTable calculate(ConferenceModel conference) throws IOException,
//      Exception
//  {
//    List<TalkModel> talks = talkRepo.findAllByConference(conference);
//    List<Vortrag> vortraege = new ArrayList<Vortrag>();
//    for (TalkModel talk : talks)
//    {
//      Vortrag vortrag = new Vortrag(Integer.toString(talk.getId()));
//      vortraege.add(vortrag);
//      String t = talk.getTitle();
//      vortrag.setTitel(t);
//      vortrag.getReferenten().add(talk.getOwner().getName());
//      List<VoteModel> findByTalk = voteRepo.findByTalk(talk);
//      if (findByTalk == null)
//      {
//        continue;
//      }
//      for (VoteModel vm : findByTalk)
//      {
//        if (vm.getRateing() != null && vm.getRateing() > 0)
//        {
//          vortrag.getTeilnehmer().add(vm.getUser().getName());
//        }
//      }
//    }
//
//    TimeTableImpl result = new TimeTableImpl();
//    buildTrackList(result, vortraege);
//
//    if (vortraege.size() % 2 != 0)
//    {
//      Vortrag vdummy = new Vortrag("-1");
//      vdummy.setTitel("dummy");
//      vdummy.getReferenten().add("dummy");
//      vdummy.getTeilnehmer();
//      vortraege.add(vdummy);
//    }
//    Calculate calc = new Calculate();
//    List<VortragPair> data = calc.calculate(vortraege, 12);
//    List<TalkModel> track1 = result.getTrack(0);
//    List<TalkModel> track2 = result.getTrack(1);
//    for (VortragPair pair : data)
//    {
//      track1.add(talk(pair.getVortrag1()));
//      track2.add(talk(pair.getVortrag2()));
//    }
//
//    return result;
//  }
//
//  private void buildTrackList(TimeTableImpl result, List<Vortrag> vortraege)
//  {
//    List<TalkDetails> details = new ArrayList<TalkDetails>();
//    for (Vortrag vortrag : vortraege)
//    {
//      details.add(new Detail(vortrag));
//    }
//    Collections.sort(details, new Comparator<TalkDetails>() {
//
//      @Override
//      public int compare(TalkDetails arg0, TalkDetails arg1)
//      {
//        if (arg0 == null && arg1 == null)
//        {
//          return 0;
//        }
//        if (arg1 == null)
//        {
//          return -1;
//        }
//        return arg1.getVotes().compareTo(arg0.getVotes());
//      }
//    });
//    result.setRawList(details);
//  }
//
//  public class Detail implements TalkDetails
//  {
//
//    Vortrag _detail;
//
//    public Detail(Vortrag vortrag)
//    {
//      _detail = vortrag;
//    }
//
//    @Override
//    public String getTitle()
//    {
//      StringBuilder sb = new StringBuilder();
//      sb.append(_detail.getTitel());
//      sb.append(" ( ");
//      for (String ref : _detail.getReferenten())
//      {
//        sb.append(ref);
//        sb.append(" ");
//      }
//      sb.append(") ");
//      return sb.toString();
//    }
//
//    @Override
//    public Integer getVotes()
//    {
//      return _detail.getVotes();
//    }
//
//    @Override
//    public Set<String> getVotedBy()
//    {
//      Set<String> teilnehmer = _detail.getTeilnehmer();
//      return teilnehmer;
//    }
//
//    @Override
//    public String getID()
//    {
//      return _detail.getId();
//    }
//
//  }
//
//  private TalkModel talk(Vortrag vortrag) throws SQLException
//  {
//    int id = Integer.parseInt(vortrag.getId());
//    TalkModel talk = talkRepo.findOne(id);
//    return talk;
//  }

  public class TimeTableImpl implements TimeTable
  {
    List<List<ProposalModel>> _tracks;
    List<TalkDetails> _allTalks;

    @Override
    public List<ProposalModel> getTrack(int track)
    {
      if (_tracks == null)
      {
        _tracks = new ArrayList<List<ProposalModel>>();
        _tracks.add(new ArrayList<ProposalModel>());
        _tracks.add(new ArrayList<ProposalModel>());
      }
      return _tracks.get(track);
    }

    public void setRawList(List<TalkDetails> details)
    {
      _allTalks = details;
    }

    @Override
    public List<TalkDetails> getRawList()
    {
      if (_allTalks == null)
      {
        _allTalks = new ArrayList<TalkDetails>();
      }
      return _allTalks;
    }

  }
}
