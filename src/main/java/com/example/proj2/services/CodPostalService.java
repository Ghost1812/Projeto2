
package com.example.proj2.services;
import com.example.proj2.models.Codpostal;
import com.example.proj2.repositories.CodPostalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CodPostalService {

    private final CodPostalRepository codPostalRepository;

    @Autowired
    public CodPostalService(CodPostalRepository codPostalRepository) {
        this.codPostalRepository = codPostalRepository;
    }

    public List<Codpostal> findAll() {
        return codPostalRepository.findAll();
    }

    public Optional<Codpostal> findById(String codpostal) {
        return codPostalRepository.findById(codpostal);
    }

    public Codpostal save(Codpostal codpostal) {
        return codPostalRepository.save(codpostal);
    }

    public void deleteById(String codpostal) {
        codPostalRepository.deleteById(codpostal);
    }
}
