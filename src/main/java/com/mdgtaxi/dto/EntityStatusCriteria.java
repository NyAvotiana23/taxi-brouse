package com.mdgtaxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Critères de filtre pour rechercher des entités par statut
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityStatusCriteria {
    private String libelleStatut;
    private Integer minScore;
    private Integer maxScore;
    private Integer exactScore;
    private LocalDateTime atDate;

    public LocalDateTime getEffectiveDate() {
        return atDate != null ? atDate : LocalDateTime.now();
    }
}