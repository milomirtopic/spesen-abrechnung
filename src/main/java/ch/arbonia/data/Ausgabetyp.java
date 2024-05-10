package ch.arbonia.data;

public enum Ausgabetyp {

   ZUG("Zug"),
   MITTAGESSEN("Mittagessen"),
   GESCHENKE("Geschenke, Mitarbeiter"),
   HOTEL("Hotel"),
   PARKING("Parkgebühren");

   private String name;
   private Ausgabetyp(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return name;
   }
}
