package com.sample;


public class Sprzedawcy {

  private long idSprzedawcy;
  private String imie;
  private String nazwisko;
  private long nrTelefonu;
  private long idSklepu;
  private String widoczny;
  private String adres_sklepu;


  public long getIdSprzedawcy() {
    return idSprzedawcy;
  }

  public void setIdSprzedawcy(long idSprzedawcy) {
    this.idSprzedawcy = idSprzedawcy;
  }


  public String getImie() {
    return imie;
  }

  public void setImie(String imie) {
    this.imie = imie;
  }


  public String getNazwisko() {
    return nazwisko;
  }

  public void setNazwisko(String nazwisko) {
    this.nazwisko = nazwisko;
  }


  public long getNrTelefonu() {
    return nrTelefonu;
  }

  public void setNrTelefonu(long nrTelefonu) {
    this.nrTelefonu = nrTelefonu;
  }


  public long getIdSklepu() {
    return idSklepu;
  }

  public void setIdSklepu(long idSklepu) {
    this.idSklepu = idSklepu;
  }


  public String getWidoczny() {
    return widoczny;
  }

  public void setWidoczny(String widoczny) {
    this.widoczny = widoczny;
  }

  public String getAdres_sklepu() {
    return adres_sklepu;
  }

  public void setAdres_sklepu(String adres_sklepu) {
    this.adres_sklepu = adres_sklepu;
  }
}
