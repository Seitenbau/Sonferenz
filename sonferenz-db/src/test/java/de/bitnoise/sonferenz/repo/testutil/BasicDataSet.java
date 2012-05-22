package de.bitnoise.sonferenz.repo.testutil;

import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;

import de.bitnoise.sonferenz.repo.testutil.tables.AuthMappingTable;
import de.bitnoise.sonferenz.repo.testutil.tables.ConferenceTable;
import de.bitnoise.sonferenz.repo.testutil.tables.LikeModelTable;
import de.bitnoise.sonferenz.repo.testutil.tables.LocalUserModelTable;
import de.bitnoise.sonferenz.repo.testutil.tables.StaticContentTable;
import de.bitnoise.sonferenz.repo.testutil.tables.TalkTable;
import de.bitnoise.sonferenz.repo.testutil.tables.User2UserRoleTable;
import de.bitnoise.sonferenz.repo.testutil.tables.UserRoleTable;
import de.bitnoise.sonferenz.repo.testutil.tables.UserTable;
import de.bitnoise.sonferenz.repo.testutil.tables.VoteModelTable;
import de.bitnoise.sonferenz.repo.testutil.tables.SuggestionModelTable;

public class BasicDataSet
{
  public UserTable userTable;

  public UserRoleTable userRoleTable;

  public User2UserRoleTable user2UserRoleTable;

  public AuthMappingTable authMappingTable;

  public TalkTable talkTable;

  public ConferenceTable conferenceTable;

  public LikeModelTable likeModelTable;

  public LocalUserModelTable localUserModeltable;

  public SuggestionModelTable whishModelTable;

  public VoteModelTable voteModelTable;
  
  public StaticContentTable staticContentModelTable;

  public BasicDataSet()
  {
    authMappingTable = new AuthMappingTable();
    conferenceTable = new ConferenceTable();
    likeModelTable = new LikeModelTable();
    localUserModeltable = new LocalUserModelTable();
    talkTable = new TalkTable();
    userTable = new UserTable();
    user2UserRoleTable = new User2UserRoleTable();
    userRoleTable = new UserRoleTable();
    voteModelTable = new VoteModelTable();
    whishModelTable = new SuggestionModelTable();
    staticContentModelTable=new StaticContentTable();
  }

  public IDataSet getDataset() throws AmbiguousTableNameException
  {
    ITable[] tables = new ITable[]
      { 
          userTable.getTable(), 
          userRoleTable.getTable(),
          user2UserRoleTable.getTable(), 
          localUserModeltable.getTable(),
          authMappingTable.getTable(),
          conferenceTable.getTable(), 
          talkTable.getTable(),
          whishModelTable.getTable(),
          voteModelTable.getTable(),
          likeModelTable.getTable(),
          staticContentModelTable.getTable(),
      };
    return new DefaultDataSet(tables);
  }
}
