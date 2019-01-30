package com.sample;


public class DowodySprzedazy {

  private long idDowoduSprzedazy;
  private String kwota;
  private java.sql.Date data;
  private long idKlienta;
  private long idSprzedawcy;
  private String  nrParagonu;
  private String nrFaktury;
  private long nip;
  private String adres;


  public long getIdDowoduSprzedazy() {
    return idDowoduSprzedazy;
  }

  public void setIdDowoduSprzedazy(long idDowoduSprzedazy) {
    this.idDowoduSprzedazy = idDowoduSprzedazy;
  }


  public String getKwota() {
    return kwota;
  }

  public void setKwota(String kwota) {
    this.kwota = kwota;
  }


  public java.sql.Date getData() {
    return data;
  }

  public void setData(java.sql.Date data) {
    this.data = data;
  }


  public long getIdKlienta() {
    return idKlienta;
  }

  public void setIdKlienta(long idKlienta) {
    this.idKlienta = idKlienta;
  }


  public long getIdSprzedawcy() {
    return idSprzedawcy;
  }

  public void setIdSprzedawcy(long idSprzedawcy) {
    this.idSprzedawcy = idSprzedawcy;
  }


  public String getNrParagonu() {
    return nrParagonu;
  }

  public void setNrParagonu(String nrParagonu) {
    this.nrParagonu = nrParagonu;
  }


  public String  getNrFaktury() {
    return nrFaktury;
  }

  public void setNrFaktury(String nrFaktury) {
    this.nrFaktury = nrFaktury;
  }


  public long getNip() {
    return nip;
  }

  public void setNip(long nip) {
    this.nip = nip;
  }


  public String getAdres() {
    return adres;
  }

  public void setAdres(String adres) {
    this.adres = adres;
  }

}
