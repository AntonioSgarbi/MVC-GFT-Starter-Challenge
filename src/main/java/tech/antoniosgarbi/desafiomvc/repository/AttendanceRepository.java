package tech.antoniosgarbi.desafiomvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.antoniosgarbi.desafiomvc.model.AttendanceList;

public interface AttendanceRepository extends JpaRepository<AttendanceList, Long> {

}
