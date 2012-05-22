package de.bitnoise.sonferenz.repo;

import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

public class DbTestBase
{

  ClassPathXmlApplicationContext ctx;
  TransactionStatus s;
  
  @Autowired
  DataSource dataSource;
  
  @Autowired
  PlatformTransactionManager tm;

  @Before
  public void initSpringContext() throws NotSupportedException, SystemException
  {
    ctx = new ClassPathXmlApplicationContext(
        "/config/spring-testinmemory-db.xml",
        "/config/spring-sonferenz-db.xml"
        );
    ctx.getAutowireCapableBeanFactory().autowireBean(this);

    s = tm.getTransaction(new DefaultTransactionAttribute());
  }

  @After
  public void shutdownSpring()
  {
    if(tm!=null) {
      tm.rollback(s);
      ctx.close();
    }
  }

 
}
