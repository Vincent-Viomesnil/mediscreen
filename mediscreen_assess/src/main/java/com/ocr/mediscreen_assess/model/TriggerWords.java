package com.ocr.mediscreen_assess.model;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TriggerWords {
    private List<String> triggerList = List.of("Hémoglobine A1C", "Hemoglobin A1C",
            "Microalbumine", "Microalbumin", "Taille", "Height", "Poids", "Weight", "Smoker", "Fumeur", "Fume",
            "Abnormal", "Anormale", "Anormaux", "Cholesterol", "Cholestérol", "Vertige", "Dizziness",
            "Rechute", "Reaction", "Réaction", "Anticorps", "Antibodies");

    //Adding word in french in order to adapt the behaviour
    public List<String> getTriggerList() {
        return triggerList;
    }
}
