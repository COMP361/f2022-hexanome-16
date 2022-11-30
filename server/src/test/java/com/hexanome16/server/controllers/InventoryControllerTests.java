package com.hexanome16.server.controllers;

import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

/**
 * Test class for the inventory controller.
 *
 * @author Elea Dufresne
 */
@WebMvcTest(InventoryController.class)
public class InventoryControllerTests {

  /**
   * Setting up before each test is done.
   */
  @BeforeEach
  void setup() {

  }

  @Test
  public void testGetOwnCards() {
    throw new NotYetImplementedException();
  }

  @Test
  public void testGetOthersCards() {
    throw new NotYetImplementedException();
  }

  @Test
  public void testGetOwnNobles() {
    throw new NotYetImplementedException();
  }

  @Test
  public void testGetOthersNobles() {
    throw new NotYetImplementedException();
  }

  @Test
  public void testGetOwnReservedCards() {
    throw new NotYetImplementedException();
  }

  @Test
  public void testGetOthersReservedCards() {
    throw new NotYetImplementedException();
  }

  @Test
  public void testGetOwnReservedNobles() {
    throw new NotYetImplementedException();
  }

  @Test
  public void testGetOthersReservedNobles() {
    throw new NotYetImplementedException();
  }

}
