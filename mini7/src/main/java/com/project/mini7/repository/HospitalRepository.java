package com.project.mini7.repository;

import com.project.mini7.entity.HospitalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<HospitalInfo, Long> {
}
