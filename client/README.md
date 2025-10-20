
Using Form Component from Shadcn allowed us to leverage react-hook-form to implicitly manage all search parameters for hotel search form. The react-hook-form will handle the state management of all input paramaters, collate them as a single object and pass it to the form submit handler. 

When the user searches hotels, the frontend should nagivate to the SearchPage, send a request to the Inventory Service to fetch list of available hotels and display them as list of Shadcn cards.

when user clicks on search from the home-page, (locationId, checkInDate, checkOutDate) are passed to hotel-listing page.

**Can we improve the below state flow using redux?**

hotel-listing page contains list of hotel-card. When a user clicks on a hotel-card, we need to navigate to the hotel-details page where we need (checkInDate, checkOutDate) details. Hence, we need to flow (checkInDate, checkOutDate) from hotel-listing to hotel-details via hotel-card (hotel-listing -> hotel-card -> hotel-details).

**We want the users selections in the room-type-card component to be shared to its sibling bookg-summary component. What is the best approach to achieve this?**

Answer: keep the simple lifted-state + prop approach. UseContext (Redux) adds value when many nested descendants need read/write access or the selection logic becomes shared across unrelated components.