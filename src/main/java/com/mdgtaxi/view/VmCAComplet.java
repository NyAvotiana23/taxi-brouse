package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Immutable
@Subselect("""  """)
@Synchronize({})

public class VmCAComplet implements Serializable{
    mois

    annee

    caPrevisionTicket

    caPrevisionDiffusion

    caPrevisionProduit

    caPrevisionTotal

    caReelTicket

    caReelDiffusion

    caReelProduit

    caReelTotal
}


