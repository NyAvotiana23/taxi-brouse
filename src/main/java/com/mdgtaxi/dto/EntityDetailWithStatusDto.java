package com.mdgtaxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour représenter une entité individuelle avec son statut
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityDetailWithStatusDto {
    private Object entity;
    private Long idStatut;
    private String libelleStatut;
    private Integer scoreStatut;
    private String spanHtmlStatut;
}