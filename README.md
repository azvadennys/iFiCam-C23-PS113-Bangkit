<h1 align="center">iFishCam Rest API Documentation

## Introduction

This documentation provides details about the iFishCam REST API endpoints and their corresponding request and response structures for the code.

The code is a Node.js with Express.js module that exposes many endpoints with specific routes.

## Base URL

The base URL for all API endpoints is: `http://<your-domain.com>/`

---

<h2 align="center"> Users Endpoint </h2>

The base URL for Users endpoints is: `http://<your-domain.com>/api/users/`

## Get User by ID

```
GET /:id
```

This endpoint retrieves a user by their ID.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description        |
  | ---- | ------ | ------------------ |
  | id   | string | The ID of the user |

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
        "uid": "user_id",
        "email": "user_email",
        "name": "user_name",
        "createdAt": "Creation Date",
        "signedIn": "Last Signed In",
        "profile_photo": "Profile Photo URL",
        "historyId": ["history_id_1", "history_id_2", ...],
        "storyId": ["story_id_1", "story_id_2", ...]
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 404 Not Found </b>

    - Content-Type: application/json

      ```json
      {
        "message": "User not found"
      }
      ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error retrieving user"
      }
      ```

## Create New User

```
POST /:id
```

This endpoint creates a new user.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description        |
  | ---- | ------ | ------------------ |
  | id   | string | The ID of the user |

- <b> Body </b>

  The request body should contain the following fields:

  - email: string (required) - The email of the user.
  - name: string (required) - The name of the user.

- <b> Headers </b>

  - Content-Type: application/json

#### Response

- <b> Success Response </b>

  - Status Code: 201 Created
  - Content-Type: application/json

    ```json
    {
      "uid": "user_id",
      "email": "user_email",
      "name": "user_name",
      "createdAt": "Creation Date",
      "signedIn": "",
      "profile_photo": "",
      "historyId": [],
      "storyId": []
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 404 Not Found </b>

    - Content-Type: application/json

      ```json
      {
        "message": "User already exists"
      }
      ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error creating user"
      }
      ```

## Update User

```
PUT /:id
```

This endpoint updates a user by their ID.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description        |
  | ---- | ------ | ------------------ |
  | id   | string | The ID of the user |

- <b> Body </b>

  The request body should contain the following fields:

  - name: string (required) - The updated name of the user.

- <b> Headers </b>

  - Content-Type: application/json

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
      "message": "User updated"
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error updating user"
      }
      ```

## Upload Profile Photo

```
POST /upload/:id
```

This endpoint uploads a profile photo for a user.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description        |
  | ---- | ------ | ------------------ |
  | id   | string | The ID of the user |

- <b> Body </b>

  The request should include a file upload with the field name <b>"image"</b>.

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
      "message": "User updated, image uploaded"
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 404 Not Found </b>

    - Content-Type: application/json

      ```json
      {
        "message": "User not found"
      }
      ```

  - <b> Status Code: 400 Bad Request </b>

    - Content-Type: text/plain

      ```
      No file uploaded.
      ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error retrieving user"
      }
      ```

## Delete User

```
DELETE /:id
```

This endpoint deletes a user by their ID.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description        |
  | ---- | ------ | ------------------ |
  | id   | string | The ID of the user |

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

  ```json
  {
    "message": "User deleted successfully"
  }
  ```

- <b> Error Responses </b>

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error deleting user"
      }
      ```

---

<h2 align="center">  User History Endpoint </h2>

The base URL for Histories endpoints is: `http://<your-domain.com>/api/histories/`

## Get User History by User ID

```
GET /:id
```

This endpoint retrieves the history data of a user based on the provided user ID.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description               |
  | ---- | ------ | ------------------------- |
  | id   | string | The unique ID of the user |

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
        "uid": "userId",
        "histories": [
            {
                "historyId": "historyId",
                "photoUrl": "URL",
                "otherData": "..."
            },
            {
                "historyId": "historyId",
                "photoUrl": "URL",
                "otherData": "..."
            },
            ...
        ]
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 404 Not Found </b>

    - Content-Type: application/json

      ```json
      {
        "message": "User not found"
      }
      ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error retrieving user"
      }
      ```

## Delete User History

```
DELETE /:id
```

This endpoint deletes a specific user history based on the provided user ID and history ID.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description               |
  | ---- | ------ | ------------------------- |
  | id   | string | The unique ID of the user |

- <b> Body </b>

  The request body should contain the following fields:

  - historyid: string (required) - The id history that will be deleted.

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
      "message": "History deleted successfully"
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 404 Not Found </b>

    - Content-Type: application/json

      ```json
      {
        "message": "User not found"
      }
      ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Delete History Failed"
      }
      ```

---

<h2 align="center"> User Story Endpoint </h2>

The base URL for Stories endpoints is: `http://<your-domain.com>/api/stories/`

## Get Story by User ID

```
GET /:id
```

This endpoint retrieves the stories associated with a specific user.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description        |
  | ---- | ------ | ------------------ |
  | id   | string | The ID of the user |

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
        "uid": "user_id",
        "stories": [
            {
                "storyId": "story_id",
                "userId": "user_id",
                "name": "Story Name",
                "description": "Story Description",
                "address": "Story Address",
                "latitude": "Latitude",
                "longitude": "Longitude",
                "createdAt": "Creation Date",
                "photoUrl": "URL"
            },
            ...
        ]
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 404 Not Found </b>

    - Content-Type: application/json

      ```json
      {
        "message": "User not found"
      }
      ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error retrieving user"
      }
      ```

## Get All User Stories

```
GET /
```

This endpoint retrieves all user stories.

#### Request

- <b> Parameters </b>

  No request parameters required.

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
        "stories": [
            {
            "storyId": "story_id",
            "userId": "user_id",
            "name": "Story Name",
            "description": "Story Description",
            "address": "Story Address",
            "latitude": "Latitude",
            "longitude": "Longitude",
            "createdAt": "Creation Date",
            "photoUrl": "URL"
            },
            ...
        ]
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error collecting story"
      }
      ```

### Create New Story

```
POST /:id
```

This endpoint creates a new story associated with a specific user.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description                            |
  | ---- | ------ | -------------------------------------- |
  | id   | string | The ID of the user                     |
  | body | object | The details of the story to be created |

- <b> Body </b>

  The request body should contain the following fields:

  - name: string (required) - The name of the story.
  - description: string (required) - The description of the story.
  - address: string (required) - The address of the story.
  - latitude: string (required) - The latitude of the story location.
  - longitude: string (required) - The longitude of the story location.
  - file: file (required) - The image file for the story.

- <b> Headers </b>

  - Content-Type: multipart/form-data

#### Response

- <b> Success Response </b>

  - Status Code: 201 Created
  - Content-Type: application/json

    ```json
    {
        "storyId": "story_id",
        "userId": "user_id",
        "name": "Story Name",
        "description": "Story Description",
        "address": "Story Address",
        "latitude": "Latitude",
        "longitude": "Longitude",
        "createdAt": "Creation Date",
        "photoUrl

        ": "URL"
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 400 Bad Request </b>

    - Content-Type: text/plain

      ```
      No file uploaded.
      ```

  - <b> Status Code: 404 Not Found </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Stories Id already exists"
      }
      ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error creating story"
      }
      ```

## Delete Story

```
DELETE /:id
```

This endpoint deletes a story associated with a specific user.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description        |
  | ---- | ------ | ------------------ |
  | id   | string | The ID of the user |

- <b> Body </b>

  The request body should contain the following fields:

  - storyid: string (required) - The id story that will be deleted.

- <b> Headers </b>

  - Content-Type: application/json

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
      "message": "Story deleted successfully"
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 404 Not Found </b>

    - Content-Type: application/json

    ```json
    {
      "message": "User not found"
    }
    ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

    ```json
    {
      "message": "Error deleting story"
    }
    ```

---

<h2 align="center"> Predictions Endpoint </h2>

The base URL for Predictions endpoints is: `http://<your-domain.com>/api/predicts/`

## Get Prediction

```
POST /:id
```

This endpoint sends an image file for prediction and returns the prediction results.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description               |
  | ---- | ------ | ------------------------- |
  | id   | string | The unique ID of the user |

- <b> Body </b>

  The body of the request should contain the <b>"image"</b> file to be processed.

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
        "fishName": "Fish Name",
        "description": "Fish Description",
        "nutrition": "Fish Nutrition",
        "predictionAccuracy": "Prediction Accuracy",
        "photoUrl": "URL",
        "recipes": [
            {
                "title": "title",
                "photoUrl": "photoUrl",
                "linkUrl": "linkUrl",
                "difficulty": "difficulty",
                "duration": "duration"
            },
            ...
        ]
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 400 Bad Request </b>

    - Content-Type: text/plain

      ```
      Image Not Found
      ```

  - <b> Status Code: 404 Not Found </b>

    - Content-Type: text/plain

      ```
      User Not Found
      ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: text/plain

      ```
      Internal Server Error
      ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: text/plain

      ```
      Error getting recipes
      ```

---

<h2 align="center"> Fish Endpoint </h2>

The base URL for Fish endpoints is: `http://<your-domain.com>/api/fishs/`

## Get Specific

```
GET /:id
```

This endpoint retrieves the details of a specific fish.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description               |
  | ---- | ------ | ------------------------- |
  | id   | string | The unique ID of the fish |

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
      "id": "fishId",
      "name": "Fish Name",
      "description": "Fish Description",
      "nutrition": ["Nutrition 1", "Nutrition 2"],
      "createdAt": "2023-06-02T12:00:00.000Z",
      "fishcol": "Fish Collection"
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 404 Not Found </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Fish not found"
      }
      ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error retrieving fish"
      }
      ```

## Add New Fish

```
POST /
```

This endpoint adds a new fish to the database.

#### Request

- <b> Parameters </b>

  No parameters required.

- <b> Headers </b>

  Content-Type: application/json

- <b> Body </b>

  ```json
  {
    "id": "fishId",
    "name": "Fish Name",
    "description": "Fish Description",
    "nutrition": ["Nutrition 1", "Nutrition 2"],
    "fishcol": "Fish Collection"
  }
  ```

#### Response

- <b> Success Response </b>

  - Status Code: 201 Created
  - Content-Type: application/json

    ```json
    {
      "id": "fishId",
      "name": "Fish Name",
      "description": "Fish Description",
      "nutrition": ["Nutrition 1", "Nutrition 2"],
      "createdAt": "Date",
      "fishcol": "Fish Collection"
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 400 Bad Request </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Please add fish id, name, description, nutrition"
      }
      ```

  - <b> Status Code: 404 Not Found </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Fish already exists"
      }
      ```

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error creating user"
      }
      ```

## Update Fish

```
PUT /:id
```

This endpoint updates the details of a specific fish.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description               |
  | ---- | ------ | ------------------------- |
  | id   | string | The unique ID of the fish |

- <b> Headers </b>

  Content-Type: application/json

- <b> Body </b>

  ```json
  {
    "name": "Fish Name",
    "description": "Fish Description",
    "nutrition": "New Nutrition",
    "fishcol": "Fish Collection"
  }
  ```

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK

  - Content-Type: application/json

    ```json
    {
      "message": "Fish updated"
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "message": "Error updating fish"
      }
      ```

## Delete Fish

```
DELETE /{id}
```

This endpoint deletes a specific fish from the database.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description               |
  | ---- | ------ | ------------------------- |
  | id   | string | The unique ID of the fish |

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
      "message": "Fish deleted successfully"
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 500 Internal Server Error </b>
  - Content-Type: application/json

    ```json
    {
      "message": "Failed deleting fish"
    }
    ```

---

<h2 align="center"> Recipes Endpoint </h2>

The base URL for Recipes endpoints is: `http://<your-domain.com>/api/recipes/`

## Get All Fish Recipes

```
GET /
```

This endpoint fetches all fish recipes available on masakapahariini.com.

#### Request

- <b> Parameters </b>

  No request parameters required.

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
        "status": true,
        "recipes": [
            {
            "title": "Recipe Title",
            "photoUrl": "URL",
            "linkUrl": "URL",
            "difficulty": "Recipe Difficulty",
            "duration": "Recipe Duration"
            },
            ...
        ]
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: text/plain

      ```
      Internal Server Error
      ```

## Get Specific Fish Recipes

```
GET /:id
```

This endpoint fetches recipes specific to a particular fish.

#### Request

- <b> Parameters </b>

  | Name | Type   | Description                 |
  | ---- | ------ | --------------------------- |
  | id   | string | The ID of the specific fish |

- <b> Headers </b>

  No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
        "status": true,
        "recipes": [
            {
            "title": "Recipe Title",
            "photoUrl": "URL",
            "linkUrl": "URL",
            "difficulty": "Recipe Difficulty",
            "duration": "Recipe Duration"
            },
            ...
        ]
    }
    ```

- <b> Error Responses </b>

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: text/plain

      ```
      Internal Server Error
      ```

---

<h2 align="center"> Articles Endpoint </h2>

The base URL for Articles endpoints is: `http://<your-domain.com>/api/articles/`

## Get Fish Articles

Exposes a single endpoint to retrieves fish articles from the Detik.com website.

```
GET /
```

#### Request

- <b> Parameters </b>

  - No parameters required.

- <b> Headers </b>

  - No special headers required.

#### Response

- <b> Success Response </b>

  - Status Code: 200 OK
  - Content-Type: application/json

    ```json
    {
          "status": true,
          "articles": [
            {
                "title": "Article Title 1",
                "photoUrl": "https://example.com/photo1.jpg",
                "linkUrl": "https://example.com/article1",
                "date": "Day, 2023-06-01"
            },
            {
                "title": "Article Title 2",
                "photoUrl": "https://example.com/photo2.jpg",
                "linkUrl": "https://example.com/article2",
                "date": "2023-05-31"
            },
            ...
        ]
    }
    ```

- <b> Error Response </b>

  - <b> Status Code: 500 Internal Server Error </b>

    - Content-Type: application/json

      ```json
      {
        "error": "Internal Server Error"
      }
      ```
