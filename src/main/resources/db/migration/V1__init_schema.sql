CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE carts (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT    NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cart_items (
    id           BIGSERIAL PRIMARY KEY,
    cart_id      BIGINT        NOT NULL REFERENCES carts(id) ON DELETE CASCADE,
    product_id   BIGINT        NOT NULL,
    product_name VARCHAR(200)  NOT NULL,
    unit_price   NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0),
    quantity     INT           NOT NULL CHECK (quantity > 0),
    UNIQUE (cart_id, product_id)
);

CREATE TABLE orders (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT        NOT NULL REFERENCES users(id),
    status       VARCHAR(20)   NOT NULL,
    total_amount NUMERIC(14,2) NOT NULL,
    created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_user_id ON orders(user_id);

CREATE TABLE order_items (
    id           BIGSERIAL PRIMARY KEY,
    order_id     BIGINT        NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id   BIGINT        NOT NULL,
    product_name VARCHAR(200)  NOT NULL,
    unit_price   NUMERIC(12,2) NOT NULL,
    quantity     INT           NOT NULL
);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);

CREATE TABLE shipments (
    id             BIGSERIAL PRIMARY KEY,
    order_id       BIGINT       NOT NULL UNIQUE REFERENCES orders(id) ON DELETE CASCADE,
    address        VARCHAR(500) NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    dispatched_at  TIMESTAMP,
    delivered_at   TIMESTAMP
);
