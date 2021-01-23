package com.royal.app.dao.impl;

import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.royal.app.constants.AppicationConstants;
import com.royal.app.dao.RateSheetDAO;
import com.royal.app.message.request.RSUpdatesCreateReq;
import com.royal.app.message.request.RateSheetMgtPriorityReqRes;
import com.royal.app.message.request.RatesheetMappingRequest;
import com.royal.app.message.request.RatesheetUpdateViewRequest;
import com.royal.app.model.RateSheetErrorLog;
import com.royal.app.model.RatesheetAgentMap;
import com.royal.app.model.RatesheetMapping;
import com.royal.app.model.RatesheetMgt;
import com.royal.app.model.RatesheetTimeLog;
import com.royal.app.model.RatesheetUpdates;
import com.royal.app.shared.dto.RateSheetDto;
import com.royal.app.shared.dto.RatesheetMappingDto;
import com.royal.app.shared.dto.RatesheetMgtDto;
import com.royal.app.util.CommonUtil;
import com.royal.app.util.DateUtil;
import com.royal.app.util.UserInfo;

@Repository("RateSheetDAO")
public class RateSheetDAOImpl implements RateSheetDAO{
  
  @PersistenceContext(unitName = AppicationConstants.FIRST_PERSISTENCE_UNIT_NAME)
  private EntityManager firstEntityManager;

  @Autowired
  @Qualifier("firstJdbcTemplate")
  JdbcTemplate firstJdbcTemplate;
  
  @Autowired
  UserInfo userInfo;
  
  private static final Logger logger = LoggerFactory.getLogger(RateSheetDAOImpl.class);
  
  
  public Map<String, Object> getRateSheetTables(final RateSheetDto rateSheetDto) throws Exception  {
    Map<String, Object> resultSetMap = new TreeMap<>();
    try {
    	   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
    	   LocalDateTime now = LocalDateTime.now(); 
    	   logger.info("procedure starts at"+now);
      if (rateSheetDto != null) {
        Connection connection = firstJdbcTemplate.getDataSource().getConnection();
        try (CallableStatement callableStatement =
            connection.prepareCall("{call New_xml_PriceWithCombination_Ratesheet(?,?,?,?,?)}")) {
        	logger.info("New_xml_PriceWithCombination_Ratesheet procedure call");
            logger.info("countrycode-->"+rateSheetDto.getCountryCode());
            logger.info("partycode-->"+rateSheetDto.getPartyCode());
            logger.info("agentcode-->"+rateSheetDto.getAgentCode());
            logger.info("fromdate-->"+rateSheetDto.getFromDate());
            logger.info("todate-->"+rateSheetDto.getToDate());
          callableStatement.setString("@ctrycode", rateSheetDto.getCountryCode());
          callableStatement.setString("@partycode", rateSheetDto.getPartyCode());
          callableStatement.setString("@agentcode", rateSheetDto.getAgentCode());
          callableStatement.setString("@frmdatec", rateSheetDto.getFromDate());
          callableStatement.setString("@todatec", rateSheetDto.getToDate());
          callableStatement.executeQuery();
          int rsCount = 0;

          if (callableStatement.getFetchSize() > 0) {
            boolean results = true;
            boolean columncheck = true;
            // Loop through the available result sets.
            while (results) {
              ResultSet rs = callableStatement.getResultSet();

              List<List<String>> listresult = new ArrayList<>();
              // Retrieve data from the result set.
              
              ResultSetMetaData rsmd = rs.getMetaData();
              int columnCount = rsmd.getColumnCount();
              ArrayList<String> columns = new ArrayList<>();
              
              if(columncheck) {
                for(int colIndex = 1; colIndex <= columnCount; colIndex++) {
                  columns.add(rsmd.getColumnName(colIndex));
                }
              listresult.add(columns);
              columncheck = false;
              }
              
              while (rs.next()) {
                ArrayList<String> valList = new ArrayList<>();
                for(int valIndex = 0; valIndex < columnCount; valIndex++) {
                  valList.add(rs.getString(columns.get(valIndex)));
                }
                listresult.add(valList);
              }
              resultSetMap.put(String.valueOf(rsCount), listresult);
              rs.close();

              // Check for next result set
              results = callableStatement.getMoreResults();
              rsCount++;
              columncheck = true;
            }
          }
        } catch (Exception ex) {
          logger.error("Exception Nested:: RateSheetDAOImpl :: getRateSheetTables() "
              + ex.fillInStackTrace());
        } finally {
          connection.close();
        }
      }
      now = LocalDateTime.now();
      logger.info("procedure ends at"+now);
    } catch (Exception e) {
        logger.error("Exception :: RateSheetDAOImpl :: getRateSheetTables() : " + e);
    } finally {
        firstEntityManager.close();
    }

    return resultSetMap;
    }
  
  public List<Object[]> getSpecialEventTables(final RateSheetDto rateSheetDto, final String rateType, final String ratePlanId) throws Exception  {
    List<Object[]> listresult = new ArrayList<>();
    try {
      if (rateSheetDto != null) {
        Connection connection = firstJdbcTemplate.getDataSource().getConnection();
        try (CallableStatement callableStatement =
            connection.prepareCall("{call New_XML_specialevents(?,?,?,?,?,?,?)}")) {
          callableStatement.setString("@sourcecountry", rateSheetDto.getCountryCode());
          callableStatement.setString("@partycode", rateSheetDto.getPartyCode());
          callableStatement.setString("@agentcode", rateSheetDto.getAgentCode());
          callableStatement.setString("@fromdate", rateSheetDto.getFromDate());
          callableStatement.setString("@todate", rateSheetDto.getToDate());
          callableStatement.setString("@rateplanid", ratePlanId);
          callableStatement.setString("@ratetype", rateType);
          callableStatement.executeQuery();
          int rsCount = 0;
          
          if (callableStatement.getFetchSize() > 0) {
            boolean results = true;
            boolean columncheck = true;
            // Loop through the available result sets.
            while (results) {
              ResultSet rs = callableStatement.getResultSet();
              // Retrieve data from the result set.
              ResultSetMetaData rsmd = rs.getMetaData();
              int columnCount = rsmd.getColumnCount();
              Object[] columns =  new Object[20];
              
              /*if(columncheck) {
                for(int colIndex = 1; colIndex <= columnCount; colIndex++) {
                  columns[colIndex-1] = rsmd.getColumnName(colIndex);
                }
                listresult.add(columns);
              columncheck = false;
              }*/
              
              while (rs.next()) {
                Object[] valList =  new Object[20];
                for(int valIndex = 0; valIndex < columnCount; valIndex++) {
                  valList[valIndex] = rs.getString(valIndex+1);
                }
                listresult.add(valList);
              } 
              // Check for next result set
              columncheck = true;
              results = false;
              rs.close();
            }
            
          }
        } catch (Exception ex) {
          logger.error("Exception Nested:: RateSheetDAOImpl :: getSpecialEventTables() "
              + ex.fillInStackTrace());
        } finally {
          connection.close();
        }
      }

    } catch (Exception e) {
        logger.error("Exception :: RateSheetDAOImpl :: getSpecialEventTables() : " + e);
    } finally {
        firstEntityManager.close();
    }

    return listresult;
    }
  
  public List<Object[]> getCancelPolicyDetails(final RateSheetDto rateSheetDto, final String ratePlanId) throws Exception  {
    List<Object[]> listresult = new ArrayList<>();
    try {
      if (rateSheetDto != null) {
        Connection connection = firstJdbcTemplate.getDataSource().getConnection();
        try (CallableStatement callableStatement =
            connection.prepareCall("{call New_XML_CancelPolicy(?,?,?,?,?,?)}")) {
          callableStatement.setString("@sourcecountry", rateSheetDto.getCountryCode());
          callableStatement.setString("@partycode", rateSheetDto.getPartyCode());
          callableStatement.setString("@agentcode", rateSheetDto.getAgentCode());
          callableStatement.setString("@fromdate", rateSheetDto.getFromDate());
          callableStatement.setString("@todate", rateSheetDto.getToDate());
          callableStatement.setString("@rateplanid", ratePlanId);
          callableStatement.executeQuery();
          if (callableStatement.getFetchSize() > 0) {
            boolean results = true;
            boolean columncheck = true;
            // Loop through the available result sets.
            while (results) {
              ResultSet rs = callableStatement.getResultSet();
              // Retrieve data from the result set.
              ResultSetMetaData rsmd = rs.getMetaData();
              int columnCount = rsmd.getColumnCount();
              Object[] columns =  new Object[20];
              
              if(columncheck) {
                for(int colIndex = 1; colIndex <= columnCount; colIndex++) {
                  columns[colIndex-1] = rsmd.getColumnName(colIndex);
                }
                listresult.add(columns);
              columncheck = false;
              }
              
              while (rs.next()) {
                Object[] valList =  new Object[20];
                for(int valIndex = 0; valIndex < columnCount; valIndex++) {
                  valList[valIndex] = rs.getString(valIndex+1);
                }
                listresult.add(valList);
              } 
              // Check for next result set
              columncheck = true;
              results = false;
              rs.close();
            }
            
          }
        } catch (Exception ex) {
          logger.error("Exception Nested:: RateSheetDAOImpl :: getCancelPolicyDetails() "
              + ex.fillInStackTrace());
        } finally {
          connection.close();
        }
      }

    } catch (Exception e) {
        logger.error("Exception :: RateSheetDAOImpl :: getCancelPolicyDetails() : " + e);
    } finally {
        firstEntityManager.close();
    }

    return listresult;
    }
  
  public List<List<String>> getMaximumOccupancyData(String type, String conPromoId, String partyCode) throws Exception  {
    List<List<String>> listresult = new ArrayList<>();
    try {
        Connection connection = firstJdbcTemplate.getDataSource().getConnection();
        try (CallableStatement callableStatement =
            connection.prepareCall("{call New_XML_MaxOccupancy(?,?,?)}")) {
          callableStatement.setString("@type", type);
          callableStatement.setString("@conpromoid", partyCode);
          callableStatement.setString("@partycode", partyCode);
          callableStatement.executeQuery();

          if (callableStatement.getFetchSize() > 0) {
            boolean results = true;
            boolean columncheck = true;
            // Loop through the available result sets.
            while (results) {
              ResultSet rs = callableStatement.getResultSet();
              // Retrieve data from the result set.
              ResultSetMetaData rsmd = rs.getMetaData();
              int columnCount = rsmd.getColumnCount();
              ArrayList<String> columns = new ArrayList<>();
              
              if(columncheck) {
                for(int colIndex = 1; colIndex <= columnCount; colIndex++) {
                  columns.add(rsmd.getColumnName(colIndex));
                }
              listresult.add(columns);
              columncheck = false;
              }
              
              while (rs.next()) {
                ArrayList<String> valList = new ArrayList<>();
                for(int valIndex = 0; valIndex < columnCount; valIndex++) {
                  valList.add(rs.getString(columns.get(valIndex)));
                }
                listresult.add(valList);
              } 
              // Check for next result set
              columncheck = true;
              results = false;
              rs.close();
            }
            
            
          }
        } catch (Exception ex) {
          logger.error("Exception :: RateSheetDAOImpl :: getMaximumOccupancyData() "
              + ex.fillInStackTrace());
        } finally {
          connection.close();
        }

    } catch (Exception e) {
        logger.error("Exception :: RateSheetDAOImpl :: getMaximumOccupancyData() : " + e);
    } finally {
        firstEntityManager.close();
    }

    return listresult;
    }
  
  
  @SuppressWarnings("unchecked")
  @Override
  public String getCurrencyCodeFromAgentMast(String agentCode) throws Exception {
    StringBuilder sqlQry = null;
    String currencyCode = "";
    List<Object[]> result = null;
    try {
      sqlQry = new StringBuilder("SELECT CURRCODE FROM AGENTMAST WHERE AGENTCODE=:AGENTCODE");
      Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
      queryObj.setParameter("AGENTCODE", agentCode);
      result = queryObj.getResultList();
      if (!result.isEmpty()) {
        currencyCode = CommonUtil.nullRemove(result.get(0));
      }
    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: getCurrencyCodeFromAgentMast() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return currencyCode;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Object[]> getdtMinNightsData(String partyCode, String contractId) throws Exception {
    StringBuilder sqlQry = null;
    List<Object[]> result = null;
    try {
      sqlQry = new StringBuilder("select distinct  (select distinct top 1 stuff((select  ',' + u.rmtypname   from partyrmtyp u(nolock)  where u.rmtypcode =rmtypcode and u.partycode =partycode and partycode=:PARTYCODE  and u.rmtypcode in (select value from string_split(roomtypes,','))    group by rmtypname    for xml path('')),1,1,'')  rmtypname from partyrmtyp where partycode=:PARTYCODE  and rmtypcode in (select value from string_split(roomtypes,',')) )rmtypname,mealplans,MLOS_Start_Date,MLOS_End_Date,MLOS_Nights,MLOS_Option from New_MLOS(nolock) where Contract_ID=:CONTRACTID");
      Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
      queryObj.setParameter("PARTYCODE", partyCode);
      queryObj.setParameter("CONTRACTID", contractId);
      result = queryObj.getResultList();
    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: getdtMinNightsData() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return result;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Object[]> getWeekDaysData(String contractId) throws Exception {
    StringBuilder sqlQry = null;
    List<Object[]> result = null;
    try {
      sqlQry = new StringBuilder("select distinct dayoftheweek from view_New_MLOS_weekdays where MLOS_ID in (select distinct MLOS_ID from New_MLOS where Contract_ID=:CONTRACTID);");
      Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
      queryObj.setParameter("CONTRACTID", contractId);
      result = queryObj.getResultList();
    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: getWeekDaysData() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return result;
  }
  
  public Map<String, Object> CheckInCheckOutPolicy(String partyCode, String contractId) throws Exception  {
    Map<String, Object> resultSetMap = new TreeMap<>();
    try {
      if (contractId != null) {
        Connection connection = firstJdbcTemplate.getDataSource().getConnection();
        try (CallableStatement callableStatement =
            connection.prepareCall("{call New_XML_CheckinOutContracts(?,?)}")) {
          callableStatement.setString("@rateplanid", contractId);
          callableStatement.setString("@Partycode", partyCode);
          callableStatement.executeQuery();
          int rsCount = 0;

          if (callableStatement.getFetchSize() > 0) {
            boolean results = true;
            boolean columncheck = true;
            // Loop through the available result sets.
            while (results) {
              ResultSet rs = callableStatement.getResultSet();

              List<List<String>> listresult = new ArrayList<>();
              // Retrieve data from the result set.
              
              ResultSetMetaData rsmd = rs.getMetaData();
              int columnCount = rsmd.getColumnCount();
              ArrayList<String> columns = new ArrayList<>();
              
              if(columncheck) {
                for(int colIndex = 1; colIndex <= columnCount; colIndex++) {
                  columns.add(rsmd.getColumnName(colIndex));
                }
              listresult.add(columns);
              columncheck = false;
              }
              
              while (rs.next()) {
                ArrayList<String> valList = new ArrayList<>();
                for(int valIndex = 0; valIndex < columnCount; valIndex++) {
                  valList.add(rs.getString(columns.get(valIndex)));
                }
                listresult.add(valList);
              }
              resultSetMap.put(String.valueOf(rsCount), listresult);
              rs.close();

              // Check for next result set
              results = callableStatement.getMoreResults();
              rsCount++;
              columncheck = true;
            }
          }
        } catch (Exception ex) {
          logger.error("Exception Nested:: RateSheetDAOImpl :: CheckInCheckOutPolicy() "
              + ex.fillInStackTrace());
        } finally {
          connection.close();
        }
      }

    } catch (Exception e) {
        logger.error("Exception :: RateSheetDAOImpl :: getRateSheetTables() : " + e);
    } finally {
        firstEntityManager.close();
    }

    return resultSetMap;
    }
  
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Object[]> getCheckInCheckOutDateDetails(String contractId, String seasonCodes) throws Exception {
    StringBuilder sqlQry = null;
    List<Object[]> result = null;
    try {
      sqlQry = new StringBuilder("select  distinct * from view_contractseasons(nolock) where contractid=:CONTRACTID  and seasonname IN ("+seasonCodes+") order by fromdate");
      Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
      queryObj.setParameter("CONTRACTID", contractId);
      result = queryObj.getResultList();
    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: getCheckInCheckOutDateDetails() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return result;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Object[]> getCheckInCheckOutRoomDetails(String checkInoutPolicyId, String partyCode) throws Exception {
    StringBuilder sqlQry = null;
    List<Object[]> result = null;
    try {
      sqlQry = new StringBuilder("select R.checkinoutpolicyid,R.rmtypcode,P.rmtypname from contracts_checkinout_roomtypes R inner join partyrmtyp P on R.rmtypcode=P.rmtypcode and R.checkinoutpolicyid=:CONTRACTID  and P.partycode=:PARTYCODE order by rankord");
      Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
      queryObj.setParameter("CONTRACTID", checkInoutPolicyId);
      queryObj.setParameter("PARTYCODE", partyCode);
      result = queryObj.getResultList();
    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: getCheckInCheckOutRoomDetails() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return result;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Object[]> getCheckInCheckOutRestrictDetails(String checkInoutPolicyId) throws Exception {
    StringBuilder sqlQry = null;
    List<Object[]> result = null;
    try {
      sqlQry = new StringBuilder("select * from contracts_checkinout_restricted where checkinoutpolicyid =:CONTRACTID order by datetype,rlineno");
     // sqlQry = new StringBuilder("select * from contracts_checkinout_restricted order by datetype,rlineno");
      Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
      queryObj.setParameter("CONTRACTID", checkInoutPolicyId);
      result = queryObj.getResultList();
    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: getCheckInCheckOutRestrictDetails() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return result;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Object[]> getCheckInCheckOutEarlyCheckDetails(String checkInoutPolicyId) throws Exception {
    StringBuilder sqlQry = null;
    List<Object[]> result = null;
    try {
      sqlQry = new StringBuilder("select * from contracts_checkinout_detail where checkinoutpolicyid =:CONTRACTID order by checkinouttype,clineno");
      //sqlQry = new StringBuilder("select * from contracts_checkinout_detail order by checkinouttype,clineno");
      Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
      queryObj.setParameter("CONTRACTID", checkInoutPolicyId);
      result = queryObj.getResultList();
    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: getCheckInCheckOutEarlyCheckDetails() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return result;
  }
  
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Object[]> getGeneralPolicyDetails(final RateSheetDto rateSheetDto, final String ratePlanId) throws Exception {
    List<Object[]> listresult = new ArrayList<>();
    try {
      if (rateSheetDto != null) {
        Connection connection = firstJdbcTemplate.getDataSource().getConnection();
        try (CallableStatement callableStatement =
            connection.prepareCall("{call sp_rep_generalpolicy(?,?,?,?,?,?)}")) {
          callableStatement.setString("@sourcecountry", rateSheetDto.getCountryCode());
          callableStatement.setString("@partycode", rateSheetDto.getPartyCode());
          callableStatement.setString("@agentcode", rateSheetDto.getAgentCode());
          callableStatement.setString("@fromdate", rateSheetDto.getFromDate());
          callableStatement.setString("@todate", rateSheetDto.getToDate());
          callableStatement.setString("@rateplanid", ratePlanId);
          callableStatement.executeQuery();
          if (callableStatement.getFetchSize() > 0) {
            boolean results = true;
            // Loop through the available result sets.
            while (results) {
              ResultSet rs = callableStatement.getResultSet();
              // Retrieve data from the result set.
              ResultSetMetaData rsmd = rs.getMetaData();
              int columnCount = rsmd.getColumnCount();
              while (rs.next()) {
                Object[] valList =  new Object[20];
                for(int valIndex = 0; valIndex < columnCount; valIndex++) {
                  valList[valIndex] = rs.getString(valIndex+1);
                }
                listresult.add(valList);
              } 
              // Check for next result set
              results = false;
              rs.close();
            }
            
          }
        } catch (Exception ex) {
          logger.error("Exception Nested:: RateSheetDAOImpl :: getGeneralPolicyDetails() "
              + ex.fillInStackTrace());
        } finally {
          connection.close();
        }
      }

    } catch (Exception e) {
        logger.error("Exception :: RateSheetDAOImpl :: getGeneralPolicyDetails() : " + e);
    } finally {
        firstEntityManager.close();
    }

    return listresult;
    }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Object[]> getHotelConstractionDetails(final RateSheetDto rateSheetDto, final String ratePlanId) throws Exception {
    List<Object[]> listresult = new ArrayList<>();
    try {
      if (rateSheetDto != null) {
        Connection connection = firstJdbcTemplate.getDataSource().getConnection();
        try (CallableStatement callableStatement =
            connection.prepareCall("{call sp_rep_hotelconstruction(?,?,?,?,?,?)}")) {
          callableStatement.setString("@sourcecountry", rateSheetDto.getCountryCode());
          callableStatement.setString("@partycode", rateSheetDto.getPartyCode());
          callableStatement.setString("@agentcode", rateSheetDto.getAgentCode());
          callableStatement.setString("@fromdate", rateSheetDto.getFromDate());
          callableStatement.setString("@todate", rateSheetDto.getToDate());
          callableStatement.setString("@rateplanid", ratePlanId);
          callableStatement.executeQuery();
          if (callableStatement.getFetchSize() > 0) {
            boolean results = true;
            // Loop through the available result sets.
            while (results) {
              ResultSet rs = callableStatement.getResultSet();
              // Retrieve data from the result set.
              ResultSetMetaData rsmd = rs.getMetaData();
              int columnCount = rsmd.getColumnCount();
              while (rs.next()) {
                Object[] valList =  new Object[20];
                for(int valIndex = 0; valIndex < columnCount; valIndex++) {
                  valList[valIndex] = rs.getString(valIndex+1);
                }
                listresult.add(valList);
              } 
              // Check for next result set
              results = false;
              rs.close();
            }
            
          }
        } catch (Exception ex) {
          logger.error("Exception Nested:: RateSheetDAOImpl :: getHotelConstractionDetails() "
              + ex.fillInStackTrace());
        } finally {
          connection.close();
        }
      }

    } catch (Exception e) {
        logger.error("Exception :: RateSheetDAOImpl :: getHotelConstractionDetails() : " + e);
    } finally {
        firstEntityManager.close();
    }

    return listresult;
    }

  @Override
  public RateSheetDto getRateSheetDet() throws Exception {
    RateSheetDto rateSheetDto = new RateSheetDto();
    List<RatesheetMgt>  result = null;
      try {
        Query qry =  firstEntityManager.createQuery("select rm from RatesheetMgt rm where status='Active' order by priority asc");
         result = (List<RatesheetMgt>) qry.getResultList();
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: getRateSheetDet() : " + e);
      } finally {
          firstEntityManager.close();
      }
      rateSheetDto.setObject(result);
      return rateSheetDto;
  }
  
  @Override
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public RatesheetMgtDto saveRateSheetDet(RatesheetMgtDto rateSheetMgtDto) throws Exception {
    try {
        RatesheetMgt rs= new RatesheetMgt();
        BeanUtils.copyProperties(rateSheetMgtDto, rs);
        rs.setCreatedBy(userInfo.getEmployeeId());
        firstEntityManager.persist(rs);
        rateSheetMgtDto.setAutogenRsId(rs.getAutogenRsId());
      } catch(Exception e) {
          throw e;
      } finally {
          firstEntityManager.close();
      }
    return rateSheetMgtDto;
  }
  
  @Override
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public boolean updateRateSheetDet(RatesheetMgtDto rateSheetMgtDto) throws Exception {
    boolean flag = false;
    try {
        RatesheetMgt rs = firstEntityManager.find(RatesheetMgt.class, rateSheetMgtDto.getAutogenRsId());
        if(rs != null) {
          BeanUtils.copyProperties(rateSheetMgtDto, rs);
          rs.setUpdatedBy(userInfo.getEmployeeId());
          firstEntityManager.merge(rs);
          flag = true;
        }
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: saveRateSheetDet() : " + e);
      } finally {
          firstEntityManager.close();
      }
    return flag;
  }
  
  @Override
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public boolean updatePriorityRateSheetDet(List<RateSheetMgtPriorityReqRes> RateSheetMgtPriorityList) throws Exception {
    boolean flag = false;
    try {
      if (RateSheetMgtPriorityList != null && !RateSheetMgtPriorityList.isEmpty()) {
        for (RateSheetMgtPriorityReqRes obj : RateSheetMgtPriorityList) {
          RatesheetMgt rs =
              firstEntityManager.find(RatesheetMgt.class, obj.getId());
          if (rs != null) {
            rs.setPriority(obj.getPriority());
            rs.setUpdatedBy(userInfo.getEmployeeId());
            firstEntityManager.merge(rs);
            flag = true;
          }
        }
      }
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: saveRateSheetDet() : " + e);
      } finally {
          firstEntityManager.close();
      }
    return flag;
  }
    

  @SuppressWarnings("unchecked")
  @Override
  public String getEndContractDate(String partyCode, String indCode) throws Exception {
    String returnValue = "";
    List<Object[]> result = null;
      try {
        Query qry =  firstEntityManager.createNativeQuery("select * from (select max(todate) as todate1 from Contracts O where O.approved = 1 and O.activestate ='Active' and  partycode =:PARTYCODE) as rpt where todate1 >= GETDATE() ");
        qry.setParameter("PARTYCODE", partyCode);
        /**qry.setParameter("INDCODE", indCode);*/
        result = qry.getResultList();
        if(result != null && !result.isEmpty()) {
          returnValue = CommonUtil.nullRemove(result.get(0));
        }
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: getEndContractDate() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return returnValue;
  }
  
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public boolean saveRateSheetTimeLog(RateSheetDto rateSheetDto, Date startTime, Date endTime) throws Exception {
    boolean flag = false;  
    try {
        RatesheetTimeLog rs= new RatesheetTimeLog();
        rs.setRsName(rateSheetDto.getRsName());
        rs.setAutogenRsId(rateSheetDto.getAutogenRsId());
        rs.setPartyCode(rateSheetDto.getPartyCode());
        rs.setPartyName(rateSheetDto.getPartyName());
        rs.setStartTime(startTime);
        rs.setEndTime(endTime);
        long diff = endTime.getTime() - startTime.getTime();
        long diffSeconds = diff / 1000 ;
        if(diffSeconds > 0) {
        rs.setTotalTime(Integer.parseInt(String.valueOf(diffSeconds)));
        }
        rs.setCreatedBy("System");
        firstEntityManager.persist(rs);
        flag = true;
      } catch(Exception e) {
        flag = false;
         logger.info("Exception :: RateSheetDAOImpl :: saveRateSheetTimeLog() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return  flag;
  }
  
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public boolean saveRateSheetErrorLog(RateSheetDto rateSheetDto, String errorMsg, String fileName, byte[] fileContent, String status) throws Exception {
    boolean flag = false;  
    try {
        RateSheetErrorLog rs= new RateSheetErrorLog();
        rs.setRsName(rateSheetDto.getRsName());
        rs.setAutogenRsId(rateSheetDto.getAutogenRsId());
        rs.setErrorMsg(errorMsg);
        rs.setFileName(fileName);
        rs.setFileContent(fileContent);
        rs.setStatus(status);
        rs.setCreatedBy("System");
        firstEntityManager.persist(rs);
        flag = true;
      } catch(Exception e) {
        flag = false;
         logger.info("Exception :: RateSheetDAOImpl :: saveRateSheetErrorLog() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return  flag;
  }
  
  
/*  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public boolean saveRateSheetUpdates(RateSheetUpdatesDto rateSheetUpdatesDto) throws Exception {
    boolean flag = false;  
    try {
      RatesheetUpdates rs= new RatesheetUpdates();
        rs.setUpdateName(rateSheetUpdatesDto.getUpdateName());
        rs.setPartyCode(rateSheetUpdatesDto.getPartyCode());
        rs.setPartyName(rateSheetUpdatesDto.getPartyName());
        rs.setUpdateDetails(rateSheetUpdatesDto.getUpdateDetails());
        rs.setValidFrom(rateSheetUpdatesDto.getValidFrom());
        rs.setValidTill(rateSheetUpdatesDto.getValidTill());
        rs.setStatus("Active");
        rs.setInternalNote(rateSheetUpdatesDto.getInternalNote());
        rs.setCreatedBy(rateSheetUpdatesDto.getCreatedBy());
        firstEntityManager.persist(rs);
        
      if (rs.getAutogenRsUpdatesId() != null
          && !BigInteger.ZERO.equals(rs.getAutogenRsUpdatesId())) {
        if (rateSheetUpdatesDto.getRatesheetUpdatesMaps() != null) {
          for (RatesheetUpdatesMap ratesheetUpdatesMap : rateSheetUpdatesDto
              .getRatesheetUpdatesMaps()) {
            ratesheetUpdatesMap.setAutogenRsUpdatesId(rs.getAutogenRsUpdatesId());
           // ratesheetUpdatesMap.setStatus("Active");
            firstEntityManager.persist(ratesheetUpdatesMap);
          }
        }
        flag = true;
      }
      } catch(Exception e) {
        flag = false;
         logger.info("Exception :: RateSheetDAOImpl :: saveRateSheetErrorLog() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return  flag;
  }
  
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public boolean updateRateSheetUpdates(RateSheetUpdatesDto rateSheetUpdatesDto) throws Exception {
    boolean flag = false;
    try {
      RatesheetUpdates rs = new RatesheetUpdates();
      
       * rs.setUpdateName(rateSheetUpdatesDto.getUpdateName());
       * rs.setPartyCode(rateSheetUpdatesDto.getPartyCode());
       * rs.setPartyName(rateSheetUpdatesDto.getPartyName());
       
      rs.setUpdateDetails(rateSheetUpdatesDto.getUpdateDetails());
      rs.setValidFrom(rateSheetUpdatesDto.getValidFrom());
      rs.setValidTill(rateSheetUpdatesDto.getValidTill());
      rs.setStatus(rateSheetUpdatesDto.getStatus());
      rs.setInternalNote(rateSheetUpdatesDto.getInternalNote());
      rs.setUpdatedBy(rateSheetUpdatesDto.getCreatedBy());
      firstEntityManager.merge(rs);

      if (rs.getAutogenRsUpdatesId() != null
          && !BigInteger.ZERO.equals(rs.getAutogenRsUpdatesId())) {
        if (rateSheetUpdatesDto.getRatesheetUpdatesMaps() != null) {
          Query qryObj = null;
          qryObj = firstEntityManager.createQuery(
              "SELECT i FROM RatesheetUpdatesMap i WHERE autogenRsUpdatesId=:RSUPDATESID");
          qryObj.setParameter("RSUPDATESID", rs.getAutogenRsUpdatesId());
          List<RatesheetUpdatesMap> ratesheetUpdatesMapList = qryObj.getResultList();
          List<RatesheetUpdatesMap> newRatesheetUpdatesMapList =
              rateSheetUpdatesDto.getRatesheetUpdatesMaps();
          List<RatesheetUpdatesMap> removeRatesheetUpdatesMapList = new ArrayList<>();
          List<BigInteger> existingIds = new ArrayList<>();
          for (RatesheetUpdatesMap ratesheetUpdatesMap : rateSheetUpdatesDto
              .getRatesheetUpdatesMaps()) {
            for (RatesheetUpdatesMap ratesheetUpdatesMap2 : ratesheetUpdatesMapList) {
              if (ratesheetUpdatesMap2.getAutogenRsId().equals(ratesheetUpdatesMap.getAutogenRsId())
                  && ratesheetUpdatesMap2.getAutogenRsUpdatesId()
                      .equals(ratesheetUpdatesMap.getAutogenRsUpdatesId())) {
                existingIds.add(ratesheetUpdatesMap2.getAutogenRsUpdatesMapId());
                removeRatesheetUpdatesMapList.add(ratesheetUpdatesMap);
              }
            }
          }

          if (!existingIds.isEmpty()) {
            existingIds.forEach(mapId -> {
              Query qryObjTemp = firstEntityManager.createNativeQuery(
                  "UPDATE RATESHEET_UPDATES_MAP SET status=:STATUS WHERE AUTOGEN_RS_UPDATES_MAP_ID=:MAPID");
              qryObjTemp.setParameter("MAPID", mapId);
              qryObjTemp.setParameter(AppicationConstants.STATUS_STR,
                  AppicationConstants.ACTIVE_STR);
              qryObjTemp.executeUpdate();
            });
          }

          newRatesheetUpdatesMapList.removeAll(removeRatesheetUpdatesMapList);
          for (RatesheetUpdatesMap ratesheetUpdatesMap : newRatesheetUpdatesMapList) {
           // ratesheetUpdatesMap.setStatus("Active");
            firstEntityManager.persist(ratesheetUpdatesMap);
          }
        }
        flag = true;
      }
    } catch (Exception e) {
      flag = false;
      logger.info("Exception :: RateSheetDAOImpl :: updateRateSheetUpdates() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return flag;
  }*/
  
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Object[]> getRateSheetUpdatesDet(BigInteger rateSheetId, String schedularDate) throws Exception {
    List<Object[]> result = null;
      try {
        Query qry =  firstEntityManager.createNativeQuery("SELECT DISTINCT UPDATE_NAME, PARTY_CODE, PARTY_NAME, UPDATE_DETAILS, VALID_FROM, VALID_TILL, INTERNAL_NOTE FROM RATESHEET_UPDATES WHERE UPDATE_NAME !='INTERNAL UPDATE' AND STATUS='Active' AND  AUTOGEN_RS_ID=:RSID AND cast(REC_ADD_DT as date) = '"
            + DateUtil.getFormattedDate(schedularDate, "dd.MM.yyyy", "yyyy-MM-dd")
            + "'");
        qry.setParameter("RSID", rateSheetId);
        result = qry.getResultList();
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: getRateSheetUpdatesDet() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return result;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> getRatesheetAppParamDet(String type) throws Exception {
    List<Object[]> result = null;
    Map<String, String> maps = new HashMap<>();
      try {
        Query qry =  firstEntityManager.createNativeQuery("SELECT PARAM_KEY, PARAM_VALUE FROM  RATESHEET_APPLICATION_PARAM WHERE STATUS='Active' AND PARAM_KEY IN("+type+")");
        result = qry.getResultList();
        
        if(result != null && !result.isEmpty()) {
          for (Object[] objects : result) {
            maps.put(CommonUtil.nullRemove(objects[0]), CommonUtil.nullRemove(objects[1]));
          }
        }
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: getRatesheetAppParamDet() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return maps;
  }
  
  public Map<String, Object[]> getPartyMastDet() throws Exception{
    Map<String, Object[]> mapResult = new HashMap<>();
      try {
        Query qry =  firstEntityManager.createNativeQuery("SELECT p.partycode, p.partyname, p.catcode, c.catname, p.sectorcode, s.sectorname, p.citycode, ct.cityname FROM " + 
            " partymast AS p INNER JOIN " + 
            " catmast AS c ON p.catcode = c.catcode INNER JOIN " + 
            " sectormaster AS s ON p.sectorcode = s.sectorcode INNER JOIN " + 
            " citymast AS ct ON p.citycode = ct.citycode");
        List<Object[]> result = qry.getResultList();
        if(result != null && !result.isEmpty()) {
          for (Object[] objects : result) {
            mapResult.put( CommonUtil.nullRemove(objects[1]), objects);
          }
        }
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: getPartyMastDet() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return mapResult;
  }
  
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public RatesheetMgt saveRateSheetDetNew(RatesheetMgt rsMgt) throws Exception {
        rsMgt.setStatus("Active");
        rsMgt.setCreatedBy(userInfo.getEmployeeId());
        firstEntityManager.persist(rsMgt);
      return rsMgt;
  }
  
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public RatesheetMapping saveRateSheetMapDetNew(RatesheetMapping rsMap) throws Exception {
      rsMap.setStatus("Active");
      rsMap.setCreatedBy(userInfo.getEmployeeId());
        firstEntityManager.persist(rsMap);
      return rsMap;
  }
  
  public List<RatesheetMgt> findRateSheetDet(String rName) throws Exception {
    List<RatesheetMgt>  result = null;
      try {
        Query qry =  firstEntityManager.createQuery("select rm from RatesheetMgt rm where rsName=:RSNAME and status='Active'");
        qry.setParameter("RSNAME", rName);
        result = (List<RatesheetMgt>) qry.getResultList();
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: findRateSheetDet() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return result;
  }
  
  public List<RatesheetMgt> getAllRateSheetDet() throws Exception {
    List<RatesheetMgt>  result = null;
      try {
        Query qry =  firstEntityManager.createQuery("select rm from RatesheetMgt rm where status='Active' order by priority asc");
        result = (List<RatesheetMgt>) qry.getResultList();
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: findRateSheetDet() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return result;
  }
  
  public Map<?, ?> getRateSheetsDivByMap(String type) throws Exception {
    List<RatesheetMgt>  result = null;
    Map<BigInteger, String> map1 = new HashMap<>();
    Map<String, String> map2 = new HashMap<>();
      try {
        if("RATESHEET".equalsIgnoreCase(type)) {
        Query qry =  firstEntityManager.createQuery("select rm from RatesheetMgt rm where status='Active' order by priority asc");
        result = (List<RatesheetMgt>) qry.getResultList();
        if(result != null && !result.isEmpty()) {
          result.stream().forEach(obj->map1.put(obj.getAutogenRsId(), obj.getDivisionId()));
          return map1;
        }
        } else {
        
        Query qry =  firstEntityManager.createNativeQuery("select RS.party_code , S.sectorname as Sector_Name\r\n" + 
            "\r\n" + 
            "from RATESHEET_MAPPING RS inner join partymast P on RS.party_code = P.partycode\r\n" + 
            "\r\n" + 
            "inner join  sectormaster s on S.sectorcode = P.sectorcode");
        List<Object[]> result2  = (List<Object[]>) qry.getResultList();
        if(result2 != null && !result2.isEmpty()) {
          result2.stream().forEach(obj->map2.put(CommonUtil.nullRemove(obj[0]), CommonUtil.nullRemove(obj[1])));
          return map2;
        }
        }
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: findRateSheetDet() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return map1;
  }
  
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public Object[] uploadRateSheetDet(List<List<String>> insertList) throws Exception {
    Object[] result = new Object[2];
    List<List<String>> failureRecList = new ArrayList<>();
    List<List<String>> successRecList = new ArrayList<>();
    Map<String, Object[]> partyMap = getPartyMastDet();
    RatesheetMgt rsMgt = new RatesheetMgt();
    List<RatesheetMapping> rsMaps = new ArrayList<>();
    for (int i = 0; i < insertList.size(); i++) {
      List<String> userList = insertList.get(i);
      RatesheetMapping rsMap = new RatesheetMapping();
      Object[] partyObj = null;
      if (0 == i) {
        for (int j = 0; j < userList.size(); j++) {
          if (1 == j) {
            rsMgt.setDivisionName(userList.get(j));
            if ("Royal Park Tourism".equalsIgnoreCase(rsMgt.getDivisionName())) {
              rsMgt.setDivisionId("001");
            }
            if ("Royal Gulf Tourism".equalsIgnoreCase(rsMgt.getDivisionName())) {
              rsMgt.setDivisionId("002");
            }
          }
          if (2 == j) {
            rsMgt.setRsName(userList.get(j));
          }
          if (3 == j) {
            rsMgt.setRsDesc(userList.get(j));
          }
          if (4 == j) {
            rsMgt.setEmailIds(userList.get(j));
          }
          if (5 == j) {
            String[] agentArr =  CommonUtil.nullRemove(userList.get(j)).split("-");
            rsMgt.setAgentName(agentArr[0]);
            rsMgt.setAgentCode(agentArr[1]);
          }
          if (6 == j) {
            rsMgt.setSendRatesheet(userList.get(j));
          }
          if (7 == j) {
            rsMgt.setSendUpdates(userList.get(j));
          }
          if (8 == j) {
            rsMgt.setSheetPassword(userList.get(j));
          }
          if (9 == j) {
            rsMap.setDisplayName(userList.get(j));
          }
          if (10 == j) {
            rsMap.setPartyName(userList.get(j));
            partyObj = partyMap.get(userList.get(j).trim());
            rsMap.setPartyCode(CommonUtil.nullRemove(partyObj[0]).trim());
            rsMap.setPartyName(CommonUtil.nullRemove(partyObj[1]).trim());
            rsMap.setCategoryCode(CommonUtil.nullRemove(partyObj[2]).trim());
            rsMap.setCategoryName(CommonUtil.nullRemove(partyObj[3]).trim());
            rsMap.setSectorCode(CommonUtil.nullRemove(partyObj[4]).trim());
            rsMap.setSectorName(CommonUtil.nullRemove(partyObj[5]).trim());
            rsMap.setCityCode(CommonUtil.nullRemove(partyObj[6]).trim());
            rsMap.setCityName(CommonUtil.nullRemove(partyObj[7]).trim());
            rsMap.setCountryCode(CommonUtil.nullRemove(partyObj[8]).trim());
            rsMap.setCountryName(CommonUtil.nullRemove(partyObj[9]).trim());
          }
        }
        
        try {
          saveRateSheetDetNew(rsMgt);
          if(partyObj != null) {
          if(rsMgt != null && rsMgt.getAutogenRsId() != null) {
            rsMap.setAutogenRsId(rsMgt.getAutogenRsId());
            saveRateSheetMapDetNew(rsMap);
          }
          int index = (insertList.get(i)).size();
          (insertList.get(i)).set(index - 1, "success");
          successRecList.add(insertList.get(i));
          } else {
            logger.error("Hotel Name Incorrect Please Correct::"+userList);
          }
        } catch (Exception e) {
          String errReason = "";
          if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            errReason = "Unique Constraint Violation Exception: Mapping";
          } else {
            errReason = e.getMessage();
          }

          int index = (insertList.get(i)).size();
          (insertList.get(i)).set(index - 1, errReason);
          failureRecList.add(insertList.get(i));
        }
        
      } else {
        for (int j = 0; j < userList.size(); j++) {
          if (9 == j) {
            rsMap.setDisplayName(userList.get(j));
          }
          if (10 == j) {
            rsMap.setPartyName(userList.get(j));
             partyObj = partyMap.get(userList.get(j).trim());
            if(partyObj == null) {
              continue;
            }
            rsMap.setPartyCode(CommonUtil.nullRemove(partyObj[0]).trim());
            rsMap.setPartyName(CommonUtil.nullRemove(partyObj[1]).trim());
            rsMap.setCategoryCode(CommonUtil.nullRemove(partyObj[2]).trim());
            rsMap.setCategoryName(CommonUtil.nullRemove(partyObj[3]).trim());
            rsMap.setSectorCode(CommonUtil.nullRemove(partyObj[4]).trim());
            rsMap.setSectorName(CommonUtil.nullRemove(partyObj[5]).trim());
            rsMap.setCityCode(CommonUtil.nullRemove(partyObj[6]).trim());
            rsMap.setCityName(CommonUtil.nullRemove(partyObj[7]).trim());
            rsMap.setCountryCode(CommonUtil.nullRemove(partyObj[8]).trim());
            rsMap.setCountryName(CommonUtil.nullRemove(partyObj[9]).trim());
          }
        }
        
        try {
          if(partyObj != null) {
          if(rsMgt != null && rsMgt.getAutogenRsId() != null) {
            rsMap.setAutogenRsId(rsMgt.getAutogenRsId());
            saveRateSheetMapDetNew(rsMap);
          }
          int index = (insertList.get(i)).size();
          (insertList.get(i)).set(index - 1, "success");
          successRecList.add(insertList.get(i));
          } else {
            logger.error("Hotel Name Incorrect Please Correct::"+userList);
          }
        } catch (Exception e) {
          String errReason = "";
          if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            errReason = "Unique Constraint Violation Exception: Mapping";
          } else {
            errReason = e.getMessage();
          }

          int index = (insertList.get(i)).size();
          (insertList.get(i)).set(index - 1, errReason);
          failureRecList.add(insertList.get(i));
        }
        
      }
      
    }
  
    result[0] = successRecList;
    result[1] = failureRecList;
    return result;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object[] getCountryCode(String agentCode) throws Exception {
    Object[] Object = new Object[3];
    List<Object[]> result = null;
      try {
        Query qry =  firstEntityManager.createNativeQuery("SELECT AM.CTRYCODE , CY.CTRYNAME, AM.CURRCODE FROM AGENTMAST AS AM INNER JOIN CTRYMAST AS CY ON AM.CTRYCODE = CY.CTRYCODE WHERE AGENTCODE=:AGENTCODE");
        qry.setParameter("AGENTCODE", agentCode);
        result = qry.getResultList();
        if(result != null && !result.isEmpty()) {
          for (Object[] objects : result) {
            Object[0] = CommonUtil.nullRemove(objects[0]).trim();
            Object[1] = CommonUtil.nullRemove(objects[1]).trim();
            Object[2] = CommonUtil.nullRemove(objects[2]).trim();
          }
        }
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: getCountryCode() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return Object;
  }
  
  @SuppressWarnings("unchecked")
  public List<Object[]> getMappedHotelDropdown(String partyCode) throws Exception {
    List<Object[]> result = null;
    Query qry = null;
    try {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT DISTINCT AUTOGEN_RS_ID, RS_NAME FROM RATESHEET_MGT WHERE  AUTOGEN_RS_ID IN (SELECT AUTOGEN_RS_ID from RATESHEET_MAPPING WHERE STATUS='Active' AND PARTY_CODE=:PARTYCODE)");
      qry =  firstEntityManager.createNativeQuery(sb.toString());
      qry.setParameter("PARTYCODE", partyCode);
      result = (List<Object[]>) qry.getResultList();
    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: getMappedHotelDropdown() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return result;
  }
  
  @SuppressWarnings("unchecked")
  public Object[] getRateSheetMgtDropdown(String type) throws Exception {
    Object[] returnObj = new Object[10];
    List<Object[]>  result = null;
    Query qry = null;
      try {
          StringBuilder sb = new StringBuilder();
          if("RSMAPS_DROPDOWN".equalsIgnoreCase(type)) {
          sb.append("SELECT AUTOGEN_RS_ID, RS_NAME from RATESHEET_MGT WHERE STATUS='Active'");
          qry =  firstEntityManager.createNativeQuery(sb.toString());
          result = (List<Object[]>) qry.getResultList();
          returnObj[0] = result;
          /**Sector*/
          sb = new StringBuilder();
          sb.append("SELECT distinct p.sectorcode, s.sectorname FROM " + 
              " partymast AS p INNER JOIN " + 
              " catmast AS c ON p.catcode = c.catcode INNER JOIN " + 
              " sectormaster AS s ON p.sectorcode = s.sectorcode INNER JOIN " + 
              " citymast AS ct ON p.citycode = ct.citycode  where  p.sptypecode='HOT' and p.ctrycode='AE' and ct.ctrycode='AE' and s.ctrycode='AE'");
          qry =  firstEntityManager.createNativeQuery(sb.toString());
          result = (List<Object[]>) qry.getResultList();
          returnObj[1] = result;
          sb = new StringBuilder();
          sb.append("SELECT distinct p.citycode, ct.cityname FROM " + 
              " partymast AS p INNER JOIN " + 
              " catmast AS c ON p.catcode = c.catcode INNER JOIN " + 
              " sectormaster AS s ON p.sectorcode = s.sectorcode INNER JOIN " + 
              " citymast AS ct ON p.citycode = ct.citycode  where  p.sptypecode='HOT' and p.ctrycode='AE' and ct.ctrycode='AE' and s.ctrycode='AE'");
          qry =  firstEntityManager.createNativeQuery(sb.toString());
          result = (List<Object[]>) qry.getResultList();
          returnObj[2] = result;
          sb = new StringBuilder();
          sb.append("SELECT distinct p.catcode, c.catname FROM " + 
              " partymast AS p INNER JOIN " + 
              " catmast AS c ON p.catcode = c.catcode INNER JOIN " + 
              " sectormaster AS s ON p.sectorcode = s.sectorcode INNER JOIN " + 
              " citymast AS ct ON p.citycode = ct.citycode  where  p.sptypecode='HOT' and p.ctrycode='AE' and ct.ctrycode='AE' and s.ctrycode='AE' and p.catcode != 'EXCU'");
          qry =  firstEntityManager.createNativeQuery(sb.toString());
          result = (List<Object[]>) qry.getResultList();
          returnObj[3] = result;
          }
          if("RS_AGENT_DROPDOWN".equalsIgnoreCase(type)) {
          sb = new StringBuilder();
          sb.append("SELECT A.AGENTCODE, A.AGENTNAME, A.DIVCODE, DM.DIVISION_MASTER_DES FROM AGENTMAST AS A INNER JOIN DIVISION_MASTER AS DM ON DM.DIVISION_MASTER_CODE=A.DIVCODE  WHERE ACTIVE=1");
          qry =  firstEntityManager.createNativeQuery(sb.toString());
          result = (List<Object[]>) qry.getResultList();
          returnObj[4] = result;
          }
          if("UPDATE_DROPDOWN".equalsIgnoreCase(type)) {
          sb = new StringBuilder();
          sb.append("SELECT PARAM_KEY, PARAM_VALUE FROM RATESHEET_APPLICATION_PARAM WHERE STATUS='Active' AND PARAM_KEY='UPDATE_TYPE'");
          qry =  firstEntityManager.createNativeQuery(sb.toString());
          result = (List<Object[]>) qry.getResultList();
          returnObj[5] = result;
          
          sb = new StringBuilder();
          sb.append("SELECT AUTOGEN_RS_ID, RS_NAME from RATESHEET_MGT WHERE STATUS='Active'");
          qry =  firstEntityManager.createNativeQuery(sb.toString());
          result = (List<Object[]>) qry.getResultList();
          returnObj[6] = result;
          
          sb = new StringBuilder();
          sb.append("SELECT PARTYCODE,PARTYNAME FROM PARTYMAST WHERE ACTIVE=1 ORDER BY PARTYNAME ASC");
          qry =  firstEntityManager.createNativeQuery(sb.toString());
          result = (List<Object[]>) qry.getResultList();
          returnObj[7] = result;
          
          sb = new StringBuilder();
          sb.append("SELECT EMPLOYEE_ID, CONCAT(FIRST_NAME ,',', LAST_NAME) AS NAME FROM RATESHEET_USERS WHERE STATUS='Active' ORDER BY FIRST_NAME ASC");
          qry =  firstEntityManager.createNativeQuery(sb.toString());
          result = (List<Object[]>) qry.getResultList();
          returnObj[8] = result;
          }
       } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: getRateSheetMgtDropdown() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return returnObj;
  }
  
  @SuppressWarnings("unchecked")
  public List<Object[]> getRateSheetMapSearch(BigInteger rsId, String categoryCode, String sectorCode, String cityCode) throws Exception {
    List<Object[]>  result = null;
    StringBuilder qryStr = new StringBuilder();
    StringBuilder conQry1 = new StringBuilder();
    StringBuilder conQry2 = new StringBuilder();
    /**Condition Qry1*/
    if(categoryCode != null && !categoryCode.isEmpty()) {
      conQry1.append(" and p.catcode ='"+categoryCode.trim()+"'");
    }
    if(sectorCode != null && !sectorCode.isEmpty()) {
      conQry1.append(" and p.sectorcode='"+sectorCode.trim()+"'");
    }
    if(cityCode != null && !cityCode.isEmpty()) {
      conQry1.append(" and p.citycode='"+cityCode.trim()+"'");
    }
    
    /**Condition Qry2*/
    if(categoryCode != null && !categoryCode.isEmpty()) {
      conQry2.append(" and rsm.CATEGORY_CODE ='"+categoryCode.trim()+"'");
    }
    if(sectorCode != null && !sectorCode.isEmpty()) {
      conQry2.append(" and rsm.SECTOR_CODE='"+sectorCode.trim()+"'");
    }
    if(cityCode != null && !cityCode.isEmpty()) {
      conQry2.append(" and rsm.CITY_CODE='"+cityCode.trim()+"'");
    }
    if(!BigInteger.ZERO.equals(rsId)) {
     conQry2.append(" and rsm.AUTOGEN_RS_ID="+rsId);
    }
    
    qryStr.append("SELECT  'false' as mapped, 0 as id, p.partycode, p.partyname, '' as displayname, p.catcode, c.catname, p.sectorcode, s.sectorname,");
    qryStr.append(" p.citycode, ct.cityname FROM partymast AS p INNER JOIN catmast AS c ON p.catcode = c.catcode INNER JOIN sectormaster AS s ON p.sectorcode = s.sectorcode INNER JOIN");
    qryStr.append(" citymast AS ct ON p.citycode = ct.citycode where p.active=1 "+conQry1);
    qryStr.append(" and p.partycode NOT IN( SELECT  rsm.party_code as partycode FROM RATESHEET_MAPPING rsm where rsm.STATUS in('Active','InActive') "+conQry2);
    qryStr.append(" ) union ");
    qryStr.append("SELECT  'true' as mapped,AUTOGEN_RS_MAP_ID as id, rsm.party_code as partycode, rsm.party_name as partyname, rsm.DISPLAY_NAME as displayname, rsm.CATEGORY_CODE as catcode, rsm.CATEGORY_NAME as catname, rsm.SECTOR_CODE as sectorcode, rsm.SECTOR_NAME as sectorcode, rsm.CITY_CODE as citycode, rsm.city_name");
    qryStr.append(" FROM RATESHEET_MAPPING rsm where rsm.STATUS='Active' "+conQry2);
    qryStr.append(" union ");
    qryStr.append("SELECT 'false' as mapped,AUTOGEN_RS_MAP_ID as id, rsm.party_code as partycode, rsm.party_name as partyname, rsm.DISPLAY_NAME as displayname, rsm.CATEGORY_CODE as catcode, rsm.CATEGORY_NAME as catname, rsm.SECTOR_CODE as sectorcode, rsm.SECTOR_NAME as sectorcode, rsm.CITY_CODE as citycode, rsm.city_name");
    qryStr.append(" FROM RATESHEET_MAPPING rsm where rsm.STATUS='InActive' "+conQry2);
    
      try {
        Query qry =  firstEntityManager.createNativeQuery(qryStr.toString());
        result = (List<Object[]>) qry.getResultList();
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: findRateSheetDet() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return result;
  }
  
  public List<RatesheetMgt> findRateSheetDetByRsId(BigInteger id) throws Exception {
    List<RatesheetMgt>  result = null;
      try {
        Query qry =  firstEntityManager.createQuery("select rm from RatesheetMgt rm where autogenRsId=:RSID and status='Active'");
        qry.setParameter("RSID", id);
        result = (List<RatesheetMgt>) qry.getResultList();
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: findRateSheetDetByRsId() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return result;
  }
  
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public RatesheetMappingDto saveRatesheetMap(RatesheetMappingDto ratesheetMappingDto) throws Exception {
      try {
        Query qry =  firstEntityManager.createNativeQuery("SELECT CTRYCODE, CTRYNAME FROM CTRYMAST WHERE ACTIVE=1 AND CTRYCODE=(SELECT CTRYCODE FROM AGENTMAST WHERE ACTIVE=1 AND AGENTCODE=(SELECT AGENT_CODE FROM  RATESHEET_MGT WHERE AUTOGEN_RS_ID=:RSID))");
        qry.setParameter("RSID", ratesheetMappingDto.getAutogenRsId());
        List<Object[]> countryObj = (List<Object[]>) qry.getResultList();
        String countryCode = "";
        String countryName = "";
        if(countryObj != null && !countryObj.isEmpty()) {
          Object[] objVal = countryObj.get(0);
          countryCode = CommonUtil.nullRemove(objVal[0]).trim();
          countryName = CommonUtil.nullRemove(objVal[1]).trim();
        }
        List<RatesheetMappingRequest> newMapList = ratesheetMappingDto.getRatesheetMappingList();
        ratesheetMappingDto.setStatus(false);
        for(RatesheetMappingRequest newObj : newMapList){
          if(newObj.getId() != null && !BigInteger.ZERO.equals(newObj.getId())) {
            Query qryObjTemp = firstEntityManager.createNativeQuery(
                "UPDATE RATESHEET_MAPPING SET STATUS=:STATUS,UPDATED_BY=:UPDATEDUSER, DISPLAY_NAME=:DNAME WHERE AUTOGEN_RS_MAP_ID=:RSMAPID");
            qryObjTemp.setParameter("RSMAPID", newObj.getId());
            qryObjTemp.setParameter("UPDATEDUSER", userInfo.getEmployeeId());
            qryObjTemp.setParameter("DNAME", CommonUtil.nullRemove(newObj.getDisplayName()).trim());
            if(newObj.isMapped()) {
              qryObjTemp.setParameter(AppicationConstants.STATUS_STR, AppicationConstants.ACTIVE_STR);
            } else {
              qryObjTemp.setParameter(AppicationConstants.STATUS_STR, AppicationConstants.INACTIVE_STR);
            }
             qryObjTemp.executeUpdate();
          } else {
            if(newObj.isMapped()) {
              String qryStr = "Select AUTOGEN_RS_MAP_ID, AUTOGEN_RS_ID from RATESHEET_MAPPING WHERE AUTOGEN_RS_ID="+ratesheetMappingDto.getAutogenRsId();
              qryStr = qryStr + " AND SECTOR_CODE='"+CommonUtil.nullRemove(newObj.getSectorCode()).trim()+"' AND CATEGORY_CODE='"+CommonUtil.nullRemove(newObj.getCategoryCode()).trim()+"'";
              qryStr = qryStr + " AND COUNTRY_CODE='"+CommonUtil.nullRemove(countryCode).trim()+"' AND CITY_CODE='"+CommonUtil.nullRemove(newObj.getCityCode()).trim()+"'";
              qryStr = qryStr + " AND PARTY_CODE='"+CommonUtil.nullRemove(countryCode).trim()+"'";
              qry =  firstEntityManager.createNativeQuery(qryStr);
              List<Object[]> existingMapList = (List<Object[]>) qry.getResultList();
            if(existingMapList != null && !existingMapList.isEmpty()) {
              Object[] objVal = existingMapList.get(0);
              Query qryObjTemp = firstEntityManager.createNativeQuery(
                  "UPDATE RATESHEET_MAPPING SET STATUS=:STATUS,UPDATED_BY=:UPDATEDUSER,DISPLAY_NAME=:DNAME WHERE AUTOGEN_RS_MAP_ID=:RSMAPID");
              qryObjTemp.setParameter("RSMAPID", CommonUtil.nullRemove(objVal[0]).trim());
              qryObjTemp.setParameter("UPDATEDUSER", userInfo.getEmployeeId());
              qryObjTemp.setParameter("DNAME", CommonUtil.nullRemove(newObj.getDisplayName()).trim());
              qryObjTemp.setParameter(AppicationConstants.STATUS_STR, AppicationConstants.ACTIVE_STR);
              qryObjTemp.executeUpdate();
            } else {
              RatesheetMapping ratesheetMapping = new RatesheetMapping();
              ratesheetMapping.setAutogenRsId(ratesheetMappingDto.getAutogenRsId());
              ratesheetMapping.setPartyCode(CommonUtil.nullRemove(newObj.getPartyCode()).trim());
              ratesheetMapping.setPartyName(CommonUtil.nullRemove(newObj.getPartyName()).trim());
              ratesheetMapping.setCategoryCode(CommonUtil.nullRemove(newObj.getCategoryCode()).trim());
              ratesheetMapping.setCategoryName(CommonUtil.nullRemove(newObj.getCategoryName()).trim());
              ratesheetMapping.setSectorCode(CommonUtil.nullRemove(newObj.getSectorCode()).trim());
              ratesheetMapping.setSectorName(CommonUtil.nullRemove(newObj.getSectorName()).trim());
              ratesheetMapping.setCityCode(CommonUtil.nullRemove(newObj.getCityCode()).trim());
              ratesheetMapping.setCityName(CommonUtil.nullRemove(newObj.getCityName()).trim());
              ratesheetMapping.setCountryCode(CommonUtil.nullRemove(countryCode).trim());
              ratesheetMapping.setCountryName(CommonUtil.nullRemove(countryName).trim());
              ratesheetMapping.setDisplayName(CommonUtil.nullRemove(newObj.getDisplayName()).trim());
              ratesheetMapping.setCreatedBy(userInfo.getEmployeeId());
              ratesheetMapping.setStatus(AppicationConstants.ACTIVE_STR);
              firstEntityManager.persist(ratesheetMapping);
            }
            }
          }
          ratesheetMappingDto.setStatus(true);
         }
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: saveRatesheetMap() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return ratesheetMappingDto;
  }

  @Override
  public List<RatesheetMgt> getRateSheetWithMaps(RatesheetMgtDto ratesheetMgtDto) throws Exception {
    List<RatesheetMgt>  result = null;
    try {
      Query qry =  firstEntityManager.createQuery("select rm from RatesheetMgt rm where status='Active' and autogenRsId=:RSID");
      qry.setParameter("RSID", ratesheetMgtDto.getAutogenRsId());
      result = (List<RatesheetMgt>) qry.getResultList();
    } catch(Exception e) {
        logger.info("Exception :: RateSheetDAOImpl :: getRateSheetWithMaps() : " + e);
    } finally {
        firstEntityManager.close();
    }
    return result;
}
  
  public List<RatesheetUpdates> getRateSheetUpdates() throws Exception {
    List<RatesheetUpdates>  result = null;
      try {
        Query qry =  firstEntityManager.createQuery("select ru from RatesheetUpdates ru where status='Active' and cast(recAddDt as date)= cast(GETDATE() as date) order by recAddDt desc");
        result = (List<RatesheetUpdates>) qry.getResultList();
      } catch(Exception e) {
          logger.info("Exception :: RateSheetDAOImpl :: getAllRateSheetUpdates() : " + e);
      } finally {
          firstEntityManager.close();
      }
      return result;
  }
  
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Object[] saveRateSheetUpdates(RSUpdatesCreateReq insertObj) throws Exception {
    Object[] result = new Object[2];
    try {
      if (insertObj != null) {
        RatesheetUpdates rsUpdates = new RatesheetUpdates();
        rsUpdates.setInternalNote(insertObj.getInternalNote());
        rsUpdates.setPartyCode(insertObj.getPartyCode());
        rsUpdates.setPartyName(insertObj.getPartyName());
        rsUpdates.setRecAddDt(new Date());
        rsUpdates.setStatus(insertObj.getStatus());
        rsUpdates.setUpdateName(insertObj.getUpdateName());
        rsUpdates.setUpdateDetails(insertObj.getUpdateDetails());
        rsUpdates.setCreatedBy(userInfo.getEmployeeId());
        if (insertObj.getValidFrom() != null && !insertObj.getValidFrom().isEmpty()) {
          rsUpdates
              .setValidFrom(DateUtil.convertStringtoDate(insertObj.getValidFrom(), "dd/MM/yyyy"));
        } else {
          rsUpdates.setValidFrom(null);
        }
        if (insertObj.getValidTill() != null && !insertObj.getValidTill().isEmpty()) {
          rsUpdates
              .setValidTill(DateUtil.convertStringtoDate(insertObj.getValidTill(), "dd/MM/yyyy"));
        } else {
          rsUpdates.setValidTill(null);
        }
        rsUpdates.setAutogenRsId(insertObj.getRsId());
        rsUpdates.setRsName(insertObj.getRsName());
        firstEntityManager.persist(rsUpdates);
        result[0] = true;
      }

    } catch (Exception e) {
      throw e;
    } finally {
      firstEntityManager.close();
    }

    return result;

  }
  
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Object[] updateRateSheetUpdates(RSUpdatesCreateReq insertObj) throws Exception {
    Object[] result = new Object[2];
    try {
      if (insertObj != null) {
        if (insertObj.getRsUpdatesId() != null
            && !BigInteger.ZERO.equals(insertObj.getRsUpdatesId())) {
          result[0] = false;
          // Existing Updates
          RatesheetUpdates rsUpdates =
              firstEntityManager.find(RatesheetUpdates.class, insertObj.getRsUpdatesId());
          if (rsUpdates != null) {
            rsUpdates.setInternalNote(insertObj.getInternalNote());
            rsUpdates.setPartyCode(insertObj.getPartyCode());
            rsUpdates.setPartyName(insertObj.getPartyName());
            rsUpdates.setRecUpdateDt(new Date());
            rsUpdates.setStatus(insertObj.getStatus());
            rsUpdates.setUpdateName(insertObj.getUpdateName());
            rsUpdates.setUpdateDetails(insertObj.getUpdateDetails());
            rsUpdates.setUpdatedBy(userInfo.getEmployeeId());
            if (insertObj.getValidFrom() != null && !insertObj.getValidFrom().isEmpty())
              rsUpdates.setValidFrom(
                  DateUtil.convertStringtoDate(insertObj.getValidFrom(), "dd/MM/yyyy"));
            else
              rsUpdates.setValidFrom(null);
            if (insertObj.getValidTill() != null && !insertObj.getValidTill().isEmpty())
              rsUpdates.setValidTill(
                  DateUtil.convertStringtoDate(insertObj.getValidTill(), "dd/MM/yyyy"));
            else
              rsUpdates.setValidTill(null);
            rsUpdates.setAutogenRsId(insertObj.getRsId());
            rsUpdates.setRsName(insertObj.getRsName());
            firstEntityManager.merge(rsUpdates);
            result[0] = true;
          }
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      firstEntityManager.close();
    }
    return result;
  }
  
  public List<Object[]> searchRSUpdatesView(RatesheetUpdateViewRequest ratesheetViewRequest)
      throws Exception {

    List<Object[]> result = null;
    try {
      StringBuilder sb = new StringBuilder();
      sb.append(
          "SELECT AUTOGEN_RS_UPDATES_ID, UPDATE_NAME, PARTY_CODE, PARTY_NAME, UPDATE_DETAILS, VALID_FROM, VALID_TILL, INTERNAL_NOTE, AUTOGEN_RS_ID, RS_NAME, CREATED_BY, UPDATED_BY FROM RATESHEET_UPDATES WHERE STATUS='Active' ");
      if (ratesheetViewRequest.getUpdateName() != null
          && !ratesheetViewRequest.getUpdateName().isEmpty()) {
        String[] updateTypes = CommonUtil.nullRemove(ratesheetViewRequest.getUpdateName()).split(",");
        String commaSepVal = "";
        for(int i = 0; i<updateTypes.length ;i++) {
          if (!CommonUtil.nullRemove(updateTypes[i]).isEmpty()) {
              commaSepVal = commaSepVal + "'" + updateTypes[i] + "'";
            if(i != updateTypes.length-1) {
              commaSepVal = commaSepVal + ",";
            }
          }
        }
        sb.append(" AND UPDATE_NAME IN(" + commaSepVal+ ")");
      }
      if (ratesheetViewRequest.getPartyCode() != null
          && !ratesheetViewRequest.getPartyCode().isEmpty()) {
        sb.append(" AND PARTY_CODE='" + ratesheetViewRequest.getPartyCode() + "'");
      }
      if (ratesheetViewRequest.getRsId() != null
          && !BigInteger.ZERO.equals(ratesheetViewRequest.getRsId())) {
        sb.append(" AND AUTOGEN_RS_ID=" + ratesheetViewRequest.getRsId());
      }
      if (ratesheetViewRequest.getCreatedBy() != null
          && !ratesheetViewRequest.getCreatedBy().isEmpty()) {
        sb.append(" AND CREATED_BY='" + ratesheetViewRequest.getCreatedBy() + "'");
      }
      if (ratesheetViewRequest.getFrom() != null && !ratesheetViewRequest.getFrom().isEmpty()) {
        sb.append(" AND REC_ADD_DT IS NOT NULL AND cast(REC_ADD_DT as date) >= '"
            + DateUtil.getFormattedDate(ratesheetViewRequest.getFrom(), "dd/MM/yyyy", "yyyy-MM-dd")
            + "'");
      }
      if (ratesheetViewRequest.getTo() != null && !ratesheetViewRequest.getTo().isEmpty()) {
        sb.append(" AND cast(REC_ADD_DT as date) <= '"
            + DateUtil.getFormattedDate(ratesheetViewRequest.getTo(), "dd/MM/yyyy", "yyyy-MM-dd")
            + "'");
      }

      Query qry = firstEntityManager.createNativeQuery(sb.toString());
      result = (List<Object[]>) qry.getResultList();

    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: searchRSUpdatesView() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return result;

  }
  
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public boolean ratesheetMapStatusChange(List<BigInteger> list, BigInteger rsId) throws Exception {
    Query qryObjTemp = null;
    try {
    for(BigInteger mapId: list) {
     qryObjTemp = firstEntityManager.createNativeQuery(
        "UPDATE ratesheet_mapping SET CURRENT_UPDATE=:STATUS WHERE autogen_rs_map_id=:AUTOGENRSMAPID and autogen_rs_id=:AUTOGENRSID");
      qryObjTemp.setParameter("AUTOGENRSID", rsId);
      qryObjTemp.setParameter(AppicationConstants.STATUS_STR, "N");
      qryObjTemp.setParameter("AUTOGENRSMAPID", mapId);
      qryObjTemp.executeUpdate();
    }
    return true;
    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: ratesheetMapStatusChange() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return false;
  }
  
  @Override
  public List<RatesheetAgentMap> getRateSheetAgnetMap() throws Exception {
    List<RatesheetAgentMap>  result = null;
    try {
      Query qry =  firstEntityManager.createQuery("select rm from RatesheetAgentMap rm");
      result = (List<RatesheetAgentMap>) qry.getResultList();
    } catch(Exception e) {
        logger.info("Exception :: RateSheetDAOImpl :: getRateSheetAgnetMap() : " + e);
    } finally {
        firstEntityManager.close();
    }
    return result;
}
  @Override
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public boolean ratesheetMapUpdateStatus(BigInteger mapId, BigInteger rsId) throws Exception {
    Query qryObjTemp = null;
    try {
     qryObjTemp = firstEntityManager.createNativeQuery(
        "UPDATE ratesheet_mapping SET CURRENT_UPDATE=:STATUS WHERE autogen_rs_map_id=:AUTOGENRSMAPID and autogen_rs_id=:AUTOGENRSID");
      qryObjTemp.setParameter("AUTOGENRSID", rsId);
      qryObjTemp.setParameter(AppicationConstants.STATUS_STR, "N");
      qryObjTemp.setParameter("AUTOGENRSMAPID", mapId);
      qryObjTemp.executeUpdate();
    return true;
    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: ratesheetMapUpdateStatus() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return false;
  }
  
  @Override
  public String checkRatesheetDate() throws Exception {
	    String  result = null;
	      try {
	        Query qry =  firstEntityManager.createNativeQuery("select SchdulerStartTime from Ratesheet_SchdulerTime");
	        Object res = qry.getResultList().get(0);
	        if(res!=null) {
	        	result=res.toString();
	        }
	      } catch(Exception e) {
	          logger.info("Exception :: RateSheetDAOImpl :: checkRatesheetDate() : " + e);
	      } finally {
	          firstEntityManager.close();
	      }
	      return result;
	  }
  
  @Override
  @Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public boolean updateRatesheetSchedularDate() throws Exception {
    Query qryObjTemp = null;
    try {
     qryObjTemp = firstEntityManager.createNativeQuery(
        "UPDATE Ratesheet_SchdulerTime SET SchdulerStartTime=NULL");
      qryObjTemp.executeUpdate();
    return true;
    } catch (Exception e) {
      logger.info("Exception :: RateSheetDAOImpl :: ratesheetMapUpdateStatus() : " + e);
    } finally {
      firstEntityManager.close();
    }
    return false;
  }
}
