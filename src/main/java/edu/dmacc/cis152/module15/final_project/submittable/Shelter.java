/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dmacc.cis152.module15.final_project.submittable;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The code controlling shelter operation
 * @author josep
 */
public class Shelter {
    public Data data;
    private String name;
    private String petDataSource;
    private String donationDataSource;
    
    
    
    public Shelter() {
        this("Unnamed Shelter");
    }
    
    public Shelter(String name) {
        this(name, "PetRecords.ser", "DonationRecords.ser");
    }
    
    public Shelter(String petDataSource, String donationDataSource) {
        this("Unnamed Shelter", petDataSource, donationDataSource);
    }
    
    public Shelter(String name, String petDataSource, String donationDataSource) {
        this.name = name;
        this.data = new Data();
        this.petDataSource = petDataSource;
        this.donationDataSource = donationDataSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPetDataSource() {
        return petDataSource;
    }

    public void setPetDataSource(String petDataSource) {
        this.petDataSource = petDataSource;
    }

    public String getDonationDataSource() {
        return donationDataSource;
    }

    public void setDonationDataSource(String donationDataSource) {
        this.donationDataSource = donationDataSource;
    }
    
    public void loadData() {
        try {
            this.data.loadData(this.petDataSource);
        } catch (IOException | IllegalArgumentException ex) {
            Logger.getLogger(Shelter.class.getName()).log(Level.SEVERE, ex.getMessage());
            Logger.getLogger(Shelter.class.getName()).log(Level.SEVERE, "{0} could not finish loading data!", this.name);
            System.out.println(ex);
        }

        try {
            this.data.loadData(this.donationDataSource);
        } catch (IOException | IllegalArgumentException ex) {
            Logger.getLogger(Shelter.class.getName()).log(Level.SEVERE, ex.getMessage());
            Logger.getLogger(Shelter.class.getName()).log(Level.SEVERE, "{0} could not finish loading data!", this.name);
            System.out.println(ex);
        }
    }
    
    public void persistData() {
        try {
            this.data.persistData(this.petDataSource);
        } catch (IOException | IllegalArgumentException ex) {
            Logger.getLogger(Shelter.class.getName()).log(Level.SEVERE, ex.getMessage());
            Logger.getLogger(Shelter.class.getName()).log(Level.SEVERE, "{0} could not finish saving data!", this.name);
            System.out.println(ex);
        }

        try {
            this.data.persistData(this.donationDataSource);
        } catch (IOException | IllegalArgumentException ex) {
            Logger.getLogger(Shelter.class.getName()).log(Level.SEVERE, ex.getMessage());
            Logger.getLogger(Shelter.class.getName()).log(Level.SEVERE, "{0} could not finish saving data!", this.name);
            System.out.println(ex);
        }
    }
}
