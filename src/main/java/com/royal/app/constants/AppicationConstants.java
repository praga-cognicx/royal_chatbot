package com.royal.app.constants;

public class AppicationConstants {
	
	private AppicationConstants() {
	    throw new IllegalStateException("Utility class");
	  }


  public static final String SPRING_BASE_CLASS_PROPERTYSOURCE = "classpath:application.properties";
  public static final String LOGGER_PROPERTYSOURCE = "classpath:log4j.properties";
  // Hibernate Configuration
  public static final String HIBERNATE_DIALECT = "hibernate.dialect";
  public static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
  public static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";

  /* Data Source Names */
  public static final String FIRST_DATA_SOURCE_BEAN_NAME = "firstDataSource";
  public static final String FIRST_ENTITY_MANAGER = "firstEntityManager";
  public static final String FIRST_TRANSACTION_MANAGER = "firstTransactionManager";
  public static final String FIRST_JDBC_TEMPLATE = "firstJdbcTemplate";
  public static final String FIRST_MODAL_PACKAGE = "com.royal.app.model";
  public static final String FIRST_PERSISTENCE_UNIT_NAME = "first";
  
  
  public static final String EMPTY_STR = "";
  public static final String BADCREDENTIALS_EXCEPTION = "Please provide a valid login credentials or if you have forgotten the password please use the ‘Forget password’ to set a new password";
  public static final String USERNAMENOTFOUNDEXCEPTION  = "User account does not exists. Please contact your system administrator.";
  public static final String CLIENT_CONTROLLER_ERR_MSG  = "Unable to save client details. Please contact admin.";
  
  public static final String CLIENTID_STR = "CLIENTID";
  public static final String CENTERID_STR = "CENTERID";
  public static final String REGIONID_STR = "REGIONID";
  public static final String PROCESSID_STR = "PROCESSID";
  public static final String CLIENTDETID_STR = "CLIENTDETID";
  public static final String CONTACTDETID_STR = "CONTACTDETID";
  public static final String YES_STR = "Y";
  public static final String NO_STR = "N";
  public static final String USERNAME_STR = "USERNAME";
  public static final String EMPLOYEEID_STR = "EMPLOYEEID";
  public static final String COMPLETE_STR = "COMPLETE";
  public static final String TOKEN_STR = "TOKEN";
  public static final String ROLESNAME_STR = "ROLESNAME";
  public static final String ACTIVE_STR = "Active";
  public static final String INACTIVE_STR = "InActive";
  public static final String STATUS_STR = "STATUS";
  public static final String CLIENT_STR = "CLIENT";
  public static final String CENTER_STR = "CENTER";
  public static final String REGION_STR = "REGION";
  public static final String PROCESS_STR = "PROCESS";
  public static final String CATEGORY_STR = "CATEGORY";
  public static final String TYPE_STR = "TYPE";
  
  
}
