package com.thoughtworks.ketsu.infrastructure.repositories;

import com.thoughtworks.ketsu.domain.product.Product;
import com.thoughtworks.ketsu.domain.product.ProductRepository;
import com.thoughtworks.ketsu.support.DatabaseTestRunner;
import com.thoughtworks.ketsu.support.TestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(DatabaseTestRunner.class)
public class MyBatisProductRepositoryTest {

    @Inject
    ProductRepository productRepository;

    @Test
    public void should_create_product() {
        Product product = productRepository.createProduct(TestHelper.productMap("apple"));
        assertThat(product.getName(), is("apple"));
    }

    @Test
    public void should_get_product() {
        Product product = productRepository.createProduct(TestHelper.productMap("apple"));
        Product productGet = productRepository.findById(product.getId()).get();
        assertThat(product.getId(), is(productGet.getId()));
    }

    @Test
    public void should_return_all_products() {
        Product product1 = productRepository.createProduct(TestHelper.productMap("apple"));
        Product product2 = productRepository.createProduct(TestHelper.productMap("banana"));
        List<Product> products = productRepository.findAllProducts();
        assertThat(products.size(), is(2));
    }
}
