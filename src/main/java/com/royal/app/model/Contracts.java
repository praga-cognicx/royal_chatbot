package com.royal.app.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

/**
 * 
 * @author balaji
 *
 */

@Entity
@Table(name="CONTRACTS")
public class Contracts {
  @Id
/*  
  @Id
  @Column(name="contractid")
  private String contractId;
 
  @Column(name="partycode")
  private String partyCode; */
  
  @Column(name="todate")
  private Date toDate;

 /* public String getContractId() {
    return contractId;
  }*/

  public Date getToDate() {
    return toDate;
  }
  
  

}
