-- Delete records with specific account numbers
DELETE FROM bank WHERE account_number IN ('1234', '1010', '5678', 'acc123');

INSERT INTO bank (account_number, trust, transaction_fee)
VALUES
    ('1234', 3.14, 17),
    ('1010', 17.0, 0),
    ('5678', 0.0, 100);