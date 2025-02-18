


https://github.com/user-attachments/assets/df3c28d1-f086-440e-812f-df0fb80d6a27


### Description
The app is based Clean Architecture principles with MVI architecture for the presentation layer, modularised for each layer and feature:
- `:data` (remote service, local database, repository implementation)
- `:domain` (domain model, repository implementation)
- `:home` (feature module for home/recipes list, ui layer + use cases)
- `:detail` (feature module for detail screen, ui layer + use cases)
- `:shared` (MVI base classes + shared UI)

The app is offline first using Room database.

### Limitations
For simplicity, 25 items are requested from the API.

### Improvements
If there was more time, the following could have been implemented:
- Pull to refresh
- Pagination

### Requirements
jvm target 11.
