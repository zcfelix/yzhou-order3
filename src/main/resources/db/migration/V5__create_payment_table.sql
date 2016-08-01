CREATE TABLE payments (
    order_id INT NOT NULL ,
    pay_type VARCHAR (255) NOT NULL,
    amount DOUBLE NOT NULL,
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);