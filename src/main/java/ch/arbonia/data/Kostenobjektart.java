package ch.arbonia.data;

public enum Kostenobjektart {
   PERSONALKOSTEN("Personalkosten"),
   MATERIALKOSTEN("Materialkosten");

   private String name;

   private Kostenobjektart(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return name;
   }
}
