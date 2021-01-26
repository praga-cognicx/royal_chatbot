package com.royal.app.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.royal.app.constants.AppicationConstants;
import com.royal.app.dao.ChatDemoDAO;

@Repository("ChatDemoDAO")
public class ChatDemoDAOImpl implements ChatDemoDAO {

  @PersistenceContext(unitName = AppicationConstants.FIRST_PERSISTENCE_UNIT_NAME)
  private EntityManager firstEntityManager;

  @Autowired
  @Qualifier("firstJdbcTemplate")
  JdbcTemplate firstJdbcTemplate;
  
  private static final Logger logger = LoggerFactory.getLogger(ChatDemoDAOImpl.class);
  @Override
  public String getKeyValuePair(String key, String id) throws Exception {
    String result = "";
    try {
      Query qry = firstEntityManager.createNativeQuery("select value from CHAT_DEMO cd where key_name='"+key+"' and id='"+id+"'");
      Object res = qry.getResultList().get(0);
      if(res!=null) {
          result = res.toString();
      }
    } catch(Exception e) {
        logger.info("Exception :: ChatDemoDAOImpl :: getKeyValuePair() : " + e);
    } finally {
        firstEntityManager.close();
    }
    return result;
  }

}
