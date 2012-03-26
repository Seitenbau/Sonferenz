package de.bitnoise.sonferenz.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import de.bitnoise.sonferenz.model.AuthMapping;

public interface AuthmappingRepository extends JpaRepository<AuthMapping, Integer>
{
  AuthMapping findByAuthIdAndAuthType(String authId, String authType);

  AuthMapping findByAuthId(String authId);
}
