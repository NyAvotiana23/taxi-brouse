package com.mdgtaxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour représenter une liste d'entités avec leurs statuts à une date donnée
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityListWithStatusDto {
    private List<EntityDetailWithStatusDto> entities;
    private LocalDateTime atDate;
    private Long totalCount;
}