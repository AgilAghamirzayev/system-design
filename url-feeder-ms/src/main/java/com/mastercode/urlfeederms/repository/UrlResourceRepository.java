package com.mastercode.urlfeederms.repository;

import com.mastercode.urlfeederms.entity.UrlResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlResourceRepository extends JpaRepository<UrlResource, Long> {
    Optional<UrlResource> findByUrl(String url);
}
