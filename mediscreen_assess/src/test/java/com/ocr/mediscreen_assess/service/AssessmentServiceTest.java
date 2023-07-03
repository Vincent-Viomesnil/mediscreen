package com.ocr.mediscreen_assess.service;

import com.ocr.mediscreen_assess.model.PatientBean;
import com.ocr.mediscreen_assess.model.PatientHistoryBean;
import com.ocr.mediscreen_assess.model.TriggerWords;
import com.ocr.mediscreen_assess.proxies.MicroserviceNotesProxy;
import com.ocr.mediscreen_assess.proxies.MicroservicePatientProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AssessmentServiceTest {

    @MockBean
    private MicroserviceNotesProxy microserviceNotesProxy;

    @MockBean
    private MicroservicePatientProxy microservicePatientProxy;


    private TriggerWords triggerWords = new TriggerWords();
    @InjectMocks
    private AssessmentService assessmentService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAssessmentById_None() {
        // Arrange
        Long patId = 1L;
        PatientBean patient = new PatientBean();
        patient.setFirstname("First");
        patient.setLastname("Last");
        patient.setBirthdate(LocalDate.of(1980, 1, 1));
        patient.setGender("M");


        PatientHistoryBean history1 = new PatientHistoryBean();
        history1.setNotes("None");


        when(microservicePatientProxy.getPatientById(patId)).thenReturn(patient);

        // Act
        String result = assessmentService.getAssessmentById(patId);

        // Assert
        assertEquals("First Last (age 43) diabetes assessment is: None", result);
        verify(microservicePatientProxy, times(1)).getPatientById(patId);
        verify(microserviceNotesProxy, times(1)).getListNotesByPatId(patId);
    }


    @Test
    public void testGetAssessmentById_Borderline() {
        // Arrange
        Long patId = 1L;
        PatientBean patient = new PatientBean();
        patient.setFirstname("First");
        patient.setLastname("Last");
        patient.setBirthdate(LocalDate.of(1980, 1, 1));
        patient.setGender("M");

        List<PatientHistoryBean> patientHistoryList = new ArrayList<>();
        PatientHistoryBean history1 = new PatientHistoryBean();
        history1.setNotes("Smoker");
        history1.setNotes("Height");

        when(microservicePatientProxy.getPatientById(patId)).thenReturn(patient);
        when(microserviceNotesProxy.getListNotesByPatId(patId)).thenReturn(patientHistoryList);

        // Act
        String result = assessmentService.getAssessmentById(patId);

        // Assert
        assertEquals("First Last (age 43) diabetes assessment is: None", result);
        verify(microservicePatientProxy, times(1)).getPatientById(patId);
        verify(microserviceNotesProxy, times(1)).getListNotesByPatId(patId);
    }


    @Test
    public void testGetAssessmentById_Danger() {
        // Arrange
        Long patId = 1L;
        PatientBean patient = new PatientBean();
        patient.setFirstname("John");
        patient.setLastname("Doe");
        patient.setBirthdate(LocalDate.of(2000, 1, 1));
        patient.setGender("M");

        List<PatientHistoryBean> patientHistoryList = new ArrayList<>() {{
            add(new PatientHistoryBean() {{
                setNotes("Smoker");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Abnormal");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Reaction");
            }});
        }};


        when(microservicePatientProxy.getPatientById(patId)).thenReturn(patient);
        when(microserviceNotesProxy.getListNotesByPatId(patId)).thenReturn(patientHistoryList);

        // Act
        String result = assessmentService.getAssessmentById(patId);

        // Assert
        assertEquals("John Doe (age 23) diabetes assessment is: In Danger", result);
        verify(microservicePatientProxy, times(1)).getPatientById(patId);
        verify(microserviceNotesProxy, times(1)).getListNotesByPatId(patId);
    }

    @Test
    public void testGetAssessmentById_EarlyOnSet_FirstCase() {
        // Arrange
        Long patId = 1L;
        PatientBean patient = new PatientBean();
        patient.setFirstname("John");
        patient.setLastname("Doe");
        patient.setBirthdate(LocalDate.of(2000, 1, 1));
        patient.setGender("M");

        List<PatientHistoryBean> patientHistoryList = new ArrayList<>() {{
            add(new PatientHistoryBean() {{
                setNotes("Smoker");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Abnormal");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Reaction");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Antibodies");
            }});

            add(new PatientHistoryBean() {{
                setNotes("Weight");
            }});

        }};


        when(microservicePatientProxy.getPatientById(patId)).thenReturn(patient);
        when(microserviceNotesProxy.getListNotesByPatId(patId)).thenReturn(patientHistoryList);

        // Act
        String result = assessmentService.getAssessmentById(patId);

        // Assert
        assertEquals("John Doe (age 23) diabetes assessment is: Early onset", result);
        verify(microservicePatientProxy, times(1)).getPatientById(patId);
        verify(microserviceNotesProxy, times(1)).getListNotesByPatId(patId);
    }

    @Test
    public void testGetAssessmentById_EarlyOnSet_SecondCase() {
        // Arrange
        Long patId = 1L;
        PatientBean patient = new PatientBean();
        patient.setFirstname("Lea");
        patient.setLastname("James");
        patient.setBirthdate(LocalDate.of(2000, 1, 1));
        patient.setGender("F");

        List<PatientHistoryBean> patientHistoryList = new ArrayList<>() {{
            add(new PatientHistoryBean() {{
                setNotes("Smoker");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Abnormal");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Reaction");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Antibodies");
            }});

            add(new PatientHistoryBean() {{
                setNotes("Weight");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Height");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Dizziness");
            }});

        }};


        when(microservicePatientProxy.getPatientById(patId)).thenReturn(patient);
        when(microserviceNotesProxy.getListNotesByPatId(patId)).thenReturn(patientHistoryList);

        // Act
        String result = assessmentService.getAssessmentById(patId);

        // Assert
        assertEquals("Lea James (age 23) diabetes assessment is: Early onset", result);
        verify(microservicePatientProxy, times(1)).getPatientById(patId);
        verify(microserviceNotesProxy, times(1)).getListNotesByPatId(patId);
    }

    @Test
    public void testGetAssessmentById_EarlyOnSet_ThirdCase() {
        // Arrange
        Long patId = 1L;
        PatientBean patient = new PatientBean();
        patient.setFirstname("Lea");
        patient.setLastname("James");
        patient.setBirthdate(LocalDate.of(2000, 1, 1));
        patient.setGender("F");

        List<PatientHistoryBean> patientHistoryList = new ArrayList<>() {{
            add(new PatientHistoryBean() {{
                setNotes("Smoker");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Abnormal");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Reaction");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Antibodies");
            }});

            add(new PatientHistoryBean() {{
                setNotes("Weight");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Height");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Dizziness");
            }});
            add(new PatientHistoryBean() {{
                setNotes("Microalbumin");
            }});
        }};


        when(microservicePatientProxy.getPatientById(patId)).thenReturn(patient);
        when(microserviceNotesProxy.getListNotesByPatId(patId)).thenReturn(patientHistoryList);

        // Act
        String result = assessmentService.getAssessmentById(patId);

        // Assert
        assertEquals("Lea James (age 23) diabetes assessment is: Early onset", result);
        verify(microservicePatientProxy, times(1)).getPatientById(patId);
        verify(microserviceNotesProxy, times(1)).getListNotesByPatId(patId);
    }
}

