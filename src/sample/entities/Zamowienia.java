package com.sample;


public class Zamowienia {

  private long idZamowienia;
  private java.sql.Date data;
  private long idSklepu;
  private long nip;


  public long getIdZamowienia() {
    return idZamowienia;
  }

  public void setIdZamowienia(long idZamowienia) {
    this.idZamowienia = idZamowienia;
  }


  public java.sql.Date getData() {
    return data;
  }

  public void setData(java.sql.Date data) {
    this.data = data;
  }


  public long getIdSklepu() {
    return idSklepu;
  }

  public void setIdSklepu(long idSklepu) {
    this.idSklepu = idSklepu;
  }


  public long getNip() {
    return nip;
  }

  public void setNip(long nip) {
    this.nip = nip;
  }

}
