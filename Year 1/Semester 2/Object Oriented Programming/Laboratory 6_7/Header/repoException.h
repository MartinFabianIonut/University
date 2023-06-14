#ifndef REPO_EXCEPTION_H
#define REPO_EXCEPTION_H

#include <exception>
#include <string>
#include <ostream>
using std::string;
using std::ostream;

class RepoException : public std::exception {
private:
	string message;
public:
	RepoException(const string& _message): 
	message{_message}{}
	

	const string& get_message() const noexcept {
		return this->message;
	}

	friend ostream& operator<<(ostream& out, const RepoException& ex);
};

ostream& operator<<(ostream& out, const RepoException& ex);

#endif
