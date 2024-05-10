package ch.arbonia.views.spesenabrechnung;

import java.util.List;
import java.util.stream.Collectors;

import ch.arbonia.data.Abrechnung;
import ch.arbonia.data.Ausgabe;

public class AbrechnungDto {

   private Abrechnung abrechnung;

   public AbrechnungDto(Abrechnung abrechnung, List<Ausgabe> ausgaben) {
      this.abrechnung = abrechnung;
      this.name = abrechnung.getAbrechnungsname();
      Double amount = ausgaben.stream().map(Ausgabe::getBetrag).collect(Collectors.summarizingDouble(Double::doubleValue)).getSum();
      this.betrag = "CHF " + amount;
      this.status = abrechnung.isUebermittelt() ? "übermittelt" : "nicht übermittelt";
   }

   private String name;
   private String betrag;

   private String status;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getBetrag() {
      return betrag;
   }

   public void setBetrag(String betrag) {
      this.betrag = betrag;
   }

   public Abrechnung getAbrechnung() {
      return abrechnung;
   }

   public void setAbrechnung(Abrechnung abrechnung) {
      this.abrechnung = abrechnung;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }
}
