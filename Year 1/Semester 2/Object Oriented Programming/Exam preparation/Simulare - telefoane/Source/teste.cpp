#include "teste.h"
#include "repo.h"
#include "domeniu.h"
#include "teste.h"
#include "service.h"
#include <assert.h>

void Teste::run_all()
{

    RepoFile repo{ "test.txt" };
    Service srv{ repo };
    assert(repo.get_all().size() == 4);

  }
