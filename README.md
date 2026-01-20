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
  - is_ready (boolean)

- OrderItem:
  - id
  - order_id (FK Order)
  - item_id (FK Item)
  - quantity
  - price
  - is_completed (boolean)

- Item:
  - id
  - item_name
  - item_price
  - in_stock

API's:
- GET /api/tables
    - This api is for host station and waitresses. They can see in what states are tables. Both occupied and vacant tables:
        - Table id
        - is_occupied
        - Table number
        - capacity
        - number of quests
        - status
        - waitress
        - orders
            - order status

- GET /api/tables/table_setup/{id}
    - This api is for vacant table configuration view. We only need table number,seat capacity and available waitresses.
      - Table id
      - Table number
      - Seat capacity
      - Available waitresses

- PUT /api/tables/occupy/
    - Send how many quests
    - Send who is the waitress
    - also change waitress is_available boolean if needed
    - Change table status to 'CHOOSING_ITEMS'
    - Send back json response for validation

- GET /api/orders
  - This will send a list with all orders and its order_items. This is for waitress
    - Order id
    - is_ready order
    - table id
    - order_time
    - waitress
    - order items
      - order items quantity
      - is_complete

- GET /api/order-items
  - This will send all order items. This is for kitchen
  - List of order items with their status

- PUT /api/order-items/{id}/is_completed
  - changes order item is_completed to true
  - Also check if all other order-items are done in parent Order. If they are then change Order is_ready to true

- PUT /api/order-items/{id}/not_completed
    - changes order item is_completed to false
    - Also check if all other order-items are done in parent Order. If they are then change Order is_ready to false

- PUT /api/tables/change_status/ready_for_order
    - changes status to 'READY_FOR_ORDER'

- GET /api/items
    - item name
    - items in stock
    - item price

- POST /api/order/add_order
  - Send order items
  - Request will be json array objects. 
    - 
      {
      "tableId": 3,
      "items": [
      {
      "itemId": 3,
      "quantity": 2
      },
      {
      "itemId": 2,
      "quantity": 3
      }
      ]
      }
  - Create order and order_items in database
  - Change table status to 'WAITING_FOR_ORDER'
  - Return just "Order added"

- PUT /api/tables/change_status/order_completed
  - changes status to 'ORDER_COMPLETED'

- PUT /api/tables/change_status/ready_to_pay
  - Changes table status to 'READY_TO_PAY'



- PUT /api/tables/{id}/vacate
  - Sets is_paid true
  - Sets table status to 'AVAILABLE'
  - Sets waitress_id in table also null
  - is_occupied also false

- POST for adding new table maybe
- POST add new waitress



Table statuses:
 - AVAILABLE
 - CHOOSING_ITEMS
 - READY_FOR_ORDER
 - WAITING_FOR_ORDER
 - ORDER_COMPLETED - If order is on the table
 - READY_TO_PAY

LIQUIBASE
For setting up liquibase you have to add liquibase dependency and plugin. 
There are three initial files that should be created:
liquibase.properties for database connection
changelog-master.yaml
changelog_tables.yaml

To run liquibase update use this command
.\mvnw liquibase:update