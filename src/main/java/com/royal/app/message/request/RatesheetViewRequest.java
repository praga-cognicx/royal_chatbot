package com.royal.app.message.request;

import java.math.BigInteger;
import javax.validation.constraints.NotNull;

public class RatesheetViewRequest {
  
  @NotNull
  private BigInteger ratesheetId;

  public BigInteger getRatesheetId() {
    return ratesheetId;
  }

  public void setRatesheetId(BigInteger ratesheetId) {
    this.ratesheetId = ratesheetId;
  }
  
}
