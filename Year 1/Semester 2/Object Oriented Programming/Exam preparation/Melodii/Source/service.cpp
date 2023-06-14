#include "service.h"

Melodie Service::cauta(int id)
{
	auto it = find_if(repo.get_all().begin(), repo.get_all().end(), [&](Melodie& t) {return t.get_id() == id; });
	if (it != repo.get_all().end()) {
		return *it;
	}
	throw RepoException("Nu exista!\n");
}

void Service::modifica(int id, const string& titlu, int rank)
{
	Melodie cautata = cauta(id);
	Melodie m{ id , titlu , cautata.get_artist() , rank};
	Melodie inainte = repo.modifica(m);
}
