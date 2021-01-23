package com.royal.app.message.response;

import java.util.List;

public class RSMapsDropdowns {
  
  private List<KeyValuePair> ratesheets;
  private List<KeyValuePair> categories;
  private List<KeyValuePair> sectors;
  private List<KeyValuePair> cities;
  
  public List<KeyValuePair> getRatesheets() {
    return ratesheets;
  }
  public void setRatesheets(List<KeyValuePair> ratesheets) {
    this.ratesheets = ratesheets;
  }
  public List<KeyValuePair> getCategories() {
    return categories;
  }
  public void setCategories(List<KeyValuePair> categories) {
    this.categories = categories;
  }
  public List<KeyValuePair> getSectors() {
    return sectors;
  }
  public void setSectors(List<KeyValuePair> sectors) {
    this.sectors = sectors;
  }
  public List<KeyValuePair> getCities() {
    return cities;
  }
  public void setCities(List<KeyValuePair> cities) {
    this.cities = cities;
  }
  
}
