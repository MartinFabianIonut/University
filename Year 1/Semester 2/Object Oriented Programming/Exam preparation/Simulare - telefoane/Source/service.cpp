#include "service.h"



Telefon Service::cauta(string& cod)
{
	auto it = find_if(repo.get_all().begin(), repo.get_all().end(), [&](Telefon& t) {return t.get_cod() == cod; });
	if (it != repo.get_all().end()) {
		return *it;
	}
	throw RepoException("Nu exista!\n");
}
