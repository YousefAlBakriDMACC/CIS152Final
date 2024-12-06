/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dmacc.cis152.module15.final_project.submittable;

import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 *
 * @author josep
 */
public class Driver implements KeyListener {
    private static Shelter shelter;
    
    private static class JComponents {
        static JFrame tempSetupWindow;
        static JFrame mainframe;

        //initMainFrame
        static JPanel contentPane;
        
        //initAdminToolBar
        static JPanel administrationContainer;
        static JButton login;
        
        //initDonationInterface
        static JPanel donationContainer;
        static JPanel donorContainer;
        static JTextField donorName;
        static JPanel moneyContainer;
        static JTextField moneyAmount;
        static JButton processDonation;
        
        //initPetDisplayInterface
        static JPanel tableContainer;
        static JTable petTable;
        
        //initAdoptionInterface
        static JPanel adoptionContainer;
        static JComboBox petType;
        static JPanel nameContainer;
        static JTextField petName;
        static JButton processAdoption;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W && !e.isAltDown()) {
            //[CTRL]+[W] & [CTRL]+[SHFT]+[W]
            Driver.JComponents.mainframe.dispatchEvent(new WindowEvent(Driver.JComponents.mainframe, WindowEvent.WINDOW_CLOSING));
        } else if ((e.isAltDown() ^ e.isControlDown()) && e.getKeyCode() == KeyEvent.VK_F4 && !e.isShiftDown()) {
            //[ALT]+[F4] & [CTRL]+[F4]
            Driver.JComponents.mainframe.dispatchEvent(new WindowEvent(Driver.JComponents.mainframe, WindowEvent.WINDOW_CLOSING));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Do nothing
    }
    
    private static void prepareShelterGUI() {
        Runnable initMainframe = () -> {
            Driver.JComponents.mainframe = new JFrame();
            Driver.JComponents.mainframe.setTitle(Driver.shelter.getName() + " Management Application");
            UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            screenSize.setSize(screenSize.getWidth() * 4/6, screenSize.getHeight() * 5/6);
            Driver.JComponents.mainframe.setSize(screenSize);
            Driver.JComponents.mainframe.setLocationRelativeTo(null);
            Driver.JComponents.mainframe.setLayout(new BorderLayout());
            Driver.JComponents.mainframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); //Closing manually handled
            Driver.JComponents.mainframe.addKeyListener(new Driver());
            Driver.JComponents.mainframe.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    //Confirm Close Operation
                    int action = JOptionPane.showConfirmDialog(Driver.JComponents.mainframe, "Save before Closing?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (action) {
                        case JOptionPane.YES_OPTION -> {
                            Driver.shelter.persistData();
                            Driver.JComponents.mainframe.dispose();
                        }
                        case JOptionPane.NO_OPTION -> {
                            Driver.JComponents.mainframe.dispose();
                        }
                        case JOptionPane.CANCEL_OPTION -> {
                            return;
                        }
                        case JOptionPane.CLOSED_OPTION -> {
                            return;
                        }
                        default -> {
                            throw new IllegalStateException("Unreachable default state reached");
                        }
                    }
                }
            });
            Driver.JComponents.contentPane = new JPanel(new BorderLayout(5, 1));
            Driver.JComponents.contentPane.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            Driver.JComponents.mainframe.add(Driver.JComponents.contentPane);
            Driver.JComponents.mainframe.setContentPane(Driver.JComponents.contentPane);
        };
        
        Runnable initAdminToolbar = () -> {
            Driver.JComponents.administrationContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
            Driver.JComponents.administrationContainer.add(new JLabel("Administration: "));
            Driver.JComponents.administrationContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            
            Driver.JComponents.login = new JButton("Login");
            Driver.JComponents.administrationContainer.add(Driver.JComponents.login);
            
            //Add to mainframe
            Driver.JComponents.mainframe.getContentPane().add(Driver.JComponents.administrationContainer, BorderLayout.NORTH);
            java.util.logging.Logger.getLogger(Driver.class.getName()).log(java.util.logging.Level.WARNING, "Not implemented yet");
        };
        
        Runnable initDonationInterface = () -> {
            Driver.JComponents.donationContainer = new JPanel();
            Driver.JComponents.donationContainer.setLayout(new BoxLayout(Driver.JComponents.donationContainer, BoxLayout.Y_AXIS));
            Driver.JComponents.donationContainer.add(new JLabel("Donate to support " + Driver.shelter.getName() + "!"));
            
            Driver.JComponents.donorContainer = new JPanel();
            Driver.JComponents.donorContainer.add(new JLabel("Name: "));
            Driver.JComponents.donorName = new JTextField(12);
            Driver.JComponents.donorContainer.add(Driver.JComponents.donorName);
            Driver.JComponents.donationContainer.add(Driver.JComponents.donorContainer);
            
            Driver.JComponents.moneyContainer = new JPanel();
            Driver.JComponents.moneyContainer.add(new JLabel("Amount: $"));
            Driver.JComponents.moneyAmount = new JTextField(12);
            Driver.JComponents.moneyContainer.add(Driver.JComponents.moneyAmount);
            Driver.JComponents.donationContainer.add(Driver.JComponents.moneyContainer);
            
            Driver.JComponents.processDonation = new JButton("Donate");
            Driver.JComponents.processDonation.addActionListener((ActionEvent e) -> {
                if (Driver.JComponents.donorName.getText().isBlank()) {
                    switch(JOptionPane.showConfirmDialog(Driver.JComponents.mainframe, "Donor Name is blank!  Leave an anonymous donation?", "Continue?", JOptionPane.YES_NO_OPTION)) {
                        case JOptionPane.NO_OPTION: return;
                        case JOptionPane.CLOSED_OPTION: return;
                    }
                }
                if (Driver.JComponents.moneyAmount.getText().isBlank()) {
                    JOptionPane.showMessageDialog(Driver.JComponents.mainframe, "Donations cannot be blank!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!Driver.JComponents.moneyAmount.getText().matches("^\\d+\\.?\\d{0,2}$")) {  //Digit(s) followed by optional period and 0-2 more digits
                    JOptionPane.showMessageDialog(Driver.JComponents.mainframe, Driver.JComponents.moneyAmount.getText() + " is not a properly formatted amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Driver.shelter.data.addDonation(new Donation(Driver.JComponents.donorName.getText(), Double.parseDouble(Driver.JComponents.moneyAmount.getText())));
                JOptionPane.showMessageDialog(Driver.JComponents.mainframe, "Donation of $" + Driver.JComponents.moneyAmount.getText() + " successfully processed", "Thank You!", JOptionPane.INFORMATION_MESSAGE);
                Driver.shelter.data.alphabetizeDonors();
            });
            //Add to mainframe
            Driver.JComponents.donationContainer.add(Driver.JComponents.processDonation);
            Driver.JComponents.mainframe.getContentPane().add(Driver.JComponents.donationContainer, BorderLayout.WEST);
        };
        
        Runnable initPetDisplayInterface = () -> {
            Driver.JComponents.tableContainer = new JPanel();
            Driver.JComponents.tableContainer.setLayout(new BoxLayout(Driver.JComponents.tableContainer, BoxLayout.Y_AXIS));
            int numPetTypes = Driver.shelter.data.pets.size();
            int maxPetCount = Driver.shelter.data.pets.values().stream().mapToInt(pQueue -> pQueue.size()).max().orElse(0);
            Vector<Vector<Pet>> rowData = new Vector(); //As [Row, Col]
            for (int i = 0; i < maxPetCount; i++) {
                Vector<Pet> crossRowData = new Vector<>();
                for (String key : Driver.shelter.data.pets.keySet()) {
                    crossRowData.add(Driver.shelter.data.pets.get(key).stream().skip(i).findFirst().orElse(null));
                }
                rowData.add(crossRowData);
            }

            //Create and size table
            Driver.JComponents.petTable = new JTable(rowData, new Vector<>(Driver.shelter.data.pets.keySet()));
            Driver.JComponents.petTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
            
            for (int column = 0; column < Driver.JComponents.petTable.getColumnCount(); column++) {
                //Set width to header row
                //int minWidth = Driver.JComponents.petTable.getTableHeader().getColumnModel().getColumn(column).getHeaderValue().toString().length() + Driver.JComponents.petTable.getIntercellSpacing().width;
                int minWidth = Math.round(1.2f * Driver.JComponents.petTable.getFontMetrics(Driver.JComponents.petTable.getTableHeader().getFont()).stringWidth(Driver.JComponents.petTable.getTableHeader().getColumnModel().getColumn(column).getHeaderValue().toString())) + Driver.JComponents.petTable.getIntercellSpacing().width;
                //Find longest row in col
                for (int row = 0; row < Driver.JComponents.petTable.getRowCount(); row++) {
                    //int rowWidth = String.valueOf(Driver.JComponents.petTable.getValueAt(row, column) + "").length() + Driver.JComponents.petTable.getIntercellSpacing().width;
                    int rowWidth = Math.round(1.1f * Driver.JComponents.petTable.getFontMetrics(Driver.JComponents.petTable.getFont()).stringWidth(String.valueOf(Driver.JComponents.petTable.getValueAt(row, column) + ""))) + Driver.JComponents.petTable.getIntercellSpacing().width;
                    minWidth = Math.max(minWidth, rowWidth);
                }
                Driver.JComponents.petTable.getColumnModel().getColumn(column).setPreferredWidth(minWidth);
            }
            /*
            petTable.getColumnModel().getColumns().asIterator().forEachRemaining((TableColumn column) -> {
                //CAUTION: Anonymous abstract implementation will break on addition of abstract methods to Pet class
                int desiredWidth = (new Pet() {}).toString().length();
                //System.out.println(desiredWidth);
                column.setMinWidth(desiredWidth);
                column.setMaxWidth(desiredWidth);
                column.setPreferredWidth(desiredWidth);
            });
            */

            //Add to mainframe
            //tableContainer.add(petTable.getTableHeader());
            //tableContainer.add(petTable);
            Driver.JComponents.tableContainer.add(new JScrollPane(Driver.JComponents.petTable));
            Driver.JComponents.mainframe.getContentPane().add(Driver.JComponents.tableContainer, BorderLayout.CENTER);
        };
        
        
        Runnable initAdoptionInterface = () -> {
            Driver.JComponents.adoptionContainer = new JPanel();
            Driver.JComponents.adoptionContainer.setLayout(new BoxLayout(Driver.JComponents.adoptionContainer, BoxLayout.Y_AXIS));
            Driver.JComponents.adoptionContainer.add(new JLabel("See a pet you like?  Adopt them today!"));
            
            Driver.JComponents.petType = new JComboBox(new Vector<String>(Driver.shelter.data.pets.keySet()));
            Driver.JComponents.petType.setMaximumRowCount(5);
            Driver.JComponents.petType.setBounds(new Rectangle(Driver.JComponents.petType.getBounds().width, Toolkit.getDefaultToolkit().getScreenSize().height * 1/6));

            Driver.JComponents.nameContainer = new JPanel();
            Driver.JComponents.nameContainer.add(new JLabel("Pet Name: "));
            Driver.JComponents.petName = new JTextField(12);
            Driver.JComponents.nameContainer.add(Driver.JComponents.petName);
            Driver.JComponents.processAdoption = new JButton("Process Adoption");
            Driver.JComponents.processAdoption.addActionListener((ActionEvent e) -> {
                if (Driver.JComponents.petName.getText().isBlank()) {
                    JOptionPane.showMessageDialog(Driver.JComponents.mainframe, "Pet Name cannot be blank!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (Driver.shelter.data.removePet(Driver.JComponents.petType.getSelectedItem().toString(), Driver.JComponents.petName.getText()) == null) {
                    JOptionPane.showMessageDialog(Driver.JComponents.mainframe, Driver.JComponents.petName.getText() + " could not be found!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(Driver.JComponents.mainframe, Driver.JComponents.petName.getText() + " was successfully adopted!  Enjoy your new " + Driver.JComponents.petType.getSelectedItem().toString().toLowerCase() + "!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                //Refresh application
                Driver.JComponents.mainframe.dispose();
                Driver.prepareShelterGUI();
                Driver.displayShelterGUI();
            });
            
            //Add to mainframe
            Driver.JComponents.adoptionContainer.add(Driver.JComponents.petType);
            Driver.JComponents.adoptionContainer.add(Driver.JComponents.nameContainer);
            Driver.JComponents.adoptionContainer.add(Driver.JComponents.processAdoption);
            Driver.JComponents.mainframe.getContentPane().add(Driver.JComponents.adoptionContainer, BorderLayout.EAST);
        };
        
        //Execute
        initMainframe.run();
        initAdminToolbar.run();
        initDonationInterface.run();
        initPetDisplayInterface.run();
        initAdoptionInterface.run();
    }
    
    private static void prepareShelter() {
        Driver.JComponents.tempSetupWindow = new JFrame();
        Driver.JComponents.tempSetupWindow.setTitle("Setup Application");
        Driver.JComponents.tempSetupWindow.setLocationRelativeTo(null);
        Driver.JComponents.tempSetupWindow.setVisible(true);
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
        int action = JOptionPane.showConfirmDialog(Driver.JComponents.tempSetupWindow, "Create New Shelter?\nNOTE: Data may be overriden", "Setup", JOptionPane.YES_NO_OPTION);
        switch (action) {
            case(JOptionPane.YES_OPTION) -> {
                String shelterName = String.valueOf(JOptionPane.showInputDialog(Driver.JComponents.tempSetupWindow, "Shelter Name: ") + "");
                Driver.shelter = new Shelter(shelterName, "PetRecords.ser", "DonationRecords.ser");
                //Seed data
                //TODO: Populate with more realistic data
                Driver.shelter.data.addPet(new Cat("Cat1", 1, 100, 5500));
                Driver.shelter.data.addPet(new Cat("Cat2", 1, 100, 5500));
                Driver.shelter.data.addPet(new Cat("Cat39999", 1, 100, 5500));
                Driver.shelter.data.addPet(new Dog());
                Driver.shelter.data.addPet(new Cat("", 1, 1, 1, java.time.LocalDate.now()));
                Driver.shelter.data.addDonation(new Donation());
            }
            default -> {
                //Load default shelter
                Driver.shelter = new Shelter("Shelter Demo", "PetRecords.ser", "DonationRecords.ser");
                Driver.shelter.loadData();
            }
        }
        Driver.JComponents.tempSetupWindow.dispose();
    }
    
    private static void displayShelterGUI() {
        Driver.JComponents.mainframe.setVisible(true);
    }
    

    
    public static void main(String[] args) {
        Driver.prepareShelter();
        Driver.prepareShelterGUI();
        Driver.displayShelterGUI();
        
        
        
        
    }
}
