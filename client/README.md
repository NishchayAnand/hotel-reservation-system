
Using Form Component from Shadcn allowed us to leverage react-hook-form to implicitly manage all search parameters for hotel search form. The react-hook-form will handle the state management of all input paramaters, collate them as a single object and pass it to the form submit handler. 

When the user searches hotels, the frontend should nagivate to the SearchPage, send a request to the Inventory Service to fetch list of available hotels and display them as list of Shadcn cards.