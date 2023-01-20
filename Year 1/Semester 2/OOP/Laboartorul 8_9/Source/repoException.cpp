#include "repoException.h"

ostream& operator<<(ostream& out, const RepoException& ex)
{
	out << ex.message;
	return out;
}

