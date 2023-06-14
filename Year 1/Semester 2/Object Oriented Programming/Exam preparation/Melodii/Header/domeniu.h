#pragma once
#include <iostream>
#include <vector>
#include <string>
using std::string;
using std::vector;

class Melodie {
private:
	int id;
	string titlu, artist;
	int rank;
public:
	Melodie(int _id, string _titlu, string _artist, int _rank) : id{ _id }, titlu{ _titlu }, artist{ _artist }, rank{ _rank }{}

	const int get_id() const noexcept;
	const string& get_titlu() const noexcept;
	const string& get_artist() const noexcept;
	const int get_rank() const noexcept;

	/*
	Aici se suprascrie operatorul =, pentru atribuiri de obiecte Melodie
	*/
	Melodie& operator=(const Melodie& altul);

	/*
	Aici se suprascrie operatorul ==, pentru verificarea egalitatii pe baza id-ului
	*/
	bool operator==(const Melodie& altul) const noexcept;

	/*
	Aici se suprascrie operatorul !=, pentru verificarea inegalitatii pe baza id-ului
	*/
	bool operator!=(const Melodie& altul) const noexcept;

};