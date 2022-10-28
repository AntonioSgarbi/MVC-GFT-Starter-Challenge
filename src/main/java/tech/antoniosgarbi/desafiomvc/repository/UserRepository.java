package tech.antoniosgarbi.desafiomvc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafiomvc.model.UserModel;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    Page<UserModel> findAll(Pageable pageable);

    Optional<UserModel> findByUsername(String username);
}