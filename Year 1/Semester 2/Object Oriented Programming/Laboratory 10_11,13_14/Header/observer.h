#pragma once
#include <vector>
using std::vector;


class Observer {
public:
	virtual void update() = 0;
};

class Observable {
private:
	std::vector<Observer*> observers;
public:
	void addObserver(Observer* obs) {
		observers.push_back(obs);
	}

	void removeObserver(Observer* obs) {
		observers.erase(std::remove(begin(observers), end(observers), obs),
			observers.end());
	}
protected:

	void notify() {
		for (auto obs : observers) {
			obs->update();
		}
	}
};