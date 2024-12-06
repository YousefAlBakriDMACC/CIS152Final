/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dmacc.cis152.module15.final_project.submittable;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author josep
 */
public class Donation implements Serializable {
    private static final long serialVersionUID = 7829136421241571165L;
    private static long totalDonationsCents = 0;
    private static long totalDonationsRemainingCents = 0;
    private String donor;
    private long donationAmountCents;
    private long balanceRemainingCents;
    
    public Donation() {
        this.donor = "";
        this.donationAmountCents = 0;
        this.balanceRemainingCents = 0;
    }
    
    public Donation(double donationAmount) {
        this.donor = "Anonymous";
        this.donationAmountCents = Double.valueOf(donationAmount * 100).longValue();
        this.balanceRemainingCents = Double.valueOf(donationAmount * 100).longValue();
        Donation.totalDonationsCents += Double.valueOf(donationAmount * 100).longValue();
        Donation.totalDonationsRemainingCents += Double.valueOf(donationAmount * 100).longValue();
    }
    
    public Donation(long donationAmountCents) {
        this.donor = "Anonymous";
        this.donationAmountCents = donationAmountCents;
        this.balanceRemainingCents = donationAmountCents;
        Donation.totalDonationsCents += donationAmountCents;
        Donation.totalDonationsRemainingCents += donationAmountCents;
    }
    
    public Donation(String donor, double donationAmount) {
        this.donor = donor.isBlank()? "Anonymous" : donor;
        this.donationAmountCents = Double.valueOf(donationAmount * 100).longValue();
        this.balanceRemainingCents = Double.valueOf(donationAmount * 100).longValue();
        Donation.totalDonationsCents += Double.valueOf(donationAmount * 100).longValue();
        Donation.totalDonationsRemainingCents += Double.valueOf(donationAmount * 100).longValue();
    }
    
    public Donation(String donor, long donationAmountCents) {
        this.donor = donor.isBlank()? "Anonymous" : donor;
        this.donationAmountCents = donationAmountCents;
        this.balanceRemainingCents = donationAmountCents;
        Donation.totalDonationsCents += donationAmountCents;
        Donation.totalDonationsRemainingCents += donationAmountCents;
    }

    public String getDonor() {
        return this.donor;
    }

    public double getDonationAmount() {
        return this.donationAmountCents / 100.00;
    }

    public long getDonationAmountCents() {
        return this.donationAmountCents;
    }

    public double getBalanceRemaining() {
        return this.balanceRemainingCents / 100.00;
    }

    public long getBalanceRemainingCents() {
        return this.balanceRemainingCents;
    }

    public static double getTotalDonations() {
        return totalDonationsCents / 100.00;
    }

    public static long getTotalDonationsCents() {
        return totalDonationsCents;
    }

    public static double getTotalDonationsRemaining() {
        return totalDonationsRemainingCents / 100.00;
    }

    public static long getTotalDonationsRemainingCents() {
        return totalDonationsRemainingCents;
    }
    
    public boolean isSpent() {
        return this.balanceRemainingCents == 0;
    }
    
    /**
     * Drains the balance remaining by the amount provided or up to 0.00
     * @param amountCents The amount in cents of this donation to spend
     * @return The portion of the amount provided left to be paid, or 0 if covered
     */
    public long spend(long amountCents) {
        if (amountCents >= this.balanceRemainingCents) {
            amountCents -= this.balanceRemainingCents;
            Donation.totalDonationsRemainingCents -= this.balanceRemainingCents;
            this.balanceRemainingCents = 0;
        } else {
            this.balanceRemainingCents -= amountCents;
            Donation.totalDonationsRemainingCents -= amountCents;
            amountCents = 0;
        }
        
        return amountCents;
    }
    
    /**
     * A convenience function to spend amounts in decimal format
     * @param amount The amount of this donation to spend
     * @return The portion of the amount provided left to be paid, or 0 if covered
     */
    public double spend(double amount) {
        return this.spend(Double.valueOf(amount * 100).longValue()) / 100.00;
    }

    /**
     * Custom serializer
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeUTF(this.donor);
        out.writeLong(this.donationAmountCents);
        out.writeLong(this.balanceRemainingCents);
        out.writeLong(Donation.totalDonationsCents);
        out.writeLong(Donation.totalDonationsRemainingCents);
    }
    
    /**
     * Custom deserializer
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.donor = in.readUTF();
        this.donationAmountCents = in.readLong();
        this.balanceRemainingCents = in.readLong();
        Donation.totalDonationsCents = in.readLong();
        Donation.totalDonationsRemainingCents = in.readLong();
    }
    
}
