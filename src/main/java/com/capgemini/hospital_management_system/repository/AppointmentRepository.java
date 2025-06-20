package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByStart(LocalDateTime startdate);

    @Query("SELECT a.patient FROM Appointment a WHERE a.appointmentId = :appointmentid")
    Optional<Patient> findPatientByAppointmentId(@Param("appointmentid") Integer appointmentid);

    @Query("SELECT a.physician FROM Appointment a WHERE a.appointmentId = :appointmentid")
    Optional<Physician> findPhysicianByAppointmentId(@Param("appointmentid") Integer appointmentid);

    @Query("SELECT a.prepNurse FROM Appointment a WHERE a.appointmentId = :appointmentid")
    Optional<Nurse> fetchNurseByAppointmentId(@Param("appointmentid") Integer appointmentid);

    @Query("SELECT a.examinationRoom FROM Appointment a WHERE a.appointmentId = :id")
    String findExaminationRoomByAppointmentId(@Param("id") Integer appointmentId);

    List<Appointment> findByPatient_Ssn(Integer ssn);

    Optional<Appointment> findByStartAndPatient_Ssn(LocalDateTime start, Integer ssn);

    List<Appointment> findByPhysician_employeeId(Integer employeeId);
    List<Appointment> findByPhysician_employeeIdAndStart(Integer physicianEmployeeId, LocalDateTime start);
    List<Appointment> findByPrepNurse_employeeIdAndStart(Integer prepNurseEmployeeId, LocalDateTime start);
    List<Appointment> findByPrepNurse_employeeId(Integer employeeId);

    @Query("SELECT a.examinationRoom FROM Appointment a WHERE a.patient.ssn = :patientId AND a.start = :date")
    Optional<String> findByPatientIdAndStartDate(@Param("patientId") Integer patientId, @Param("date") LocalDateTime date);

    @Query("SELECT a.examinationRoom FROM Appointment a WHERE a.physician.employeeId = :physicianId AND a.start = :date")
    List<String> findByPhysicianIdAndStartDate(@Param("physicianId") Integer physicianId, @Param("date") LocalDateTime date);

    @Query("SELECT a.examinationRoom FROM Appointment a WHERE a.prepNurse.employeeId = :nurseId AND a.start = :date")
    List<String> findByNurseIdAndStartDate(@Param("nurseId") Integer nurseId, @Param("date") LocalDateTime date);

    @Query("SELECT a.patient FROM Appointment a WHERE a.physician.employeeId = :employeeId AND a.patient.ssn = :patientId")
    Optional<Patient> findByPhysicianIdAndPatientId(@Param("employeeId") Integer employeeId, @Param("patientId") Integer patientId);

    @Query("SELECT a.patient FROM Appointment a WHERE a.prepNurse.employeeId = :employeeId AND a.patient.ssn = :patientId")
    Optional<Patient> findByNurseIdAndPatientId(@Param("nurseId") Integer nurseId, @Param("patientId") Integer patientId);
}