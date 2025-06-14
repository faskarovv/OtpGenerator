package org.example.otpgenerator.repo;

import org.example.otpgenerator.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Card, UUID> {

    Optional<Card> existsCardByEmail(String email);
}
