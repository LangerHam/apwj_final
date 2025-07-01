package com.PetCare.Service;

import com.PetCare.Entity.Appointment;
import com.PetCare.Repository.AppointmentRepository;
import com.PetCare.Entity.EmployeeSchedule;
import com.PetCare.Repository.EmployeeScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeScheduleService {
    @Autowired
    private EmployeeScheduleRepository scheduleRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    private static final long APPOINTMENT_DURATION_MINUTES = 60;

    public int setEmployeeSchedule(EmployeeSchedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<EmployeeSchedule> getEmployeeSchedule(int employeeId) {
        return scheduleRepository.findAllByEmployeeId(employeeId);
    }

    public boolean isEmployeeAvailable(int employeeId, LocalDateTime dateTime) {
        DayOfWeek day = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();

        Optional<EmployeeSchedule> scheduleOpt = scheduleRepository.findByEmployeeIdAndDay(employeeId, day);
        if (scheduleOpt.isEmpty()) {
            return false;
        }
        EmployeeSchedule schedule = scheduleOpt.get();
        if (time.isBefore(schedule.getStartTime()) || time.isAfter(schedule.getEndTime())) {
            return false;
        }

        List<Appointment> existingAppointments = appointmentRepository.findAllByEmployeeId(employeeId);
        for (Appointment existing : existingAppointments) {
            LocalDateTime existingStart = existing.getAppointmentDate();
            LocalDateTime existingEnd = existingStart.plusMinutes(APPOINTMENT_DURATION_MINUTES);

            LocalDateTime requestedEnd = dateTime.plusMinutes(APPOINTMENT_DURATION_MINUTES);
            if (dateTime.isBefore(existingEnd) && requestedEnd.isAfter(existingStart)) {
                return false;
            }
        }
        return true;
    }
}
