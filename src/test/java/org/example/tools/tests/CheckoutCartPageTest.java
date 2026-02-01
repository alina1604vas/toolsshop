package org.example.tools.tests;

import org.example.tools.infra.EnabledForSprint;
import org.example.tools.pageobject.CheckoutCartPage;
import org.example.tools.pageobject.entity.Cart;
import org.example.tools.pageobject.entity.UiCartElement;
import org.example.tools.tests.utils.CheckoutTestHelper;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@EnabledForSprint(4)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CheckoutCartPageTest extends BaseTest {
    private static CheckoutTestHelper checkoutHelper;

    @BeforeAll
    public static void setUp() {
        checkoutHelper = new CheckoutTestHelper(driver);
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Verify products in the cart and their subtotal")
    public void cart_contains_products_with_correct_subtotals() {
        Cart expectedCart = checkoutHelper.buildCartWithRandomProducts(5, 5);
        CheckoutCartPage cartPage = checkoutHelper.openCart();
        List<UiCartElement> actualUiProducts = cartPage.getUIProductsInCart();
        checkoutHelper.assertCartSubtotalsMatch(expectedCart, actualUiProducts);

    }

    @Test
    @Tag("sprint3")
    @DisplayName("Verify if there are missed products in a cart")
    public void cart_containsAll_addedProducts() {
        Cart expectedCart = checkoutHelper.buildCartWithRandomProducts(5, 5);

        CheckoutCartPage cartPage = checkoutHelper.openCart();
        List<UiCartElement> actualUiProducts = cartPage.getUIProductsInCart();
        List<UiCartElement> missedProducts = expectedCart.getItems().stream()
                .filter(expected -> !actualUiProducts.contains(expected))
                .toList();

        assertTrue(missedProducts.isEmpty(), "There are missed products: " + missedProducts);
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Verify total price per cart")
    public void cartTotal_matchesSumOfItems() {
        Cart expectedCart = checkoutHelper.buildCartWithRandomProducts(5, 5);
        CheckoutCartPage cartPage = checkoutHelper.openCart();
        List<UiCartElement> actualUiProducts = cartPage.getUIProductsInCart();
        checkoutHelper.assertCartSubtotalsMatch(expectedCart, actualUiProducts);
    }

    @Test
    @Tag("sprint3")
    @DisplayName("Verify that after product is deleted, list of products in the cart is updated")
    public void cart_updatesList_afterProductDeletion() {
        CheckoutCartPage cartPage = checkoutHelper.openCart();
        checkoutHelper.addRandomProductAndAssertDeletion(cartPage, 5);
    }
}


