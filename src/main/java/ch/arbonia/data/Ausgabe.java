package ch.arbonia.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "ausgabe")
public class Ausgabe extends AbstractEntity {

   @ManyToOne
   @JoinColumn(name="abrechnung_id", nullable=false)
   private Abrechnung abrechnung;

   private String ausgabetyp;

   private LocalDate transaktionsdatum;

   private String geschaeftszweck;

   private String zahlungsart;

   private String waehrung;

   private String ausstellungsort;

   private Double betrag;

   public Abrechnung getAbrechnung() {
      return abrechnung;
   }

   public void setAbrechnung(Abrechnung abrechnung) {
      this.abrechnung = abrechnung;
   }

   public String getAusgabetyp() {
      return ausgabetyp;
   }

   public Ausgabetyp getAusgabetypByEnum() {
      for (Ausgabetyp value : Ausgabetyp.values()) {
         if (value.toString().equalsIgnoreCase(ausgabetyp)) {
            return value;
         }
      }
      return null;
   }

   public void setAusgabetyp(String ausgabetyp) {
      this.ausgabetyp = ausgabetyp;
   }

   public void setAusgabetypByEnum(Ausgabetyp typ) {
      this.ausgabetyp = typ.toString();
   }

   public LocalDate getTransaktionsdatum() {
      return transaktionsdatum;
   }

   public String getTransaktionsdatumFormatted() {
      return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(transaktionsdatum);
   }

   public void setTransaktionsdatum(LocalDate transaktionsdatum) {
      this.transaktionsdatum = transaktionsdatum;
   }

   public String getGeschaeftszweck() {
      return geschaeftszweck;
   }

   public void setGeschaeftszweck(String geschaeftszweck) {
      this.geschaeftszweck = geschaeftszweck;
   }

   public String getZahlungsart() {
      return zahlungsart;
   }

   public void setZahlungsart(String zahlungsart) {
      this.zahlungsart = zahlungsart;
   }

   public String getWaehrung() {
      return waehrung;
   }

   public void setWaehrung(String waehrung) {
      this.waehrung = waehrung;
   }

   public String getAusstellungsort() {
      return ausstellungsort;
   }

   public void setAusstellungsort(String ausstellungsort) {
      this.ausstellungsort = ausstellungsort;
   }

   public Double getBetrag() {
      return betrag;
   }

   public void setBetrag(Double betrag) {
      this.betrag = betrag;
   }
}
