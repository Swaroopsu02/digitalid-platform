package com.digitalid.person;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    
    // Rohan's Test Cases 
    // 6) Valid Passport
    @Test
    void test1_ValidPassport() {
        // Birth year 1990 = adult
        Person p = new Person("22@#$AB", "John", "Doe", "32|St|Melb|Victoria|AU", "15-11-1990");
        assertTrue(p.addID("22@#$AB", "Passport", "AB123456"));
    }

    // 7) Invalid Medicare (Too Short)
    @Test
    void test2_InvalidMedicare() {
        Person p = new Person("22@#$AB", "John", "Doe", "32|St|Melb|Victoria|AU", "15-11-1990");
        assertFalse(p.addID("22@#$AB", "Medicare", "123"));
    }

    // 8) Invalid Passport (No Letters)
    @Test
    void test3_InvalidPassport_NoLetters() {
        Person p = new Person("22@#$AB", "John", "Doe", "32|St|Melb|Victoria|AU", "15-11-1990");
        assertFalse(p.addID("22@#$AB", "Passport", "12345678"));
    }

    // 9) Valid Student (Minor)
    @Test
    void test4_ValidStudent_Minor() {
        Person minor = new Person("22@#$AB", "Tim", "Young", "32|St|Melb|Victoria|AU", "01-01-2015");
        assertTrue(minor.addID("22@#$AB", "Student Card", "123456789012"));
    }

    // 10) Invalid Student (Adult)
    @Test
    void test5_InvalidStudent_Adult() {
        Person adult = new Person("22@#$AB", "Old", "Mate", "32|St|Melb|Victoria|AU", "01-01-1990");
        assertFalse(adult.addID("22@#$AB", "Student Card", "123456789012"));
    }

// Abi's Test Cases:
// 11) Update valid details (PASS)
@Test
void testUpdateValidDetails_ReturnsTrue() throws IOException {
    Person original = new Person(
        "56s_d%&fAB",
        "John",
        "Doe",
        "32|Highland Street|Melbourne|Victoria|Australia",
        "15-11-2000"
    );
    person.addPerson(original);

    Person updated = new Person(
        "56s_d%&fAB",
        "Johnny",
        "Doe",
        "32|Highland Street|Melbourne|Victoria|Australia",
        "15-11-2000"
    );

    assertTrue(person.updatePersonalDetails("56s_d%&fAB", updated));
}

// 12) Under 18 cannot change address (FAIL)
@Test
void testUpdateUnder18AddressChange_ReturnsFalse() throws IOException {
    Person minor = new Person(
        "56s_d%&fAB",
        "Alice",
        "Smith",
        "32|Highland Street|Melbourne|Victoria|Australia",
        "15-11-2010"
    );
    person.addPerson(minor);

    Person updated = new Person(
        "56s_d%&fAB",
        "Alice",
        "Smith",
        "33|Highland Street|Melbourne|Victoria|Australia", // address change
        "15-11-2010"
    );

    assertFalse(person.updatePersonalDetails("56s_d%&fAB", updated));
}

// 13) Changing birthday + other details at same time (FAIL)
@Test
void testBirthdayChangeWithOtherDetails_ReturnsFalse() throws IOException {
    Person p = new Person(
        "56s_d%&fAB",
        "John",
        "Doe",
        "32|Highland Street|Melbourne|Victoria|Australia",
        "15-11-2000"
    );
    person.addPerson(p);

    Person updated = new Person(
        "56s_d%&fAB",
        "Johnny", // name changed
        "Doe",
        "32|Highland Street|Melbourne|Victoria|Australia",
        "16-11-2000" // birthday changed
    );

    assertFalse(person.updatePersonalDetails("56s_d%&fAB", updated));
}

// 14) Even first digit in ID → cannot change ID (FAIL)
@Test
void testEvenFirstDigitIDChange_ReturnsFalse() throws IOException {
    Person p = new Person(
        "26s_d%&fAB", // starts with 2 (even)
        "John",
        "Doe",
        "32|Highland Street|Melbourne|Victoria|Australia",
        "15-11-2000"
    );
    person.addPerson(p);

    Person updated = new Person(
        "56s_d%&fAB", // trying to change ID
        "John",
        "Doe",
        "32|Highland Street|Melbourne|Victoria|Australia",
        "15-11-2000"
    );

    assertFalse(person.updatePersonalDetails("26s_d%&fAB", updated));
}

// 15) PersonID not found (FAIL)
@Test
void testPersonIDNotFound_ReturnsFalse() {
    Person updated = new Person(
        "56s_d%&fAB",
        "John",
        "Doe",
        "32|Highland Street|Melbourne|Victoria|Australia",
        "15-11-2000"
    );

    assertFalse(person.updatePersonalDetails("NONEXISTENT", updated));
}
}
