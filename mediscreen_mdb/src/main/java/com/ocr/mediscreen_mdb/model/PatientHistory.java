package com.ocr.mediscreen_mdb.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Notes")
public class PatientHistory {

    @Id
    private ObjectId _id;
    private Long noteId;
    private Long patId;
    private String lastname;
    private String notes;

}
