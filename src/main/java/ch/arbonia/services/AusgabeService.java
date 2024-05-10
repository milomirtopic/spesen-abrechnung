package ch.arbonia.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.arbonia.data.Abrechnung;
import ch.arbonia.data.Ausgabe;
import ch.arbonia.data.AusgabeRepository;

@Service
public class AusgabeService {

   private final AusgabeRepository repository;

   public AusgabeService(AusgabeRepository repository) {
      this.repository = repository;
   }

   public List<Ausgabe> findByAbrechnung(Abrechnung abrechnung) {
      return repository.findByAbrechnung(abrechnung);
   }

   public Ausgabe save(Ausgabe ausgabe) {
      return repository.save(ausgabe);
   }
}
