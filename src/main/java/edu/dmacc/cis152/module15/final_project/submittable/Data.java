/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dmacc.cis152.module15.final_project.submittable;
import java.io.*;
import java.util.*;
import org.reflections.Reflections;

/**
 * Holds the data structures used in the application
 * TODO: Add persistence with JSON
 * @author josep
 */
public class Data {
    public List<Donation> donations;
    public Map<String, PriorityQueue<Pet>> pets;    //String key to max-heap of pets
    
    public Data() {
        this.donations = new ArrayList<>();
        this.pets = new HashMap<>();
        
        Reflections reflections = new Reflections("edu.dmacc.cis152.module15.final_project.submittable");
        for (Class petType : reflections.getSubTypesOf(Pet.class)) {
            //Adds map entries for all pet types
            this.pets.put(petType.getSimpleName(), new PriorityQueue<>(Comparator.reverseOrder()));
        }
    }
    
    public void addDonation(Donation donation) {
        this.donations.add(donation);
    }
    
    /**
     * Uses a quick sort algorithm to sort donations by donor names
     */
    public void alphabetizeDonors() {
        //Untested.  TODO: Replace with quick sort
        int n = this.donations.size() -1;
        if (this.donations.isEmpty()) {
            return;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                if (this.donations.get(j).getDonor().compareToIgnoreCase(this.donations.get(j + 1).getDonor()) < 0) {
                    //Swap
                    Donation tempDonation = this.donations.get(j);
                    this.donations.set(j, this.donations.get(j + 1));
                    this.donations.set(j + 1, tempDonation);
                }
            }
        }
    }
    
    
    public void addPet(Pet p) {
        if (this.pets.containsKey(p.getClass().getSimpleName())) {
            this.pets.get(p.getClass().getSimpleName()).add(p);
        }
    }
    
    public Pet removePet(String type) {
        if (this.pets.containsKey(type)) {
            return this.pets.get(type).poll();
        } else {
            return null;
        }
    }
    
    public Pet removePet(String type, String name) {
        if (this.pets.containsKey(type)) {
            return this.removePet(this.pets.get(type).stream().filter(pet -> pet.getName().equalsIgnoreCase(name)).findFirst().orElse(null));
        }
        return null;
    }
    
    public Pet removePet(Pet p) {
        if (p != null && this.pets.containsKey(p.getClass().getSimpleName())) {
            if (this.pets.get(p.getClass().getSimpleName()).contains(p)) {
                if (this.pets.get(p.getClass().getSimpleName()).remove(p)) {
                    return p;
                }
            }
        } 
        return null;
    }
    
    /**
     * Resorts pet heaps, to be called after each cycle
     */
    public void resortPets() {
        //Use priority queue sorting to update the heap
        for (PriorityQueue<Pet> queue : this.pets.values()) {
            List<Pet> list = new ArrayList<>();
            for (int i = 0; i < queue.size(); i++) {
                list.add(queue.poll());
            }
            queue.addAll(list);
        }
    }
    
    
    public void persistData(String filename) throws IOException {
        File dataSource = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + this.getClass().getPackageName().replace('.', File.separatorChar) + File.separator + filename);
        if (!dataSource.getName().endsWith("Records.ser")) {
            throw new IllegalArgumentException(dataSource.getName() + " does not meet application convention (${DataType}Records.ser)");
        }
        if (!Arrays.stream(this.getClass().getDeclaredFields()).map(field -> field.getName()).anyMatch(dataSource.getName().substring(0, dataSource.getName().indexOf("Records.ser")).concat("s").toLowerCase()::equals)) {
            throw new IllegalArgumentException(dataSource.getName().substring(0, dataSource.getName().indexOf("Records.ser")).concat("s") + " is not a supported data structure");
        }
        if (!Arrays.stream(dataSource.getParentFile().listFiles()).anyMatch(file -> file.getName().equals(dataSource.getName()))) {
            throw new IOException("No such file as " + dataSource.getName() + " exists");
        }
        
        try {
            //FIXME: Finish implementation
            var currentStructure = this.getClass().getField(dataSource.getName().substring(0, dataSource.getName().indexOf("Records.ser")).concat("s").toLowerCase()).get(this).getClass().cast(this.getClass().getField(dataSource.getName().substring(0, dataSource.getName().indexOf("Records.ser")).concat("s").toLowerCase()).get(this));
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            
        }
        
        if (dataSource.getName().startsWith("Donation")) {
            FileOutputStream writer = new FileOutputStream(dataSource, false);
            ObjectOutputStream persister = new ObjectOutputStream(writer);
            for (Donation data : this.donations) {
                persister.writeObject(data);
            }
            persister.close();
            writer.close();
        } else if (dataSource.getName().startsWith("Pet")) {
            FileOutputStream writer = new FileOutputStream(dataSource, false);
            ObjectOutputStream persister = new ObjectOutputStream(writer);
            for (PriorityQueue<Pet> species : this.pets.values()) {
                for (Pet data : species) {
                    persister.writeObject(data);
                }
            }
            persister.close();
            writer.close();
        } else {
            throw new IllegalStateException();
        }
    }
    
    public void loadData(String filename) throws IOException {
        File dataSource = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + this.getClass().getPackageName().replace('.', File.separatorChar) + File.separator + filename);
        if (!dataSource.getName().endsWith("Records.ser")) {
            throw new IllegalArgumentException(dataSource.getName() + " does not meet application convention (${DataType}Records.ser)");
        }
        if (!Arrays.stream(this.getClass().getDeclaredFields()).map(field -> field.getName()).anyMatch(dataSource.getName().substring(0, dataSource.getName().indexOf("Records.ser")).concat("s").toLowerCase()::equals)) {
            throw new IllegalArgumentException(dataSource.getName().substring(0, dataSource.getName().indexOf("Records.ser")).concat("s") + " is not a supported data structure");
        }
        if (!Arrays.stream(dataSource.getParentFile().listFiles()).anyMatch(file -> file.getName().equals(dataSource.getName()))) {
            throw new IOException("No such file as " + dataSource.getName() + " exists");
        }
        
        if (dataSource.getName().startsWith("Donation")) {
            FileInputStream reader = new FileInputStream(dataSource);
            ObjectInputStream loader = new ObjectInputStream(reader);
            do {
                try {
                    Donation data = (Donation) loader.readObject();
                    this.addDonation(data);
                } catch (EOFException e) {
                    //Stream finished
                    break;
                } catch (ClassNotFoundException ex) {
                    //Unreachable, terminate
                    throw new IllegalStateException(ex);
                } catch (ClassCastException ex) {
                    //Invalid data
                    throw new IOException(dataSource.getName() + " contained invalid objects : " + ex.getMessage());
                }
            } while (true);
        } else if (dataSource.getName().startsWith("Pet")) {
            FileInputStream reader = new FileInputStream(dataSource);
            ObjectInputStream loader = new ObjectInputStream(reader);
            do {
                try {
                    Pet data = (Pet) loader.readObject();
                    this.addPet(data);
                } catch (EOFException e) {
                    //Stream finished
                    break;
                } catch (ClassNotFoundException ex) {
                    //Unreachable, terminate
                    throw new IllegalStateException(ex);
                } catch (ClassCastException ex) {
                    //Invalid data
                    throw new IOException(dataSource.getName() + " contained invalid objects : " + ex.getMessage());
                }
            } while (true);            
        } else {
            throw new IllegalStateException();
        }
    }
}
