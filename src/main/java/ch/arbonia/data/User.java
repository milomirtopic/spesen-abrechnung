package ch.arbonia.data;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "application_user")
public class User extends AbstractEntity {

   private String username;
   private String name;
   @JsonIgnore
   private String hashedPassword;
   @Enumerated(EnumType.STRING)
   @ElementCollection(fetch = FetchType.EAGER)
   private Set<Role> roles;

   @OneToMany(mappedBy="user")
   private Set<Abrechnung> abrechnungen;

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      if (username != null && !username.isBlank()) {
         username = username.toLowerCase().trim();
      }
      this.username = username;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getHashedPassword() {
      return hashedPassword;
   }

   public void setHashedPassword(String hashedPassword) {
      this.hashedPassword = hashedPassword;
   }

   public void setRawPassword(String password) {
      this.hashedPassword = new BCryptPasswordEncoder().encode(password);
   }

   public Set<Role> getRoles() {
      return roles;
   }

   public void setRoles(Set<Role> roles) {
      this.roles = roles;
   }

   public Set<Abrechnung> getAbrechnungen() {
      return abrechnungen;
   }

   public void setAbrechnungen(Set<Abrechnung> abrechnungen) {
      this.abrechnungen = abrechnungen;
   }

}
