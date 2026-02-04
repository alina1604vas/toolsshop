package org.example.tools.utils;

import org.example.tools.pageobject.entity.BillingAddress;

public interface User {

    String getFirstName();

    String getLastName();

    String getEmail();

    BillingAddress getBillingAddress();
}
