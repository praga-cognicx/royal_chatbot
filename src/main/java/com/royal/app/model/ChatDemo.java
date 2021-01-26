package com.royal.app.model;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author balaji
 *
 */

@Entity
@Table(name="CHAT_DEMO")
public class ChatDemo implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="AUTOGEN_RS_PARAM_ID")
  private BigInteger autogenRsParamId;
  
  @Column(name="ID")
  private String id;
  
  @Column(name="KEY_NAME")
  private String key;
  
  @Column(name="VALUE")
  private String value;

  public BigInteger getAutogenRsParamId() {
    return autogenRsParamId;
  }

  public void setAutogenRsParamId(BigInteger autogenRsParamId) {
    this.autogenRsParamId = autogenRsParamId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
  
}
