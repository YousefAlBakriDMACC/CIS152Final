/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dmacc.cis152.module15.final_project.submittable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

/**
 * The base class for all pets
 * @author josep
 */
public abstract class Pet implements Serializable, Comparable<Pet> {
    protected String name;
    protected int age;
    protected int healthScore;
    protected long costPerCycleCents;
    protected LocalDate inductionDate;
    
    /**
     * Generates a null pet
     */
    protected Pet() {
        this.name = "Empty";
        this.age = -1;
        this.healthScore = -1;
        this.costPerCycleCents = -1;
        this.inductionDate = LocalDate.MIN;
    }

    /**
     * Generates a pet with today's date as the induction date
     * @param name
     * @param age
     * @param healthScore
     * @param costPerCycleCents 
     */
    protected Pet(String name, int age, int healthScore, long costPerCycleCents) {
        this(name, age, healthScore, costPerCycleCents, LocalDate.now());
    }

    /**
     * Generates a pet with all parameters provided
     * @param name
     * @param age
     * @param healthScore
     * @param costPerCycleCents
     * @param inductionDate 
     */
    protected Pet(String name, int age, int healthScore, long costPerCycleCents, LocalDate inductionDate) {
        this.name = name;
        this.age = age;
        this.healthScore = healthScore;
        this.costPerCycleCents = costPerCycleCents;
        this.inductionDate = inductionDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(int healthScore) {
        this.healthScore = healthScore;
    }

    public long getCostPerCycleCents() {
        return costPerCycleCents;
    }

    public void setCostPerCycleCents(long costPerCycleCents) {
        this.costPerCycleCents = costPerCycleCents;
    }

    public LocalDate getInductionDate() {
        return inductionDate;
    }

    public void setInductionDate(LocalDate inductionDate) {
        this.inductionDate = inductionDate;
    }
    
    /**
     * Gets the number of days since shelter induction
     * @return The number of complete days since induction
     */
    public long getDaysSinceInduction() {
        return getDaysSinceInduction(LocalDate.now());
    }

    /**
     * Gets the number of days between shelter induction and the provided LocalDate
     * @param until The date to calculate the difference from
     * @return The number of complete days since induction
     */    
    public long getDaysSinceInduction(LocalDate until) {
        return ChronoUnit.DAYS.between(inductionDate, until);
    }
    
    /**
     * Ages the pet according to induction date, to be called during each cycle
     */
    public void age() {
        if (this.getDaysSinceInduction() % 365 == 0) {
            this.age++;
        }
    }
    
    /**
     * Performs general pet operations, to be called during each cycle
     * @return The cost in cents of the performed operations
     */
    public long feed() {
        java.util.logging.Logger.getLogger(Pet.class.getName()).log(java.util.logging.Level.WARNING, "Not implemented yet");
        return Long.MAX_VALUE;
    }
    
    /**
     * Determines how adoptable a pet is, on the basis of health and other factors
     * @return A wrapped long with the adoptability score
     */
    protected Long getAdoptabilityScore() {
        return (this.costPerCycleCents * this.healthScore) - (this.age * 2) - this.getDaysSinceInduction();
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Pet other) {
            Boolean[] doesEqual = new Boolean[5];
            doesEqual[0] = (this.name != null && other.name != null)
                            ? (this.name.equals(other.name)) 
                            : (this.name == null && other.name == null);
            doesEqual[1] = (this.age == other.age);
            doesEqual[2] = (this.costPerCycleCents == other.costPerCycleCents);
            doesEqual[3] = (this.healthScore == other.healthScore);
            doesEqual[4] = (this.inductionDate != null && other.inductionDate != null)
                            ? (this.inductionDate.isEqual(other.inductionDate)) 
                            : (this.inductionDate == null && other.inductionDate == null);
            return Arrays.stream(doesEqual).allMatch(Boolean::valueOf);
        } else {
            return false;
        }
    }

    /**
     * Compares two pets on the basis of adoptability
     * @param other The pet to compare against
     * @return An int with a value corresponding to this pet's adoptability relative to the other's
     * A value greater than 0 indicates that this pet is more adoptable than the other
     * A value equal to 0 indicates that both pets are equally adoptable
     * A value less than 0 indicates that this pet is less adoptable than the other
     */
    @Override
    public int compareTo(Pet other) {
        return this.getAdoptabilityScore().compareTo(other.getAdoptabilityScore());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + this.name + ", aged " + this.age;
        //return "Pet{" + "name=" + name + ", age=" + age + ", healthScore=" + healthScore + ", costPerCycleCents=" + costPerCycleCents + ", inductionDate=" + inductionDate + '}';
    }
    
    
}
