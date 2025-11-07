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
  - table_capacity
  - waitress_id (FK Waitress)
  - status

- Waitress:
  - id
  - name
  - is_available (boolean)

- Order:
  - id
  - table_id (FK RestaurantTable)
  - is_paid (boolean)
  - order_time (when was it ordered)
  - waitress_id (FK Waitress)
  - order_price

- OrderItem:
  - id
  - order_id (FK Order)
  - item_id (FK Item)
  - quantity
  - price
  - is_completed

- Item:
  - id
  - item_name
  - item_price
  - in_stock

API's:
- GET /api/tables/free_tables
  - This api is for overview view, gives us list of vacant tables with data: 
    - Table id
    - Table number
    - Seat capacity

- GET /api/tables/{id}/table_setup
    - This api is for vacant table configuration view. We only need table number,seat capacity and available waitresses.
      - Table number
      - Seat capacity
      - Available waitresses

- GET /api/tables/occupied_tables
  - table number
  - number of quests
  - waitress
  - status
  - orders (conditional, if status is NOT READY_FOR_ORDER or CHOOSING_ITEMS)
    - order id
    - order items

- POST /api/order-items/{id}/is_completed
  - changes order item is_completed to true

- POST /api/order-items/{id}/not_completed
    - changes order item is_completed to false

- POST /api/tables/{id}/occupy
  - Send how many quests
  - Send who is the waitress
  - also change waitress is_available boolean if needed
  - Change table status to 'CHOOSING_ITEMS'

- POST /api/tables/{id}/ready_for_order
    - changes status to 'READY_FOR_ORDER'

- GET /api/items
    - item name
    - items in stock
    - item price

- POST /api/tables/{id}/add_order
  - Send order items
  - Create order and order_item in database
  - Change table status to 'WAITING_FOR_ORDER'

- POST /api/tables/{id}/order_completed
  - changes status to 'ORDER_COMPLETED'

- POST /api/tables/{id}/ready_to_pay
  - Changes table status to 'READY_TO_PAY'

- POST /api/tables/{id}/vacate
  - Sets is_paid true
  - Sets table status to 'FREE'


Table statuses:
 - FREE
 - CHOOSING_ITEMS
 - READY_FOR_ORDER
 - WAITING_FOR_ORDER
 - ORDER_COMPLETED
 - READY_TO_PAY

