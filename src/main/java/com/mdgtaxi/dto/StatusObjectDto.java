package com.mdgtaxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusObjectDto {
    private Long id;
    private String libelle;
    private Integer score;
    private String spanHtml;

}
