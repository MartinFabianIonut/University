Messages

Implement a client-server system as follows.
The server provides an API REST resource Message.
A message has the following props:
  id - number,
  text - string,
  read - boolean flag indicating whether the message was read or not,
  sender - string representing the name of the user who sent the message,
  created - number representing the date when the message was created.
Write a mobile app (client) providing the following features.

1. When the app starts, the list of messages is fetched from the server, via
http GET /message. The app shows the list of users who sent messages.

2. If there are unread messages received from certain users, those users will be
presented first in the list, sorted descending by the date of last unread message.

3. For each user the application shows the number of unread messages using the format: user [unread count].

4. When a user is selected, the app shows the messages received from that user
(on the same screen), sorted by the created prop.

5. When an unread message is shown in the list (4), the message is shown in bold for one second to indicate
visually the fact that it is a new message that the user is just reading.

6. When the user navigates through messages, for each unread message displayed in the message list,
the app sends to the server that the user just read that message via PUT /message/:id,
including the message props in the request body.
Also, the list of users will be updated, showing the number of unread messages
for the selected user.

7. The app receives notifications from the server regarding the new messages received (via ws).

8. The app persists locally the messages and the status of the read messages.

9. If the device cannot connect to the server at step (6), the app will automatically
retry the put operation.
