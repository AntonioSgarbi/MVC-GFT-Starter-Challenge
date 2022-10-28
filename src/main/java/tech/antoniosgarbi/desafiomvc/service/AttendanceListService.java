package tech.antoniosgarbi.desafiomvc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import tech.antoniosgarbi.desafiomvc.model.AttendanceList;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.repository.AttendanceRepository;

@Service
public class AttendanceListService {
    private final AttendanceRepository AttendanceRepository;

    public AttendanceListService(AttendanceRepository attendanceRepository) {
        AttendanceRepository = attendanceRepository;
    }

    public AttendanceList createNewList(Date date) {
        return this.AttendanceRepository.save(new AttendanceList(null, date, new ArrayList<>(), new ArrayList<>()));
    }

    public AttendanceList findById(Long id) {
        return this.AttendanceRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }
    
}
