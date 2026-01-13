package com.mdgtaxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Critères de filtre pour rechercher des mouvements de statut
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MouvementStatusCriteria {
    private Long idEntite;
    private String libelleAncienStatut;
    private String libelleNouveauStatut;
    private Integer minScoreAncien;
    private Integer maxScoreAncien;
    private Integer exactScoreAncien;
    private Integer minScoreNouveau;
    private Integer maxScoreNouveau;
    private Integer exactScoreNouveau;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String observationContains;

    public LocalDateTime getEffectiveDateDebut() {
        return dateDebut;
    }

    public LocalDateTime getEffectiveDateFin() {
        return dateFin != null ? dateFin : LocalDateTime.now();
    }
}