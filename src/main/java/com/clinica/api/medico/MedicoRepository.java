package com.clinica.api.medico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    @Override
    @NonNull
    Page<Medico> findAll(@NonNull Pageable pageable); // Método para paginar resultados
    Page<Medico> findAllByAtivoTrue(Pageable pageable);

}
