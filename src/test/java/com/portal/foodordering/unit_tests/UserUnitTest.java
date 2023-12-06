package com.portal.foodordering.unit_tests;

import com.portal.foodordering.serivces.interfaces.UserService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class UserUnitTest {
    private UserService userService;

    @Before
    public void setUp() {
        userService = mock(UserService.class);
    }



}
