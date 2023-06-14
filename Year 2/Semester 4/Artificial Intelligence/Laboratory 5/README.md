# Machine learning - evaluation methods

## Introduction

This homework code evaluates the performance of a machine learning (ML) algorithm for different tasks, including regression and classification problems. It also implements cross-entropy and multi-label cross-entropy loss functions.

## Usage

I used the following input files:

- For the _multi-target regression problem_ and _prediction error determination_: `inputs/sport.csv`
- For the problem of _multi-class classification_ and the _determination of accuracy, precision and recall_: `inputs/flowers.csv`
- For _loss determination in binary classification problems_: `inputs/true-binary.txt`, `inputs/probabilities-binary.txt`
- For _loss determination in multi-class classification problems_: `inputs/true-multi-class.txt`, `inputs/probabilities-multi-class.txt`
- For _loss determination in multi-target classification problems_: `inputs/true-multi-target.txt`, `inputs/probabilities-multi-target.txt`

The code will perform the following tasks:

1. **Read the input** files and store the data in appropriate data structures.
2. Evaluate the ML algorithm's performance on the regression problem using `root mean square error` (RMSE).
3. Evaluate the ML algorithm's performance on the classification problem by calculating `accuracy`, `precision`, and `recall` for each class and computing macro average accuracy, macro average precision, and macro average recall.
4. **Calculate cross-entropy and multi-label cross-entropy** using the provided probability files.
5. **Write** the evaluation results to output files.

## Output

The code generates the following output files:

- `sport-error.txt`: Contains the calculated error (RMSE) for the regression problem.
- `APR.txt`: Contains the macro average accuracy, macro average precision, and macro average recall for the classification problem.
- `binary.txt`: Contains the cross-entropy value for binary classification.
- `multi-class.txt`: Contains the cross-entropy value for multi-class classification.
- `multi-target.txt`: Contains the multi-label cross-entropy value for multi-target classification.
