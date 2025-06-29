package com.capgemini.hospital_management_system.controller;

import com.capgemini.hospital_management_system.dto.NurseDto;
import com.capgemini.hospital_management_system.mapper.NurseCustomMapper;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.repository.NurseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NurseController.class)
class NurseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NurseRepository nurseRepository;

    @MockitoBean
    private NurseCustomMapper nurseCustomMapper;

    @Autowired
    private ObjectMapper objectMapper;

    //adding nurse details
    @Test
    void testAddNurseDetails() throws Exception {
        NurseDto dto = new NurseDto();
        dto.setEmployeeId(1);
        dto.setName("Jane Doe");
        dto.setPosition("Senior Nurse");
        dto.setRegistered(true);
        dto.setSsn(123456);

        Nurse nurseEntity = new Nurse();
        nurseEntity.setEmployeeId(1);
        nurseEntity.setName("Jane Doe");
        nurseEntity.setPosition("Senior Nurse");
        nurseEntity.setRegistered(true);
        nurseEntity.setSsn(123456);

        when(nurseCustomMapper.toEntity(any(NurseDto.class))).thenReturn(nurseEntity);
        when(nurseRepository.save(any(Nurse.class))).thenReturn(nurseEntity);
        when(nurseCustomMapper.toDto(any(Nurse.class))).thenReturn(dto);

        mockMvc.perform(post("/api/nurse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Nurse saved successfully"))
                .andExpect(jsonPath("$.data.name").value("Jane Doe"));
    }

    // accessing all nurse details
    @Test
    void testGetNurseDetails() throws Exception {
        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);
        nurse.setName("John Smith");

        NurseDto nurseDto = new NurseDto();
        nurseDto.setEmployeeId(1);
        nurseDto.setName("John Smith");

        List<Nurse> nurses = List.of(nurse);
        List<NurseDto> nurseDtos = List.of(nurseDto);

        when(nurseRepository.findAll()).thenReturn(nurses);
        when(nurseCustomMapper.toDtoList(nurses)).thenReturn(nurseDtos);

        mockMvc.perform(get("/api/nurse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Nurses retrieved successfully"))
                .andExpect(jsonPath("$.data[0].name").value("John Smith"));
    }

    //accessing nurse details by id
    @Test
    void testGetNurseDetailsById() throws Exception {
        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);
        nurse.setName("John Smith");

        NurseDto nurseDto = new NurseDto();
        nurseDto.setEmployeeId(1);
        nurseDto.setName("John Smith");

        when(nurseRepository.findById(1)).thenReturn(Optional.of(nurse));
        when(nurseCustomMapper.toDto(nurse)).thenReturn(nurseDto);

        mockMvc.perform(get("/api/nurse/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Nurse with employee ID 1 retrieved successfully"))
                .andExpect(jsonPath("$.data.name").value("John Smith"));
    }
    
    //testing for accessing position by id
    @Test
    void testGetPositionById() throws Exception {
        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);
        nurse.setName("John Smith");
        nurse.setPosition("Head Nurse");

        // Mocking the repository
        when(nurseRepository.findById(1)).thenReturn(Optional.of(nurse));

        mockMvc.perform(get("/api/nurse/position/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Position retrieved successfully for employee ID 1"))
                .andExpect(jsonPath("$.data").value("Head Nurse"));
    }
    
    //testing for accessing registered status for nurse
    
    @Test
    public void testGetRegisteredStatus() throws Exception{
    	Nurse nurse = new Nurse();
    	nurse.setEmployeeId(1);
    	nurse.setRegistered(true);
    	
    	when(nurseRepository.findById(1)).thenReturn(Optional.of(nurse));
    	
    	mockMvc.perform(get("/api/nurse/registered/1"))
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$.status").value(200))
    	.andExpect(jsonPath("$.message").value("Registered status retrieved successfully for employee ID 1"))
    	.andExpect(jsonPath("$.data").value("true"));
    }
    
    
    //testing for updating registered status for nurse
    @Test
    public void testUpdateRegisteredStatus() throws Exception {
        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);
        nurse.setRegistered(false);  

        when(nurseRepository.findById(1)).thenReturn(Optional.of(nurse));

        boolean newStatus = true;

        nurse.setRegistered(newStatus);
        when(nurseRepository.save(any(Nurse.class))).thenReturn(nurse);

        mockMvc.perform(put("/api/nurse/registered/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("true"))  // raw boolean, not JSON object
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value("Registered status updated successfully for employee ID 1"))
            .andExpect(jsonPath("$.data").value(true));
    }
    
    //testing for updating ssn 
    @Test
    void testUpdateNurseSSN() throws Exception {
        // Arrange
        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);
        nurse.setSsn(123456);

        NurseDto updatedDto = new NurseDto();
        updatedDto.setEmployeeId(1);
        updatedDto.setSsn(999999);

        when(nurseRepository.findById(1)).thenReturn(Optional.of(nurse));
        when(nurseRepository.save(any(Nurse.class))).thenReturn(nurse);
        when(nurseCustomMapper.toDto(any(Nurse.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/api/nurse/ssn/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("999999")) // sending new SSN in the request body
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value("SSN updated successfully for employee ID 1"))
            .andExpect(jsonPath("$.data.employeeId").value(1))
            .andExpect(jsonPath("$.data.ssn").value(999999));
    }


}
