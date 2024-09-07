-- Create User Table
CREATE TABLE "User" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    cin VARCHAR(255) UNIQUE NOT NULL
);

-- Create Consumption Table
CREATE TABLE Consumption (
     id SERIAL PRIMARY KEY,
     startDate DATE NOT NULL,
     endDate DATE NOT NULL,
     amount DOUBLE PRECISION NOT NULL,
     consumptionImpact DOUBLE PRECISION NOT NULL,
     consumptionType VARCHAR(20) CHECK (consumptionType IN ('TRANSPORT', 'HOUSING', 'FOOD')) NOT NULL,
     userId INT,
     FOREIGN KEY (userId) REFERENCES "User"(id)
);

-- Create Food Table
CREATE TABLE Food (
      id SERIAL PRIMARY KEY,
      typeOfFood VARCHAR(20) CHECK (typeOfFood IN ('MEAT', 'VEGETABLE')) NOT NULL,
      weight DOUBLE PRECISION NOT NULL,
      consumptionId INT,
      FOREIGN KEY (consumptionId) REFERENCES Consumption(id)
);

-- Create Housing Table
CREATE TABLE Housing (
     id SERIAL PRIMARY KEY,
     energyConsumption DOUBLE PRECISION NOT NULL,
     energyType VARCHAR(20) CHECK (energyType IN ('ELECTRICITY', 'GAS')) NOT NULL,
     consumptionId INT,
     FOREIGN KEY (consumptionId) REFERENCES Consumption(id)
);

-- Create Transport Table
CREATE TABLE Transport (
   id SERIAL PRIMARY KEY,
   distanceTraveled DOUBLE PRECISION NOT NULL,
   vehicleType VARCHAR(20) CHECK (vehicleType IN ('CAR', 'TRAIN')) NOT NULL,
   consumptionId INT,
   FOREIGN KEY (consumptionId) REFERENCES Consumption(id)
);
