package com.sample;


public class Produkty {

  private long idProduktu;
  private String cena;
  private int iloscWSklepie;
  private String nazwa;
  private String kategorieNazwa;
  private long nip;
  private String wOfercie;
  private String nazwaFirmy;


  public long getIdProduktu() {
    return idProduktu;
  }

  public void setIdProduktu(long idProduktu) {
    this.idProduktu = idProduktu;
  }


  public String getCena() {
    return cena;
  }

  public void setCena(String cena) {
    this.cena = cena;
  }


  public String getNazwa() {
    return nazwa;
  }

  public void setNazwa(String nazwa) {
    this.nazwa = nazwa;
  }


  public String getKategorieNazwa() {
    return kategorieNazwa;
  }

  public void setKategorieNazwa(String kategorieNazwa) {
    this.kategorieNazwa = kategorieNazwa;
  }


  public long getNip() {
    return nip;
  }

  public void setNip(long nip) {
    this.nip = nip;
  }


  public String getWOfercie() {
    return wOfercie;
  }

  public void setWOfercie(String wOfercie) {
    this.wOfercie = wOfercie;
  }

  public String getNazwaFirmy() {
    return nazwaFirmy;
  }

  public void setNazwaFirmy(String nazwaFirmy) {
    this.nazwaFirmy = nazwaFirmy;
  }

    public int getIloscWSklepie() {
        return iloscWSklepie;
    }

    public void setIloscWSklepie(int iloscWSklepie) {
        this.iloscWSklepie = iloscWSklepie;
    }
}
