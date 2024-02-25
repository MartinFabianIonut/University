Tests

To evaluate the participants in a course, a teacher designed a client-server system.
The server provides a list of questions, each question having the following props
  id - integer,
  text - string,
  options - a list of possible answers/options,
  indexCorrectOption - integer.
Develop a mobile application (client) as follows.

1. The first screen allows the participant to register, by entering an id received from the teacher
(e.g. p1). The id is sent to the server via POST /auth, including { id } in the body of the request.
The server responds with { questionIds }, an array of question ids.

2. Immediately after the successful authentication (/auth), the client will download the questions.
The client will repeatedly call GET /question/:id for each item of questionIds received from the server.
The client will display the download progress "Downloading m/n", where m is the number of downloaded questions,
and n is the total number of questions.

3. After completing step 2, the client will automatically switch to the second screen. If the client fails
to download all questions, a 'retry' button will allow the participant to resume the download process.

4. When the app is started, if the step 2 was completed previously, the app switches to the second screen.

5. The second screen allows the participant to answer the questions, one by one.
The screen shows "Questions m/n" where m is the number of questions answered and n the total number to be answered.
It also presents "Correct answers: k / l" where k represents the number of correct answers, and l
the number of questions he/she answered.

6. In the context of the second screen, in order to answer to the current question, the participant selects an option
(the application will highlight the selection made) and triggers a next button.

7. If the participant does not trigger the next button within a 5 seconds interval, the app automatically
switches to the next question.

8. The app persists locally the questions and the answers.

9. The app receives notifications from the server regarding new questions (via ws).
