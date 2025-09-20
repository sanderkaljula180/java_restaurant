This project is just to prove to myself that I can build something independently with Java.
I want to build an application for a restaurant. It will allow me to track the number of people at a table, what they ordered, the total bill,
and whether the table is free or not (essentially, if it's paid and available).

The application must be multithreaded, have an API, and be connected to a simple database.

I'm not going to implement security, I can't be bothered.

Database:
- RestaurantTable:
  - id
  - table_number
  - is_occupied
  - number_of_guests

- Order:
  - id
  - table_id (FK RestaurantTable)
  - is_paid (boolean)
  - order_time (when was it ordered)
  - order_price

- OrderItem:
  - id
  - order_id (FK Order)
  - item_id (FK Item)
  - quantity
  - price

- Item:
  - id
  - item_name
  - item_price

API's:
- GET /api/tables 
- POST /api/tables/{id}/occupy
- POST /api/tables/{id}/vacate
- POST /api/tables/{id}/orders
- GET /api/tables/{id}/bill