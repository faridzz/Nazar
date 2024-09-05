package org.example.nazar.util.time.datereformater;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JalaliStringToGregorianDateDigikalaTest {

    private JalaliStringToGregorianDateDigikala gregorianDateDigikala;

    @BeforeEach
    void setUp() {
        // Initialize the class before each test
        gregorianDateDigikala = new JalaliStringToGregorianDateDigikala();
    }

    @Test
    void testDateSplitter_Return_ValidDate_If_InputIsValid() {
        // Arrange
        String dateInString = "4 شهریور 1403"; // Given a valid Jalali date string

        // Act
        List<Integer> listOfDateInt = gregorianDateDigikala.dateSplitter(dateInString); // When the dateSplitter method is called

        // Assert
        assertThat(listOfDateInt)
                .as("Check if the Jalali date string is correctly split into year, month, and day")
                .containsExactly(1403, 6, 4); // Expecting year 1403, month 6 (شهریور), day 4
    }
}
