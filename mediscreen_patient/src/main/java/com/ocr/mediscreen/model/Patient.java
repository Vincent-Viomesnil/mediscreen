package com.ocr.mediscreen.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(max = 50, message = "must be limited to 50")
    private String firstname;
    @NotNull
    @Size(max = 50, message = "must be limited to 50")
    private String lastname;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    @NotNull
    @Pattern(regexp = "[MF]", message = "Gender must be either 'M' or 'F'")
    private String gender;
    @Size(max = 255, message = "must be limited to 255")
    private String address;
    @Size(max = 20, message = "must be limited to 20")
    private String phonenumber;

}

