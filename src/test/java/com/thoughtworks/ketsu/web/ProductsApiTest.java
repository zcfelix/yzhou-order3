package com.thoughtworks.ketsu.web;


import com.thoughtworks.ketsu.domain.product.Product;
import com.thoughtworks.ketsu.domain.product.ProductRepository;
import com.thoughtworks.ketsu.support.ApiSupport;
import com.thoughtworks.ketsu.support.ApiTestRunner;
import com.thoughtworks.ketsu.support.TestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(ApiTestRunner.class)
public class ProductsApiTest extends ApiSupport {

    @Inject
    ProductRepository productRepository;

    @Test
    public void should_return_201_and_location_when_create_a_product() {
        final Response POST = post("products", TestHelper.productMap("apple"));
        assertThat(POST.getStatus(), is(201));
        assertThat(Pattern.matches(".*?/products/[0-9-]*", POST.getLocation().toASCIIString()), is(true));
    }

    @Test
    public void should_return_400_when_create_an_invalid_product() {
        final Response POST = post("products", TestHelper.productMap(""));
        assertThat(POST.getStatus(), is(400));
    }

    @Test
    public void should_return_200_and_products_when_get_all() {
        productRepository.createProduct(TestHelper.productMap("apple"));
        productRepository.createProduct(TestHelper.productMap("banana"));
        final Response GET = get("products");
        List<Product> products = GET.readEntity(List.class);
        assertThat(GET.getStatus(), is(200));
        assertThat(products.size(), is(2));
    }

    @Test
    public void should_return_200_when_a_product_found() {
        Product product =  productRepository.createProduct(TestHelper.productMap("apple"));
        final Response GET = get("products/" + product.getId());
        Map<String, Object> ret = GET.readEntity(Map.class);
        assertThat(GET.getStatus(), is(200));
        assertThat(ret.get("name"), is("apple"));
        assertThat(ret.get("price"), is(2.5));
        assertThat(ret.get("uri"), is("products/" + product.getId()));
        assertThat(ret.get("id"), is(nullValue()));
    }

    @Test
    public void should_return_404_when_a_product_not_found() {
        Product product =  productRepository.createProduct(TestHelper.productMap("apple"));
        final Response GET = get("products/" + (product.getId() + 1));
        assertThat(GET.getStatus(), is(404));
    }
}
