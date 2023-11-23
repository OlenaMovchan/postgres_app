package com.shpp;

import com.shpp.dto.Category;
import com.shpp.dto.Delivery;
import com.shpp.dto.Product;
import com.shpp.dto.Store;
import org.junit.jupiter.api.Test;

import jakarta.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorClassTest {

    @Test
    void testValidateValidCategory() {
        ValidatorClass validatorClass = new ValidatorClass();
        Category validCategory = new Category("ValidCategory");

        assertTrue(validatorClass.validateDTO(validCategory));

    }

    @Test
    void testValidateInvalidCategory() {
        ValidatorClass validatorClass = new ValidatorClass();
        Category invalidCategory = new Category("");

        assertThrows(ValidationException.class, () -> validatorClass.validateDTO(invalidCategory));
        invalidCategory.setCategoryName(null);
        assertThrows(ValidationException.class, () -> validatorClass.validateDTO(invalidCategory));

    }

    @Test
    void testValidateValidDelivery() {
        ValidatorClass validatorClass = new ValidatorClass();
        Delivery validDelivery = new Delivery(1, 1, 10);

        assertTrue(validatorClass.validateDTO(validDelivery));

    }

    @Test
    void testValidateInvalidDelivery() {
        ValidatorClass validatorClass = new ValidatorClass();
        Delivery invalidDelivery = new Delivery(1, 1, -10);

        assertThrows(ValidationException.class, () -> validatorClass.validateDTO(invalidDelivery));
        invalidDelivery.setProductId(10);
        invalidDelivery.setStoreId(10);
        invalidDelivery.setProductCount(-100);
        assertThrows(ValidationException.class, () -> validatorClass.validateDTO(invalidDelivery));

    }

    @Test
    void testValidateValidProduct() {
        ValidatorClass validatorClass = new ValidatorClass();
        Product validProduct = new Product("ValidProduct", 1);

        assertTrue(validatorClass.validateDTO(validProduct));

    }

    @Test
    void testValidateInvalidProduct() {
        ValidatorClass validatorClass = new ValidatorClass();
        Product invalidProduct = new Product("", 0);

        assertThrows(ValidationException.class, () -> validatorClass.validateDTO(invalidProduct));
        invalidProduct.setCategoryId(-1);
        invalidProduct.setProductName(null);
        assertThrows(ValidationException.class, () -> validatorClass.validateDTO(invalidProduct));

    }

    @Test
    void testValidateValidStore() {
        ValidatorClass validatorClass = new ValidatorClass();
        Store validStore = new Store("ValidLocation");

        assertTrue(validatorClass.validateDTO(validStore));

    }

    @Test
    void testValidateInvalidStore() {
        ValidatorClass validatorClass = new ValidatorClass();
        Store invalidStore = new Store("");

        assertThrows(ValidationException.class, () -> validatorClass.validateDTO(invalidStore));
        invalidStore.setLocation(null);
        assertThrows(ValidationException.class, () -> validatorClass.validateDTO(invalidStore));

    }
}