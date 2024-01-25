CREATE TABLE xm04_homework (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    description VARCHAR(255),
    deadline TIMESTAMP,
    teacher VARCHAR(255),
    if_submit BOOLEAN
    -- 其他属性...
);
