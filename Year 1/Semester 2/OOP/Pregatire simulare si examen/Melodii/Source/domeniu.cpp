#include "domeniu.h"

const int Melodie::get_id() const noexcept
{
    return id;
}

const string& Melodie::get_titlu() const noexcept
{
    return titlu;
}

const string& Melodie::get_artist() const noexcept
{
    return artist;
}

const int Melodie::get_rank() const noexcept
{
    return rank;
}

Melodie& Melodie::operator=(const Melodie& altul)
{
    id = altul.id;
    titlu = altul.titlu;
    artist = altul.artist;
    rank = altul.rank;
    return *this;
}

bool Melodie::operator==(const Melodie& altul) const noexcept
{
    return id == altul.id;
}

bool Melodie::operator!=(const Melodie& altul) const noexcept
{
    return id != altul.id;
}
