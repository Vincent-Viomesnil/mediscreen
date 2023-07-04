package com.ocr.mediscreen_ui.controller;

import com.ocr.mediscreen_ui.exceptions.PatientNotFoundException;
import com.ocr.mediscreen_ui.model.PatientBean;
import com.ocr.mediscreen_ui.model.PatientHistoryBean;
import com.ocr.mediscreen_ui.proxies.AssessmentProxy;
import com.ocr.mediscreen_ui.proxies.MicroserviceNotesProxy;
import com.ocr.mediscreen_ui.proxies.MicroservicePatientProxy;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig
@WebMvcTest(FrontController.class)
public class FrontControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MicroserviceNotesProxy microserviceNotesProxy;

    @MockBean
    private AssessmentProxy assessmentProxy;

    @MockBean
    private MicroservicePatientProxy microservicePatientProxy;




    @Test
    public void testHome() throws Exception {
        mockMvc.perform(get("/PatientList"))
                .andExpect(status().isOk())
                .andExpect(view().name("Home"))
                .andExpect(model().attributeExists("uniquePatientList"));
    }



    @Test
    public void testGetPatientById_PatientNotFoundException() throws Exception {
        long patientId = 1;

        given(microservicePatientProxy.getPatientById(patientId)).willThrow(PatientNotFoundException.class);

        mockMvc.perform(get("/Patient/id/{id}", patientId))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void testGetPatientHistory() throws Exception {
        List<PatientBean> patients = new ArrayList<>();
        given(microservicePatientProxy.patientList()).willReturn(patients);

        mockMvc.perform(get("/PatHistory/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("AddNote"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", patients))
                .andExpect(model().attributeExists("patientHistory"));
    }

    @Test
    public void testGetPatient() throws Exception {
        mockMvc.perform(get("/Patient/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("AddPatient"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attribute("patient", instanceOf(PatientBean.class)));
    }

    @Test
    public void testAddPatient() throws Exception {
        PatientBean patient = new PatientBean();
        patient.setFirstname("John");
        patient.setLastname("Doe");
        patient.setBirthdate(LocalDate.of(1990, 1, 1));

        given(microservicePatientProxy.getPatientById(anyLong())).willReturn(patient);

        mockMvc.perform(post("/Patient/validate")
                        .flashAttr("patient", patient)
                        .sessionAttr("patientExisting", patient))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/PatientList"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    public void testAddPatient_PatientAlreadyExists() throws Exception {
        PatientBean patient = new PatientBean();
        patient.setFirstname("John");
        patient.setLastname("Doe");
        patient.setBirthdate(LocalDate.of(1990, 1, 1));

        PatientBean patientExisting = new PatientBean();
        patientExisting.setFirstname("John");
        patientExisting.setLastname("Doe");
        patientExisting.setBirthdate(LocalDate.of(1990, 1, 1));

        given(microservicePatientProxy.getPatientById(anyLong())).willReturn(patientExisting);

        mockMvc.perform(post("/Patient/validate")
                        .flashAttr("patient", patient)
                        .sessionAttr("patientExisting", patientExisting))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/PatientList"));
    }

    @Test
    public void testAddPatientHistory() throws Exception {
        PatientHistoryBean patientHistory = new PatientHistoryBean();

        given(microserviceNotesProxy.addNote(patientHistory)).willReturn(patientHistory);

        mockMvc.perform(post("/PatHistory/validate")
                        .flashAttr("patientHistory", patientHistory))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patientdetails?patId=null"));
    }

    @Test
    public void testUpdatePatient() throws Exception {
        long patientId = 1;
        PatientBean patientToUpdate = new PatientBean();
        PatientBean patientUpdated = new PatientBean();
        List<PatientBean> uniquePatientList = new ArrayList<>();

        given(microservicePatientProxy.updatePatientById(patientId, patientToUpdate)).willReturn(patientUpdated);
        given(microservicePatientProxy.patientList()).willReturn(uniquePatientList);

        mockMvc.perform(post("/Patient/update/{id}", patientId)
                        .flashAttr("patientToUpdate", patientToUpdate))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patientdetails?patId=1"));
    }

    @Test
    public void testUpdatePatient_BadRequest() throws Exception {
        long patientId = 1;
        PatientBean patientToUpdate = new PatientBean();

        given(microservicePatientProxy.updatePatientById(patientId, patientToUpdate)).willThrow(FeignException.BadRequest.class);

        mockMvc.perform(post("/Patient/update/{id}", patientId)
                        .flashAttr("patientToUpdate", patientToUpdate))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patientdetails?patId=1"));
    }

    @Test
    public void testUpdateForm() throws Exception {
        Long id = 1L;
        PatientHistoryBean patientHistory = new PatientHistoryBean();
        patientHistory.setPatId(id);

        List<PatientHistoryBean> patientHistoryList = new ArrayList<>();
        patientHistoryList.add(patientHistory);

        when(microserviceNotesProxy.getNoteById(id)).thenReturn(patientHistory);
        when(microserviceNotesProxy.getListNotesByPatId(id)).thenReturn(patientHistoryList);

        mockMvc.perform(MockMvcRequestBuilders.get("/PatHistory/update/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("patientHistoryList", patientHistoryList))
                .andExpect(MockMvcResultMatchers.model().attribute("patientHistory", patientHistory))
                .andExpect(MockMvcResultMatchers.view().name("UpdateNote"));

        verify(microserviceNotesProxy).getNoteById(id);
        verify(microserviceNotesProxy).getListNotesByPatId(id);
    }

    @Test
    public void testUpdatePatientForm() throws Exception {
        Long id = 1L;
        PatientBean patient = new PatientBean();

        List<PatientBean> uniquePatientList = new ArrayList<>();

        when(microservicePatientProxy.getPatientById(id)).thenReturn(patient);
        when(microservicePatientProxy.patientList()).thenReturn(uniquePatientList);

        mockMvc.perform(MockMvcRequestBuilders.get("/Patient/update/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("uniquePatientList", uniquePatientList))
                .andExpect(MockMvcResultMatchers.model().attribute("patient", patient))
                .andExpect(MockMvcResultMatchers.view().name("Update"));

        verify(microservicePatientProxy).getPatientById(id);
        verify(microservicePatientProxy).patientList();
    }

    @Test
    public void testGetPatientDetails() throws Exception {
        Long patId = 1L;
        List<PatientHistoryBean> patientHistoryList = new ArrayList<>();
        String assessmentResult = "Assessment Result";
        String assessmentResultWithoutLastElement = "Assessment Result Without Last Element";
        PatientBean patient = new PatientBean();

        when(microserviceNotesProxy.getListNotesByPatId(patId)).thenReturn(patientHistoryList);
        when(assessmentProxy.getAssessmentById(patId)).thenReturn(assessmentResult);
        when(microservicePatientProxy.getPatientById(patId)).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.get("/patientdetails")
                        .param("patId", patId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("patientHistoryList", patientHistoryList))
                .andExpect(MockMvcResultMatchers.model().attribute("assessmentResult", assessmentResult))
                .andExpect(MockMvcResultMatchers.model().attribute("patient", patient))
                .andExpect(MockMvcResultMatchers.view().name("PatientDetails"));

        verify(microserviceNotesProxy).getListNotesByPatId(patId);
        verify(assessmentProxy).getAssessmentById(patId);
        verify(microservicePatientProxy).getPatientById(patId);
    }


    @Test
    public void testDeletePatient() throws Exception {
        long patientId = 1;
        List<PatientBean> uniquePatientList = new ArrayList<>();

        mockMvc.perform(post("/Patient/delete/{id}", patientId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/PatientList"));
    }

    @Test
    public void testDeletePatientHistory() throws Exception {
        long noteId = 1;
        List<PatientHistoryBean> uniquePatientList = new ArrayList<>();

        mockMvc.perform(post("/PatHistory/delete/{id}", noteId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/PatientList"));
    }

    @Test
    public void testPatientList() throws Exception {
        List<PatientBean> patients = new ArrayList<>();

        given(microservicePatientProxy.patientList()).willReturn(patients);

        mockMvc.perform(get("/PatientList"))
                .andExpect(status().isOk())
                .andExpect(view().name("Home"));
    }


    @Test
    public void testAddPatientFeignException() throws Exception {
        Long patId = 1L;
        PatientBean patientBean = new PatientBean();
        when(microservicePatientProxy.getPatientById(patId)).thenReturn(null);

        mockMvc.perform(post("/Patient/validate")
                        .param("firstname", "First")
                        .param("lastname", "Last")
                        .param("birthdate", "1990-01-01"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/PatientList"));
    }

    @Test
    public void testConstructorAndGetMessage() {
        String message = "Patient not found";
        PatientNotFoundException exception = new PatientNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }


}




