package ch.arbonia.views.spesenabrechnung;

public enum Filter {
   LETZTE_90("Letzte 90 Tage"),
   DIESES_JAHR("Dieses Jahr"),
   LETZTES_JAHR("Letztes Jahr"),
   ;

   private String name;
   private Filter(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return name;
   }
}
