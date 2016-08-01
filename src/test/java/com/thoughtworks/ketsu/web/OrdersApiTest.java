package com.thoughtworks.ketsu.web;

import com.thoughtworks.ketsu.domain.order.Order;
import com.thoughtworks.ketsu.domain.product.Product;
import com.thoughtworks.ketsu.domain.product.ProductRepository;
import com.thoughtworks.ketsu.domain.user.User;
import com.thoughtworks.ketsu.domain.user.UserRepository;
import com.thoughtworks.ketsu.support.ApiSupport;
import com.thoughtworks.ketsu.support.ApiTestRunner;
import com.thoughtworks.ketsu.support.TestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(ApiTestRunner.class)
public class OrdersApiTest extends ApiSupport{

    private User user;
    private Product product1, product2;

    @Inject
    UserRepository userRepository;

    @Inject
    ProductRepository productRepository;

    @Before
    @Override
    public void setUp() throws Exception{
        super.setUp();
        user = userRepository.createUser(TestHelper.userMap("felix"));
        product1 = productRepository.createProduct(TestHelper.productMap("apple"));
        product2 = productRepository.createProduct(TestHelper.productMap("banana"));

    }

    @Test
    public void should_return_201_and_location_when_create_order() {
        final Response POST = post("users/" + user.getId() + "/orders/", TestHelper.orderMap("kitty", product1, product2));
        assertThat(POST.getStatus(), is(201));
        assertThat(Pattern.matches(".*?/users/[0-9-]*/orders/[0-9-]*", POST.getLocation().toASCIIString()), is(true));
    }

    @Test
    public void should_return_400_when_create_an_invalid_order() {
        final Response POST = post("users/" + user.getId() + "/orders/", TestHelper.orderMap("", product1, product2));
        assertThat(POST.getStatus(), is(400));
    }

    @Test
    public void should_return_orders_list_when_get_all_orders() {
        user.createOrder(TestHelper.orderMap("kitty", product1, product2));
        user.createOrder(TestHelper.orderMap("alex", product1, product2));
        final Response GET = get("users/" + user.getId() + "/orders");
        List<Map<String, Object>> ordersList = GET.readEntity(List.class);
        assertThat(GET.getStatus(), is(200));
        assertThat(ordersList.size(), is(2));
    }

    @Test
    public void should_return_order_when_find_by_id() {
        Order order = user.createOrder(TestHelper.orderMap("kitty", product1, product2));
        final Response GET = get("users/" + user.getId() + "/orders/" + order.getId());
        Map<String, Object> ret = GET.readEntity(Map.class);
        assertThat(GET.getStatus(), is(200));
        assertThat(ret.get("name"), is("kitty"));
        assertThat(ret.get("address"), is("xian"));
        assertThat(ret.get("phone"), is("13996630396"));
    }

    @Test
    public void should_return_404_when_order_not_found() {
        Order order = user.createOrder(TestHelper.orderMap("kitty", product1, product2));
        final Response GET = get("users/" + user.getId() + "/orders/" + (order.getId() + 1));
        assertThat(GET.getStatus(), is(404));
    }

}
