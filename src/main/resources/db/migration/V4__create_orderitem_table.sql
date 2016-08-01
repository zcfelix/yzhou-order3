CREATE TABLE orderitems (
    order_id INT NOT NULL ,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    amount DOUBLE NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);