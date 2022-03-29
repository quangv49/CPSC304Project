DROP TABLE LocationAffects;
DROP TABLE DrawsSurface;
DROP TABLE DrawsGround;
DROP TABLE BodyOfWaterConnected;
DROP TABLE SewagePlantHandles;

DROP TABLE SurfaceWaterLicense;
DROP TABLE GroundWaterLicense;

DROP TABLE UserBusiness;
DROP TABLE UserHousehold;

DROP TABLE StationMeasurements;
DROP TABLE VariableUnits;

DROP TABLE Dams;
DROP TABLE SewagePlant;
DROP TABLE Stations;

DROP TABLE SurfaceWater;
DROP TABLE GroundWater;
DROP TABLE BodyOfWater;

DROP TABLE Location;

CREATE TABLE Location (name VARCHAR(50), PRIMARY KEY (name));

insert into Location values('Point Grey');
insert into Location values('Yaletown');
insert into Location values('Kerrisdale');
insert into Location values('Kitsilano');
insert into Location values('Dunbar');

CREATE TABLE UserBusiness (userID VARCHAR(50), address VARCHAR(50), isWaterEnough CHAR(1), isWaterClean CHAR(1), username VARCHAR(50) NOT NULL, password VARCHAR(50) NOT NULL, location varchar(50) not null, PRIMARY KEY (userID), UNIQUE (username), FOREIGN KEY (location) REFERENCES Location ON DELETE SET NULL);

insert into UserBusiness values('baaaa', '4480 W 10th Ave, Vancouver, BC V6R 2H9', 'T', 'T', 'vpl_pointgrey', 'P@ssword!', 'Point Grey');
insert into UserBusiness values('baaab', '1261 Hamilton St, Vancouver, BC V6B 6K3', 'T', 'T', 'tibistro_yaletown', 'P@ssw0rd!', 'Yaletown');
insert into UserBusiness values('baaac', '2114 W 41st Ave, Vancouver, BC V6M 1Z2', 'T', 'F', 'baoguette', 'P@ssword!123', 'Kerrisdale');
insert into UserBusiness values('baaad', '2153 W 4th Ave, Vancouver, BC V6K 1N7', 'F', 'T', 'jamcafe_kits', 'P@ssword!456', 'Kitsilano');
insert into UserBusiness values('baaae', '3453 Dunbar St, Vancouver, BC V6S 2C3', 'F', 'F', 'bcliquor_dunbar', 'P@ssword!789', 'Dunbar');

CREATE TABLE UserHousehold (userID VARCHAR(50), address VARCHAR(50), isWaterEnough CHAR(1), isWaterClean CHAR(1), username  VARCHAR(50) NOT NULL, password VARCHAR(50) NOT NULL, location VARCHAR(50) NOT NULL, PRIMARY KEY(userID), UNIQUE(username), FOREIGN KEY(location) REFERENCES Location ON DELETE SET NULL);

insert into UserHousehold values('haaaa', '983 W 16th Ave, Vancouver, BC', 'F', 'T', '983w16', 'P@ssword!', 'Point Grey');
insert into UserHousehold values('haaab', '888 Burrard St, Vancouver, BC', 'T', 'T', 'aroundtheblock', 'P@ssword!', 'Yaletown');
insert into UserHousehold values('haaac', '9245 E 33rd Ave, Vancouver, BC', 'T', 'F', 'theredplate', 'P@ssword!123', 'Kerrisdale');
insert into UserHousehold values('haaad', '4000 Manitoba St, Vancouver, BC', 'F', 'T', 'aninkbottle', 'P@ssword!456', 'Kitsilano');
insert into UserHousehold values('aaaae', '1567 Cambie St, Vancouver, BC', 'F', 'F', 'usbdrive', 'P@ssword!789', 'Dunbar');

CREATE TABLE GroundWaterLicense (licenseID VARCHAR(50), expiryDate VARCHAR(50), dateAuthorized VARCHAR(50), userID VARCHAR(50) NOT NULL, PRIMARY KEY (licenseID), FOREIGN KEY (userID) REFERENCES UserBusiness ON DELETE CASCADE);

insert into GroundWaterLicense values('A2B6Z', '2023-12-20', '2020-12-20', 'baaaa');
insert into GroundWaterLicense values('B4F5J', '2023-01-25', '2020-01-25', 'baaab');
insert into GroundWaterLicense values('C9L0H', '2024-02-08', '2021-02-08', 'baaac');
insert into GroundWaterLicense values('D7J8J', '2025-01-29', '2022-01-29', 'baaad');
insert into GroundWaterLicense values('E4P9N', '2025-02-24', '2022-02-24', 'baaae');

CREATE TABLE SurfaceWaterLicense (licenseID VARCHAR(50), expiryDate VARCHAR(50), dateAuthorized VARCHAR(50), userID VARCHAR(50) NOT NULL, PRIMARY KEY (licenseID), FOREIGN KEY (userID) REFERENCES UserBusiness ON DELETE CASCADE);

insert into SurfaceWaterLicense values('F8H7P', '2023-01-17', '2020-01-17', 'baaaa');
insert into SurfaceWaterLicense values('G3H4E', '2022-08-02', '2019-08-02', 'baaab');
insert into SurfaceWaterLicense values('H9J6L', '2022-05-05', '2019-05-05', 'baaac');
insert into SurfaceWaterLicense values('I4B3M', '2024-07-01', '2021-07-01', 'baaad');
insert into SurfaceWaterLicense values('J9H7G', '2024-04-02', '2019-05-05', 'baaac');

CREATE TABLE BodyOfWater (waterID VARCHAR(50), name VARCHAR(50), type VARCHAR(50), PRIMARY KEY (waterID));

insert into BodyOfWater values('7236', 'River 1', 'River');
insert into BodyOfWater values('8326', 'River 2', 'River');
insert into BodyOfWater values('3039', 'Lake 1', 'Lake');
insert into BodyOfWater values('4758', 'Ravine 2', 'Ravine');
insert into BodyOfWater values('4637', 'Stream 3', 'Stream');
insert into BodyOfWater values('5642', 'Well 1', 'Aquifer');
insert into BodyOfWater values('5645', 'Well 5', 'Aquifer');
insert into BodyOfWater values('9613', 'Well 9', 'Aquifer');
insert into BodyOfWater values('7895', 'Well 8', 'Aquifer');
insert into BodyOfWater values('9862', 'Well 4', 'Aquifer');

CREATE TABLE SurfaceWater (waterID VARCHAR(50), PRIMARY KEY (waterID), FOREIGN KEY (waterID) REFERENCES BodyOfWater ON DELETE CASCADE);

insert into SurfaceWater values('7236');
insert into SurfaceWater values('8326');
insert into SurfaceWater values('3039');
insert into SurfaceWater values('4758');
insert into SurfaceWater values('4637');

CREATE TABLE GroundWater (waterID VARCHAR(50), PRIMARY KEY (waterID), FOREIGN KEY (waterID) REFERENCES BodyOfWater ON DELETE CASCADE);

insert into GroundWater values('5642');
insert into GroundWater values('5645');
insert into GroundWater values('9613');
insert into GroundWater values('7895');
insert into GroundWater values('9862');


CREATE TABLE Stations (stationID VARCHAR(50), isActive CHAR(1), measures VARCHAR(50) NOT NULL, PRIMARY KEY (stationID), FOREIGN KEY (measures) REFERENCES BodyOfWater ON DELETE CASCADE);

insert into Stations values('w82k', 'T', '3039');
insert into Stations values('ru7e', 'T', '4758');
insert into Stations values('43o2', 'F', '4637');
insert into Stations values('45js', 'F', '5642');
insert into Stations values('12lo', 'T', '5645');


CREATE TABLE BodyOfWaterConnected (upstream VARCHAR(50), downstream VARCHAR(50), PRIMARY KEY (upstream, downstream), FOREIGN KEY (upstream) REFERENCES BodyOfWater ON DELETE CASCADE, FOREIGN KEY (downstream) REFERENCES BodyOfWater ON DELETE CASCADE);

insert into BodyOfWaterConnected values('4758', '7236');
insert into BodyOfWaterConnected values('8326', '4758');
insert into BodyOfWaterConnected values('4637', '4637');
insert into BodyOfWaterConnected values('7236', '7236');
insert into BodyOfWaterConnected values('5642', '4758');


CREATE TABLE SewagePlant (plantID VARCHAR(50), name VARCHAR(50), isPrimary CHAR(1), dateOperated VARCHAR(50), PRIMARY KEY (plantID));

insert into SewagePlant values('splant0001', 'Wastewater Treatment Plant 1', 'T', '2008-02-01');
insert into SewagePlant values('splant0002', 'Advanced Wastewater Treatment Plant 2', 'T', '2001-03-04');
insert into SewagePlant values('splant0003', 'Water Reclamation Plant 3', 'F', '1998-12-30');
insert into SewagePlant values('splant0004', 'Wastewater Treatment Plant 4', 'F', '2002-11-02');
insert into SewagePlant values('splant0005', 'Wastewater Treatment Plant 5', 'T', '1990-09-09');


CREATE TABLE SewagePlantHandles (plantID VARCHAR(50), location VARCHAR(50), PRIMARY KEY (plantID, location), FOREIGN KEY (location) REFERENCES Location ON DELETE SET NULL);

insert into SewagePlantHandles values('splant0001', 'Point Grey');
insert into SewagePlantHandles values('splant0002', 'Yaletown');
insert into SewagePlantHandles values('splant0003', 'Kerrisdale');
insert into SewagePlantHandles values('splant0004', 'Kitsilano');
insert into SewagePlantHandles values('splant0003', 'Dunbar');



CREATE TABLE LocationAffects (waterID VARCHAR(50), Location VARCHAR(50), PRIMARY KEY (waterID, location), FOREIGN KEY(waterID) REFERENCES BodyOfWater ON DELETE CASCADE, FOREIGN KEY(location) REFERENCES Location ON DELETE CASCADE);

insert into LocationAffects values('7236', 'Point Grey');
insert into LocationAffects values('8326', 'Yaletown');
insert into LocationAffects values('3039', 'Kerrisdale');
insert into LocationAffects values('4758', 'Kitsilano');
insert into LocationAffects values('4637', 'Dunbar');


CREATE TABLE DrawsSurface (licenseID VARCHAR(50), waterID VARCHAR(50), PRIMARY KEY (licenseID, waterID), FOREIGN KEY (licenseID) REFERENCES SurfaceWaterLicense ON DELETE CASCADE, FOREIGN KEY (waterID) REFERENCES BodyOfWater ON DELETE CASCADE);

insert into DrawsSurface values('F8H7P', '7236');
insert into DrawsSurface values('G3H4E', '8326');
insert into DrawsSurface values('H9J6L', '3039');
insert into DrawsSurface values('I4B3M', '8326');
insert into DrawsSurface values('J9H7G', '3039');

CREATE TABLE DrawsGround (licenseID VARCHAR(50), waterID VARCHAR(50), PRIMARY KEY (licenseID, waterID), FOREIGN KEY (licenseID) REFERENCES GroundWaterLicense ON DELETE CASCADE, FOREIGN KEY (waterID) REFERENCES BodyOfWater ON DELETE CASCADE);

insert into DrawsGround values('A2B6Z', '5642');
insert into DrawsGround values('B4F5J', '5645');
insert into DrawsGround values('C9L0H', '9613');
insert into DrawsGround values('D7J8J', '7895');
insert into DrawsGround values('E4P9N', '9862');

CREATE TABLE VariableUnits (variable VARCHAR(50), unit VARCHAR(50), PRIMARY KEY (variable));

insert into VariableUnits values('Turbidity', 'NTU');
insert into VariableUnits values('O2 concentration', 'mg/L');
insert into VariableUnits values('Arsenic concentration', 'μg/L');
insert into VariableUnits values('Temperature', 'C');
insert into VariableUnits values('CaCO3 concentration', 'mg/L');

CREATE TABLE StationMeasurements (stationID VARCHAR(50), variable VARCHAR(50), time VARCHAR(50), value FLOAT, PRIMARY KEY (stationID, variable, time), FOREIGN KEY (stationID) REFERENCES Stations ON DELETE CASCADE, FOREIGN KEY (variable) REFERENCES VariableUnits ON DELETE SET NULL);

insert into StationMeasurements values('w82k', 'Turbidity', '2019-08-15 12:00:00', 0.2);
insert into StationMeasurements values('ru7e', 'O2 concentration', '2019-08-15 12:00:00', 6.7);
insert into StationMeasurements values('43o2', 'Turbidity', '2019-08-16 15:00:00', 0.3);
insert into StationMeasurements values('45js', 'O2 concentration', '2019-08-16 15:00:00', 6.5);
insert into StationMeasurements values('12lo', 'Arsenic concentration', '2019-08-16 15:00:00', 7);

CREATE TABLE Dams (damID VARCHAR(50), name VARCHAR(50), dateOperated VARCHAR(50), upstream VARCHAR(50) NOT NULL, downstream VARCHAR(50), PRIMARY KEY (damID), UNIQUE (name), FOREIGN KEY (upstream) REFERENCES BodyOfWater ON DELETE SET NULL, FOREIGN KEY (downstream) REFERENCES BodyOfWater ON DELETE SET NULL);

insert into Dams values('23', 'Memorial', '2000-09-14', '7236', '7236');
insert into Dams values('65', 'Wetsuweten', '1999-05-26', '7236', '7236');
insert into Dams values('49', 'Syilx', '2005-04-30', '8326', '8326');
insert into Dams values('77', 'Saints', '2012-07-15', '3039', '7236');
insert into Dams values('32', 'Local', '1998-09-30', '3039', '8326');

