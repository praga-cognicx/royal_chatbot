package com.royal.app.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class KeyValuePair {
  
  private String key;
  private String value;
  private String subKey;
  private String subValue;
  
  public KeyValuePair (String key, String value, String subKey, String subValue) {
    this.key = key;
    this.value = value;
    this.subKey = subKey;
    this.subValue = subValue;
  }
  
  public KeyValuePair (String key, String value) {
    this.key = key;
    this.value = value;
  }
  
  public KeyValuePair () {}
  
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
  public String getSubKey() {
    return subKey;
  }
  public void setSubKey(String subKey) {
    this.subKey = subKey;
  }
  public String getSubValue() {
    return subValue;
  }
  public void setSubValue(String subValue) {
    this.subValue = subValue;
  }

}
