package com.example.proj2.repositories;

import com.example.proj2.models.AgenteFeedback;
import com.example.proj2.models.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
@Repository
public interface AgenteFeedbackRepository extends JpaRepository<AgenteFeedback, Integer> {
}
