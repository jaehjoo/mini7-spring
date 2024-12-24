package com.project.mini7.repository;

import com.project.mini7.entity.EmergencyInfo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ComponentScan
public interface EmergencyRepository extends JpaRepository<EmergencyInfo, Long> {
}
