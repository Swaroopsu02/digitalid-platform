package com.digitalid.person;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


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
    
    // Placeholder for Person 2 (Abi)
// calculating age for rule 1
private int calculateAge(String birthDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate birth = LocalDate.parse(birthDate, formatter);
    return LocalDate.now().getYear() - birth.getYear();
}


public boolean updatePersonalDetails(String personID, Person updatedPerson) {

    File inputFile = new File(FILE_NAME);
    List<String> lines = new ArrayList<>();
    boolean updated = false;

    try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");

            if (parts[0].equals(personID)) {
                String oldID = parts[0];
                String oldFirstName = parts[1];
                String oldLastName = parts[2];
                String oldAddress = parts[3];
                String oldBirthday = parts[4];

                int age = calculateAge(oldBirthday);

                // RULE 1: under 18's cant change their address
                if (age < 18 && !oldAddress.equals(updatedPerson.getAddress())) return false;

                // RULE 2: If birthday changes, nothing else can change
                if (!oldBirthday.equals(updatedPerson.getBirthday())) {
                    if (!oldID.equals(updatedPerson.getPersonID()) ||
                        !oldFirstName.equals(updatedPerson.getFirstName()) ||
                        !oldLastName.equals(updatedPerson.getLastName()) ||
                        !oldAddress.equals(updatedPerson.getAddress())) {
                        return false;
                    }
                }
                // RULE 3: If first digit of old ID is even, cannot change ID
                int firstDigit = Character.getNumericValue(oldID.charAt(0));
                if (firstDigit % 2 == 0 && !oldID.equals(updatedPerson.getPersonID())) return false;

                //update line
                line = updatedPerson.getPersonID() + "|" +
                       updatedPerson.getFirstName() + "|" +
                       updatedPerson.getLastName() + "|" +
                       updatedPerson.getAddress() + "|" +
                       updatedPerson.getBirthday();

                updated = true;
            }

            lines.add(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }

    if (!updated) return false;

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
        for (String l : lines) {
            writer.write(l);
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }

    return true;
}
        

    
    // Placeholder for Person 3 (Rohan)
    // personID (pID) 
    public boolean addID(String pID, String idType, String idValue) {
        boolean isValid = false;

        // Passport has 8 chars: 2 Uppercase letters + 6 Numbers
        if (idType.equalsIgnoreCase("Passport")) {
            if (idValue != null && idValue.matches("^[A-Z]{2}[0-9]{6}$")) {
                isValid = true;
            }
        } 
        // driver's License has 10 chars: 2 Uppercase letters + 8 Numbers
        else if (idType.equalsIgnoreCase("Drivers Licence")) {
            if (idValue != null && idValue.matches("^[A-Z]{2}[0-9]{8}$")) {
                isValid = true;
            }
        } 
        // medicare has 9 numbers
        else if (idType.equalsIgnoreCase("Medicare")) {
            if (idValue != null && idValue.matches("^[0-9]{9}$")) { 
                isValid = true;
            }
        } 
        // Student Card has 12 digits only if under 18
        else if (idType.equalsIgnoreCase("Student Card")) {
            if (isUnder18() && idValue != null && idValue.matches("^[0-9]{12}$")) {
                isValid = true;
            }
        }

        if (isValid) {
            return saveIDToFile(pID, idType, idValue);
        }

        return false;
    }
    // helper
    private boolean isUnder18() {
        if (this.birthday == null){ 
            return false;
        }
        String[] parts = this.birthday.split("-");
        int birthYear = Integer.parseInt(parts[2]);
        int currentYear = LocalDate.now().getYear();
        
        int age = currentYear - birthYear;
        return age < 18;
    }

    private boolean saveIDToFile(String pID, String type, String value) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ids.txt", true))) {
            writer.write("PersonID: " + pID + " | User: " + firstName + " " + lastName + " | " + type + ": " + value);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("File Error: " + e.getMessage());
            return false;
        }
    }
    
}
