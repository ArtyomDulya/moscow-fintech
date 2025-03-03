CREATE database moscow_fintech;

CREATE TABLE securities (
	id BIGSERIAL PRIMARY KEY ,
	secid VARCHAR(36) NOT NULL UNIQUE,
	regnumber VARCHAR(189),
	name VARCHAR(765) NOT NULL,
	emitenttitle VARCHAR(765)
);

CREATE TABLE histories(
	secid VARCHAR(36) REFERENCES securities(secid) ON DELETE CASCADE NOT NULL,
	tradedate DATE,
	open DOUBLE PRECISION,
    close DOUBLE PRECISION
);

CREATE UNIQUE INDEX  idx_sec_id_trade_date ON histories (secid, tradedate);






