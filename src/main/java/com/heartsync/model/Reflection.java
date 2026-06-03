package com.heartsync.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reflections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reflection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String notes;

    private Integer rating;

    @OneToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;
}