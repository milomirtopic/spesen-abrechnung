package ch.arbonia.data;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AbrechnungRepository extends JpaRepository<Abrechnung, Long>, JpaSpecificationExecutor<Abrechnung> {

   List<Abrechnung> findByUser(User user);

}
