package ch.arbonia.data;


import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
   // The initial value is to account for data.sql demo data ids
   @SequenceGenerator(name = "idgenerator", initialValue = 1000)
   private Long id;

   @Version
   private int version;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public int getVersion() {
      return version;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      AbstractEntity that = (AbstractEntity) o;

      return Objects.equals(id, that.id);
   }

   @Override
   public int hashCode() {
      return id != null ? id.hashCode() : 0;
   }
}
