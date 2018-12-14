# proj2
Mobile Programming Proj. 2
RentAnything is a mobile application connecting people who need an item and are not necessarily willing to purchase it. RentAnything allows users to find anything they need, and rent it from someone else in their community. Consequently, our application creates an opportunity to earn money for people willing to lend items and opportunity to save it for people renting.

Bugs:
- Able to create messages but unable to pull those messages to be displayed in a ListView
- When running the app each time in the emulator, there is the occassional lag when logging in and creating users. A short term fix is to clean and rebuild project and run again.
- UI is not refined.

Relationship/Schema:
Login page includes the activity for signing up new users.
After signing up, login will use FirebaseAuth to get authentication for the email/password to verfiy user.
MainPage lists all the listings in the available city of which the user assigned themselves to.
Two buttons at the bottom, one directs to MyListingActivity (lists all your items you've listed) and MyRentalActivity (lists all your items you're renting).
In MyListingActivity, there is a button to create new listing and that starts the activity CreateItemActivity.
Everytime a listing is click, it opens up the ListDetailActivity and supporting functions for it are the Item class and ItemArrayAdapter.
