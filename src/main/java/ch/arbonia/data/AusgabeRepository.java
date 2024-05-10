package ch.arbonia.data;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AusgabeRepository extends JpaRepository<Ausgabe, Long>, JpaSpecificationExecutor<Ausgabe> {

   List<Ausgabe> findByAbrechnung(Abrechnung abrechnung);
}
