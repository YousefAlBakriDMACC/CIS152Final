/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dmacc.cis152.module15.final_project.submittable;

import java.time.LocalDate;

/**
 *
 * @author josep
 */
public class Cat extends Pet {

    public Cat() {
        super();
    }

    public Cat(String name, int age, int healthScore, long costPerCycleCents) {
        super(name, age, healthScore, costPerCycleCents);
    }

    public Cat(String name, int age, int healthScore, long costPerCycleCents, LocalDate inductionDate) {
        super(name, age, healthScore, costPerCycleCents, inductionDate);
    }
    
}
