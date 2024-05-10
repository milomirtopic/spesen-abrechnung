package ch.arbonia.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.arbonia.data.Abrechnung;
import ch.arbonia.data.AbrechnungRepository;
import ch.arbonia.data.User;

@Service
public class AbrechnungService {

   private final AbrechnungRepository repository;

   public AbrechnungService(AbrechnungRepository repository) {
      this.repository = repository;
   }

   public List<Abrechnung> findByUser(User user) {
      return repository.findByUser(user);
   }

   public Abrechnung save(Abrechnung abrechung) {
      return repository.save(abrechung);
   }

}
