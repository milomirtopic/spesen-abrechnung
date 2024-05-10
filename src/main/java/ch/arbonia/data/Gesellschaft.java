package ch.arbonia.data;

public enum Gesellschaft {
   ARBONIA_CLIMATE("(8985) Arbonia Climate"),
   ARBONIA_DOORS("(8982) Arbonia Doors");

   private String name;

   private Gesellschaft(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return name;
   }
}
