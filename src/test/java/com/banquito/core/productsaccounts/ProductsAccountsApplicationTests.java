package com.banquito.core.productsaccounts;

import com.banquito.core.productsaccounts.exception.CRUDException;
import com.banquito.core.productsaccounts.model.ProductAccount;
import com.banquito.core.productsaccounts.repository.ProductAccountRepository;
import com.banquito.core.productsaccounts.service.ProductAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProductsAccountsApplicationTests {

    @Mock
    private ProductAccountRepository productAccountRepository;

    private ProductAccountService productAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        productAccountService = new ProductAccountService(productAccountRepository);
    }

    @Test
    void testListAllActives() {
        List<ProductAccount> mockAccounts = new ArrayList<>();
        mockAccounts.add(new ProductAccount());
        mockAccounts.add(new ProductAccount());

        when(productAccountRepository.findByState("ACT")).thenReturn(mockAccounts);

        List<ProductAccount> result = productAccountService.listAllActives();

        assertEquals(mockAccounts.size(), result.size());
    }

    @Test
    void testObtainByIdExistingAccount() throws CRUDException {
        String accountId = "someId";
        ProductAccount expectedAccount = new ProductAccount();
        expectedAccount.setId(accountId);

        when(productAccountRepository.findById(accountId)).thenReturn(Optional.of(expectedAccount));

        ProductAccount result = productAccountService.obtainById(accountId);

        assertEquals(expectedAccount, result);
    }

    @Test
    void testObtainByIdNonExistingAccount() {
        String accountId = "nonExistentId";

        when(productAccountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(CRUDException.class, () -> productAccountService.obtainById(accountId));
    }

    @Test
    void testCreateProductAccount() throws CRUDException {
        ProductAccount newAccount = new ProductAccount();
        newAccount.setId("newCode");
        productAccountService.create(newAccount);

        verify(productAccountRepository).save(newAccount);
        assertNotNull(newAccount.getCreationDate());
    }

    @Test
    void testCreateProductAccountException() {
        ProductAccount newAccount = new ProductAccount();
        newAccount.setId("newCode");

        when(productAccountRepository.save(newAccount)).thenThrow(new RuntimeException("Some error"));

        assertThrows(CRUDException.class, () -> productAccountService.create(newAccount));
    }

}
