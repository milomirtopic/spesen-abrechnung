package ch.arbonia.data;

public enum Kostenobjekt {
   TEAM_BACKOFFICE("Team Backoffice"),
   TEAM_SALES("Team Sales");

   private String name;
   private Kostenobjekt(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return name;
   }
}
