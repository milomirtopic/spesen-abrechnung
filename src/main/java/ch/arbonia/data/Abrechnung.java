package ch.arbonia.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "abrechnung")
public class Abrechnung extends AbstractEntity {

   @ManyToOne
   @JoinColumn(name="user_id", nullable=false)
   private User user;

   private String abrechnungsname;

   private String gesellschaft;

   private String kostenobjektart;

   private String kostenobjekt;

   private String kommentar;

   private boolean uebermittelt;

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   @OneToMany(mappedBy="abrechnung", cascade = CascadeType.ALL)
   private Set<Ausgabe> ausgaben = new HashSet<>();

   public String getAbrechnungsname() {
      return abrechnungsname;
   }

   public void setAbrechnungsname(String abrechnungsname) {
      this.abrechnungsname = abrechnungsname;
   }

   public String getGesellschaft() {
      return gesellschaft;
   }

   public void setGesellschaft(String gesellschaft) {
      this.gesellschaft = gesellschaft;
   }

   public String getKostenobjektart() {
      return kostenobjektart;
   }

   public void setKostenobjektart(String kostenobjektart) {
      this.kostenobjektart = kostenobjektart;
   }

   public String getKostenobjekt() {
      return kostenobjekt;
   }

   public void setKostenobjekt(String kostenobjekt) {
      this.kostenobjekt = kostenobjekt;
   }

   public String getKommentar() {
      return kommentar;
   }

   public void setKommentar(String kommentar) {
      this.kommentar = kommentar;
   }

   public Set<Ausgabe> getAusgaben() {
      return ausgaben;
   }

   public void setAusgaben(Set<Ausgabe> ausgaben) {
      this.ausgaben = ausgaben;
   }

   public boolean isUebermittelt() {
      return uebermittelt;
   }

   public void setUebermittelt(boolean uebermittelt) {
      this.uebermittelt = uebermittelt;
   }
}
