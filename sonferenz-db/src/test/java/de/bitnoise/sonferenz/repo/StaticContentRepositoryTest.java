package de.bitnoise.sonferenz.repo;

import static org.fest.assertions.Assertions.assertThat;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import de.bitnoise.sonferenz.model.StaticContentModel;
import de.bitnoise.sonferenz.repo.StaticContentRepository;
import de.bitnoise.sonferenz.repo.testutil.DatabaseWithContent;

public class StaticContentRepositoryTest extends DbTestBase
{
  @Autowired
  StaticContentRepository sut;

  @Test
  public void findByName_Null() throws Exception
  {
    // prepare
    // execute
    StaticContentModel result = sut.findByName(null);

    // verify
    assertThat(result).isNull();
  }

  @Test
  public void findByName_Empty() throws Exception
  { 
    // prepare
    // execute
    StaticContentModel result = sut.findByName("  ");

    // verify
    assertThat(result).isNull();
  }

  @Test
  public void findByName_NonExistingName() throws Exception
  {
    // prepare
    // execute
    StaticContentModel result = sut.findByName("2");

    // verify
    assertThat(result).isNull();
  }

  @Test
  public void findByName_ExistingName() throws Exception
  {
    IDatabaseConnection connection = new DatabaseConnection(dataSource.getConnection());
    // prepare
    DatabaseWithContent ds = new DatabaseWithContent();
    DatabaseOperation.CLEAN_INSERT.execute(connection, ds.getDataset());

    // execute
    StaticContentModel result = sut.findByName("rainer");

    // verify
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1);
    assertThat(result.getName()).isEqualTo("rainer");
    assertThat(result.getHtml()).isEqualTo("<html/>");
  }

}
