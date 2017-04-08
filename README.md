# Warehouser
Project consists of designing and implementing warehouse tracking software that handles the selection and loading of fascia for bumpers on minivans.
Created with the help of Alice Southey and Matthew Tory
----------------------------------------------

Summary: Design and implement a system for h



 - read and understood the requirements for a project
 - used design concepts taught in class
 - shared the design process with other people
 - designed a Java program that solves the problem
 - implemented your design
 - worked with related code that others are developing at the same time as you are developing yours
 - worked with Checkstyle and Google's style formatter



A warehousing company has hired you to create a system to handle orders for minivan bumper fascia of various colours from an automotive factory. (Fascia are covers for bumpers.) The system tracks the status of the orders, provides computer support for warehousing workers, and keeps track of inventory levels in the warehouse. Workers pick fascia off the warehouse floor and load individual fascia into pallets, and load the pallets into trucks. If a truck arrives at the automotive factory with even one fascia in the incorrect order in the truckload, the warehousing company is fined tens of thousands of dollars because such an error holds up the entire assembly line.

Warehouses are organized into zones, and the zones have aisles of shelves. A pick face in a warehouse is one side of a shelving unit that holds racks of products — in this case, fascia. The "floor" of the warehouse is the area in which the warehouse pickers are working, and the "reserve room" is where the bulk of the inventory is stored.

Below is a more detailed description of the problem the company has asked you to solve, such as you might receive in an initial email from the company.

The requirements may not be complete. We recommend that one or more group members monitor the discussion board. Clarifications and related announcements there will be pinned, and are required reading by your group.

The requirements from the warehousing company

Our warehouse handles the selection and loading of fascia for bumpers on minivans. We sort and deliver pallets of fascia to the automotive factory for direct loading into their assembly line.

Each order from the automotive factory describes a single minvan, including colour and model. The software will translate that into a pair of fascia for the minivan, front and back, that must be sequenced in the right order on the pallets so that, when they arrive at the factory and are loaded into the assembly line, each pair of fascia is matched with the right colour minivan.

Pickers are on the floor of the warehouse. They drive forklifts from rack to rack, picking fascia of various colours from pallets, and taking them to a marshalling area for sequencing. The fascia are then placed on a special pallet designed to protect them as they are shipped, 4 fascia per pallet. Pickers have a handheld device with a barcode reader that will direct them to the next zone, aisle, rack location and level on the rack containing the next fascia to pick, and the barcode reader will read the Stock Keeping Unit (SKU) of the fascia they just picked. After sequencing, the SKU is registered again to mark that it has been sequenced.

In order to minimize picking time, there is already generic software to tell pickers the order in which to traverse the warehouse with their forklifts, because an unoptimized traversal wastes a time (and money).

Each truck holds 40 orders (80 bumpers) that are stacked 10 high. Each front/back pair of pallets needs to be placed in the right order on the truck, facing the right direction. The pallets are loaded in pairs, front bumpers and matched back bumpers, so that there are no mismatched or missing fascia at the automotive factory.

When a new shipment of fascia arrives at the warehouse, it is unloaded from the truck and checked for problems such as damaged fascia or incorrect colours. It is then entered in the system and put away in the warehouse reserve room, which is where racks on the warehouse floor are resupplied from. (There is already code that optimizes the putaway process.)

When a pick face gets low, it triggers a replenish request to get more fascia of the type that is running low from the reserve room. When replenishing happens, the resupplier records that information so that the system knows that the fascia have been moved to the pick face.

Technical requirements

Orders

An order is for a single minivan, and includes the model and colour of the minivan. Orders will arrive by FAX, one order per FAX, to a single FAX machine in the warehouse, and the orders must be entered into the system in the order in which they are received.

Using a translation table, you look up SKU numbers for front and back fascia. The translation table will look like this, with of course more colour options:

Colour,Model,SKU (front),SKU (back)
Red,XLE,11203,10432
Yellow,LE,33104,42105
Maroon,CE,21444,21576

The file contents will never change until the minivan goes out of production: all colours and models are already determined.

Four orders will be processed at a time. Until there are four, they should not be entered in the system. Once there are four orders, the colours and models of the four minivans are entered into the system. The system then looks up the SKUs for the front and back fascia and produces a picking request for the four pairs of fascia. Each picking request receives a unique id.

Picking

Pickers are on the floor of the warehouse. They drive forklifts from rack to rack, picking fascia of various colours from pallets, and taking them to a marshalling area for sequencing. They are then placed on a special pallet designed to protect them as they are shipped, 4 fascia per pallet. Pickers each have a handheld barcode reader that has some sort of interactive display and a wireless connection to the software system.

When a picker is ready, they will ask the system for the next picking request to process. The barcode reader will instruct the picker to get eight fascia, one at a time, for the four minivans in the picking request.

On the warehouse floor, each rack on a pick face holds a single kind (colour and model) of fascia.

The barcode reader directs them to the next zone (identified by a capital letter), aisle (identified by an integer), rack location in the aisle (an integer), and level on the rack containing the next fascia to pick (again, an integer). After placing a fascia on the forklift, the picker uses the barcode reader to record the SKU number of that fascia. When all eight fascia have been gathered on the forklift, the system directs the picker to the marshalling area for sequencing.

In order to minimize picking time, there is already generic software to tell pickers the order in which to traverse the warehouse with their forklifts, because an unoptimized traversal wastes a time (and money). This software has a Java wrapper available in a Java jar file. This generic software is kept up to date by a separate group in the warehousing company, and they have entered the locations of all the kinds of fascia in the warehouse: your software need merely ask for the 8 locations on the warehouse floor by sending the generic software the a list of 8 SKU numbers; it will return the list of SKU/location pairs in the correct order to be picked.

Sequencing

The eight fascia must be placed in two special pallets. One pallet will contain the four front fascia, and the other pallet the four rear fascia, in the same order that the minivan orders came in. This process is called sequencing.

After the sequencer sequences the fascia onto the pallets they visually inspect that they have the fascia in the correct places. They then record all eight SKUs using a barcode reader, first the front fascia and then the back fascia in the same order they were sequenced, to mark that they have been sequenced and to verify that they are in the correct order. If any are incorrect, it must be because the picker picked a wrong fascia. The current set of fascia are discarded (all of them thrown away!), and 8 new ones must be retrieved.

Loading

Each truck holds 80 orders (160 bumpers) that are stacked 10 high. Each front/back pair of pallets needs to be placed in the correct order on the truck, facing the right direction. (It is easy to tell which way they should face.) There are four pallets per level. 
Loaders will look at the picking request id and, using the barcode reader, scan the SKUs of the fascia to be loaded to make sure that orders are loaded in the correct order, and they will record that the picking request has been loaded. If the next picking request has not yet been processed (for example, a forklift broke down and other pickers finished first) then nothing will be loaded until the picking request arrives.

Trucks never leave the warehouse unless they are fully loaded.

Supply

When a new shipment of fascia arrives, it is unloaded from the truck and checked for problems such as damaged fascia or incorrect colours. Any problem fascia are not entered in the system.

Fascia come in crates of 40 (all of them front fascia or all of them back fascia, never mixed), all for the same model and colour of minvan. They are entered into the system and put away in the warehouse reserve room, which is where racks on the warehouse floor are resupplied from. There is already code that optimizes the putaway process; it is a separate system and your software does not need to interact with it.

Replenishing

Whenever a picker records that a fascia has been picked, the system checks whether that kind of fascia is running out on the level of the rack it is on. This happens when there are exactly 5 of that kind of fascia left. If there are 5 left after picking, the system triggers a replenish request to get 25 more fascia of the appropriate type from the reserve room. When replenishing happens, using the barcode reader the resupplier records the SKU for the fascia so that the system knows that more fascia have moved from the reserve room to the pick face.

By the nature of picking and replenishing, the pick faces will never run out of any kind of fascia.

The reserve room never runs out of fascia: if the warehouse does not have enough fascia of a particular colour and model, the automotive company will not send orders for minivans of that colour and model.


Warehouse floor layout

There are two zones ('A' and 'B'), 2 aisles in each zone (numbered 0 and 1), 3 racks in each aisle (numbered 0 through 2), and 4 levels on each rack (numbered 0 through 3).

Initial warehouse state

We will assume that each level on a rack can hold 30 fascia. Assume most levels of most racks are full. Some may start out low; for example, a level on a rack may only have 22 fascia to start the day. There is an input file called initial.csv specifying any racks that do not have 30 fascia, in this format:

Zone,Aisle,Rack,Level,Amount
Generic software for picking order

The generic software is in a class called WarehousePicking. There is a single method:

/**
 * Based on the Integer SKUs in List 'skus', return a List of locations,
 * where each location is a String containing 5 pieces of information: the
 * zone character (in the range ['A'..'B']), the aisle number (an integer
 * in the range [0..1]), the rack number (an integer in the range ([0..2]),
 * and the level on the rack (an integer in the range [0..3]), and the SKU
 * number.
 * @param skus the list of SKUs to retrieve.
 * @return the List of locations.
 */
public static List optimize(List skus)
Here is the traversal table that will be used by the generic software. When a request comes in, the optimize method will order the traversal in the order the SKUs appear in this file.

Event order

When your program is run, there will be in input file describing the state of the warehouse and the sequence of warehousesimulator.warehouser.events. Remember, this is a warehousesimulator.simulation: your main program will read warehousesimulator.warehouser.events from a file (as if the workers were completing warehousesimulator.warehouser.events with their barcod readers and so on) and direct the rest of your system.

A replenish request is triggered by the software, and so replenish requests aren't part of the event list.

Logging

Program should produce the following files:

final.csv: identical in format to initial.csv. The current levels for any pick face that does not have 30 fascia. Just below is example output where the first has 12 fascia left and the second 26. Notice that this can be used as input for the next run.

        A,1,1,2,12
        A,0,0,1,26
orders.csv: the orders that were placed on the truck. Note that not every input order will end up on the truck, because there might not be enough warehousesimulator.warehouser.events to finish.

log.txt:
The events
The messages that the system sends to the barcode readers
The messages the barcode readers sends to the system
The lines from the event input file
