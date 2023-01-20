#include "repo.h"
#include <fstream>
using std::ifstream;
using std::ofstream;

void Repo::adauga(Melodie& m)
{
	auto it = find_if(this->melodii.begin(), this->melodii.end(), [&](const Melodie& p) noexcept { return p.get_id() == m.get_id(); });
	if (it != melodii.end()) {
		throw RepoException("Mai exista!\n");
	}
	melodii.push_back(m);
}

Melodie Repo::modifica(const Melodie& m)
{
	auto it = find_if(this->melodii.begin(), this->melodii.end(), [&](const Melodie& p) noexcept { return p.get_id() == m.get_id(); });
	if (it != melodii.end()) {
		Melodie l = *it;
		*it = m;
		return l;
	}
	throw RepoException("Element inexistent!\n");
}

void RepoFile::read()
{
	ifstream f(path);
	if (!f.is_open()) {
		throw RepoException("Nu am putut deschide fisierul de la calea " + path + " !");
	}
	int id;
	string titlu, artist;
	int rank;
	Repo::melodii.clear();
	while (!f.eof()) {
		f >> id >> titlu >> artist >> rank;
		Melodie nou{ id , titlu , artist , rank };
		Repo::adauga(nou);
	}
	f.close();
}

void RepoFile::write()
{
	ofstream f(path);
	if (!f.is_open()) {
		throw RepoException("Nu am putut deschide fisierul de la calea " + path + " !");
	}
	int i = 0;
	for (auto& el : Repo::melodii) {
		f << el.get_id() << " " << el.get_titlu() << " " << el.get_artist() << " " << el.get_rank();
		if (i < Repo::melodii.size() - 1) {
			f << "\n";
		}
		i++;
	}
	f.close();
}

Melodie Repo::sterge_dupa_id(int id)
{
	auto it = find_if(this->melodii.begin(), this->melodii.end(), [&](const Melodie& p) noexcept { return p.get_id() == id; });
	if (it != this->melodii.end()) {
		Melodie l = *it;
		this->melodii.erase(it);
		return l;
	}
	throw RepoException("Element inexistent!\n");
}