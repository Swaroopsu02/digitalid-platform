package com.digitalid.person;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Person {
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthday;
    
    private static final String FILE_NAME = "persons.txt";
    
    // Constructors
    public Person() {}
    
    public Person(String personID, String firstName, String lastName, 
                  String address, String birthday) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthday = birthday;
    }
    
    // Getters and Setters
    public String getPersonID() { return personID; }
    public void setPersonID(String personID) { this.personID = personID; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    
    // Validation methods
    private boolean isValidPersonID(String id) {
        if (id == null || id.length() != 10) return false;
        
        // First two chars: numbers between 2-9
        char first = id.charAt(0);
        char second = id.charAt(1);
        if (first < '2' || first > '9' || second < '2' || second > '9') return false;
        
        // Last two chars: uppercase letters A-Z
        char lastSecond = id.charAt(8);
        char last = id.charAt(9);
        if (lastSecond < 'A' || lastSecond > 'Z' || last < 'A' || last > 'Z') return false;
        
        // At least two special chars between positions 3-8 (indices 2-7)
        String middlePart = id.substring(2, 8);
        int specialCount = 0;
        for (int i = 0; i < middlePart.length(); i++) {
            char c = middlePart.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                specialCount++;
            }
        }
        return specialCount >= 2;
    }
    
    private boolean isValidAddress(String addr) {
        if (addr == null) return false;
        
        String[] parts = addr.split("\\|");
        if (parts.length != 5) return false;
        
        // Check if State is Victoria
        return parts[3].equals("Victoria");
    }
    
    private boolean isValidBirthday(String bday) {
        if (bday == null) return false;
        
        // Check format DD-MM-YYYY
        if (!bday.matches("\\d{2}-\\d{2}-\\d{4}")) return false;
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(bday, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    // addPerson function
    public boolean addPerson(Person person) {
        // Check all conditions
        if (!isValidPersonID(person.getPersonID())) return false;
        if (!isValidAddress(person.getAddress())) return false;
        if (!isValidBirthday(person.getBirthday())) return false;
        
        // If all conditions met, write to file
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            // Format: personID|firstName|lastName|address|birthday
            out.println(person.getPersonID() + "|" + 
                       person.getFirstName() + "|" + 
                       person.getLastName() + "|" + 
                       person.getAddress() + "|" + 
                       person.getBirthday());
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Placeholder for Person 2
    public boolean updatePersonalDetails(String personID, Person updatedPerson) {
        return false;
    }
    
    // Placeholder for Person 3
    public boolean addID(String personID, String idType, String idValue) {
        return false;
    }
}
