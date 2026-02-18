package com.digitalid.person;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    
    private Person person;
    private static final String TEST_FILE = "persons.txt";
    
    @BeforeEach
    void setUp() {
        person = new Person();
        // Delete test file before each test
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // TEST 1: Valid person
    @Test
    void testAddPerson_ValidPerson_ReturnsTrue() {
        Person validPerson = new Person(  // Note: Capital P in Person
            "56s_d%&fAB",
            "John",
            "Doe",
            "32|Highland Street|Melbourne|Victoria|Australia",  // No spaces after pipes
            "15-11-1990"
        );
        
        assertTrue(person.addPerson(validPerson));
        
        // Verify file was created
        File file = new File(TEST_FILE);
        assertTrue(file.exists());
    }
    
    // TEST 2: Invalid personID
    @Test
    void testAddPerson_InvalidPersonID_ReturnsFalse() {
        Person invalidPerson = new Person(  // Note: Capital P in Person
            "12345", // Too short
            "John",
            "Doe",
            "32|Highland Street|Melbourne|Victoria|Australia",
            "15-11-1990"
        );
        
        assertFalse(person.addPerson(invalidPerson));
    }
    
    // TEST 3: Invalid address (wrong state)
    @Test
    void testAddPerson_InvalidAddress_WrongState_ReturnsFalse() {
        Person invalidPerson = new Person(  // Note: Capital P in Person
            "56s_d%&fAB",
            "John",
            "Doe",
            "32|Highland Street|Melbourne|New South Wales|Australia",
            "15-11-1990"
        );
        
        assertFalse(person.addPerson(invalidPerson));
    }
    
    // TEST 4: Invalid address (wrong format)
    @Test
    void testAddPerson_InvalidAddress_WrongFormat_ReturnsFalse() {
        Person invalidPerson = new Person(
            "56s_d%&fAB",
            "John",
            "Doe",
            "32 Highland Street Melbourne Victoria Australia", // No pipes
            "15-11-1990"
        );
        
        assertFalse(person.addPerson(invalidPerson));
    }
    
    // TEST 5: Invalid birthday
    @Test
    void testAddPerson_InvalidBirthday_ReturnsFalse() {
        Person invalidPerson = new Person(
            "56s_d%&fAB",
            "John",
            "Doe",
            "32|Highland Street|Melbourne|Victoria|Australia",
            "15/11/1990"  // Wrong separator
        );
        
        assertFalse(person.addPerson(invalidPerson));
    }
}