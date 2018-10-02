# Exchange Traded Securities Temporal Store

An implementation of a read-biased in-memory temporal data store in Java, that allows point-in-time queries about data, <br>
which can be the price of an exchange traded security. <br>

We can query the temporal store to determine at any given time what the most recently captured price was during a trading day.

#### Example Interaction

An example interaction with your temporal data store might look like this: <br>
<br>
CREATE 0 100 1.5 <br>
OK 1.5 <br>
UPDATE 0 105 1.6 <br>
OK 1.5 <br>
GET 0 100 <br>
OK 1.5 <br>
GET 0 110 <br>
OK 1.6 <br>
LATEST 0  <br>
OK 105 1.6 <br>
LATEST 1 <br>
ERR No history exists for identifier 1  <br>
CREATE 1 110 2.5  <br>
OK 2.5 <br>
CREATE 1 115 2.4 <br>
ERR A history already exists for identifier 1  <br>
UPDATE 1 115 2.4 <br>
OK 2.5 <br>
UPDATE 1 120 2.3 <br>
OK 2.4 <br>
UPDATE 1 125 2.2 <br>
OK 2.3 <br>
LATEST 1 <br>
OK 125 2.2 <br>
GET 1 120 <br>
OK 2.3 <br>
UPDATE 1 120 2.35 <br>
OK 2.3 <br>
GET 1 122 <br>
OK 2.35 <br>
DELETE 1 122 <br>
OK 2.35 <br>
GET 1 125 <br>
OK 2.35 <br>
DELETE 1 <br>
OK 2.35 <br>
QUIT
