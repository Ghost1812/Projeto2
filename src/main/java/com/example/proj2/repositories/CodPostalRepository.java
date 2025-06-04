package com.example.proj2.repositories;

import com.example.proj2.models.Codpostal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodPostalRepository extends JpaRepository<Codpostal, String> {
}
