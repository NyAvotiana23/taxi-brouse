package com.mdgtaxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour représenter un mouvement de statut générique
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStatusDto {
    private Long idMouvement;
    private Long idEntite;
    private String entityTableName;

    private Long idNouveauStatut;
    private String libelleNouveauStatut;
    private Integer scoreNouveauStatut;
    private String spanHtmlNouveauStatut;

    private LocalDateTime dateMouvement;
    private String observation;

}