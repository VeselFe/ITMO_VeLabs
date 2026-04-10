DROP TABLE IF EXISTS results_of_fortune_telling;
DROP TABLE IF EXISTS responsibility;
DROP TABLE IF EXISTS fate;
DROP TABLE IF EXISTS character_trait;
DROP TABLE IF EXISTS fortune_teller;
DROP TABLE IF EXISTS method_of_prediction;
DROP TABLE IF EXISTS fortune_things;
DROP TABLE IF EXISTS characters;
DROP TABLE IF EXISTS city;

CREATE TABLE city (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    common_characteristic TEXT
);

CREATE TABLE characters (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    city_id BIGINT REFERENCES city(id) ON DELETE SET NULL,
    difference BOOLEAN,
    duration_of_life INTEGER NOT NULL,
    birthday DATE
);

CREATE TABLE fate (
    character_id BIGSERIAL PRIMARY KEY REFERENCES characters(id) ON DELETE CASCADE,
    description TEXT NOT NULL,
    type TEXT
);

CREATE TABLE character_trait (
    character_id BIGSERIAL PRIMARY KEY REFERENCES characters(id) ON DELETE CASCADE,
    trait_type TEXT NOT NULL,
    intensity TEXT
);

CREATE TABLE responsibility (
    responsible_id BIGSERIAL PRIMARY KEY REFERENCES characters(id) ON DELETE CASCADE,
    for_character_id BIGINT NOT NULL REFERENCES characters(id) ON DELETE RESTRICT,
    share TEXT CHECK (share IN ('некоторая', 'полная', 'отсутствует'))
);

CREATE TABLE fortune_things (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    character_id BIGINT REFERENCES characters(id) ON DELETE SET NULL
);

CREATE TABLE method_of_prediction (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    fate_type VARCHAR(20),
    fortune_things_id BIGINT REFERENCES fortune_things(id) ON DELETE SET NULL
);

CREATE TABLE fortune_teller (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    metod_id BIGINT NOT NULL REFERENCES method_of_prediction(id) ON DELETE RESTRICT,
    chance INTEGER NOT NULL
);

CREATE TABLE results_of_fortune_telling (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL REFERENCES characters(id) ON DELETE RESTRICT,
    fortune_teller_id BIGINT NOT NULL REFERENCES fortune_teller(id) ON DELETE RESTRICT,
    fortune_things_id BIGINT REFERENCES fortune_things(id), 
    date_of_result DATE NOT NULL,
    person_status BOOLEAN
);